package project.backend.business.post.request.summary;

import lombok.Getter;

@Getter
public enum SummaryTone {
  FORMAL("formal"),
  CASUAL("casual"),
  CUTE("cute"),
  WITTY("witty");

  private final String value;

  SummaryTone(String value) {
    this.value = value;
  }

  public static SummaryTone stringToEnum(String level) {
    return SummaryTone.valueOf(level);
  }
}
