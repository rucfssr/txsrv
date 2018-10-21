package com.test.exception;

import javax.validation.ValidationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.test.dto.Response;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author rucfssr
 *
 */
@Slf4j
@ControllerAdvice
public class RESTExceptionHandler {

	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Response invalidAccountOpening(ValidationException ex) {
		log.debug("Validation Error: ", ex);
		return new Response(com.test.dto.ResponseStatus.ERROR, ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Response processUnknownErrors(Exception ex) {
		log.error("Unknown error: ", ex);
		return new Response(com.test.dto.ResponseStatus.ERROR, "Internal server error.");
	}

}
