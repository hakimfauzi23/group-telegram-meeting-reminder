package com.hakimfauzi.groupreminderbot.service.impl;

import com.hakimfauzi.groupreminderbot.data.MessageMapper;
import com.hakimfauzi.groupreminderbot.data.dto.MessageDto;
import com.hakimfauzi.groupreminderbot.data.entity.Message;
import com.hakimfauzi.groupreminderbot.repository.MessageRepository;
import com.hakimfauzi.groupreminderbot.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private static Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepository messageRepository;

    @Value("${application.reminder.minutes-span.setting}")
    private Integer spanTimeReminder;

    @Override
    public List<MessageDto> getAllMessages() {
        List<Message> messages = messageRepository.findAll();
        return messages.stream().map(MessageMapper::mapToMsgDto).toList();

    }

    @Override
    public MessageDto createMessage(MessageDto messageDto) {
        Message message = MessageMapper.mapToMsg(messageDto);
        message.setStatus(0);
        Message savedMsg = messageRepository.save(message);

        return MessageMapper.mapToMsgDto(savedMsg);
    }

    @Override
    public List<MessageDto> getStartOfDayMessage() {
        LocalDateTime start = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX);

        List<Message> messages = messageRepository.findMessagesBetweenAndStatus(start, end, 0);
        return messages.stream().map(MessageMapper::mapToMsgDto).collect(Collectors.toList());
    }

    @Override
    public List<MessageDto> getSoonMessage() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(spanTimeReminder);

        List<Message> soonMessages = messageRepository.findMessagesBetweenAndStatus(start, end, 1);
        return soonMessages.stream().map(MessageMapper::mapToMsgDto).collect(Collectors.toList());
    }

    @Override
    public void updateMessageStatus(Integer status, List<Long> dataIds) {
        try {
            messageRepository.updateStatusMessages(status, dataIds);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public MessageDto updateMessage(MessageDto messageDto) throws Exception {
        Message extMessage = messageRepository.findById(messageDto.getId()).orElse(null);

        if (extMessage != null) {
            extMessage.setMessage(messageDto.getMessage());
            extMessage.setLink(messageDto.getLink());
            extMessage.setTime(messageDto.getTime());

            Message updateMessage = messageRepository.save(extMessage);
            return MessageMapper.mapToMsgDto(updateMessage);
        } else {
            LOGGER.error("Try to update message but id not found");
            throw new Exception("Data not found!");
        }
    }

    @Override
    public void deleteMessage(Long messageId) throws Exception {
        Message extMessage = messageRepository.findById(messageId).orElse(null);

        if (extMessage != null) {
            messageRepository.deleteById(messageId);
        } else {
            LOGGER.error("Try to delete message but id not found");
            throw new Exception("Data not found!");
        }
    }


}
