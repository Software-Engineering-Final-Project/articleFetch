package com.articlefetch.app.Controller.JacksonModels;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class AccountStatus{

    public String message;

    @ResponseStatus(code = HttpStatus.CREATED)
    public AccountStatus accountQuerySuccess() {
        this.message = "Action was successfully complete";
        return this;
    }

    @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
    public AccountStatus accountFailed(Exception e){
        this.message = "There was an error creating your account: " + e.getMessage();
        return this;
    }


    public String getMessage() {
        return message;
    }
}