package com.fitu.benefitu.domain.auth.errors;

import com.fitu.benefitu.global.error.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthException implements BaseErrorCode {
    NOT_VALID_TOKEN_UNAUTHORIZED("AUTH_401", "인증되지 않는 사용자입니다. 유효한 토큰을 포함해주세요.",HttpStatus.UNAUTHORIZED),
    DID_NOT_MATCH_ID_AND_PW("AUTH_401", "아이디 또는 비밀번호가 일치하지 않습니다.",  HttpStatus.UNAUTHORIZED),
    ALREADY_EXIST_USER_ID_BAD_REQUEST("AUTH_4001", "이미 존재하는 아이디입니다.",HttpStatus.BAD_REQUEST),
    WRONG_USER_FORM_BAD_REQUEST("AUTH_4002","비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.",HttpStatus.BAD_REQUEST);

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
