package com.hw.clazz;

import com.hw.aggregate.comment.exception.CommentAccessException;
import com.hw.aggregate.comment.exception.CommentNotFoundException;
import com.hw.aggregate.comment.exception.CommentUnsupportedSortOrderException;
import com.hw.aggregate.post.exception.PostAccessException;
import com.hw.aggregate.post.exception.PostNotFoundException;
import com.hw.aggregate.post.exception.PostUnsupportedSortOrderException;
import com.hw.aggregate.reaction.exception.*;
import com.hw.shared.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.PersistenceException;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DomainExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {
            CommentNotFoundException.class,
            PostNotFoundException.class,
            ReactionNotFoundException.class,
            PostUnsupportedSortOrderException.class,
            CommentUnsupportedSortOrderException.class,
            DataIntegrityViolationException.class,
            ReferenceServiceNotFoundException.class,
            ReferenceNotFoundException.class,
            FieldValidationException.class,
            SQLIntegrityConstraintViolationException.class,
            InsertViolationException.class,
            PersistenceException.class,
            ReactionTypeNotFoundException.class
    })
    protected ResponseEntity<?> handle400Exception(RuntimeException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(ex);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Error-Id", errorMessage.errorId);
        return handleExceptionInternal(ex, errorMessage, httpHeaders, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {
    })
    protected ResponseEntity<?> handle500Exception(RuntimeException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(ex);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Error-Id", errorMessage.errorId);
        return handleExceptionInternal(ex, errorMessage, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {
            PostAccessException.class,
            CommentAccessException.class
    })
    protected ResponseEntity<?> handle403Exception(RuntimeException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(ex);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Error-Id", errorMessage.errorId);
        return handleExceptionInternal(ex, errorMessage, httpHeaders, HttpStatus.FORBIDDEN, request);
    }
}
