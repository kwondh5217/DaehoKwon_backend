package com.extension.test.accounts;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "account_number", nullable = false)
  private String accountNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "status_type", nullable = false)
  private AccountStatusType statusType;

  @Column(name = "balance", nullable = false)
  private long balance;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "deleted", nullable = false)
  private boolean deleted;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  protected Account() {
  }

  public Account(String accountNumber) {
    this.accountNumber = accountNumber;
    this.statusType = AccountStatusType.ACTIVE;
    this.balance = 0L;
    this.deleted = false;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = this.createdAt;
  }

  public Long getId() {
    return id;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public AccountStatusType getStatusType() {
    return statusType;
  }

  public long getBalance() {
    return balance;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public LocalDateTime getDeletedAt() {
    return deletedAt;
  }


}
