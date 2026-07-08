package com.fitu.benefitu.domain.users.errors;

import com.fitu.benefitu.global.error.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthException implements BaseErrorCode {
    ALREADY_EXIST_USER_ID_BAD_REQUEST("AUTH_4001", "이미 존재하는 아이디입니다.",HttpStatus.BAD_REQUEST),
    WRONG_USER_FORM_BAD_REQUEST("AUTH_4002","비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.",HttpStatus.BAD_REQUEST),
    //사용자 정보 제출 검증
    INVALID_GRADE_BAD_REQUEST("USER_4002", "학년은 1학년부터 5학년 사이여야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_GPA_BAD_REQUEST("USER_4002", "학점은 0.0점에서 4.5점 사이여야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_BIRTHDATE_BAD_REQUEST("USER_4002", "생년월일은 현재 날짜 이전이어야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_INCOME_BAD_REQUEST("USER_4002", "소득분위는 1에서 10 사이의 정수여야 합니다.", HttpStatus.BAD_REQUEST);

    private String code;
    private String message;
    private HttpStatus httpStatus;

    AuthException(String code, String message, HttpStatus httpStatus){
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
