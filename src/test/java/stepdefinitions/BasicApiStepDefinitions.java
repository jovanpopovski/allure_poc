package stepdefinitions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class BasicApiStepDefinitions {
    //New instance of wiremock is added
    WireMockServer wireMockServer = new WireMockServer(wireMockConfig().dynamicPort()); //No-args constructor will start on port 8080, no HTTPS
    //JSON body is defined
    final String jsonBody = "{\"id\":\"08da08b6-35ae-47b1-8c04-93ccf3645214\",\"name\":\"bgtest_001_180322\",\"createdOn\":\"2022-03-18T08:06:30.49527\",\"scheduledOn\":\"2022-03-18T08:06:49\",\"startedOn\":\"2022-03-18T08:07:05.167075\",\"finishedOn\":\"2022-03-18T08:08:47.45827\",\"status\":4,\"automaticallySwitchDns\":true,\"migrations\":[{\"id\":\"08da08b6-3ba6-402b-8d6c-c2ef42f3ad2e\",\"customerId\":\"1001960\",\"customerName\":\"TestDySmR UserIiJHV\",\"status\":\"Complete\",\"subscriptionMigrations\":[{\"id\":\"9f5846b6-a2e3-4768-a92d-23d6e48fba6c\",\"subscriptionName\":\"Advanced - Shared Linux Hosting 2013 (dtcmp-demodomain6870956931.com)\",\"subscriptionId\":1001923,\"webspaceMigrations\":[{\"id\":\"08da08b6-4b55-4a57-898b-4ac226d17e55\",\"webSpaceId\":\"101900\",\"status\":\"Complete\",\"websiteMatchPercent\":100,\"errorMessage\":null}],\"mailboxMigrations\":[{\"id\":\"08da08b6-4f5f-4ea8-86ad-49995f6eceff\",\"sourceMailboxId\":\"54492\",\"destinationMailboxId\":null,\"status\":\"Complete\",\"mailboxTotalSize\":47623,\"migratedItemsSize\":103058,\"migratedItemsCount\":17,\"failedItemsSize\":0,\"failedItemsCount\":0,\"failedItemIds\":[\"\",\"\"],\"errorMessage\":\"\",\"startedOn\":\"2022-03-18T07:59:41.938012\",\"finishedOn\":\"2022-03-18T08:08:15.72516\",\"resyncStatus\":\"Complete\",\"lastResyncOn\":\"2022-03-18T08:00:49.52305\",\"sourceMailboxAddress\":\"rnd.cmp@cmp-source.devtech-labs.com\"}],\"domainMigrations\":[{\"id\":\"08da08b6-4b60-41ca-8fea-ff02c7d32b75\",\"domain\":\"dtcmp-demodomain6870956931.com\",\"status\":\"Complete\",\"errorMessage\":null}],\"status\":\"Complete\",\"numberOfWebspaces\":1,\"numberOfFailedWebspaces\":0,\"numberOfCompletedWebspaces\":1,\"numberOfDomains\":1,\"numberOfFailedDomains\":0,\"numberOfCompletedDomains\":1,\"numberOfMailboxes\":1,\"numberOfFailedMailboxes\":0,\"numberOfCompletedMailboxes\":1}],\"disabledForMigration\":false,\"destinationCustomerId\":\"1001960\",\"errorMessage\":null,\"numberOfMigrations\":1,\"numberOfCompletedMigrations\":1,\"numberOfFailedMigrations\":0}],\"servicePlanId\":7,\"numberOfMigrations\":1,\"numberOfCompletedMigrations\":1,\"numberOfFailedMigrations\":0,\"editable\":false,\"deletable\":false,\"scheduleTimeCanBeChanged\":false,\"settings\":{\"migrateCrm\":true,\"migrateDomain\":true,\"migrateEmail\":true,\"migrateHosting\":true}}";

    private static HttpResponse<JsonNode> response;

    @Description("Sending request")
    @When("I send request to service")
    public void iSendRequestToService() throws JsonProcessingException {
        //Setting headers inside map
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("accept", "application/json");
        //Setting body inside map
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "morpheus");
        requestBody.put("job", "leader");
        //Logging headers and request body in Allure
        Allure.addAttachment("Request headers: ", requestHeaders.toString());
        Allure.addAttachment("Request that is sent: ", convertRequestMapToFormattedJsonString(requestBody));
        response = Unirest.post("http://localhost:"+wireMockServer.port()+"/wetest").headers(requestHeaders).fields(requestBody).asJson();
    }

    @Description("Receiving response")
    @Then("Data is retrieved")
    public void dataIsRetrieved() {
        //Validating response
        Assert.assertTrue(response.isSuccess());
        //Logging response in Allure
        Allure.addAttachment("Response that is returned ", response.getBody().toPrettyString());
    }

    private String convertRequestMapToFormattedJsonString(Map<String, Object> requestBody) throws JsonProcessingException {
        //Instantiating mapper for handling Map objects
        ObjectMapper objectMapper = new ObjectMapper();
        //Writing map objects as strings
        String json = objectMapper.writeValueAsString(requestBody);
        JSONObject jsonObject = new JSONObject(json);
        //Writing string object as JSON
        return jsonObject.toString(2);
    }

    @Given("Service is up and running")
    public void serviceIsUpAndRunning() {
        //Stating stub server with our predefined response
        wireMockServer.start();
        wireMockServer.stubFor(post(urlPathEqualTo("/wetest"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonBody)));
    }

}