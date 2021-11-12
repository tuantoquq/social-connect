package com.company.socialadapter.exception;

import com.company.socialadapter.dto.builder.Response;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SocialAdapterExceptionHandler extends ResponseEntityExceptionHandler {

    private final Gson gson = new Gson();
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response response = new Response.Builder(HttpStatus.METHOD_NOT_ALLOWED.value())
                .buildMessage("Http request method not supported")
                .build();
        return buildResponse(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response response = new Response.Builder(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .buildMessage("Http media type not supported")
                .build();
        return buildResponse(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response response = new Response.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .buildMessage("Http media type not acceptable")
                .build();
        return buildResponse(response);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response response = new Response.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .buildMessage("Missing path variable")
                .build();
        return buildResponse(response);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response response = new Response.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .buildMessage("Missing servlet request parameter")
                .build();
        return buildResponse(response);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response response = new Response.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .buildMessage("Method Argument not valid")
                .build();
        return buildResponse(response);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Response response = new Response.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .buildMessage("Missing servlet request part")
                .build();
        return buildResponse(response);
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    protected ResponseEntity<Object> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        Response response = (new Response.Builder(HttpStatus.EXPECTATION_FAILED.value())).buildMessage("File too large!").build();
        return buildResponse(response);
    }
    protected ResponseEntity<Object> buildResponse(Response response){
        return new ResponseEntity<>(gson.toJson(response), HttpStatus.BAD_REQUEST);
    }

}
