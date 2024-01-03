package socialcarepassport;

import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class EmployerListSimulation extends Simulation {

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://38v0wv1juh.execute-api.eu-west-2.amazonaws.com")
            .inferHtmlResources(AllowList(), DenyList(".*\\.js", ".*\\.css", ".*\\.gif", ".*\\.jpeg", ".*\\.jpg", ".*\\.ico", ".*\\.woff", ".*\\.woff2", ".*\\.(t|o)tf", ".*\\.png", ".*\\.svg", ".*detectportal\\.firefox\\.com.*"))
            .acceptHeader("application/json, text/plain, */*")
            .acceptEncodingHeader("gzip, deflate, br")
            .acceptLanguageHeader("en-GB,en-US;q=0.9,en;q=0.8")
            .authorizationHeader("Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjI0NjkyODMxNjY1MTU0OTUyNyIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2h1Yi1jYXJlLWlkLWU3aWxrbS56aXRhZGVsLmNsb3VkIiwic3ViIjoiMjQ0NzcxOTk4NDkwNzk1OTgwIiwiYXVkIjpbIjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiMjQ0MjEzOTA1MzQwMDA1NzI0Il0sImV4cCI6MTcwMzcyOTQ0OSwiaWF0IjoxNzAzNjg2MjQ5LCJub25jZSI6Ikh3VVR3Ujh4MndYZyIsImF6cCI6IjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiY2xpZW50X2lkIjoiMjQ0MjE0MjE0MDU3NjIyODc2QGh1Yl93aXRoX2NhcmVfaWQiLCJhdF9oYXNoIjoiQUduUmtlcmxTWUZ6SDBjc0VFeWVOZyIsImNfaGFzaCI6IkNHZVZhOUw3TjNDeGhGenBIWFVTalEifQ.SS0pe6lr1bq9M_c5sMPuVPpmLovtJG_vHhKFIIwh_ktYPWUSI5qznBqEpEZFYjo_-cD-EdD_Xj1hRnH67wU07oQkRR35F_HSTE5ApH9FGrcXlH3evbsjj3Vi5UWF1Uhqynhdy3aFkrFF6-BayHAn1kE55z_LGGN9ry8795Meh1rFZxMbbStb_BPTR-M2zriHPq9VehE9xXKFJgb3j4l2kDWdkGGmiG7RON4UoF2yhf5GBioVzZAzVItisUJmLx4dpDatq_BxHeuWuH9yVQV1k-NymJw3gMKjm9pdnK8_NqpxbZKCWYh_7hMi9Eu0i0Y-n_6loOGviSQReooyfUzD_Q")
            .originHeader("https://qa.socialcarepassport.co.uk")
            .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

    private Map<CharSequence, String> headers = Map.ofEntries(
            Map.entry("sec-ch-ua", "Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120")
    );

  private ScenarioBuilder scn = scenario("RecordedSimulation")
          .exec(
                  http("Get Org List")
                          .get("/qa/organisation/list")
                          .headers(headers)
                          .check(status().is(200))
                          .resources(
                                  http("Get Emp List")
                                          .get("/qa/organisation/employment/list")
                                          .headers(headers)
                                          .check(status().is(200))
                          )
          );

  {
      setUp(
              scn.injectOpen(rampUsers(10).during(20)
              )
      ).protocols(httpProtocol);
  }
}
