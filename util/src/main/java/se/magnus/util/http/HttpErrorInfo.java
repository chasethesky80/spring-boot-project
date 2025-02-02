package se.magnus.util.http;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record HttpErrorInfo(ZonedDateTime dateTime, String path, HttpStatus httpStatus,
                            String message) {
}
