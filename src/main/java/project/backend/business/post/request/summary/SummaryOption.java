package project.backend.business.post.request.summary;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;

@Getter
@Builder
@Slf4j
public class SummaryOption {

  private SummaryLevel level;
  private SummaryTone tone;
  private SummaryLanguage language;
  private String keywords;


  public static SummaryOption of(String level, String tone, String language, String keywords) {
    try {
      return SummaryOption.builder()
                          .level(SummaryLevel.stringToEnum(level.toUpperCase()))
                          .tone(SummaryTone.stringToEnum(tone.toUpperCase()))
                          .language(
                              SummaryLanguage.stringToEnum(language.toUpperCase()))
                          .keywords(keywords)
                          .build();
    } catch (IllegalArgumentException e) {
      log.info("[ERROR] 입력 값에 오류가 있습니다. level: {}, tone: {}, lang: {}, keywords: {}",
          level, tone, language, keywords);
      throw new CustomException(ErrorCode.BAD_REQUEST);
    }
  }
}
