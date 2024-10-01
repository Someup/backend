package project.backend.business.tag.implement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.entity.tag.Tag;
import project.backend.repository.tag.TagRepository;

@Component
@RequiredArgsConstructor
public class TagReader {
    private final TagRepository tagRepository;

    public Map<String, Tag> getTagNameMapByPostId(Long postId) {
        List<Tag> tagList = tagRepository.findAllByPostId(postId);
        return tagList.stream().collect(Collectors.toMap(Tag::getName, tag -> tag));
    }

    public Map<Long, List<String>> getPostTagMap(List<Long> postIdList) {
        List<Object[]> tagResults = tagRepository.findPostIdAndTagNamesByPostIdIn(postIdList);
        return tagResults.stream().collect(
                Collectors.groupingBy(
                        res -> (Long) res[0],
                        Collectors.mapping(res -> (String) res[1], Collectors.toList())
                )
        );
    }

    public List<String> readTagNamesByPostId(Long postId) {
        return tagRepository.findTagNamesByPostId(postId);
    }

    public List<Tag> readTagByName(List<String> nameList) {
        return tagRepository.findAllByNameIn(nameList);
    }
}
