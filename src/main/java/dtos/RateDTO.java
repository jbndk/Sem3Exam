package dtos;

/**
 *
 * @author claes
 */
public class RateDTO {
    
    public Double rate;

    public RateDTO(Double rate) {
        this.rate = rate;
    }

    public RateDTO() {
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "RatesDTO: " + rate;
    }

    
    
    

}