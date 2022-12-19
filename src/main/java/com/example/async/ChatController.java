package com.example.async;

import com.example.async.dto.MessagesRequest;
import com.example.async.dto.MessagesResponse;
import com.example.async.dto.WriteMessageRequest;
import com.example.async.dto.WriteMessageResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Controller
@Slf4j
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final SseEmitters sseEmitters;

    private List<ChatMessage> chatMessages = new ArrayList<>();

/*
    public record WriteMessageRequest(String authorName, String content) {
    }
*/



    @GetMapping("/room")
    public String showRoom() {
        return "chat/room";
    }

    /*@PostMapping("/writeMessage")
    @ResponseBody
    public RsData<WriteMessageResponse> writeMessage(@RequestBody WriteMessageRequest req) {
        ChatMessage message = new ChatMessage(req.authorName(), req.content());

        chatMessages.add(message);

        sseEmitters.noti("chat__messageAdded");

        return new RsData<>(
                "S-1",
                "메세지가 작성되었습니다.",
                new WriteMessageResponse(message.getId())
        );
    }*/

    @PostMapping("/writeMessage")
    @ResponseBody
    public RsData<ChatMessage> writeMessage(@RequestBody  WriteMessageRequest req) {
        ChatMessage message = new ChatMessage(req.getAuthorName(),req.getContent());
        log.info("json : {}",req);

        chatMessages.add(message);

        return new RsData(
                "S-1",
                "메세지가 작성되었습니다.",
                new WriteMessageResponse(message.getId())
        );
    }


    @GetMapping("/messages")
    @ResponseBody
    public RsData<MessagesResponse> messages(MessagesRequest req) {
        //메세지 리스트
        log.debug("req:{}",req);

        return new RsData<>(
                "S-1",
                "성공",
                new MessagesResponse(chatMessages,chatMessages.size())
        );
    }

     /* @GetMapping("/messages")
    @ResponseBody
    public RsData<MessagesResponse> messages(MessagesRequest req) {
        //메세지 리스트
        List<ChatMessage> messages = chatMessages;

        // 번호가 입력되었다면
        if (req.fromId != null) {
            // 해당 번호의 채팅메세지가 전체 리스트에서의 배열인덱스 번호를 구한다.
            // 없다면 -1
            int index = IntStream.range(0, messages.size())
                    .filter(i -> chatMessages.get(i).getId() == req.fromId)
                    .findFirst()
                    .orElse(-1);


            int foundIndex = -1;

            for ( int i = 0; i < messages.size(); i++ ) {
                if ( messages.get(i).getId() == req.fromId ) {
                    foundIndex = i;
                    break;
                }
            }


            if (index != -1) {
                // 만약에 index가 있다면, 0번 부터 index 번 까지 제거한 리스트를 만든다.
                messages = messages.subList(index + 1, messages.size());
            }
        }

        return new RsData<>(
                "S-1",
                "성공",
                new MessagesResponse(messages, messages.size())
        );
    }*/
}
