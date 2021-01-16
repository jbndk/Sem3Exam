
package facades;

import dtos.UserDTO;
import entities.Role;
import entities.User;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import utils.EMF_Creator;

/**
 *
 * @author Jonas
 */

//@Disabled
public class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;
    private static User u1, u2, u3;
    private static Role r1;

    public UserFacadeTest() {

    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);        

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            
        u1 = new User("user11", "test11", "user11", "123");
        u2 = new User("user12", "test12", "user12", "456");
        u3 = new User("user13", "test13", "user13", "789");
        
        r1 = new Role("user");
        
        u1.addRole(r1);
        u2.addRole(r1);
        u3.addRole(r1);

            em.persist(u1);
            em.persist(u2);
            em.persist(u3);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterAll
    public static void tearDwonClass() {

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testGetUserFacade() {
        EntityManagerFactory emf = null;
        UserFacade expectedResult = null;
        UserFacade result = UserFacade.getUserFacade(emf);
        assertNotEquals(expectedResult, result);
    }
    
    @Test
    public void testGetAllUsers() {
        int expectedCount = 3;
        int actual = facade.getAllUsers().size();
        assertEquals(expectedCount, actual);
    }

    @Test
    public void testDeleteUser() throws NotFoundException {
        int expectedCount = 0;

        facade.deleteUser(u2.getUserName());
        List<UserDTO> allUsers = new ArrayList<>();
        allUsers = facade.getAllUsers();
        int actualCount = allUsers.size();

        assertEquals(expectedCount, actualCount);

    }
}