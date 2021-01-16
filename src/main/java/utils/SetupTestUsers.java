  
package utils;

import dtos.UserDTO;
import entities.Booking;
import entities.Creditcard;
import entities.Role;
import entities.User;
import errorhandling.AlreadyExistsException;
import errorhandling.MissingInputException;
import facades.Facade;
import facades.UserFacade;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestUsers {

    public static void main(String[] args) throws MissingInputException, AlreadyExistsException {



        // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
        // CHANGE the three passwords below, before you uncomment and execute the code below
        // Also, either delete this file, when users are created or rename and add to .gitignore
        // Whatever you do DO NOT COMMIT and PUSH with the real passwords
        
        //Favourite favor = new Favourite("Germany");
        
        //em.getTransaction().begin();
        //em.persist(favor);
        //em.getTransaction().commit();
        
        //UserFacade FACADE = UserFacade.getUserFacade(emf);
        
        //FACADE.getFavorites("user");
        
        

        //FACADE.addFavourite("belgium", "user");
        
        //System.out.println(FACADE.newUser("nybruger", "claes"));
        //FACADE.addFavourite("greece", "user");
        //FACADE.addFavourite("ireland", "user");
        
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        Facade FACADE = Facade.getFacade(emf);
        UserFacade userFacade = UserFacade.getUserFacade(emf);
                
        User user1 = new User("user1", "test1", "user one", "12345678");
        User user2 = new User("user2", "test2", "user two", "22345678");
        User user3 = new User("user3", "test3", "user three", "32345678");
        User user4 = new User("user4", "test4", "user four", "42345678");
        User user5 = new User("user5", "test5", "user five", "52345678");
        
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");

        Booking b1 = new Booking("4042", "1234", "2021-02-25", "5", "200");
        Booking b2 = new Booking("4042", "1234", "2021-08-20", "3", "350");
        
        Creditcard c1 = new Creditcard("Visa", "321", "2022-05-28", "user1");
        
        em.getTransaction().begin();
        
        User u4 = em.find(User.class, "user1");

        //System.out.println(FACADE.getMyBookings("user1"));
        
        //u1.addBooking(b1);
        //u1.addBooking(b2);
        u4.addCreditcard(c1);
        
        //em.persist(b1);
        //em.persist(b2);

        /*
        //FIRST TIME SET-UP:
        user1.addRole(userRole);
        user1.addRole(adminRole);
        user2.addRole(userRole);
        user3.addRole(userRole);
        user4.addRole(userRole);
        user5.addRole(userRole);
        
        em.persist(userRole);
        em.persist(adminRole);
        
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);
        em.persist(user5);
        */
        
        em.getTransaction().commit();
    }

}