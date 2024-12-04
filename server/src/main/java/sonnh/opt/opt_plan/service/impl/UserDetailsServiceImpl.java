
package sonnh.opt.opt_plan.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.model.UserDetailsImpl;
import sonnh.opt.opt_plan.repository.UserRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username));

        return UserDetailsImpl.builder().id(user.getId()).username(user.getUsername())
                .email(user.getEmail()).password(user.getPassword())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority(user.getRole().getRole())))
                .build();
    }
}