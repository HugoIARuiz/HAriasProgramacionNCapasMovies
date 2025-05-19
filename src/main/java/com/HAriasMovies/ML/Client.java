
package com.HAriasMovies.ML;

public class Client {
    private String username;
    private String password;
    private String request_token;
    private String account_id;

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

    public String getRequest_token() {
        return request_token;
    }

    public void setRequest_token(String request_token) {
        this.request_token = request_token;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public Client() {
    }

    public Client(String username, String password, String request_token, String account_id) {
        this.username = username;
        this.password = password;
        this.request_token = request_token;
        this.account_id = account_id;
    }
    
    
}
