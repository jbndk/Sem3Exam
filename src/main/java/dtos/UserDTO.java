package dtos;

import entities.User;

/**
 *
 * @author am
 */
public class UserDTO {
    
    private String userName;
    private String userPass;

    public UserDTO(String userName, String userPass) {
        this.userName = userName;
        this.userPass=userPass;
    }

    public UserDTO(String userName) {
        this.userName = userName;
    }
    
    
    public UserDTO (User user){
        this.userName=user.getUserName();
        this.userPass=user.getUserPass();
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
    
    
    
    
}
