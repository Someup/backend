package project.backend.entity.post.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import project.backend.entity.post.PostType;

import java.util.Arrays;

@Converter
public class PostTypeConverter implements AttributeConverter<PostType, String> {

  @Override
  public String convertToDatabaseColumn(PostType postType) {
    if (postType == null) {
      return null;
    }
    return postType.getValue();
  }

  @Override
  public PostType convertToEntityAttribute(String dbData) {
    return Arrays.stream(PostType.values())
                 .filter(v -> v.getValue().equals(dbData))
                 .findAny()
                 .orElseThrow(() -> new IllegalArgumentException(
                     String.format("%s는 유효하지 않는 게시글 타입입니다.", dbData)));
  }
}
