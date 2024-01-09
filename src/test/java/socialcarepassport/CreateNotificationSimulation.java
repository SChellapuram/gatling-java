package socialcarepassport;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class CreateNotificationSimulation extends Simulation {

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://38v0wv1juh.execute-api.eu-west-2.amazonaws.com")
            .contentTypeHeader("application/json")
            .acceptHeader("application/json")
            .authorizationHeader("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyMzk5ODYxNjMzNzU0NjU0NzkiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.DIt6l_1zKQ0wY5lL_ue6_KljYjs9noTEgbZj8AXEG8Q");
    private final String userId = "C8059AD1-C985-43B7-9E96-525240956E86";

    private final String notificationBody = "{\"userId\": \"" + userId + "\"," +
            "\"title\": \"Congratulations! You have been accredited for Oliver McGowan Mandatory Training\"," +
            "\"message\": \"You have been accredited for Oliver McGowan Mandatory Training by e-Learning for Healthcare\"," +
            "\"meta\": {\"employeeId\": \"iqwjeoi213\",\"aisojdi\": \"123\",\"saodj\": {\"ioasjd\": 23}}}"; // Extracted notification body

    private final ScenarioBuilder scn = scenario("Create Notification")
            .exec(http("Post Request")
                    .post("/qa/notification/create")
                    .body(StringBody(notificationBody))
                    .asJson()
                    .check(bodyString().saveAs("CreateNotification"))
                    .check(jsonPath("$.userId").is(userId))
            ).exec(session -> {
                String createdNotification = session.get("CreateNotification");
                System.out.println("Notification is : " + createdNotification);
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
