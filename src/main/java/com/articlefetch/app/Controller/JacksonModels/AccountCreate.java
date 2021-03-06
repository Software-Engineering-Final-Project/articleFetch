package com.articlefetch.app.Controller.JacksonModels;

/** AccountCreate is mapped to a Jackson Object and is reserved only for creating an account. The main difference
 *   between an Account and a AccountCreate object is that the AccountCreate object does not have an id of a image.
 */
public class AccountCreate {

    private Integer id;
    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String email;
    private String path;
    private boolean status;

    public AccountCreate(String username, String password, String first_name, String last_name, String email,
                         String path, boolean status) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.username = username;
        this.id = null;
        this.path = path;
        this.status = status;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean bool){
        this.status = bool;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
