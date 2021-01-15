  
package utils;

import entities.Favourite;
import entities.Role;
import entities.User;
import entities.UserInfo;
import errorhandling.AlreadyExistsException;
import errorhandling.MissingInputException;
import facades.UserFacade;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

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
                
        User user = new User("user", "test1");
        User admin = new User("admin", "test1");
        User both = new User("user_admin", "test1");
        User claes = new User("claes", "rufbtr1");
        User kunde = new User("kunde", "test22");
        User user1 = new User("user1", "test1");
        User user2 = new User("user2", "test2");
        User user3 = new User("user3", "test3");
        User user4 = new User("user4", "test4");
        User user5 = new User("user5", "test5");

        if (admin.getUserPass().equals("test") || user.getUserPass().equals("test") || both.getUserPass().equals("test")) {
            throw new UnsupportedOperationException("You have not changed the passwords");
        }

        em.getTransaction().begin();
        UserInfo inf = new UserInfo("claesvonh", "1234");
        inf.setUser(claes);
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        user.addRole(userRole);
        claes.addRole(userRole);
        kunde.addRole(userRole);
        admin.addRole(adminRole);
        both.addRole(userRole);
        both.addRole(adminRole);
        user1.addRole(userRole);
        user2.addRole(userRole);
        user3.addRole(userRole);
        user4.addRole(userRole);
        user5.addRole(userRole);
        em.persist(claes);
        em.persist(kunde);
        em.persist(userRole);
        em.persist(adminRole);
        em.persist(user);
        em.persist(admin);
        em.persist(both);
        em.persist(inf);
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);
        em.persist(user5);
        //em.persist(fav);
        em.getTransaction().commit();
        System.out.println("PW: " + user.getUserPass());
        System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
        System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
        System.out.println("Created TEST Users");

        //FACADE.addFavourite("Denmark", "user");
        /*
                
                //Favourite fav = em.find(Favourite.class, "denmark");
<<<<<<< HEAD
                
=======

                

>>>>>>> 01705eaa8783ee5a6907e8ff242afddb551da7a4
                
                //Favourite favourite = new Favourite("denmark");
                
                TypedQuery<Favourite> query = em.createQuery("SELECT f FROM Favourite f WHERE f.countryName = :country", Favourite.class);
                query.setParameter("country", "norway");
                Favourite favourite = query.getSingleResult();
                             
                User user = em.find(User.class, "user4");
                
                //user.removeFavourite(favourite);                
                
                em.getTransaction().begin();
                //user.getFavourites().remove(favourite);
                user.removeFavourite(favourite);
                em.getTransaction().commit();
                em.close();
*/
    }

}