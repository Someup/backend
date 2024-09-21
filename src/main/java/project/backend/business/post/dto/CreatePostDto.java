package project.backend.business.post.dto;


import lombok.Builder;
import lombok.Getter;
import project.backend.business.post.dto.summary.SummaryOption;

@Getter
@Builder
public class CreatePostDto {

  String url;
  SummaryOption option;
}
