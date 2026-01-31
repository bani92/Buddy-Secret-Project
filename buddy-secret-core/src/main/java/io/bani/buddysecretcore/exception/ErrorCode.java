package io.bani.buddysecretcore.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    OK(200,"C002","OK"),
    CREATE(201,"C003","Created"),
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    BAD_REQUEST(400, "C004", "Bad Request"),
    FORBIDDEN(403,"C007","Forbidden"),
    NOT_FOUND(404,"C008","Not Found"),
    CONFLICT(409,"C016","Conflict"),
    INTERNAL_SERVER_ERROR(500, "C011", "Server Error"),

    // Member
    EMAIL_DUPLICATION(400, "M001", "이미 존재하는 이메일입니다."),
    LOGIN_INPUT_INVALID(400, "M002", "로그인 정보가 올바르지 않습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
