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

    @GetMapping("/room")
    public String showRoom() {
        return "chat/room";
    }

    /**메세지쓰기**/
    @PostMapping("/writeMessage")
    @ResponseBody
    public RsData<ChatMessage> writeMessage(@RequestBody  WriteMessageRequest req) {
        ChatMessage message = new ChatMessage(req.getAuthorName(),req.getContent());
        log.info("json : {}",req);
        log.info("entity:{}",message);
        chatMessages.add(message);
        /**서버 측 리스트 **/
        sseEmitters.noti("chat__messageAdded");

        return new RsData(
                "S-1",
                "메세지가 작성되었습니다.",
                new WriteMessageResponse(message.getId())
        );
    }

    /**내용을 가져옴**/
    @GetMapping("/messages")
    @ResponseBody
    public RsData<MessagesResponse> messages(MessagesRequest req) {
        //메세지 리스트
        log.debug("req:{}",req);
        //subList될 저장소
        List<ChatMessage> messages = chatMessages;

        //req.getFromId 에서의 fromId값으로 !
        if (req.getFromId() != null) {
            // 0 부터 messageList의 size만큼의 int 생성
            // 해당하는 인덱스를 찾고 안나오면 -1 반환
            int index = IntStream.range(0, messages.size()) // 0부터 messageList의 size만큼 반복
                    .filter(i -> chatMessages.get(i).getId() == req.getFromId())
                    .findFirst() // 찾으면 멈춘다.
                    .orElse(-1);
            //해당 인덱스를 찾았다면
            if (index != -1) {
                //찾은 index 전을 날리는 것이다. 찾은 index후부터 list 에 담긴다.
                messages = messages.subList(index + 1, messages.size());
                // 0 1 2 3 4 ->5개
                // 1 2 3 4 5
            }
        }

        return new RsData<>(
                "S-1",
                "성공",
                new MessagesResponse(messages,messages.size())
        );
    }


}
