package project.backend.business.post.request.summary;

import lombok.Getter;

@Getter
public enum SummaryLevel {
  BRIEF("brief summary", 10),
  BASE("moderate summary", 20),
  DETAIL("detail summary", 30);

  private final String value;
  private final int lines;

  SummaryLevel(String value, int lines) {
    this.value = value;
    this.lines = lines;
  }

  public String getLines() {
    return "About " + this.lines + " lines, " + this.value;
  }

  public static SummaryLevel stringToEnum(String level) {
    return SummaryLevel.valueOf(level);
  }
}
