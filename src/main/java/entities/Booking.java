/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Jonas
 */
@Entity
public class Booking implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    String hotelID;
    String cardNumber;
    String startDate;    
    String numberOfNights;
    String price;
    
    @JoinColumn(name = "user")
    @ManyToOne
    private User user;
    
    public Booking() {
    }
    
    public Booking(String hotelID, String cardNumber, String startDate, String numberOfNights, String price) {
        this.hotelID = hotelID;
        this.cardNumber = cardNumber;
        this.startDate = startDate;
        this.numberOfNights = numberOfNights;
        this.price = price;
    }
    
    public String getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(String numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getHotelID() {
        return hotelID;
    }

    public void setHotelID(String hotelID) {
        this.hotelID = hotelID;
    }
    
        public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
