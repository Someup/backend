package project.backend.business.post.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.stereotype.Component;
import project.backend.business.post.dto.CreatePostDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class SummaryAIManager {

  private final VertexAiGeminiChatModel chatModel;


  public String getSummary(CreatePostDto createPostDto) {
    Prompt prompt = getPrompt(createPostDto);
    ChatResponse response = chatModel.call(prompt);
    String content = response.getResult().getOutput().getContent();
    log.info(content);
    return content;
  }


  private Prompt getPrompt(CreatePostDto createPostDto) {
    String requestMessage = ("URL: %s\n" +
        "위 웹사이트를 블로그 형태로 요약해줘.\n" +
        "길이는 10줄 정도로,\n" +
        "말투는 '~합니다'가 아닌 '~한다'의 형태로 해줘").formatted(createPostDto.getUrl());

    return new Prompt(requestMessage,
        VertexAiGeminiChatOptions.builder()
                                 .withModel(VertexAiGeminiChatModel.ChatModel.GEMINI_1_5_FLASH)
                                 .build());
  }

}
