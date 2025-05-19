package com.HAriasMovies.ML;

public class RequestLogin {

    public boolean succes;
    public String request_token;
    public int status_code;
    public String session_id;

    public boolean isSucces() {
        return succes;
    }

    public void setSucces(boolean succes) {
        this.succes = succes;
    }

    public String getRequest_token() {
        return request_token;
    }

    public void setRequest_token(String request_token) {
        this.request_token = request_token;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public RequestLogin() {
    }

    public RequestLogin(boolean succes, String request_token, int status_code, String session_id) {
        this.succes = succes;
        this.request_token = request_token;
        this.status_code = status_code;
        this.session_id = session_id;
    }
    
    

}
