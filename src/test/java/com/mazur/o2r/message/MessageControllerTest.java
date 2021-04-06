package com.mazur.o2r.message;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {
  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void when_existing_one_message_expect_message_on_get() throws Exception {
    //given
    Message message = new Message();
    message.setContent("hello world");
    messageRepository.save(message);

    // expect
    mockMvc
        .perform(MockMvcRequestBuilders.get("/message").contentType("application/json"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(containsString(message.getId())))
        .andExpect(MockMvcResultMatchers.content().string(containsString(message.getContent())));

  }

  @Test
  void when_existing_one_message_update_message_with_responed_with_updated_content()
      throws Exception {
    //given
    Message message = new Message();
    message.setContent("msg-1-1");
    messageRepository.save(message);

    String newMessageContent = "msg-2-2";

    //expect
    String expectedJsonResponse = "{'id':'" + message.getId() + "','content':'" + newMessageContent + "'}";

    mockMvc
        .perform(MockMvcRequestBuilders
            .put("/message/"+ message.getId())
            .contentType("application/json")
          .content(newMessageContent)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(containsString(message.getId())))
        .andExpect(MockMvcResultMatchers.content().json(expectedJsonResponse));
  }

  @Test
  void when_calling_random_messages_should_return_ten_random_messages() throws Exception {
    // given
    int maxMessages = 20;
    for (int i = 0; i < maxMessages; i++) {
      Message message = new Message();
      message.setContent("msg-" + i);
      assertNotNull(messageRepository.save(message));
    }

    //when
    String callResponse = callRandomMessagesEndpoint();
    String callResponseSecond = callRandomMessagesEndpoint();

    assertNotEquals(callResponse, callResponseSecond);
  }

  private String callRandomMessagesEndpoint() throws Exception {
    return mockMvc
        .perform(MockMvcRequestBuilders.get("/message/random").contentType("application/json"))
        .andReturn().getResponse()
        .getContentAsString();
  }

}