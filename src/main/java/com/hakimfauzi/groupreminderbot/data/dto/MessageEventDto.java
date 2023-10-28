package com.hakimfauzi.groupreminderbot.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEventDto {
    private String typeScheduler;
    private List<MessageDto> messages;
}
