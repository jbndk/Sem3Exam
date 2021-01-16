package rest;

import entities.Creditcard;
import entities.User;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import utils.EMF_Creator;

/**
 *
 * @author Jonas
 */

@Disabled
public class HotelResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/sem3/api/";

    private static User u1, u2;
    private static Creditcard c1, c2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

        EntityManager em = emf.createEntityManager();

        u1 = new User("Hans", "test123", "Hansen Hansen", "12345678");
        u2 = new User("Grethe", "test321", "Grethe Larsen", "87654321");

        c1 = new Creditcard("Visa", "1234", "2022-05-28", "Hans");
        c2 = new Creditcard("Visa", "4321", "2023-05-28", "Grethe");

        try {
            u1.addCreditcard(c1);
            u2.addCreditcard(c2);

            em.persist(u1);
            em.persist(u2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private static String securityToken;

    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                .when().post("/login")
                .then()
                .extract().path("token");
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @Test
    public void testBookHotelSuccessfully() {
        String json = String.format("{\"username\":\"Hans\",\"hotelID\":\"4042\",\"cardNumber\":\"1234\",\"startDate\":\"2021-09-14\",\"numberOfNights\":\"5\",\"price\":\"280\"}");
        login("Hans", "test123");
        given()
                .header("x-access-token", securityToken)
                .contentType("application/json")
                .body(json)
                .when()
                .post("/hotel/book").then()
                .statusCode(200)
                .body("msg", equalTo("Booking successfully made!"));
    }

    
    @Test
    public void testBookHotelInvalidCardNumber() {
        String json = String.format("{\"username\":\"Hans\",\"hotelID\":\"4042\",\"cardNumber\":\"1561\",\"startDate\":\"2021-09-14\",\"numberOfNights\":\"5\",\"price\":\"280\"}");
        login("Hans", "test123");
            given()
                .header("x-access-token", securityToken)                  
                .contentType("application/json")
                .body(json)
                .when()
                .post("/hotel/book").then()
                .statusCode(200)
                .body("msg", equalTo("Invalid creditcard number"));
    }
    
    @Test
    public void testBookHotelInvalidUser() {
        String json = String.format("{\"username\":\"Hans\",\"hotelID\":\"4042\",\"cardNumber\":\"4321\",\"startDate\":\"2021-09-14\",\"numberOfNights\":\"5\",\"price\":\"280\"}");
        login("Hans", "test123");
            given()
                .header("x-access-token", securityToken)                  
                .contentType("application/json")
                .body(json)
                .when()
                .post("/hotel/book").then()
                .statusCode(200)
                .body("msg", equalTo("The credit card with number 4321 does not belong to this user"));
    } 
    
}