package com.system.event.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * @author mark ortiz
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ServiceException extends Exception {

    public ServiceException(Throwable e, String message) {
        super(message, e);
    }

}
