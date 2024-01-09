package socialcarepassport;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class GetNotificationsByCurrentUser extends Simulation {
    private FeederBuilder feeder = csv("data.csv").circular();
    private static final String BASE_URL = "https://38v0wv1juh.execute-api.eu-west-2.amazonaws.com";
    private static final String AUTH_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjI0ODY5NzY4NTAxODcwMjg3NyIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2h1Yi1jYXJlLWlkLWU3aWxrbS56aXRhZGVsLmNsb3VkIiwic3ViIjoiMjQ0NzcxNTM2MjQ0ODg2MDM0IiwiYXVkIjpbIjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiMjQ0MjEzOTA1MzQwMDA1NzI0Il0sImV4cCI6MTcwNDc3Nzk1MSwiaWF0IjoxNzA0NzM0NzUxLCJub25jZSI6IjZOTWEwa0xjWXRuZyIsImF6cCI6IjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiY2xpZW50X2lkIjoiMjQ0MjE0MjE0MDU3NjIyODc2QGh1Yl93aXRoX2NhcmVfaWQiLCJhdF9oYXNoIjoiQkswblZ3ODhVbl9VN08tYzRlc1pNUSIsImNfaGFzaCI6IlRfLWpiQmdJejQ0bmllbVVRazFfYkEifQ.pjpaC63cz1hdzeIVHuM2ODYRm5c9CLrUGr1I2rRDEs0qvANLgHnjit5uq_j9jF7TgFhrv8uOWYpczCucwprNnjQoA1tXHzxAbEBagBdPdCGYqFR9mGzEcL74wlAxEAMS7ZoaG10yqIFE35NmvzthRLqH-9NjgRU7-BaNj9GJdD8--WzaHdc4W98FNA-5KJZ447-NG8nQ9MIiYjRPfkq2a_kdDONrjpPaX-O1qOLOfUKujDMLc4TfHcPcLYaHnnyVZVlLsF3zAWQMl-zFnEjfhlJp8GMppIPpSJ_gDuulwcY69Y6bd7Xx_c0Ig_3Fe7CeTt6SuuR6ZL17_M9YEe6v0w";

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(BASE_URL)
            .acceptHeader("application/json")
            .authorizationHeader("Bearer " + AUTH_TOKEN);
    private final String userId = "C8059AD1-C985-43B7-9E96-525240956E86";
    private final ScenarioBuilder scn = scenario("ViewTrainingRecords")
            .feed(feeder)
            .exec(
                    http("Get Training Courses")
                            .get("/qa/notification/list?userId=#{CareWorker-UserId}")
                            .check(bodyString().saveAs("Get Notifications"))
                            .check(jsonPath("$[?(@.userId == '#{CareWorker-UserId}')]").exists())
                            .check(status().is(200))
            ).pause(6)
            .exec(session -> {
                // Retrieve and print Notifications List
                String getNotificationsList = session.get("Get Notifications");
                System.out.println("Notifications List for current user: " + getNotificationsList);
                return session;
            });

    {
        setUp(
                scn.injectOpen(
                        constantUsersPerSec(1).during(1)

                        /*
                        rampUsersPerSec(1).to(10).during(300), // Ramp up from 1 to 9 users per second over 5 minutes
                        constantUsersPerSec(18).during(1200), // Maintain a constant rate of 16 users per second for 20 minutes
                        rampUsers(10).during(300) // Ramp down maintaining 9 users per second for 5 minutes
                         */
                )
        ).protocols(httpProtocol);
    }
}