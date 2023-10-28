package com.hakimfauzi.groupreminderbot.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakimfauzi.groupreminderbot.service.TelegramBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class TelegramBotServiceImpl implements TelegramBotService {

    private Logger LOGGER = LoggerFactory.getLogger(TelegramBotServiceImpl.class);

    @Value("${telegram.config.chat.id}")
    private String chatId;

    @Value("${telegram.config.url}")
    private String telegramApiUrl;

    @Value("${telegram.config.bot-token}")
    private String telegramBotToken;

    @Override
    public void sendNotification(StringBuilder message) {

        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        HashMap<String, String> requestBodyMap = new HashMap<>();
        String requestBody = "";

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestBodyMap.put("chat_id", chatId);
        requestBodyMap.put("text", String.valueOf(message));

        try {
            requestBody = objectMapper.writeValueAsString(requestBodyMap);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }

        HttpEntity<String> request = new HttpEntity<>(requestBody, httpHeaders);

        StringBuilder sendMessageUrl = new StringBuilder(telegramApiUrl);
        sendMessageUrl.append("bot").append(telegramBotToken).append("/sendMessage");
        restTemplate.postForEntity(sendMessageUrl.toString(), request, String.class);

    }
}
