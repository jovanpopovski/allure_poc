package builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {

    public static Map<String,String> wiremockRequestHeadersBuilder(){
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("accept", "application/json");
        return requestHeaders;
    }

    public static Map<String,Object> wiremockRequestBodyBuilder(){
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "morpheus");
        requestBody.put("job", "leader");
        return requestBody;
    }

    public String convertRequestMapToFormattedJsonString(Map<String, Object> requestBody) throws JsonProcessingException {
        //Instantiating mapper for handling Map objects
        ObjectMapper objectMapper = new ObjectMapper();
        //Writing map objects as strings
        String json = objectMapper.writeValueAsString(requestBody);
        JSONObject jsonObject = new JSONObject(json);
        //Writing string object as JSON
        return jsonObject.toString(2);
    }
}