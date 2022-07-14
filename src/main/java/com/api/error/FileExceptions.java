package com.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
@ResponseStatus
public class FileExceptions extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(("Please upload a file with size less than 10Mb!"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity productNotFoundException(ProductNotFoundException e){
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found! Please check "+ message);
    }
//    @ExceptionHandler(ProductNotFoundException.class)
//    public ResponseEntity<ResponseMessage> productNotFoundException(ProductNotFoundException e){
//        ResponseMessage responseMessage = new ResponseMessage();
//        responseMessage.setResponse(e.getLocalizedMessage());
////        responseMessage.setPayload(e.g);
//        responseMessage.setResponseCode(HttpStatus.NOT_FOUND.value());
//
//        String message = e.getMessage();
//        // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found! Please check "+ message);
//        return new ResponseEntity<ResponseMessage>(responseMessage,HttpStatus.OK);
//    }



}
