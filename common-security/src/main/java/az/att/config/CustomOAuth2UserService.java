//package az.ingress.common.security.config;
//
//import az.ingress.common.security.auth.services.JwtService;
//import az.ingress.common.security.auth.services.UserPrincipal;
//import az.ingress.common.security.domain.UserEntity;
//import az.ingress.common.security.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//
//    private final UserRepository userRepository;
//    private final BaseSecurityConfig baseSecurityConfig;
//    private final JwtService jwtService;
//    private final SecurityProperties securityProperties;
//
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
//        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // github / google
//
//        String email = oAuth2User.getAttribute("email");
//        String name = oAuth2User.getAttribute("name");
//
//        UserEntity user = userRepository.findByUsername(email)
//                .orElseGet(() -> {
//                    UserEntity newUser = UserEntity.builder()
//                            .username(email)
//                            .password(baseSecurityConfig.passwordEncoder().encode(UUID.randomUUID().toString()))
//                            .firstName(name)
//                            .role("USER")
//                            .enabled(true)
//                            .accountNonExpired(true)
//                            .accountNonLocked(true)
//                            .credentialsNonExpired(true)
//                            .build();
//                    return userRepository.save(newUser);
//                });
//
//        UserPrincipal principal = UserPrincipal.create(user, user.getTenantId() != null ? user.getTenantId().toString() : null);
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                principal,
//                null,
//                principal.getAuthorities()
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = jwtService.issueToken(authentication,
//                Duration.ofSeconds(securityProperties.getJwtProperties().getTokenValidityInSeconds()));
//
//        return new DefaultOAuth2User(
//                user.getAuthorities().stream().map(a -> new SimpleGrantedAuthority(a.getAuthority())).toList(),
//                oAuth2User.getAttributes(),
//                "email"
//        );
//    }
//}
