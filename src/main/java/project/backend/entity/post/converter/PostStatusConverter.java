package project.backend.entity.post.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import project.backend.entity.post.PostStatus;

import java.util.Arrays;

@Converter
public class PostStatusConverter implements AttributeConverter<PostStatus, String> {

    @Override
    public String convertToDatabaseColumn(PostStatus postStatus) {
        if (postStatus == null) return null;
        return postStatus.getValue();
    }

    @Override
    public PostStatus convertToEntityAttribute(String dbData) {
        return Arrays.stream(PostStatus.values())
                .filter(v -> v.getValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s는 유효하지 않는 게시글 상태 입니다.", dbData)));
    }
}