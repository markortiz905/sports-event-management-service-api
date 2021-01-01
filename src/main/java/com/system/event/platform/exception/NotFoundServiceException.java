package com.system.event.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * @author mark ortiz
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundServiceException extends Exception {

    public NotFoundServiceException(Throwable e, String message) {
        super(message, e);
    }

    public NotFoundServiceException(String message) {
        super(message);
    }

}
