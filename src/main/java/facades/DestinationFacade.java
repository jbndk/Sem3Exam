package facades;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.DestinationDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import utils.HttpUtils;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import dtos.CombinedDTO;
import dtos.CovidInfoDTO;
import dtos.RateDTO;
import dtos.UserDTO;
import entities.Favourite;
import entities.User;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author claes
 */
public class DestinationFacade {

    private static EntityManagerFactory emf;
    private static DestinationFacade instance;

    private DestinationFacade() {

    }

    final static String DESTINATION_SERVER = "https://restcountries.eu/rest/v2/name/";
    final static String RATES_SERVER = "https://api.exchangeratesapi.io/latest?base=USD&symbols=";
    final static String COVID_SERVER = "https://covid19-api.org/api/status/";

    static String currencyCode;
    static String countryCode;

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static DestinationFacade getDestinationFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DestinationFacade();
        }
        return instance;
    }

    public static double calculateInfectionRate(double population, double cases) {
        double rate = (cases / population) * 100;
        return Math.round(rate * 100.0) / 100.0;
    }

    public static String getDestination(String country, ExecutorService threadPool, final Gson gson) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        Callable<DestinationDTO> destTask = new Callable<DestinationDTO>() {
            @Override
            public DestinationDTO call() throws IOException {
                String dest = HttpUtils.fetchData(DESTINATION_SERVER + country);
                Type listCity = new TypeToken<ArrayList<DestinationDTO>>() {
                }.getType();
                ArrayList<DestinationDTO> cityArray = gson.fromJson(dest, listCity);

                currencyCode = cityArray.get(0).getCurrencies().get(0).getCode();
                countryCode = cityArray.get(0).getAlpha2Code();

                return cityArray.get(0);
            }
        };

        Future<DestinationDTO> futureDestination = threadPool.submit(destTask);

        DestinationDTO destinationDTO = futureDestination.get();

        RateDTO rate = getRate(currencyCode);

        CovidInfoDTO covidDTO = getCovidInfo(countryCode, threadPool, gson);

        double cases = (double) covidDTO.getCases();
        double population = (double) destinationDTO.getPopulation();

        destinationDTO.setInfectionRate(calculateInfectionRate(population, cases));

        CombinedDTO combinedDTO = new CombinedDTO(destinationDTO, rate, covidDTO);

        String combinedDTOString = gson.toJson(combinedDTO);

        return combinedDTOString;
    }

    public static CovidInfoDTO getCovidInfo(String countryAlpha, ExecutorService threadPool, final Gson gson) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        Callable<CovidInfoDTO> destTask = new Callable<CovidInfoDTO>() {
            @Override
            public CovidInfoDTO call() throws IOException {

                String rates = HttpUtils.fetchData(COVID_SERVER + countryAlpha);

                CovidInfoDTO covidDTO = gson.fromJson(rates, CovidInfoDTO.class);

                return covidDTO;
            }
        };

        Future<CovidInfoDTO> future = threadPool.submit(destTask);

        CovidInfoDTO result = future.get();

        return result;
    }

    public static RateDTO getRate(String code) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        String rates = HttpUtils.fetchData(RATES_SERVER + code);

        ObjectMapper objectMapper = new ObjectMapper();

        RateDTO ratedto = new RateDTO();

        try {
            JsonNode node = objectMapper.readValue(rates, JsonNode.class);
            JsonNode child = node.get("rates");
            JsonNode childField = child.get(code);
            Double rate = childField.asDouble();
            System.out.println(rate);
            ratedto.setRate(rate);
            System.out.println(ratedto);

        } catch (IOException e) {
        }
        return ratedto;

    }

    public List<Favourite> getFavorites(String user) {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Favourite> query = em.createQuery("select f FROM Favourite f INNER JOIN f.users User WHERE User.userName = :user", Favourite.class);
        query.setParameter("user", user);
        List<Favourite> resultList = query.getResultList();
        return resultList;
    }

    public String deleteFavourite(String country, String userName) throws MissingInputException {

        EntityManager em = emf.createEntityManager();
        
        try {
                
                TypedQuery<Favourite> query = em.createQuery("SELECT f FROM Favourite f WHERE f.countryName = :country", Favourite.class);
                query.setParameter("country", country);
                Favourite favourite = query.getSingleResult();
                             
                User user = em.find(User.class, userName);            
                
                em.getTransaction().begin();
                user.removeFavourite(favourite);
                em.getTransaction().commit();
                
        } finally {
            em.close();
        }
    
        return country;
    }

    public String addFavourite(String country, String userName) throws MissingInputException {

        String lowerCountry = country.toLowerCase();

        EntityManager em = emf.createEntityManager();

        if (lowerCountry.length() == 0 || (userName.length() == 0)) {
            throw new MissingInputException("One or both values are missing");
        }

        String returnString = "";
        
        Favourite favourite = new Favourite(lowerCountry);
        
        //Checks if favourite already exists in DB:
        try {
            
        TypedQuery<Favourite> query = em.createQuery("SELECT f FROM Favourite f WHERE f.countryName = :country", Favourite.class);
        query.setParameter("country", lowerCountry);
        Favourite result = query.getSingleResult();

        favourite = result;
        
        } catch (Exception e) {
            System.out.println("Failed to find favourite in DB");
        }
        
        List<Favourite> usersFavorites = getFavorites(userName);

        //Sikrer at vi kun sammenligner CountryName og ikke ID'et/key'en:
        boolean favoriteExists = usersFavorites.stream().anyMatch(o -> o.getCountryName().equals(lowerCountry));

        if (favoriteExists == true) {
            System.out.println(getFavorites(userName));
            returnString = "You have already saved the destination";
            return returnString;

        } else {

            try {
//
                User user = em.find(User.class, userName);

                user.addFavourite(favourite);

                em.getTransaction().begin();
                em.persist(user);
                em.getTransaction().commit();

                returnString = favourite.getCountryName();

                return returnString;
            } finally {
                em.close();
            }
        }

    }

}
