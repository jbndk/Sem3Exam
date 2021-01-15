package dtos;

import entities.UserInfo;


public class UserInfoDTO {
    
    private String mail;
    private String telno;

    public UserInfoDTO(String mail, String telno) {
        this.mail = mail;
        this.telno = telno;
    }

    public UserInfoDTO(UserInfo userinfo) {
        this.mail = userinfo.getEmail();
        this.telno = userinfo.getPhone();
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }
    
    
    
    
    
}
