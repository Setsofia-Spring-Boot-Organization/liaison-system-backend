package com.backend.liaison_system.google_maps;

import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.backend.liaison_system.google_maps.responses.GMapLocation;
import org.cloudinary.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleMapServices {

    private final RestTemplate restTemplate = new RestTemplate();

    public GMapLocation getCoordinates(String placeName) {

        String apiKey = System.getenv("GOOGLE_MAP_API_KEY");

        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="
                + placeName.replace(" ", "%20")
                + "&inputtype=textquery&fields=geometry&key=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);

        //extract the coordinates from the response
        assert response != null;
        JSONObject jsonObject = new JSONObject(response);
        if ("OK".equals(jsonObject.getString("status"))) {
            JSONObject location = jsonObject
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location");
            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");

            return new GMapLocation(lat, lng);
        } else {
            System.out.println("LOCATION DOES NOT EXIST: " + placeName);
            throw new LiaisonException(Error.ERROR_SAVING_DATA, new Throwable(Message.THE_EXACT_COMPANY_LOCATION_DOES_NOT_EXISTS.label));
        }

    }
}
