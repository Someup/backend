package project.backend.business.tag.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.entity.post.Post;
import project.backend.entity.tag.PostTag;
import project.backend.entity.tag.Tag;
import project.backend.repository.tag.PostTagRepository;
import project.backend.repository.tag.TagRepository;

@Component
@RequiredArgsConstructor
public class TagManager {

  private final TagReader tagReader;
  private final TagRepository tagRepository;
  private final PostTagRepository postTagRepository;

  public void updateTag(Post post, List<String> requestTagNameList) {
    Map<String, Tag> tagNameMap = tagReader.getTagNameMapByPostId(post.getId());

    List<String> newTagNameList = new ArrayList<>();

    for (String name : requestTagNameList) {
      if (tagNameMap.containsKey(name)) {
        tagNameMap.remove(name);
      } else {
        newTagNameList.add(name);
      }
    }

    // 기존 tag 중 연결 끊어야 하는 것들
    if (!tagNameMap.isEmpty()) {
      this.disconnectTagAndPost(new ArrayList<>(tagNameMap.values()), post);
    }

    // 새롭게 연결해야 하는 tag
    if (!newTagNameList.isEmpty()) {
      List<Tag> tags = this.getOrCreateTags(newTagNameList);
      this.connectTagAndPost(tags, post);
    }
  }

  private List<Tag> getOrCreateTags(List<String> newTagNameList) {
    List<Tag> existTags = tagReader.readTagByName(newTagNameList);

    Map<String, Tag> existTagNameMap = existTags.stream()
                                                .collect(Collectors.toMap(Tag::getName, tag -> tag));

    List<Tag> newTags = newTagNameList.stream()
                                      .filter(tagName -> !existTagNameMap.containsKey(tagName))
                                      .map(Tag::createTag)
                                      .toList();

    List<Tag> savedNewTags = tagRepository.saveAll(newTags);

    List<Tag> allTags = new ArrayList<>();
    allTags.addAll(savedNewTags);
    allTags.addAll(existTags);

    return allTags;
  }

  private void disconnectTagAndPost(List<Tag> tagList, Post post) {
    List<Long> disconnectTagIdList = tagList.stream()
                                            .map(Tag::getId)
                                            .toList();

    postTagRepository.deletePostTagByPostIdAndTagIdListIn(post.getId(), disconnectTagIdList);
  }

  private void connectTagAndPost(List<Tag> tags, Post post) {
    List<PostTag> postTagList = tags.stream()
                                    .map(tag -> PostTag.create(post, tag))
                                    .toList();

    postTagRepository.saveAll(postTagList);
  }
}
