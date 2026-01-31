package io.bani.buddysecretcore.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponseInfo {
    private String txId;
    private Integer status;
    private ErrorData error;

    public ErrorResponseInfo(ErrorCode errorCode) {
        this.txId = UUID.randomUUID().toString().substring(0, 8); // 추적용 ID
        this.status = errorCode.getStatus();
        this.error = new ErrorData(errorCode.getCode(), errorCode.getMessage());
    }

    public ErrorResponseInfo(ErrorCode errorCode, String customMessage) {
        this(errorCode);
        this.error = new ErrorData(errorCode.getCode(), customMessage);
    }

}