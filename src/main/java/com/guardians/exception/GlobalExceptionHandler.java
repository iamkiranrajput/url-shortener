package com.guardians.exception;

import com.guardians.dto.UrlResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(ShortUrlNotFoundException.class)
    public ResponseEntity<UrlResponse> handleEmployeeNotFoundException(ShortUrlNotFoundException ex){
        log.error("ShortUrlNotFoundException: {}", ex.getMessage());

        UrlResponse response = new UrlResponse();
        response.setMessage(ex.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UrlResponse> handleGeneralException(Exception ex){
        log.error("General Exception occurred: {}", ex.getMessage(), ex);
        UrlResponse response = new UrlResponse();
        response.setMessage("An Error Occurred: "+ex.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR) ;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<UrlResponse> handleNoResourceFoundException(NoResourceFoundException ex){
        log.error("NoResourceFoundException: {}", ex.getMessage());
        UrlResponse response = new UrlResponse();
        response.setMessage("Page not found");
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND) ;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<UrlResponse> handleNoResourceFoundException(HttpRequestMethodNotSupportedException ex){
        log.error("HttpRequestMethodNotSupportedException: {}", ex.getMessage());
        UrlResponse response = new UrlResponse();
        response.setMessage("Page not found :: check URL "+ex.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR) ;
    }
    @ExceptionHandler(PaginationException.class)
    public ResponseEntity<UrlResponse> handlePaginationException(PaginationException ex){
        log.error("PaginationException: {}",ex.getMessage(),ex);
        UrlResponse response = new UrlResponse();
        response.setMessage("Error occurred during Pagination :: "+ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return  new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(AlreadyRatedException.class)
    public ResponseEntity<UrlResponse> handleAlreadyRatedException(AlreadyRatedException ex){
        log.error("Already Rated: {}",ex.getMessage(),ex);
        UrlResponse response = new UrlResponse();
        response.setMessage("You Already given rating :: "+ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return  new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
