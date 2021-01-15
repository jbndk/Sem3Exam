package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Favourite;
import errorhandling.AlreadyExistsException;
import errorhandling.MissingInputException;
import facades.UserFacade;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import facades.DestinationFacade;
import facades.Facade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import utils.EMF_Creator;

@Path("destination")
public class DestinationResource {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final ExecutorService es = Executors.newCachedThreadPool();
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);
    private static final Facade FACADE = Facade.getFacade(EMF);
    private static final DestinationFacade DESTINATIONFACADE = DestinationFacade.getDestinationFacade(EMF);
    
    
    @GET
    @Path("joke/{categories}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJokeByCategory(@PathParam("categories") String categories) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        String result = FACADE.getJokeByCategory(categories, es, gson);
        return result;
    }
    
    @GET
    @Path("open/favourites/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getFavorites(@PathParam("user") String user) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        List<Favourite> result = DESTINATIONFACADE.getFavorites(user);
        ArrayList<String> stringResult = new ArrayList<String>();
        
        for (Favourite favourite : result) {
            stringResult.add(favourite.getCountryName());
        }
        
        return stringResult;
    }
    
    @GET
    @Path("open/{country}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getOpenDestination(@PathParam("country") String country) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        String result = DestinationFacade.getDestination(country, es, gson);
        return result;
    }
    
    @GET
    @Path("restricted/{country}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public String getRestrictedDestination(@PathParam("country") String country) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        String result = DestinationFacade.getDestination(country, es, gson);
        return result;
    }
    
    @POST
    @Path("open/{country}/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String saveFavourite(@PathParam("country") String country, @PathParam("userName") String userName) throws IOException, InterruptedException, ExecutionException, TimeoutException, MissingInputException, AlreadyExistsException {
        String result = gson.toJson(DESTINATIONFACADE.addFavourite(country, userName));
        return result;
    }
    
    @DELETE
    @Path("open/{country}/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteFavourite(@PathParam("country") String country, @PathParam("userName") String userName) throws IOException, InterruptedException, ExecutionException, TimeoutException, MissingInputException, AlreadyExistsException {
        String result = gson.toJson(DESTINATIONFACADE.deleteFavourite(country, userName));
        return result;
    }
    
}
