package com.backend.liaison_system.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class SchedulerServiceImpl {
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 100_000)
    public void keepServerAlive() {
        String url = "https://liaison-system-backend.onrender.com/liaison/api/v1/schedule";

        try {
            restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            System.out.println("e = " + e.getMessage());
        }
    }
}
