package project.backend.business.archive.respone.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArchiveDto {

  private final Long id;
  private final String name;
}
