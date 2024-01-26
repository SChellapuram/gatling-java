package socialcarepassport;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class PostCreateCourseSimulation extends Simulation {

    private final String baseUrl = "https://38v0wv1juh.execute-api.eu-west-2.amazonaws.com/qa/training-record";
    private final String authToken = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjI0ODY5NzY4NTAxODcwMjg3NyIsInR5cCI6IkpXVCJ9..."; // Replace with your actual token

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl(baseUrl)
            .authorizationHeader(authToken)
            .contentTypeHeader("application/json");

    private final String requestBody = """
            {
                "name": "Load Testing -Oliver McGowan Mandatory Training",
                "description": "The training is named after Oliver McGowan. Oliver was a young man whose death shone a light on the need for health and social care staff to have better skills, knowledge and understanding of the needs for autistic people and people with a learning disability. The Oliver McGowan Mandatory Training on Learning Disability and Autism is the government’s preferred and recommended training for health and social care staff.",
                "modules": [
                    {
                        "type": "inPerson",
                        "description": "Some generic training module description"
                    }
                ],
                "validity": 24
            }
            """;

    private final ScenarioBuilder scn = scenario("Training Record Scenario")
            .exec(http("Create Training Course")
                    .post("/course/create")
                    .body(StringBody(requestBody))
                    .check(status().is(201))
                    .check(jsonPath("$.description").saveAs("responseBody"))

            ).exec(session -> {
                String responseBody = session.get("responseBody");
                System.out.println("Response Body: " + responseBody);
                return session;
            });

    {
        setUp(
                scn.injectOpen(
                        constantUsersPerSec(1).during(1)
                )).protocols(httpProtocol);
    }
}