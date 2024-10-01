package project.backend.business.archive.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUpdateArchiveServiceRequest {

  private final String name;
}
