package com.extension.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.extension.test.accounts.Account;
import com.extension.test.accounts.AccountRepository;
import com.extension.test.transactions.TransactionService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestConstructor;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class AccountIntegrationTest extends AbstractIntegrationTest {

  private final TransactionService transactionService;
  private final AccountRepository accountRepository;

  AccountIntegrationTest(
      TransactionService transactionService,
      AccountRepository accountRepository
  ) {
    this.transactionService = transactionService;
    this.accountRepository = accountRepository;
  }

  @Test
  void concurrent_deposits_are_applied_correctly() throws Exception {
    // given
    String accountNumber = "12345678";
    accountRepository.save(new Account(accountNumber));

    int threads = 20;
    long amount = 1000L;

    ExecutorService pool = Executors.newFixedThreadPool(threads);
    CountDownLatch ready = new CountDownLatch(threads);
    CountDownLatch start = new CountDownLatch(1);
    CountDownLatch done = new CountDownLatch(threads);

    List<Future<?>> futures = new ArrayList<>();

    // when
    for (int i = 0; i < threads; i++) {
      futures.add(pool.submit(() -> {
        ready.countDown();
        try {
          start.await();
          transactionService.deposit(accountNumber, amount);
        } catch (Exception e) {
          throw new RuntimeException(e);
        } finally {
          done.countDown();
        }
      }));
    }

    ready.await();     // 모두 준비될 때까지
    start.countDown(); // 동시에 시작
    done.await();      // 모두 끝날 때까지

    // 예외가 있었으면 여기서 터지게
    for (Future<?> f : futures) f.get();

    // then
    Account reloaded = accountRepository.findByAccountNumber(accountNumber).orElseThrow();
    assertThat(reloaded.getBalance()).isEqualTo(threads * amount);

    pool.shutdownNow();
  }
}