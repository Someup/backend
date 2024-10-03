package project.backend.common.config;

import static org.springframework.security.config.Customizer.withDefaults;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.backend.business.auth.oauth.KakaoUserDetailsService;
import project.backend.common.error.ExceptionHandlerFilter;
import project.backend.security.jwt.JwtAccessDeniedHandler;
import project.backend.security.jwt.JwtAuthenticationFailEntryPoint;
import project.backend.security.jwt.JwtFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile("dev")
public class SecurityConfig {

  private final JwtAuthenticationFailEntryPoint jwtAuthenticationFailEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final JwtFilter jwtFilter;
  private final KakaoUserDetailsService kakaoUserDetailsService;
  private final ExceptionHandlerFilter exceptionHandlerFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .cors(withDefaults())
        .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2Login(
            oauth -> oauth.userInfoEndpoint(config -> config.userService(kakaoUserDetailsService)))
        .authorizeHttpRequests(request -> request
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/exception/**").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .requestMatchers("/api-docs/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/posts").permitAll()
            .requestMatchers(HttpMethod.GET, "/posts/*").permitAll()
            .requestMatchers(HttpMethod.PATCH, "/posts/*/summary").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(exceptionHandlerFilter,
            JwtFilter.class) // ExceptionHandlerFilter 의존성 주입으로 사용
        .exceptionHandling(exceptionHandling -> {
          exceptionHandling.authenticationEntryPoint(jwtAuthenticationFailEntryPoint);
          exceptionHandling.accessDeniedHandler(jwtAccessDeniedHandler);
        });

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
