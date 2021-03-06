package com.cyhee.rabit.web.cmm.exception.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cyhee.rabit.exception.cmm.NoSuchContentException;
import com.cyhee.rabit.web.cmm.model.ApiError;
import com.cyhee.rabit.web.cmm.model.ApiErrorCode;

/**
 * Api 내에서 발생하는 기본적인 Exception들에 대한 Globl Handler
 * @author chy
 *
 */
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {
	
	private static Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(value=NoSuchContentException.class)
	public ResponseEntity<ApiError> notFoundExceptionHandler(NoSuchContentException e) {
		logger.debug(e);
		return new ResponseEntity<>(new ApiError(e.getApiErrorCode(), e), e.getStatus());
	}
	
	@ExceptionHandler(value=EmptyResultDataAccessException.class)
	public ResponseEntity<ApiError> emptyResultDataAccessExceptionHandler(EmptyResultDataAccessException e) {
		logger.debug(e);
		return new ResponseEntity<>(new ApiError(ApiErrorCode.NOT_FOUND, e), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value=HttpMessageNotReadableException.class)
	public ResponseEntity<ApiError> httpMessageNotReadableException(HttpMessageNotReadableException e) {
		logger.debug(e);
		return new ResponseEntity<>(new ApiError(ApiErrorCode.INVALID_INPUT_TYPE, e), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value=DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> dataIntegrityViolationException(DataIntegrityViolationException e) {
		logger.debug(e);
		return new ResponseEntity<>(new ApiError(ApiErrorCode.INVALID_INPUT_TYPE, e), HttpStatus.BAD_REQUEST);
	}
}
