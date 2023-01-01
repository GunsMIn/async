package com.example.async.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter @ToString
public class WriteMessageRequest {

    private String authorName;
    private String content;
}
