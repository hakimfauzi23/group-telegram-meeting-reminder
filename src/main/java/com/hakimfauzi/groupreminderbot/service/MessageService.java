package com.hakimfauzi.groupreminderbot.service;

import com.hakimfauzi.groupreminderbot.data.dto.MessageDto;

import java.util.List;

public interface MessageService {

    List<MessageDto> getAllMessages();

    MessageDto createMessage(MessageDto messageDto);

    List<MessageDto> getStartOfDayMessage();

    List<MessageDto> getSoonMessage();

    void updateMessageStatus(Integer status, List<Long> dataIds);

    MessageDto updateMessage(MessageDto messageDto) throws Exception;

    void deleteMessage(Long messageId) throws Exception;
}
