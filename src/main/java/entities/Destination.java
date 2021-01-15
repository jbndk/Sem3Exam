package entities;

/**
 *
 * @author claes
 */
public class Destination {
    
    private String name;
    private String alpha2Code;
    private String capital;
    private Long population;

    public Destination() {
    }

    public Destination(String name, String alpha2Code, String capital, Long population) {
        this.name = name;
        this.alpha2Code = alpha2Code;
        this.capital = capital;
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

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    

    
    
    
}
