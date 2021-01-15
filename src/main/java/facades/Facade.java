/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.JokeDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
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

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    final static String DESTINATION_SERVER = "https://api.chucknorris.io/jokes/random?category=";

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

                    JokeDTO joke = new JokeDTO(gson.fromJson(fetchResult, JokeDTO.class).getValue(), category);

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