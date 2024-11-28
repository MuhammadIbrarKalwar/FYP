package com.example.fyp;

public class SignupResponse {
    private String message;
    User UserObject;


    // Getter Methods

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return UserObject;
    }

    // Setter Methods

    public void setMessage( String message ) {
        this.message = message;
    }

    public void setUser( User userObject ) {
        this.UserObject = userObject;
    }
}
class User {
    private String name;
    private String email;
    private String password;
    private String updated_at;
    private String created_at;
    private String id;


    // Getter Methods

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getId() {
        return id;
    }

    // Setter Methods

    public void setName( String name ) {
        this.name = name;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public void setPassword( String password ) {
        this.password = password;
    }

    public void setUpdated_at( String updated_at ) {
        this.updated_at = updated_at;
    }

    public void setCreated_at( String created_at ) {
        this.created_at = created_at;
    }

    public void setId( String id ) {
        this.id = id;
    }
}