/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jonas
 */

public class HotelsDTO {
    
    private List<HotelDTO> hotels = new ArrayList();
    
    public HotelsDTO() {
    }
        public HotelsDTO(List<HotelDTO> hotels) {
        this.hotels = hotels;
    }
    public List<HotelDTO> getHotels() {
        return hotels;
    }
    public void setHotels(List<HotelDTO> hotels) {
        this.hotels = hotels;
    }
    
}
