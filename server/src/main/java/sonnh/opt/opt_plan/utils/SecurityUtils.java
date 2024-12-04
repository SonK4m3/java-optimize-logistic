package sonnh.opt.opt_plan.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import sonnh.opt.opt_plan.model.User;
import sonnh.opt.opt_plan.model.UserDetailsImpl;
import sonnh.opt.opt_plan.repository.UserRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityUtils {
    private final UserRepository userRepository;

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        log.debug("Current authentication: {}", authentication);

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            log.debug("Found user details: {}", userDetails);
            return userRepository.findById(userDetails.getId());
        }

        return Optional.empty();
    }
}