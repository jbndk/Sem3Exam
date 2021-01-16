package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.Booking;
import errorhandling.API_Exception;
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
import facades.Facade;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

@Path("hotel")
public class HotelResource {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final ExecutorService es = Executors.newCachedThreadPool();
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    
    private static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);
    private static final Facade FACADE = Facade.getFacade(EMF);
    
    @Context
    SecurityContext securityContext;
    
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllHotels() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        String result = Facade.getAllHotels(gson);
        return result;
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getHotel(@PathParam("id") String id) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        String result = FACADE.getHotel(id, es, gson);
        return result;
    }
    
    @RolesAllowed("user")
    @GET
    @Path("mybookings/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getMyBookings(@PathParam("username") String username) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        List<Booking> result = FACADE.getMyBookings(username);
        
        List<String> bookings = new ArrayList<>();
        
        result.forEach(booking -> {
            bookings.add(booking.toString());
        });
        
        return bookings;
    }    
    
    @RolesAllowed("user")
    @POST
    @Path("book")
    @Produces(MediaType.APPLICATION_JSON)
    public Response bookHotel(String jsonString) throws IOException, InterruptedException, ExecutionException, TimeoutException, API_Exception {
        String username;
        String hotelID;
        String cardNumber;
        String startDate;
        String numberOfNights;
        String price;
        
        try {
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            username = json.get("username").getAsString();
            hotelID = json.get("hotelID").getAsString();
            cardNumber = json.get("cardNumber").getAsString();
            startDate = json.get("startDate").getAsString();
            numberOfNights = json.get("numberOfNights").getAsString();
            price = json.get("price").getAsString();

        } catch (Exception e) {
           throw new API_Exception("One or more fields are incorrect/missing",400,e);
        }
            
            Booking booking = new Booking(hotelID, cardNumber, startDate, numberOfNights, price);
            
            String message = FACADE.newBooking(username, booking, es, gson);
            
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("username", username);
            responseJson.addProperty("hotelID", hotelID);
            responseJson.addProperty("message", message);
            
            return Response.ok(new Gson().toJson(responseJson)).build();
    }
}
