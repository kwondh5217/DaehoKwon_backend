package com.extension.test.accounts;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.extension.test.exception.AccountNotFoundException;
import com.extension.test.exception.DuplicateAccountNumberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

class AccountServiceTest {

  private AccountService accountService;
  private AccountRepository accountRepository;

  @BeforeEach
  void setUp() {
    this.accountRepository = mock(AccountRepository.class);
    this.accountService = new AccountService(accountRepository);
  }

  @DisplayName("중복된 계좌번호가 존재할 경우 계좌 생성에 실패한다")
  @Test
  void createAccount_duplicateAccountNumber_fail() {
    // given
    String duplicateAccountNumber = "12345678";
    given(accountRepository.save(any()))
        .willThrow(new DataIntegrityViolationException("duplicate key"));

    // when & then
    assertThrows(DuplicateAccountNumberException.class, () -> {
      accountService.createAccount(duplicateAccountNumber);
    });
  }

  @DisplayName("삭제할 계좌가 존재하지 않다면 커스텀 예외를 반환한다")
  @Test
  void deleteAccount_notExistAccount_fail() {
    // given
    String notExistsAccountNumber = "12345678";
    given(accountRepository.findByAccountNumber(notExistsAccountNumber))
        .willThrow(new AccountNotFoundException(notExistsAccountNumber));

    // when & then
    assertThrows(AccountNotFoundException.class, () -> {
      accountService.deleteAccount(notExistsAccountNumber);
    });
  }
}
