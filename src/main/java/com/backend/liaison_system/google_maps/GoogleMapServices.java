package com.backend.liaison_system.google_maps;

import org.cloudinary.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleMapServices {

    private final RestTemplate restTemplate = new RestTemplate();

    public JSONObject getCoordinates(String placeName) {

        String apiKey = System.getenv("GOOGLE_MAP_API_KEY");

        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="
                + placeName.replace(" ", "%20")
                + "&inputtype=textquery&fields=geometry&key=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);

        //extract the coordinates from the response
        assert response != null;
        return new JSONObject(response);
    }
}
