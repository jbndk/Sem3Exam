/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

/**
 *
 * @author Jonas
 */

public class JokeDTO {
    
    String value;
    String category;
    
    JokeDTO(){
    }

    public JokeDTO(String value, String category) {
        this.value = value;
        this.category = category;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
   
}
