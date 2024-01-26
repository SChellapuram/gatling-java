package socialcarepassport;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.Duration;


import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


public class UploadPhotoSimulation extends Simulation {
    private static final String AUTH_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjI1MTI2MzUwNDg3MzQ3MTg0OSIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2h1Yi1jYXJlLWlkLWU3aWxrbS56aXRhZGVsLmNsb3VkIiwic3ViIjoiMjQ0NzcxNTM2MjQ0ODg2MDM0IiwiYXVkIjpbIjI1MDY5MTU4MTk0NDg2MjcwNUBodWJfd2l0aF9jYXJlX2lkIiwiMjUwNzEzNjU1NzQ0NzQ3NDAxQGh1Yl93aXRoX2NhcmVfaWQiLCIyNDQyMTQyMTQwNTc2MjI4NzZAaHViX3dpdGhfY2FyZV9pZCIsIjI0NDIxMzkwNTM0MDAwNTcyNCJdLCJleHAiOjE3MDYzMTUzNDcsImlhdCI6MTcwNjI3MjE0Nywibm9uY2UiOiJvbW9SYUljcEtXa28iLCJhenAiOiIyNDQyMTQyMTQwNTc2MjI4NzZAaHViX3dpdGhfY2FyZV9pZCIsImNsaWVudF9pZCI6IjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiYXRfaGFzaCI6Ik5tVUw1d05uMUFROEc1TkpsUjlIVHciLCJjX2hhc2giOiJPUkQtWXFsbFFUZXFZNFpXeWRueXhBIn0.KeEpsIqv_KIhOd911kY1HsChL0mzWoW2YFQ7pWVgeRdjok20oXKV9wCbtEGxg2zWFhF9D4XAPDENcJOWi7LpJ9LGhDymL-iqN1IewSSwxLPKInRI5J9TIb7uAGFtq-Z_0DL0yiDlneUik7JMiX8shq9g43T9QyvusK2UsRHu6HLtPwwwcAusHE1XMcLK13pAV9buNtRBB4ClMe2Br7Zo3IB0WqvpX38TMuwbgYfzj8QGhpNfzoXPUkCH7YH7vD_cUfFK5WJZ2p1ZVU-zu9lsmazLXRu79BL2pBBmjYit_Je3tgxtwGDRhWlkUTmM8-5tEnIN26jOVm10emL07oV7wQ";

    private static final String BASE_URL = "https://38v0wv1juh.execute-api.eu-west-2.amazonaws.com";
    private static final String UPLOAD_ENDPOINT = "/qa/asset/upload";
    private static final String OWNER_ID = "C8059AD1-C985-43B7-9E96-525240956E86";

    private static final HttpProtocolBuilder httpProtocol = http
            .baseUrl(BASE_URL)
            .disableWarmUp()
            .disableCaching()
            .header("Authorization", "Bearer " + AUTH_TOKEN)
            .acceptHeader("application/json")
            .contentTypeHeader("image/jpeg");

    private static final ScenarioBuilder scn = scenario("Upload Asset Service")
            .exec(session -> {
                try {
                    File file = new File(UploadPhotoSimulation.class.getClassLoader().getResource("ProfilePhoto1.jpeg").toURI());
                    byte[] fileBytes = Files.readAllBytes(file.toPath());
                    return session.set("fileBytes", fileBytes);
                } catch (URISyntaxException | java.io.IOException e) {
                    throw new RuntimeException("Error resolving URI or reading file", e);
                }
            }).pause(Duration.ofSeconds(2))
            .exec(http("Upload Profile Photo")
                    .post(UPLOAD_ENDPOINT)
                    .queryParam("ownerId", OWNER_ID)
                    .queryParam("ownerType", "User")
                    .queryParam("type", "profilePhoto")
                    .body(ByteArrayBody("#{fileBytes}"))
                    .check(bodyString().saveAs("responseBody"))
                    .check(jsonPath("$.ownerId").find().is(OWNER_ID))
                    .check(status().is(200))
            ).exec(session -> {
                String uploadPhotoResponse = session.get("responseBody");
                System.out.println("UploadPhotoResponse is: " + uploadPhotoResponse);
                return session;
            });

    {
        setUp(
                scn.injectOpen(
                        constantUsersPerSec(1).during(1)

                        /*
                        rampUsersPerSec(1).to(5).during(60), // Ramp up from 1 to 9 users per second over 5 minutes
                        constantUsersPerSec(10).during(120), // Maintain a constant rate of 16 users per second for 20 minutes
                        rampUsers(5).during(60) // Ramp down maintaining 9 users per second for 5 minutes
                        */
                )
        ).protocols(httpProtocol);
    }
}
