package project.backend.business.post.implement;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.stereotype.Component;
import project.backend.business.post.request.CreatePostServiceRequest;
import project.backend.business.post.request.summary.SummaryOption;
import project.backend.business.post.response.dto.SummaryResultDto;
import project.backend.business.post.util.JsonParser;

@Slf4j
@Component
@RequiredArgsConstructor
public class SummaryAIManager {

  private final VertexAiGeminiChatModel chatModel;

  public SummaryResultDto getSummary(CreatePostServiceRequest createPostServiceRequest) {
    Prompt prompt = getPrompt(createPostServiceRequest);
    ChatResponse response = chatModel.call(prompt);
    String responseContent = response.getResult()
                                     .getOutput()
                                     .getContent();

    Map<String, String> result = JsonParser.convertString2Json(responseContent);

    return SummaryResultDto.builder()
                           .title(result.get("title"))
                           .content(result.get("content"))
                           .build();
  }

  private Prompt getPrompt(CreatePostServiceRequest createPostServiceRequest) {
    SummaryOption options = createPostServiceRequest.getOption();

    String requestMessage = "URL: " + createPostServiceRequest.getUrl() + "\n" +
        "Summarize the website corresponding to the URL below in a blog style according to the following summary conditions.\n"
        + "Please also recommend the title\n"
        + "The answer is given in json format string with title and content as keys.\n"
        + "Translate the content into the summary language!\n"
        + "Summary conditions: \n"
        + "Summary length: " + options.getLevel().getLines() + "\n"
        + "Summary tone:" + options.getTone().getValue() + "\n"
        + "Summary language: " + options.getLanguage().getValue() + "\n"
        + "Summary keywords: " + options.getKeywords();

    return new Prompt(requestMessage,
        VertexAiGeminiChatOptions.builder()
                                 .withModel(VertexAiGeminiChatModel.ChatModel.GEMINI_1_5_FLASH)
                                 .build());
  }
}
