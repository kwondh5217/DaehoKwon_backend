package com.extension.test.transactions;

import com.extension.test.accounts.Account;
import com.extension.test.accounts.AccountRepository;
import com.extension.test.exception.AccountNotFoundException;
import com.extension.test.exception.DailyTransferLimitExceededException;
import com.extension.test.exception.DailyWithdrawLimitExceededException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TransactionService {

    public static final long DAILY_WITHDRAW_LIMIT = 1_000_000L;
    public static final long DAILY_TRANSFER_LIMIT = 3_000_000L;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Long deposit(String accountNumber, long amount) {
        Account account = accountRepository.findByAccountNumberWithLock(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        account.deposit(amount);

        Transaction tx = Transaction.depositSuccess(account.getId(), amount);
        return transactionRepository.save(tx).getId();
    }

    @Transactional
    public Long withdraw(String accountNumber, long amount) {
        Account account = accountRepository.findByAccountNumberWithLock(accountNumber)
            .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        LocalDate today = LocalDate.now();
        LocalDateTime from = today.atStartOfDay();
        LocalDateTime to = today.plusDays(1).atStartOfDay();

        long todayWithdrawSum =
            transactionRepository.sumTodayWithdrawAmount(account.getId(), from, to);


        if (todayWithdrawSum + amount > DAILY_WITHDRAW_LIMIT) {
            throw new DailyWithdrawLimitExceededException(
                DAILY_WITHDRAW_LIMIT,
                todayWithdrawSum,
                amount
            );
        }

        account.withdraw(amount);

        Transaction success = Transaction.withdrawSuccess(account.getId(), amount);
        return transactionRepository.save(success).getId();
    }

    @Transactional
    public Long transfer(String fromAccountNumber, String toAccountNumber, long amount) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("같은 계좌로 이체할 수 없습니다.");
        }

        long fee = amount / 100;
        long totalDebit = amount + fee;

        // 데드락 방지: accountNumber 정렬로 락 순서 고정
        String firstNo = fromAccountNumber.compareTo(toAccountNumber) <= 0 ? fromAccountNumber : toAccountNumber;
        String secondNo = firstNo.equals(fromAccountNumber) ? toAccountNumber : fromAccountNumber;

        Account first = accountRepository.findByAccountNumberWithLock(firstNo)
            .orElseThrow(() -> new AccountNotFoundException(firstNo));
        Account second = accountRepository.findByAccountNumberWithLock(secondNo)
            .orElseThrow(() -> new AccountNotFoundException(secondNo));

        Account from = first.getAccountNumber().equals(fromAccountNumber) ? first : second;
        Account to = first.getAccountNumber().equals(toAccountNumber) ? first : second;

        LocalDate today = LocalDate.now();
        LocalDateTime fromTime = today.atStartOfDay();
        LocalDateTime toTime = today.plusDays(1).atStartOfDay();

        long todayTransferSum = transactionRepository.sumTodayTransferAmount(from.getId(), fromTime, toTime);
        if (todayTransferSum + amount > DAILY_TRANSFER_LIMIT) {
            throw new DailyTransferLimitExceededException(DAILY_TRANSFER_LIMIT, todayTransferSum, amount);
        }

        from.withdraw(totalDebit);
        to.deposit(amount);

        Transaction tx = Transaction.transferSuccess(from.getId(), to.getId(), amount, fee);
        return transactionRepository.save(tx).getId();
    }

}
