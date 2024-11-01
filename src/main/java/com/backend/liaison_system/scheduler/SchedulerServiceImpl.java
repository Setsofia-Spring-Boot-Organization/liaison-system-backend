package com.backend.liaison_system.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class SchedulerServiceImpl {
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 3000)
    public void keepServerAlive() {
        String url = "http://localhost:8040/liaison/api/v1/schedule";

        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("response = " + response);
        } catch (RestClientException e) {
            System.out.println("e = " + e.getMessage());
        }
    }
}
