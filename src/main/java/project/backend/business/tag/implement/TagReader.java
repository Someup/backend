package project.backend.business.tag.implement;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.dao.tag.entity.Tag;
import project.backend.dao.tag.respository.TagRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TagReader {
    private final TagRepository tagRepository;

    public Map<String, Tag> getTagNameMapByPostId(Long postId) {
        List<Tag> tagList =  tagRepository.findAllByPostId(postId);
        return tagList.stream().collect(Collectors.toMap(Tag::getName, tag -> tag));
    }

    public List<Object[]> readTagNamesByPostIdList(List<Long> postIdList) {
        return tagRepository.findPostIdAndTagNamesByPostIdIn(postIdList);
    }

    public List<String> readTagNamesByPostId(Long postId) {
        return tagRepository.findTagNamesByPostId(postId);
    }

    public List<Tag> readTagByName(List<String> nameList) {
        return tagRepository.findAllByNameIn(nameList);
    }
}
