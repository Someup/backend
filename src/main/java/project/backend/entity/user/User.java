package project.backend.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.backend.entity.BaseEntity;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column
  private String name;

  @Column
  private String email;

  @Column
  private String profileImageUrl;

  @Builder
  private User(String name, String email, String profileImageUrl) {
    this.name = name;
    this.email = email;
    this.profileImageUrl = profileImageUrl;
  }

  public static User createUser(String email, String name, String profileImageUrl) {
    return User.builder()
               .email(email)
               .name(name)
               .profileImageUrl(profileImageUrl)
               .build();
  }

  public void withdraw() {
    this.setActivated(false);
    this.email = null;
  }
}
