package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.UserDTO;
import dtos.UserInfoDTO;
import entities.Favourite;
import entities.Role;
import entities.User;
import entities.UserInfo;
import errorhandling.AlreadyExistsException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import security.errorhandling.AuthenticationException;

public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public UserInfoDTO addUserInfo(UserInfoDTO userinfodto, String userName) throws MissingInputException {

        if (userinfodto.getMail().length() == 0 || (userinfodto.getTelno().length() == 0)) {
            throw new MissingInputException("One or both values are missing");
        }

        EntityManager em = emf.createEntityManager();

        try {

            User user = em.find(User.class, userName);

            UserInfo info = new UserInfo(userinfodto.getMail(), userinfodto.getTelno());

            info.setUser(user);

            em.getTransaction().begin();
            em.persist(info);
            em.getTransaction().commit();

            return new UserInfoDTO(info.getEmail(), info.getPhone());
        } finally {
            em.close();
        }
    }



    public UserDTO newUser(UserDTO userDTO) throws MissingInputException, AlreadyExistsException {
        User newuser = new User(userDTO.getUserName(), userDTO.getUserPass());

        EntityManager em = emf.createEntityManager();
        
        //Role userRole = new Role("user");
        Role userRole = em.find(Role.class, "user");

        if (userDTO.getUserName().length() == 0 || (userDTO.getUserPass().length() == 0)) {
            throw new MissingInputException("One or both values are missing");
        } 
        if (em.find(User.class, newuser.getUserName())!= null) {
            throw new AlreadyExistsException("User already exists");
        }
        
        else {

            try {

                em.getTransaction().begin();
                newuser.addRole(userRole);
                em.persist(newuser);
                em.getTransaction().commit();
                UserDTO newUserDTO = new UserDTO(newuser);
                return newUserDTO;
            } finally {
                em.close();
            }
        }

    }

    public UserDTO deleteUser(String userName) throws NotFoundException {

        EntityManager em = emf.createEntityManager();
        try {
            User user = em.find(User.class, userName);

            if (user != null) {
                em.getTransaction().begin();
                em.remove(user);
                em.getTransaction().commit();
                return new UserDTO(user);

            } else {
                throw new NotFoundException("User not found");
            }
        } finally {
            em.close();
        }
    }
    
public List<UserDTO> getAllUsers() {
        EntityManager em = emf.createEntityManager();

        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u INNER JOIN u.roleList Role WHERE Role.roleName = :userType", User.class);
            query.setParameter("userType", "user");
            List<User> users = query.getResultList();
            List<UserDTO> userDTOs = new ArrayList();
            users.forEach((User user) -> {
                userDTOs.add(new UserDTO(user));
            });
            return userDTOs;
        } finally {
            em.close();
        }
    }

public String changeToAdmin(String userName) throws NotFoundException {
            
        EntityManager em = emf.createEntityManager();
        
        try {
            User user = em.find(User.class, userName);

            if (user != null) {
                
                em.getTransaction().begin();
                Role adminRole = new Role("admin");
                user.addRole(adminRole);
                em.getTransaction().commit();
                return user.getUserName();

            } else {
                throw new NotFoundException("User not found");
            }
        } finally {
            em.close();
        }
}

}
