package com.articlefetch.app.Busniess.DTO;

import com.articlefetch.app.Controller.JacksonModels.Account;
import com.articlefetch.app.Controller.JacksonModels.JacksonObject;
import com.articlefetch.app.DataAccess.ModelDomain.AccountEntity;
import com.articlefetch.app.DataAccess.ModelDomain.Entity;

public class AccountDTO implements DTO {
    public Integer id;
    public String username;
    public String password;
    public String first_name;
    public String last_name;
    public String email;
    public String path;
    public boolean status;

    public AccountDTO(String username, String password, String first_name, String last_name, String email, Integer id,
                      String path, boolean status) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.username = username;
        this.id = id;
        this.path = path;
        this.status = status;
    }

    //TODO: Add implementation
    public AccountDTO(JacksonObject object){}

    public AccountDTO(Entity object){}

    @Override
    public JacksonObject convertToJackson() {
        return new Account(username, password, first_name, last_name, email, id, path, status);
    }

    @Override
    //TODO add path
    public Entity convertToModel() {
        return new AccountEntity().create(id, first_name,last_name, username, password, email, status);
    }


}