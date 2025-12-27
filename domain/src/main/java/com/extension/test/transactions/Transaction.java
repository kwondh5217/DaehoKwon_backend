package com.extension.test.transactions;

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
@Table(name = "transactions")
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type", nullable = false)
  private TransactionType transactionType;

  @Enumerated(EnumType.STRING)
  @Column(name = "status_type", nullable = false)
  private TransactionStatusType statusType;

  @Column(name = "from_account_id")
  private Long fromAccountId;

  @Column(name = "to_account_id")
  private Long toAccountId;

  @Column(name = "amount", nullable = false)
  private long amount;

  @Column(name = "fee", nullable = false)
  private long fee;

  @Column(name = "occurred_at", nullable = false)
  private LocalDateTime occurredAt;

  @Column(name = "failure_reason")
  private String failureReason;

  protected Transaction() {
  }

  public Long getId() {
    return id;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public TransactionStatusType getStatusType() {
    return statusType;
  }

  public Long getFromAccountId() {
    return fromAccountId;
  }

  public Long getToAccountId() {
    return toAccountId;
  }

  public long getAmount() {
    return amount;
  }

  public long getFee() {
    return fee;
  }

  public LocalDateTime getOccurredAt() {
    return occurredAt;
  }

  public String getFailureReason() {
    return failureReason;
  }
}
