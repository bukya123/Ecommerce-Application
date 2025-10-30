package com.example.ecommercebackend.ControllerAdivce;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



public class APIException extends RuntimeException{
    public APIException() {
    }
    public APIException(String message){
        super(message);
    }
}
