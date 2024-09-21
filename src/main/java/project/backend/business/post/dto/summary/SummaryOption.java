package project.backend.business.post.dto.summary;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import project.backend.common.error.CustomException;
import project.backend.common.error.ErrorCode;

@Getter
@Slf4j
public class SummaryOption {

  private SummaryLevel level;
  private SummaryTone tone;
  private SummaryLanguage language;

  public SummaryOption(String level, String tone, String language) throws CustomException {
    try {
      this.level = SummaryLevel.stringToEnum(level.toUpperCase());
      this.tone = SummaryTone.stringToEnum(tone.toUpperCase());
      this.language = SummaryLanguage.stringToEnum(language.toUpperCase());
    } catch (IllegalArgumentException e) {
      log.info("[ERROR] 입력 값에 오류가 있습니다. level: {}, tone: {}, lang: {}",
          this.level, this.tone, this.language);
      throw new CustomException(ErrorCode.BAD_REQUEST);
    }
  }
}
