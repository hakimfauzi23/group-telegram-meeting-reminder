package com.hakimfauzi.groupreminderbot.data;

import com.hakimfauzi.groupreminderbot.data.dto.MessageDto;
import com.hakimfauzi.groupreminderbot.data.entity.Message;

public class MessageMapper {

    public static MessageDto mapToMsgDto(Message message) {

        return new MessageDto(
                message.getId(),
                message.getMessage(),
                message.getLink(),
                message.getTime()
        );
    }

    public static Message mapToMsg(MessageDto messageDto) {

        return new Message(
                messageDto.getId(),
                messageDto.getMessage(),
                messageDto.getLink(),
                messageDto.getTime()
        );
    }
}
