package com.extension.test.api.exception;

public enum ErrorCode {
  DUPLICATE_ACCOUNT_NUMBER("DUPLICATE_ACCOUNT_NUMBER", "이미 존재하는 계좌번호입니다."),
  INVALID_REQUEST("INVALID_REQUEST", "요청 값이 올바르지 않습니다."),
  ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND", "계좌를 찾을 수 없습니다."),
  DAILY_WITHDRAW_LIMIT_EXCEEDED("DAILY_WITHDRAW_LIMIT_EXCEEDED", "일 출금 한도를 초과했습니다."),
  DAILY_TRANSFER_LIMIT_EXCEEDED("DAILY_TRANSFER_LIMIT_EXCEEDED", "일 이체 한도를 초과했습니다.");


  private final String code;
  private final String defaultMessage;

  ErrorCode(String code, String defaultMessage) {
    this.code = code;
    this.defaultMessage = defaultMessage;
  }

  public String code() {
    return code;
  }

  public String defaultMessage() {
    return defaultMessage;
  }
}

