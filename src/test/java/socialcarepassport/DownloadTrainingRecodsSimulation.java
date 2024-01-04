package socialcarepassport;

import java.util.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class DownloadTrainingRecodsSimulation extends Simulation {
    private static final String BASE_URL = "https://38v0wv1juh.execute-api.eu-west-2.amazonaws.com";

    private HttpProtocolBuilder httpProtocol = http
            .baseUrl(BASE_URL)
            .inferHtmlResources()
            .acceptHeader("*/*")
            .acceptEncodingHeader("gzip, deflate, br")
            .acceptLanguageHeader("en-GB,en-US;q=0.9,en;q=0.8")
            .originHeader("https://qa.socialcarepassport.co.uk")
            .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

    private final Map<String, String> headers = Map.of(
            "access-control-request-headers", "authorization",
            "access-control-request-method", "GET",
            "accept", "application/json, text/plain, */*",
            "authorization", "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjI0NzkyODE0ODM3OTg4MzUxOCIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2h1Yi1jYXJlLWlkLWU3aWxrbS56aXRhZGVsLmNsb3VkIiwic3ViIjoiMjQ0NzcxNTM2MjQ0ODg2MDM0IiwiYXVkIjpbIjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiMjQ0MjEzOTA1MzQwMDA1NzI0Il0sImV4cCI6MTcwNDMyMTY4NywiaWF0IjoxNzA0Mjc4NDg3LCJub25jZSI6IjYwMmZlRHpDY0NBeiIsImF6cCI6IjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiY2xpZW50X2lkIjoiMjQ0MjE0MjE0MDU3NjIyODc2QGh1Yl93aXRoX2NhcmVfaWQiLCJhdF9oYXNoIjoic1NabjVkNXRoNGI1QktJNzhmM1M5USIsImNfaGFzaCI6IkJvNW1tc1U4TDB4NlRaa1oyTXZyOGcifQ.VDLdMyDJMMNNiKYde5j4nEU3j45GTVruEPtVqPXIJ1_05qEjBaC7Nx-aXfOKK6sU47UTzaGYFC7nDdImMTa6rgHfZzEHosnMG4HmtGqQNsMgr5u69OFBPQvmb-yKFFVp-xjeHoNv60MVq3_kpknuGU2bIUfwtx2FDGnJ011OHlwclEGEUXdVaRRk9fsMAaTKH1891gxSDFxt9uLdkRKe7rGF9YyHtopPLVrcE88-nAB4hDnSnQuayIAkNzOxiiQZ4rORWV-k3H1JaeVM25u6L7JCtC3okb5FUZYqMl3l49B02OGi7pzwrCuh2sKozPYRsyTxlfHgGa9fmszPsjlnKQ",
            "sec-fetch-mode", "cors",
            "sec-fetch-site", "cross-site");


    private ScenarioBuilder scn = scenario("DownloadTrainingRecodsSimulation")
            .exec(
                    http("download")
                            .get("/qa/training-record/download?courseId[]=9659140C-07C5-4740-96A1-70369BDF77DF&courseId[]=2A1156EA-5A23-4724-9A01-6882A57B4612")
                            .headers(headers)
                            .check(status().is(200))
            );


    {
        setUp(scn.injectOpen(atOnceUsers(1))).protocols(httpProtocol);
    }
}
