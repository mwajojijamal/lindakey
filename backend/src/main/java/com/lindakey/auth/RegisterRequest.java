package com.lindakey.auth;

public class RegisterRequest {
	private String username;
	private String email;
	private String password;
	
	public RegisterRequest() {}
	
	public String getUsername() {
		return username;
	}
    public void setUsername(String username) {this.username = username;
    
    }
    
    public String getEmail() {return email;}
    public void getEmail(String email) {this.email = email;}
    
    
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    
    
}
