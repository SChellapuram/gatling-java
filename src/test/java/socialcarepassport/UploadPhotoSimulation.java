package socialcarepassport;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;


public class UploadPhotoSimulation extends Simulation {
    private static final String BASE_URL = "https://38v0wv1juh.execute-api.eu-west-2.amazonaws.com";
    private static final String UPLOAD_ENDPOINT = "/qa/asset/upload";

    // List of owner IDs
    private static final List<String> OWNER_IDS = Arrays.asList(
            "C8059AD1-C985-43B7-9E96-525240956E86",
            "20bd05d1-8ef8-4270-8cf5-18bb39ddea10",
            "51d50e0f-ad19-4283-a6a0-613fa901061d",
            "44cccd95-50d6-4aa2-aa6a-8611bd37401d"
    );
    private static final Map<String, String> AUTH_TOKENS = new HashMap<>();

    static {
        AUTH_TOKENS.put("C8059AD1-C985-43B7-9E96-525240956E86", "eyJhbGciOiJSUzI1NiIsImtpZCI6IjI1MjEzNTI4NTIzODgzODUwNiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2h1Yi1jYXJlLWlkLWU3aWxrbS56aXRhZGVsLmNsb3VkIiwic3ViIjoiMjQ0NzcxNTM2MjQ0ODg2MDM0IiwiYXVkIjpbIjI1MDY5MTU4MTk0NDg2MjcwNUBodWJfd2l0aF9jYXJlX2lkIiwiMjUwNzEzNjU1NzQ0NzQ3NDAxQGh1Yl93aXRoX2NhcmVfaWQiLCIyNDQyMTQyMTQwNTc2MjI4NzZAaHViX3dpdGhfY2FyZV9pZCIsIjI0NDIxMzkwNTM0MDAwNTcyNCJdLCJleHAiOjE3MDY4Mjg2NTAsImlhdCI6MTcwNjc4NTQ1MCwibm9uY2UiOiI2RGdxWjExanFtVloiLCJhenAiOiIyNDQyMTQyMTQwNTc2MjI4NzZAaHViX3dpdGhfY2FyZV9pZCIsImNsaWVudF9pZCI6IjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiYXRfaGFzaCI6InpPaWtpVU50UTV2M1k4R2ZfN1dXSHciLCJjX2hhc2giOiJOSFpHRHhGYmg3bG5EVk9sMTBiZ293In0.VU98OTH_ELb6D0I3jM79vWBbDeyMNIsycpl6BcU5T-GS_81vB9xl2IO4nVsoXvkKMrSRD_ySOuCmRIg5ueB-3c7ayr20HT4a8zg0NSIX4O84AlvpkmdOztbIivQiI_ctbKbq9Wlg2CwUY5HZxqD94Jji7NWRNQbYFQdcvEs07Yx8Gd14Bjq3shJ-T-n5fPgQjSkDm37oDrOxEWqzH-9cA4ewzMtea_vuTpy_bKGDqGP-yIAJV7g-KIUU401nzi4bLiEGO6FRe2yxP3_qXpKfGjsKasnxNNtY8A5pLplHF52FlV6p3UAMcJCNs3qly4a3C1nmnjCGSByqj2KhDq5slw");
        AUTH_TOKENS.put("20bd05d1-8ef8-4270-8cf5-18bb39ddea10", "eyJhbGciOiJSUzI1NiIsImtpZCI6IjI1MjEzNTI4NTIzODgzODUwNiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2h1Yi1jYXJlLWlkLWU3aWxrbS56aXRhZGVsLmNsb3VkIiwic3ViIjoiMjQ1MTA1NzAzMjA1Nzc3NjkwIiwiYXVkIjpbIjI1MDY5MTU4MTk0NDg2MjcwNUBodWJfd2l0aF9jYXJlX2lkIiwiMjUwNzEzNjU1NzQ0NzQ3NDAxQGh1Yl93aXRoX2NhcmVfaWQiLCIyNDQyMTQyMTQwNTc2MjI4NzZAaHViX3dpdGhfY2FyZV9pZCIsIjI0NDIxMzkwNTM0MDAwNTcyNCJdLCJleHAiOjE3MDY4Mjg3NDUsImlhdCI6MTcwNjc4NTU0NSwibm9uY2UiOiJ5TXNOYllXcXROYTgiLCJhenAiOiIyNDQyMTQyMTQwNTc2MjI4NzZAaHViX3dpdGhfY2FyZV9pZCIsImNsaWVudF9pZCI6IjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiYXRfaGFzaCI6Im5pcUtDWWpnUWZybjRxY0JuQk8tREEiLCJjX2hhc2giOiJnTFhkb3pkdThabHdGVFNNUnpFaGlRIn0.BulEQHexBR8yKj-uC4I9tu69iA33TC9FIz8CqR3-NhBMDCSgeQfVnUkXIJWV4QmNY4fzK8XTENMuTfo3YErpq6wT1JBE4PkWSpBGon2pXP1Y5ZnE9T4mqDMNLmbkUv6FPnaCY24Iy_H7Q1ieHCjFfMIOaI4fqGWOgAYRL_VM_9ntA7vfo31vOegMeSrxtM9QcjvGoQ2hP-1pVk9gqyHJ-IIHr6XTveJ3Ds4yuMtHScF22nH66rCkxGaifbfypMDRu0bHj1o1QZEnmjtqJ6kE65ouNZYHkGIe8VzgcqSXEMgB6Ii5Nh8tgEsmKPFfSCe6KkgkRQQTfmiWWDhtvYprAg");
        AUTH_TOKENS.put("51d50e0f-ad19-4283-a6a0-613fa901061d", "eyJhbGciOiJSUzI1NiIsImtpZCI6IjI1MjEzNTI4NTIzODgzODUwNiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2h1Yi1jYXJlLWlkLWU3aWxrbS56aXRhZGVsLmNsb3VkIiwic3ViIjoiMjQ1MTA1OTU0Mzk0MjQwNDA0IiwiYXVkIjpbIjI1MDY5MTU4MTk0NDg2MjcwNUBodWJfd2l0aF9jYXJlX2lkIiwiMjUwNzEzNjU1NzQ0NzQ3NDAxQGh1Yl93aXRoX2NhcmVfaWQiLCIyNDQyMTQyMTQwNTc2MjI4NzZAaHViX3dpdGhfY2FyZV9pZCIsIjI0NDIxMzkwNTM0MDAwNTcyNCJdLCJleHAiOjE3MDY4Mjg4MDAsImlhdCI6MTcwNjc4NTYwMCwibm9uY2UiOiJkbms1Q2NjMVh5MFkiLCJhenAiOiIyNDQyMTQyMTQwNTc2MjI4NzZAaHViX3dpdGhfY2FyZV9pZCIsImNsaWVudF9pZCI6IjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiYXRfaGFzaCI6IjV4WDBfdk5GMHNOaW80UHZ0WnNjdnciLCJjX2hhc2giOiJhRkNLMUpiMmg1dFNVTjgtbDJ2U01RIn0.FM1NldTACi1XQXQ-XX1LVEVBKvHbOm6eLk-K0YKf99zbq2LgeNZpU_64h2xOuUGHBHKYmG6qTzADC3f6MMr_SQ_0d2FlVqzUGTw4fwEfBCiip-eHr-0JkhMPEAMScXjOEZslThwud3ZJt2D_NbKfDmzz3rfdATJ7yUJ1xel_o4WXl71f4oeaaS6lu7-CnsWIdPP8GakzZI5T8LBds2eb6kCVb-yuYMsicuYMIjDqzaqiWknAjmyIGInl9qhB5vtxVJbqE2N21vaSvaH3JmydNOuWDuSFmO-fH7gLhplLEFqAUn-I04ztLlqhV_HHNsRqC5ffzCHdMyTXa1jENsY7GQ");
        AUTH_TOKENS.put("44cccd95-50d6-4aa2-aa6a-8611bd37401d", "eyJhbGciOiJSUzI1NiIsImtpZCI6IjI1MjEzNTI4NTIzODgzODUwNiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2h1Yi1jYXJlLWlkLWU3aWxrbS56aXRhZGVsLmNsb3VkIiwic3ViIjoiMjQ1MTA2MzY0MzQ1NTI4NjAyIiwiYXVkIjpbIjI1MDY5MTU4MTk0NDg2MjcwNUBodWJfd2l0aF9jYXJlX2lkIiwiMjUwNzEzNjU1NzQ0NzQ3NDAxQGh1Yl93aXRoX2NhcmVfaWQiLCIyNDQyMTQyMTQwNTc2MjI4NzZAaHViX3dpdGhfY2FyZV9pZCIsIjI0NDIxMzkwNTM0MDAwNTcyNCJdLCJleHAiOjE3MDY4Mjg4NTMsImlhdCI6MTcwNjc4NTY1Mywibm9uY2UiOiJsRTRsWDRIOExZNkIiLCJhenAiOiIyNDQyMTQyMTQwNTc2MjI4NzZAaHViX3dpdGhfY2FyZV9pZCIsImNsaWVudF9pZCI6IjI0NDIxNDIxNDA1NzYyMjg3NkBodWJfd2l0aF9jYXJlX2lkIiwiYXRfaGFzaCI6IlI2N01HVXdMOU5mUWxsYXBLRWpkdmciLCJjX2hhc2giOiJ2NEYyb2lXSFhzY1U1WmlQNGgwamhBIn0.QIHzJUWqela2kZ4pxhdmMSNhGuD0S_1IUaVoydxiwBYxznf2jez_gjVWQ8z2blPEuvefXcvMWCsrWwGTz7pEPpRewynxJsF0KJv2Jba4AxkYURrvwQ7rGXDcud_3YtBuT-4tS_kgNdkp8IvZ8JY_QX_9Vp-dH5Oaf_V0vhrTTbNT_9k2mkqoV0L0_5BKS38_m9CKwLvbwdfUKCPInLgdK0niiWGbZQvEzSs5ZNaXdqhtTdYMnz3cB4kCMSeM6n_OfzlnrrZGGfLshSKAvKxWvgGo-F0AFJCcHd--BFLS1pIdigYOKibrYPnSMehIwr285A4g6Yti75B263-PZo8UJQ");
    }

    private static final HttpProtocolBuilder httpProtocol = http
            .baseUrl(BASE_URL)
            .disableWarmUp()
            .disableCaching()
            .header("Authorization", "Bearer " + AUTH_TOKENS)
            .acceptHeader("application/json")
            .contentTypeHeader("image/jpeg");

    private static final ScenarioBuilder scn = scenario("Upload Asset Service")
            .exec(session -> {
                try {
                    File file = new File(UploadPhotoSimulation.class.getClassLoader().getResource("ProfilePhoto.jpg").toURI());
                    byte[] fileBytes = Files.readAllBytes(file.toPath());
                    return session.set("fileBytes", fileBytes);
                } catch (URISyntaxException | java.io.IOException e) {
                    throw new RuntimeException("Error resolving URI or reading file", e);
                }
            }).exec(session -> {
                // Randomly select an owner ID
                String randomOwnerId = OWNER_IDS.get(ThreadLocalRandom.current().nextInt(OWNER_IDS.size()));
                // Retrieve the corresponding auth token from the mapping
                String authToken = AUTH_TOKENS.get(randomOwnerId);
                // Set the selected owner ID and auth token in the session
                return session.set("randomOwnerId", randomOwnerId).set("authToken", authToken);
            })
            .exec(http("Upload Profile Photo")
                    .post(UPLOAD_ENDPOINT)
                    .header("Authorization", "Bearer #{authToken}")
                    .queryParam("ownerId", "#{randomOwnerId}")
                    .queryParam("ownerType", "User")
                    .queryParam("type", "profilePhoto")
                    .body(ByteArrayBody("#{fileBytes}"))
                    .check(bodyString().saveAs("responseBody"))
                    .check(status().is(200))
            )

            .exec(session -> {
                String uploadPhotoResponse = session.get("responseBody");
                System.out.println("UploadPhotoResponse is: " + uploadPhotoResponse);
                return session;
            });

    {
        setUp(
                scn.injectOpen(
                        constantUsersPerSec(1).during(1)

//                        rampUsersPerSec(1).to(9).during(220),
//                        constantUsersPerSec(12).during(540),
//                        rampUsers(10).during(220)

                )
        ).protocols(httpProtocol);
    }
}
