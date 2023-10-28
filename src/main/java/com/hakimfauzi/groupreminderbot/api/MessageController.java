package com.hakimfauzi.groupreminderbot.api;

import com.hakimfauzi.groupreminderbot.data.dto.MessageDto;
import com.hakimfauzi.groupreminderbot.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<MessageDto>> getAllMessages() {
        List<MessageDto> allMessages = messageService.getAllMessages();
        return new ResponseEntity<>(allMessages, HttpStatus.OK);
    }

    @GetMapping("/today")
    public ResponseEntity<List<MessageDto>> getMessageToday() {
        List<MessageDto> allMessages = messageService.getStartOfDayMessage();
        return new ResponseEntity<>(allMessages, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MessageDto> createMessage(@RequestBody MessageDto messageDto) {
        MessageDto message = messageService.createMessage(messageDto);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<MessageDto> updateMessage(
            @PathVariable("id") Long messageId,
            @RequestBody MessageDto messageDto) throws Exception {
        messageDto.setId(messageId);
        MessageDto updatedUser = messageService.updateMessage(messageDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable("id") Long messageId) throws Exception {
        messageService.deleteMessage(messageId);
        return new ResponseEntity<>("Message successfully deleted!", HttpStatus.OK);
    }

}
