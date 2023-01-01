package com.example.async.dto;


import com.example.async.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MessagesResponse {

    private List<ChatMessage> messages;

    private Integer count;
}
