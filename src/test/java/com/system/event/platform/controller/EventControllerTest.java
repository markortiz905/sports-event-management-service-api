package com.system.event.platform.controller;

import com.system.event.platform.dto.EventDto;
import com.system.event.platform.entities.Event;
import com.system.event.platform.services.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("junit")
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Test
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder().eventName("Event Test").build();
        when(eventService.create(any(String.class), any())).thenReturn(event);
        this.mockMvc.perform(
                post("/event/" + event.getEventName()).contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().string(containsString(event.getEventName())));
    }

    @Test
    public void getEvent() throws Exception {
        createEvent();
        EventDto event = EventDto.builder().eventName("Event Test").build();
        when(eventService.getEvent(any(Long.class), any())).thenReturn(event);
        this.mockMvc.perform(
                get("/event/0")
        ).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(event.getEventName())));
    }

}
