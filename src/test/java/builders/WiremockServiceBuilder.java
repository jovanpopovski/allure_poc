package builders;

import com.github.tomakehurst.wiremock.WireMockServer;
import data.ConfigFileReader;

import static builders.ResponseBuilder.getJsonMockedBody;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WiremockServiceBuilder {
    static ConfigFileReader configFileReader = new ConfigFileReader();

    public static void startServer(WireMockServer wireMockServer){
        wireMockServer.start();
        wireMockServer.stubFor(post(urlPathEqualTo(configFileReader.getWiremockServerEndpoint()))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", configFileReader.getWiremockServerContentType())
                        .withBody(getJsonMockedBody())));
    }
}