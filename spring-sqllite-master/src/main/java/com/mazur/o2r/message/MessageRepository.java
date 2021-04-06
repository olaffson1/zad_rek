package com.mazur.o2r.message;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

interface MessageRepository extends CrudRepository<Message, UUID> {

  List<Message> findAll();

  @Query(
          value = "select * from message WHERE id IN (SELECT id FROM message ORDER BY RANDOM() LIMIT 10)",
          nativeQuery = true)
  List<Message> findRandomQuestions();
}
