package com.neosoft.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceNotFoundException extends Exception{
    private int statusCode;
    private String message;


}
