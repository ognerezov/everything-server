package net.okhotnikov.everything.dao;

import net.okhotnikov.everything.api.in.StatusResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestDao {

    private final RestTemplate restTemplate;

    public RestDao( RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public <T extends StatusResponse> StatusResponse post(String url, Map<String, Object> msg, Class<T> tClass){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(msg, headers);

        ResponseEntity<T> response = restTemplate.postForEntity(url, entity, tClass);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return new StatusResponse(response.getStatusCode().value());
        }
    }

    public Map<String, Object> getOneFieldMap(String key, Object value){
        Map<String, Object> res = new HashMap<>();
        res.put(key, value);
        return res;
    }
}
