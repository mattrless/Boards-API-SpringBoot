package com.boards.api.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.boards.api.users.entities.User;
import com.boards.api.users.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findWithSystemRoleAndPermissionsByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return buildUserDetails(user);
  }

  public UserDetails buildUserDetails(User user) {
    return new AuthenticatedUser(
            user.getId(),
            user.getEmail(),
            user.getPassword(),
            user.getSystemRole().getPermissions().stream()
                  .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                  .toList()
    );
  }

}
