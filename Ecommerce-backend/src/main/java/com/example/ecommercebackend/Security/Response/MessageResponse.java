package com.example.ecommercebackend.Security.Response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {
    private String message;

    public MessageResponse(String msg) {
        this.message = msg;
    }
}
