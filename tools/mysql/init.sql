-- 계좌
CREATE TABLE IF NOT EXISTS `accounts`
(
    `id`             bigint        NOT NULL AUTO_INCREMENT,
    `account_number` varchar(128)  NOT NULL COMMENT '계좌번호',
    `status`         varchar(128)  NOT NULL COMMENT 'ACTIVE/CLOSED 등',
    `balance`        bigint        NOT NULL COMMENT '현재 잔액',
    `created_at`     datetime(6)   NOT NULL,
    `updated_at`     datetime(6)   NOT NULL,
    `deleted`        bit(1)        NOT NULL DEFAULT b'0',
    `deleted_at`     datetime(6)   DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_accounts_account_number` (`account_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '계좌';

-- 거래 내역(입금/출금/이체)
CREATE TABLE IF NOT EXISTS `transactions`
(
    `id`                bigint          NOT NULL AUTO_INCREMENT,
    `transaction_type`  varchar(128)    NOT NULL COMMENT 'DEPOSIT/WITHDRAW/TRANSFER',
    `status`            varchar(128)    NOT NULL COMMENT 'SUCCESS/FAILED',
    `from_account_id`   bigint          DEFAULT NULL COMMENT '출금 계좌',
    `to_account_id`     bigint          DEFAULT NULL COMMENT '입금 계좌',
    `amount`            bigint          NOT NULL COMMENT '거래 금액',
    `fee`               bigint          NOT NULL COMMENT '수수료',
    `occurred_at`       datetime(6)     NOT NULL COMMENT '거래 발생 일시',
    `failure_reason`    varchar(1000)   DEFAULT NULL COMMENT '실패 사유',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '거래 내역';
