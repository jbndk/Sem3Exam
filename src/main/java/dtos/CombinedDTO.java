package dtos;

import entities.Currencies;
import java.util.List;

/**
 *
 * @author Jonas
 */
public class CombinedDTO {
    
    private String name;
    private String capital;
    private Long population;
    private List<DestinationDTO> destinations;
    private Currencies currencies;
    private Double fxRate;
    private String last_update;
    private Long cases;
    private Long deaths;
    private Long recovered;
    private double infectionRate;
    private RateDTO ratedto;  
    

    public CombinedDTO(DestinationDTO destinationDTO, RateDTO ratedto, CovidInfoDTO covidinfoDTO) {
        this.name = destinationDTO.getName();
        this.capital = destinationDTO.getCapital();
        this.population = destinationDTO.getPopulation();
        this.infectionRate = destinationDTO.getInfectionRate();
        this.destinations = destinationDTO.getDestinations();
        this.currencies = destinationDTO.getCurrencies().get(0);
        this.fxRate = ratedto.getRate();
        this.last_update = covidinfoDTO.getLastUpdate();
        this.cases = covidinfoDTO.getCases();
        this.deaths = covidinfoDTO.getDeaths();
        this.recovered = covidinfoDTO.getRecovered();
        
       
        
    }

    public CombinedDTO() {
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Currencies getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Currencies currencies) {
        this.currencies = currencies;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public Long getDeaths() {
        return deaths;
    }

    public void setDeaths(Long deaths) {
        this.deaths = deaths;
    }

    public String getLastUpdate() {
        return last_update;
    }

    public void setLastUpdate(String lastUpdate) {
        this.last_update = lastUpdate;
    }

    public Long getCases() {
        return cases;
    }

    public void setCases(Long cases) {
        this.cases = cases;
    }

    public Long getRecovered() {
        return recovered;
    }

    public void setRecovered(Long recovered) {
        this.recovered = recovered;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public double getInfectionRate() {
        return infectionRate;
    }

    public void setInfectionRate(Long infectionRate) {
        this.infectionRate = infectionRate;
    }
    
    
    
    
    
    
    
    

   


}
