package dtos;

import entities.Currencies;
import java.util.List;

/**
 *
 * @author claes
 */

public class DestinationDTO {
    
    private String name;
    private String alpha2Code;
    private String capital;
    private List<DestinationDTO> destinations;
    private List<Currencies> currencies;
    private Long population;
    private double infectionRate;

    public DestinationDTO() {
    }

    public DestinationDTO(String name, String alpha2Code, String capital, Long population, List<Currencies> currencies) {
        this.name = name;
        this.alpha2Code = alpha2Code;
        this.capital = capital;
        this.currencies = currencies;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlpha2Code() {
        return alpha2Code;
    }

    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public List<DestinationDTO> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<DestinationDTO> destinations) {
        this.destinations = destinations;
    }
    
    public List<Currencies> getCurrencies() {
        return currencies;
    }
    
    public void setCurrencies(List<Currencies> currencies) {
        this.currencies = currencies;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public double getInfectionRate() {
        return infectionRate;
    }

    public void setInfectionRate(double infectionRate) {
        this.infectionRate = infectionRate;
    }
    
    
    
    
    
    
}
