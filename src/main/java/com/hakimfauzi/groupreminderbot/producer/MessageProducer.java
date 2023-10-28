package com.hakimfauzi.groupreminderbot.producer;

import com.hakimfauzi.groupreminderbot.data.dto.MessageDto;
import com.hakimfauzi.groupreminderbot.data.dto.MessageEventDto;
import com.hakimfauzi.groupreminderbot.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableScheduling
public class MessageProducer {

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //    @Scheduled(cron = "0 30 10 * * *")
    @Scheduled(fixedRate = 10000)
    public void startOfTheDayReminder() {
        MessageEventDto eventDto = new MessageEventDto();
        List<MessageDto> messageToday = messageService.getStartOfDayMessage();

        if (!messageToday.isEmpty()) {
            eventDto.setTypeScheduler("start-of-the-day");
            eventDto.setMessages(messageToday);
            rabbitTemplate.convertAndSend(exchange, routingKey, eventDto);

            LOGGER.info(String.format("Message send to MQ: %s", eventDto));
        }
    }


    @Scheduled(fixedRate = 60000)
    public void soonReminder() {
        MessageEventDto eventDto = new MessageEventDto();
        List<MessageDto> messages = messageService.getSoonMessage();

        if (!messages.isEmpty()) {
            eventDto.setTypeScheduler("soon-reminder");
            eventDto.setMessages(messages);
            rabbitTemplate.convertAndSend(exchange, routingKey, eventDto);

            LOGGER.info(String.format("Message send to MQ: %s", eventDto));
        }
    }
}
