/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dtos.HotelDTO;
import dtos.HotelsDTO;
import entities.Booking;
import entities.Creditcard;
import entities.User;
import errorhandling.API_Exception;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import utils.EMF_Creator;
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

    final static String HOTEL_SERVER = "http://exam.cphdat.dk:8000/hotel/";

    public static String getAllHotels(Gson gson) throws IOException, InterruptedException, ExecutionException, TimeoutException {

        String dest = HttpUtils.fetchData(HOTEL_SERVER + "all");
        Type listHotel = new TypeToken<ArrayList<HotelDTO>>() {
        }.getType();
        ArrayList<HotelDTO> hotelArray = gson.fromJson(dest, listHotel);
        HotelsDTO hotels = new HotelsDTO();
        hotels.setHotels(hotelArray);

        String result = gson.toJson(hotels);
        
        return result;

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

    public List<Booking> getMyBookings(String username) {

        EntityManager em = emf.createEntityManager();
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.user = :name", Booking.class);
        query.setParameter("name", username);
        List<Booking> bookings = query.getResultList();

        em.close();

        return bookings;
    }

    public static String newBooking(String username, Booking booking, ExecutorService threadPool, final Gson gson) throws IOException, InterruptedException, ExecutionException, TimeoutException, API_Exception {

        String returnString = "";

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            Creditcard creditCard = em.find(Creditcard.class, booking.getCardNumber());
            //Checks if the credit card belongs to the user:
            if (!creditCard.getUser().getUserName().equalsIgnoreCase(username)) {
                returnString = "The credit card with number " + booking.getCardNumber() + " does not belong to this user.";
                return returnString;
            }

        } catch (Exception e) {
            returnString = "Invalid creditcard number";
            return returnString;
        }

        try {
            //Checks if the user exists in DB:
            User user = em.find(User.class, username);
            user.addBooking(booking);
        } catch (Exception e) {
            returnString = "Invalid username";
            return returnString;
        }

        em.getTransaction().commit();

        returnString = "Booking successfully made!";

        return returnString;

    }
}
