package com.extension.test.exception;

import lombok.Getter;

@Getter
public class DailyTransferLimitExceededException extends RuntimeException {
  private final long limit;
  private final long todaySum;
  private final long requestedAmount;

  public DailyTransferLimitExceededException(long limit, long todaySum, long requestedAmount) {
    super("일 이체 한도를 초과했습니다. limit=" + limit + ", todaySum=" + todaySum + ", requested=" + requestedAmount);
    this.limit = limit;
    this.todaySum = todaySum;
    this.requestedAmount = requestedAmount;
  }
}