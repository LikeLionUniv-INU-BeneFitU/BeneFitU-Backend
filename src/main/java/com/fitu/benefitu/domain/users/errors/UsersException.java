package com.fitu.benefitu.domain.users.errors;

import com.fitu.benefitu.global.error.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum UsersException implements BaseErrorCode {
    INVALID_GRADE_BAD_REQUEST("USER_4002", "학년은 1학년부터 5학년 사이여야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_GPA_BAD_REQUEST("USER_4002", "학점은 0.0점에서 4.5점 사이여야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_BIRTHDATE_BAD_REQUEST("USER_4002", "생년월일은 현재 날짜 이전이어야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_INCOME_BAD_REQUEST("USER_4002", "소득분위는 1에서 10 사이의 정수여야 합니다.", HttpStatus.BAD_REQUEST),
    ALREADY_INSERTED_CONFLICT("USER_409", "이미 기본 정보를 입력하셨습니다.", HttpStatus.CONFLICT);

    private String code;
    private String message;
    private HttpStatus httpStatus;

    UsersException(String code, String message, HttpStatus httpStatus){
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }
}
