package project.backend.business.tag.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import project.backend.entity.post.Post;
import project.backend.entity.tag.PostTag;
import project.backend.entity.tag.Tag;
import project.backend.repository.tag.PostTagRepository;
import project.backend.repository.tag.TagRepository;

@Component
@AllArgsConstructor
public class TagManager {

  private final TagReader tagReader;
  private final TagRepository tagRepository;
  private final PostTagRepository postTagRepository;

  /**
   * 게시물에 대한 태그를 업데이트합니다.
   * 기존 태그를 유지하거나 제거하고, 새 태그를 추가합니다.
   */
  public void updateTag(Post post, List<String> requestTagNameList) {
    Map<String, Tag> existingTags = getExistingTagsForPost(post);
    List<String> newTagNames = getNewTagNames(requestTagNameList, existingTags);

    removeUnusedTagsFromPost(existingTags, post);
    addNewTagsToPost(newTagNames, post);
  }

  /**
   * 게시물에 연결된 기존 태그를 조회합니다.
   */
  private Map<String, Tag> getExistingTagsForPost(Post post) {
    return tagReader.getTagNameMapByPostId(post.getId());
  }

  /**
   * 요청된 태그 목록에서 기존에 존재하지 않는 새 태그 이름들을 필터링합니다.
   */
  private List<String> getNewTagNames(List<String> requestTagNameList, Map<String, Tag> existingTags) {
    return requestTagNameList.stream()
                             .filter(tagName -> !existingTags.containsKey(tagName))
                             .toList();
  }

  /**
   * 기존 태그 중 게시물에서 연결이 끊어져야 할 태그들을 게시물과의 관계에서 제거합니다.
   */
  private void removeUnusedTagsFromPost(Map<String, Tag> existingTags, Post post) {
    if (existingTags.isEmpty()) {
      return;  // 제거할 태그가 없으면 작업을 중단
    }
    List<Tag> tagsToRemove = new ArrayList<>(existingTags.values());
    disconnectTagAndPost(tagsToRemove, post);
  }

  /**
   * 새롭게 추가되어야 하는 태그들을 생성하거나 찾아서 게시물과 연결합니다.
   */
  private void addNewTagsToPost(List<String> newTagNames, Post post) {
    if (newTagNames.isEmpty()) {
      return;  // 추가할 태그가 없으면 작업을 중단
    }
    List<Tag> tags = getOrCreateTags(newTagNames);
    connectTagAndPost(tags, post);
  }

  /**
   * 태그 이름을 기반으로 태그를 조회하고, 존재하지 않는 태그는 새로 생성합니다.
   */
  private List<Tag> getOrCreateTags(List<String> newTagNames) {
    List<Tag> existingTags = tagReader.readTagByName(newTagNames);

    Map<String, Tag> existingTagNameMap = existingTags.stream()
                                                      .collect(Collectors.toMap(Tag::getName, tag -> tag));

    List<Tag> newTags = newTagNames.stream()
                                   .filter(tagName -> !existingTagNameMap.containsKey(tagName))
                                   .map(Tag::createTag)
                                   .toList();

    List<Tag> savedNewTags = tagRepository.saveAll(newTags);

    List<Tag> allTags = new ArrayList<>(savedNewTags);
    allTags.addAll(existingTags);

    return allTags;
  }

  /**
   * 주어진 태그 리스트를 게시물과의 연결에서 해제합니다.
   */
  private void disconnectTagAndPost(List<Tag> tags, Post post) {
    List<Long> tagIds = tags.stream()
                            .map(Tag::getId)
                            .toList();
    postTagRepository.deletePostTagByPostIdAndTagIdListIn(post.getId(), tagIds);
  }

  /**
   * 주어진 태그 리스트를 게시물과 연결합니다.
   */
  private void connectTagAndPost(List<Tag> tags, Post post) {
    List<PostTag> postTags = tags.stream()
                                 .map(tag -> PostTag.create(post, tag))
                                 .toList();
    postTagRepository.saveAll(postTags);
  }
}
