package dtos;

/**
 *
 * @author claes
 */
public class CovidInfoDTO {
    private String country;
    private String last_update;
    private Long cases;
    private Long deaths;
    private Long recovered;


    public CovidInfoDTO(String country, String last_update, Long cases, Long deaths, Long recovered) {
        this.country = country;
        this.last_update = last_update;
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
    }

    
    public CovidInfoDTO() {
    }

    public String getLastUpdate() {
        return last_update;
    }

    public void setLastUpdate(String last_update) {
        this.last_update = last_update;
    }

    public Long getCases() {
        return cases;
    }

    public void setCases(Long cases) {
        this.cases = cases;
    }

    public Long getDeaths() {
        return deaths;
    }

    public void setDeaths(Long deaths) {
        this.deaths = deaths;
    }

    public Long getRecovered() {
        return recovered;
    }

    public void setRecovered(Long recovered) {
        this.recovered = recovered;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

}
