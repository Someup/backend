package project.backend.business.post.request.summary;

import lombok.Getter;

@Getter
public enum SummaryLevel {
  BRIEF("간단 요약", 10),
  BASE("중간 요약", 20),
  DETAIL("상세 요약", 30);


  private String value;
  private int lines;

  SummaryLevel(String value, int lines) {
    this.value = value;
    this.lines = lines;
  }

  public static SummaryLevel stringToEnum(String level) {
    return SummaryLevel.valueOf(level);
  }
}
