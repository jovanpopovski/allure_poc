package stepdefinitions;

import builders.RequestBuilder;
import builders.ResponseBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import data.ConfigFileReader;
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

import static builders.RequestBuilder.wiremockRequestBodyBuilder;
import static builders.RequestBuilder.wiremockRequestHeadersBuilder;
import static builders.ResponseBuilder.getJsonMockedBody;
import static builders.WiremockServiceBuilder.startServer;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class BasicApiStepDefinitions {
    ConfigFileReader configFileReader = new ConfigFileReader();
    RequestBuilder requestBuilder = new RequestBuilder();
    //New instance of wiremock is added
    WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(Integer.parseInt(configFileReader.getWiremockServerPort()))); //No-args constructor will start on port 8080, no HTTPS
    //JSON body is defined
    private static HttpResponse<JsonNode> response;

    @Description("Sending request")
    @When("I send request to service")
    public void iSendRequestToService() throws JsonProcessingException {
        //Logging headers and request body in Allure
        Allure.addAttachment("Request headers: ", wiremockRequestHeadersBuilder().toString());
        Allure.addAttachment("Request that is sent: ", requestBuilder.convertRequestMapToFormattedJsonString(wiremockRequestBodyBuilder()));
        response = Unirest.post(configFileReader.getWiremockServerUrl()+wireMockServer.port()+configFileReader.getWiremockServerEndpoint()).headers(wiremockRequestHeadersBuilder()).fields(wiremockRequestBodyBuilder()).asJson();
    }

    @Description("Receiving response")
    @Then("Data is retrieved")
    public void dataIsRetrieved() {
        //Validating response
        Assert.assertTrue("Response is not returned",response.isSuccess());
        Assert.assertEquals("Status code is not 201",201,response.getStatus());
        //Logging response in Allure
        Allure.addAttachment("Response that is returned ", response.getBody().toPrettyString());
    }

    @Given("Service is up and running")
    public void serviceIsUpAndRunning() {
        //Stating stub server with our predefined response
        startServer(wireMockServer);
    }
}