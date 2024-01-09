package socialcarepassport;

import java.util.Random;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class CreateUserSimulation extends Simulation {
    Random random = new Random();
    long min = 100000000000000000L; // Minimum 18-digit number
    long max = 999999999999999999L; // Maximum 18-digit number

    String[] firstNames = {"John", "Alice", "Michael", "Emily", "William"};
    String[] lastNames = {"Smith", "Johnson", "Brown", "Davis", "Wilson"};
    String[] role = {"employee", "training provider"};

    String randomFirstName = "Test " + firstNames[random.nextInt(firstNames.length)];
    String randomLastName = lastNames[random.nextInt(lastNames.length)];
    String randomEmail = "randomuser" + random.nextInt(1000) + "@" + "servita.com";
    long randomValue = min + ((long) (random.nextDouble() * (max - min)));


    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://38v0wv1juh.execute-api.eu-west-2.amazonaws.com")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .authorizationHeader("Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjI0ODY5NzY4NTAxODcwMjg3NyIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2h1Yi1jYXJlLWlkLWU3aWxrbS56aXRhZGVsLmNsb3VkIiwic3ViIjoiMjQ0NzcxNTM2MjQ0ODg2MDM0IiwiYXVkIjpbIjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiMjQ0MjEzOTA1MzQwMDA1NzI0Il0sImV4cCI6MTcwNDc3Nzk1MSwiaWF0IjoxNzA0NzM0NzUxLCJub25jZSI6IjZOTWEwa0xjWXRuZyIsImF6cCI6IjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiY2xpZW50X2lkIjoiMjQ0MjE0MjE0MDU3NjIyODc2QGh1Yl93aXRoX2NhcmVfaWQiLCJhdF9oYXNoIjoiQkswblZ3ODhVbl9VN08tYzRlc1pNUSIsImNfaGFzaCI6IlRfLWpiQmdJejQ0bmllbVVRazFfYkEifQ.pjpaC63cz1hdzeIVHuM2ODYRm5c9CLrUGr1I2rRDEs0qvANLgHnjit5uq_j9jF7TgFhrv8uOWYpczCucwprNnjQoA1tXHzxAbEBagBdPdCGYqFR9mGzEcL74wlAxEAMS7ZoaG10yqIFE35NmvzthRLqH-9NjgRU7-BaNj9GJdD8--WzaHdc4W98FNA-5KJZ447-NG8nQ9MIiYjRPfkq2a_kdDONrjpPaX-O1qOLOfUKujDMLc4TfHcPcLYaHnnyVZVlLsF3zAWQMl-zFnEjfhlJp8GMppIPpSJ_gDuulwcY69Y6bd7Xx_c0Ig_3Fe7CeTt6SuuR6ZL17_M9YEe6v0w");

    String requestBody = "{\n" +
            "    \"zitadelId\": \"" + randomValue + "\",\n" +
            "    \"firstName\": \"" + randomFirstName + "\",\n" +
            "    \"lastName\": \"" + randomLastName + "\",\n" +
            "    \"fullName\": \"" + randomFirstName + " " + randomLastName + "\",\n" +
            "    \"email\": \"" + randomEmail + "\",\n" +
            "    \"phone\": \"+447986260490\",\n" +
            "    \"role\": \"employee\",\n" +
            "    \"address\": {\n" +
            "        \"line1\": \"3 Woodway\",\n" +
            "        \"town\": \"Long Compton\",\n" +
            "        \"county\": \"Warwickshire Cotswolds\",\n" +
            "        \"postcode\": \"CV36 5BJ\"\n" +
            "    }\n" +
            "}";

    private final ScenarioBuilder scn = scenario("Create User")
            .exec(
                    http("Post Request")
                            .post("/qa/user/create")
                            .body(StringBody(requestBody)).asJson()
                            .check(bodyString().saveAs("CreateUser"))
                            .check(status().is(201))
            ).exec(session -> {
                System.out.println("Request Body: " + requestBody);

                // Retrieve and print BookedCourseList
                String createdUser = session.get("CreateUser");
                System.out.println("Created User : " + createdUser);
                return session;
            });


    {
        setUp(
                scn.injectOpen(
                        constantUsersPerSec(1).during(1)
                )
        ).protocols(httpProtocol);
    }
}
