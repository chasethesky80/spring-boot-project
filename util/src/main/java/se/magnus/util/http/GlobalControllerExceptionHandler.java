package se.magnus.util.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import se.magnus.api.core.exceptions.InvalidInputException;
import se.magnus.api.core.exceptions.NotFoundException;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorInfo handleNotFoundException(
            ServerHttpRequest serverHttpRequest, NotFoundException notFoundException
    ) {
        return createHttpErrorInfo(HttpStatus.NOT_FOUND, serverHttpRequest,
                notFoundException);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody HttpErrorInfo handleInvalidInputException(
            ServerHttpRequest serverHttpRequest, InvalidInputException invalidInputException
    ) {
        return createHttpErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, serverHttpRequest,
                invalidInputException);
    }

    private HttpErrorInfo createHttpErrorInfo(
            HttpStatus httpStatus, ServerHttpRequest request, Exception ex
    ) {
        final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage();
        LOG.debug("Returning HTTP status: {} for path: {} with message: {}",
                  httpStatus, path, message);
        ZonedDateTime now = ZonedDateTime.now();
        return new HttpErrorInfo(now, path, httpStatus, message);
    }
}
