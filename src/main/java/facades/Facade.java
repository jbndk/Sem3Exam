/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dtos.HotelDTO;
import dtos.HotelsDTO;
import dtos.JokeDTO;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.HttpUtils;

/**
 *
 * @author Jonas
 */
public class Facade {

    private static EntityManagerFactory emf;
    private static Facade instance;

    public static Facade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new Facade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    final static String DESTINATION_SERVER = "https://api.chucknorris.io/jokes/random?category=";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    final static String HOTEL_SERVER = "http://exam.cphdat.dk:8000/hotel/";

    public static String getAllHotels(ExecutorService threadPool, final Gson gson) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        
        //TODO: REFACTOR
        Callable<HotelsDTO> destTask = new Callable<HotelsDTO>() {
            @Override
            public HotelsDTO call() throws IOException {
                String dest = HttpUtils.fetchData(HOTEL_SERVER + "all");
                Type listHotel = new TypeToken<ArrayList<HotelDTO>>() {
                }.getType();
                ArrayList<HotelDTO> hotelArray = gson.fromJson(dest, listHotel);
                HotelsDTO hotels = new HotelsDTO();
                hotels.setHotels(hotelArray);
                return hotels;
            }
        };
        Future<HotelsDTO> futureDestination = threadPool.submit(destTask);
        HotelsDTO hotels2 = new HotelsDTO();
        try {
            hotels2 = futureDestination.get();
        } catch (ExecutionException ex) {
            Logger.getLogger(Facade.class.getName()).log(Level.SEVERE, null, ex);
        }
        String combinedDTOString = gson.toJson(hotels2);
        return combinedDTOString;

        /*
        //Solution with JSONArray:
        JSONArray jsonArray = new JSONArray(fetch);
        return IntStream.range(0, jsonArray.length())
                .mapToObj(index -> ((JSONObject) jsonArray.get(index)).optString(key))
                .collect(Collectors.toList());
         */
 /*
        //Solution with object mapper:
        ObjectMapper objectMapper = new ObjectMapper();
        
        ArrayList<HotelDTO> hotelArray = objectMapper.readValue(fetch, HotelDTO.class);
        List<HotelDTO> hotelList = new ArrayList(Arrays.asList(hotelArray));
        
        String hotels = hotelList.get(0).getId();
        
        return hotels;
         */
    }

    public static String getHotel(String id, ExecutorService threadPool, final Gson gson) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        Callable<HotelDTO> taskCallable = new Callable<HotelDTO>() {

            @Override
            public HotelDTO call() throws IOException {

                String fetch = HttpUtils.fetchData(HOTEL_SERVER + id);

                HotelDTO hotelDTO = gson.fromJson(fetch, HotelDTO.class
                );

                return hotelDTO;
            }
        };

        Future<HotelDTO> future = threadPool.submit(taskCallable);

        HotelDTO futureResult = future.get();

        String result = gson.toJson(futureResult);

        return result;

    }

    public static String getJokeByCategory(String categories, ExecutorService threadPool, final Gson gson) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        //Makes sure that we use lowerCase for DB:
        String categoriesLower = categories.toLowerCase();

        //Separates the 'categories' string by commas into a new array:
        String[] categoryArray = categoriesLower.split(",");

        //Creates a new list with the strings the 'categoryArray':
        List<String> categoryList = Arrays.asList(categoryArray);

        //Initialize a list for all jokeDTO:
        List<JokeDTO> jokeDTOs = new ArrayList();

        //Initialize a list for all callables:
        List<Callable> callableList = new ArrayList();

        //For each category:
        for (String category : categoryList) {

            //A callable is created:
            Callable<JokeDTO> cat = new Callable<JokeDTO>() {
                @Override
                public JokeDTO call() throws IOException {

                    String fetchResult = HttpUtils.fetchData(DESTINATION_SERVER + category);

                    JokeDTO joke = new JokeDTO(gson.fromJson(fetchResult, JokeDTO.class
                    ).getValue(), category);

                    return joke;
                }
            };

            //And added to the list of callables:
            callableList.add(cat);
        }

        //For each callable in the callable list:            
        for (Callable callable : callableList) {
            //The callable is submitted to the threadPool (ExecutorService) and a future is made:
            Future<JokeDTO> future = threadPool.submit(callable);
            //A jokeDTO is made from the future:
            JokeDTO joke = future.get();
            //And the jokeDTO is added to the list of jokeDTOs:
            jokeDTOs.add(joke);
        }

        //jokeDTOs list is made to JSON and = the result string:
        String result = gson.toJson(jokeDTOs);

        return result;
    }
}
