package com.extension.test.api;

import com.extension.test.api.dto.ApiResponse;
import com.extension.test.api.dto.DepositRequest;
import com.extension.test.api.dto.DepositResponse;
import com.extension.test.api.dto.TransferRequest;
import com.extension.test.api.dto.TransferResponse;
import com.extension.test.api.dto.WithdrawRequest;
import com.extension.test.api.dto.WithdrawResponse;
import com.extension.test.transactions.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<ApiResponse<DepositResponse>> deposit(
            @PathVariable String accountNumber,
            @Valid @RequestBody DepositRequest req
    ) {
        Long txId = transactionService.deposit(accountNumber, req.amount());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(new DepositResponse(txId)));
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<ApiResponse<WithdrawResponse>> withdraw(
        @PathVariable String accountNumber,
        @Valid @RequestBody WithdrawRequest req
    ) {
        Long txId = transactionService.withdraw(accountNumber, req.amount());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(new WithdrawResponse(txId)));
    }

    @PostMapping("/{accountNumber}/transfer")
    public ResponseEntity<ApiResponse<TransferResponse>> transfer(
        @PathVariable String accountNumber,
        @Valid @RequestBody TransferRequest req
    ) {
        Long txId = transactionService.transfer(accountNumber, req.toAccountNumber(), req.amount());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(new TransferResponse(txId)));
    }
}