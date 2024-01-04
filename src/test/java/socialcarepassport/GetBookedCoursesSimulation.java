package socialcarepassport;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class GetBookedCoursesSimulation extends Simulation {
    private FeederBuilder feeder = csv("data.csv").circular();
    private static final String BASE_URL = "https://38v0wv1juh.execute-api.eu-west-2.amazonaws.com";
    private static final String AUTH_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjI0NzA2MjYwMzE4Mjk0NDU0OSIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2h1Yi1jYXJlLWlkLWU3aWxrbS56aXRhZGVsLmNsb3VkIiwic3ViIjoiMjQ0NzcxNTM2MjQ0ODg2MDM0IiwiYXVkIjpbIjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiMjQ0MjEzOTA1MzQwMDA1NzI0Il0sImV4cCI6MTcwMzgxMzg2NiwiaWF0IjoxNzAzNzcwNjY2LCJub25jZSI6InNxVjJROVBQTzFHRyIsImF6cCI6IjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiY2xpZW50X2lkIjoiMjQ0MjE0MjE0MDU3NjIyODc2QGh1Yl93aXRoX2NhcmVfaWQiLCJhdF9oYXNoIjoiWXg0NlZkM0JmSnVBd1MwU0Rmd0NfdyIsImNfaGFzaCI6IjVWeTVFN2pPMVpkRW85a0IycEZTQ2cifQ.pEgBbx14i_428c6aYi7pthanNLBwFW581AbhlkgnvJygUoPGsxh9pxPWpB-SnXLJpcodRl55m5dH0UnikdnZwVKHR_7z7WlY0CACfrPkMlJmGQkmFprkQe0-euac0ziWHCwNip6C1X-Zqpk9fttg3gFEAGBRp8UItBQ3oPuvNIyQkHlorhwODgw1NBCikwUG32yJVLahSM6gdKPyvCruoDZLChKRS2N5hpIFGgs-EquqwiLG2L9EB4lt1u5u39RSfpcVMtofLA_GzKeBsVFa1USrsGKWYTZmAkjo9kDAeG9r8070LuKsyn4F-VqERnvKTmctIsZRb4VU5eUTDm7dGw";

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(BASE_URL)
            .inferHtmlResources(AllowList())
            .acceptHeader("*/*")
            .acceptEncodingHeader("gzip, deflate, br")
            .acceptLanguageHeader("en-GB,en-US;q=0.9,en;q=0.8")
            .originHeader("https://qa.socialcarepassport.co.uk")
            .acceptHeader("application/json, text/plain, */*")
            .authorizationHeader("Bearer " + AUTH_TOKEN)
            .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

    private final ScenarioBuilder scn = scenario("ViewTrainingRecords")
            .feed(feeder)
            .exec(
                    http("Get Training Courses")
                            .get("/qa/training-record/getBookedCourses?userId=#{CareWorker-UserId}")
                            .check(bodyString().saveAs("BookedCourseList"))
                            .check(jsonPath("$[?(@.userId == '#{CareWorker-UserId}')]").exists().saveAs("UserId"))
                            .check(status().is(200))
            )
            .exec(session -> {
                boolean userIdExists = session.contains("UserId");
                if (!userIdExists) {
                    System.out.println("Assertion failed: 'userId' does not exist");
                    session.markAsFailed();
                }
                return session;
            });

    {
        setUp(scn.injectOpen(rampUsers(10).during(10))).protocols(httpProtocol);

        /*
        rampUsersPerSec(10).to(20).during(1800): This ramps users from 10 to 20 users per second over a period of 30 minutes (1800 seconds).
        constantUsersPerSec(20).during(900): After reaching the peak rate of 20 users per second, this maintains a constant load of 20 users per second for the remaining 15 minutes (900 seconds) of the total duration.
         */

        /*
        setUp(
                scn.injectOpen(
                        rampUsersPerSec(10).to(20).during(1800), // Ramps users from 10 to 20 per second over 30 mins (1800 secs)
                        constantUsersPerSec(20).during(900) // Maintains 20 users per second for the remaining 15 mins (900 secs)
                ).protocols(httpProtocol)
        );
         */
    }
}