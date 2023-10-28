package com.hakimfauzi.groupreminderbot.consumer;

import com.hakimfauzi.groupreminderbot.data.dto.MessageDto;
import com.hakimfauzi.groupreminderbot.data.dto.MessageEventDto;
import com.hakimfauzi.groupreminderbot.service.MessageService;
import com.hakimfauzi.groupreminderbot.service.TelegramBotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageConsumer {

    private Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private TelegramBotService telegramBotService;

    @Autowired
    private MessageService messageService;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consume(MessageEventDto messageEventDto) {
        StringBuilder sb = new StringBuilder();
        List<MessageDto> messages = messageEventDto.getMessages();
        List<Long> dataIds = new ArrayList<>();

        switch (messageEventDto.getTypeScheduler()) {
            case "start-of-the-day":
                sb.append("Hi!! there are meeting(s) scheduled today! \n\n");
                for (MessageDto message : messages) {
                    LocalDateTime time = message.getTime();
                    sb.append(message.getMessage()).append(" at ")
                            .append(String.format("%02d:%02d", time.getHour(), time.getMinute()))
                            .append(" \nLink: ").append(message.getLink())
                            .append("\n\n");
                    dataIds.add(message.getId());
                }

                messageService.updateMessageStatus(1, dataIds);
                telegramBotService.sendNotification(sb);
                break;
            case "soon-reminder":
                sb.append("This meeting will starting soon! \n\n");
                for (MessageDto message : messages) {
                    LocalDateTime time = message.getTime();
                    sb.append(message.getMessage()).append(" at ")
                            .append(String.format("%02d:%02d", time.getHour(), time.getMinute()))
                            .append(" \nLink: ").append(message.getLink())
                            .append("\n\n");
                    dataIds.add(message.getId());
                }

                messageService.updateMessageStatus(2, dataIds);
                telegramBotService.sendNotification(sb);
                break;
        }

    }

}
