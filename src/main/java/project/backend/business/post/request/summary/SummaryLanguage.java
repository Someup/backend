package project.backend.business.post.request.summary;

import lombok.Getter;

@Getter
public enum SummaryLanguage {
  KR("korean"),
  EN("english");

  private final String value;

  SummaryLanguage(String value) {
    this.value = value;
  }

  public static SummaryLanguage stringToEnum(String level) {
    return SummaryLanguage.valueOf(level);
  }
}
