package com.eli.ads.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder @Getter @Setter
public class ExceptionResponse {
    private Instant timestamp;
    private int code;
    private ErrorType errorType;
    private String message;
    private String path;
}
