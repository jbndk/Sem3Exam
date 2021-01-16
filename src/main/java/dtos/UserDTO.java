package dtos;

import entities.User;

/**
 *
 * @author am
 */
public class UserDTO {
    
    private String userName;
    private String userPass;
    private String name;
    private String phone;

    public UserDTO(String userName, String userPass, String name, String phone) {
        this.userName = userName;
        this.userPass = userPass;
        this.name = name;
        this.phone = phone;
    }

    public UserDTO(String userName) {
        this.userName = userName;
    }
    
    public UserDTO (User user){
        this.userName=user.getUserName();
        this.userPass=user.getUserPass();
        this.name=user.getName();
        this.phone=user.getPhone();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
}
