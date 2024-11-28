package com.backend.liaison_system.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class SchedulerServiceImpl {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${scheduler.url}")
    private String SCHEDULER_URL;

    @Scheduled(fixedRate = 10_000)
    public void keepServerAlive() {
        String url = SCHEDULER_URL;

        try {
            restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            System.out.println("e = " + e.getMessage());
        }
    }
}
