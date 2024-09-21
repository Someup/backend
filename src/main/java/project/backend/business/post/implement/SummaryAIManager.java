package project.backend.business.post.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.stereotype.Component;
import project.backend.business.post.dto.CreatePostDto;
import project.backend.business.post.dto.summary.SummaryLanguage;

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
    String requestMessage = "URL: " + createPostDto.getUrl() + "\n" +
        "위 웹사이트를 요약 조건에 맞춰서 블로그 형태로 요약해줘. 출처도 명시해줘!\n"+
        "요약조건: \n" +
        "1. 요약 길이: " + createPostDto.getOption().getLevel().getLines() + "\n" +
        "2. 요약 말투: " + createPostDto.getOption().getTone().getValue() +
        "3. 요약 언어: " + createPostDto.getOption().getLanguage().getValue();

    return new Prompt(requestMessage,
        VertexAiGeminiChatOptions.builder()
                                 .withModel(VertexAiGeminiChatModel.ChatModel.GEMINI_1_5_FLASH)
                                 .build());
  }

}
