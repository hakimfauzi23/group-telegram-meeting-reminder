package com.hakimfauzi.groupreminderbot.repository;

import com.hakimfauzi.groupreminderbot.data.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
    @Query(value = "SELECT * FROM messages WHERE time BETWEEN :start AND :end AND status=:status", nativeQuery = true)
    List<Message> findMessagesBetweenAndStatus(LocalDateTime start, LocalDateTime end, Integer status);

    @Transactional
    @Modifying
    @Query(value = "UPDATE messages SET status = :status  WHERE id IN :dataIds", nativeQuery = true)
    void updateStatusMessages(Integer status, List<Long> dataIds);
}
