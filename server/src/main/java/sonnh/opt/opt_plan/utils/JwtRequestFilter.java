
package sonnh.opt.opt_plan.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sonnh.opt.opt_plan.service.impl.UserDetailsServiceImpl;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
	private static final String BEARER_PREFIX = "Bearer ";
	private static final String AUTHORIZATION_HEADER = "Authorization";

	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response, @NonNull FilterChain chain)
			throws ServletException, IOException {
		try {
			extractAndValidateJwt(request)
					.ifPresent(jwt -> authenticateUser(jwt, request));
		} catch (Exception e) {
			log.error("Authentication failed: {}", e.getMessage());
		}
		chain.doFilter(request, response);
	}

	private Optional<String> extractAndValidateJwt(HttpServletRequest request) {
		return Optional.ofNullable(parseJwt(request))
				.filter(jwt -> jwtUtil.validateJwtToken(jwt));
	}

	private void authenticateUser(String jwt, HttpServletRequest request) {
		String username = jwtUtil.getUserNameFromJwtToken(jwt);
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		UsernamePasswordAuthenticationToken authentication = createAuthenticationToken(
				userDetails);
		authentication
				.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private UsernamePasswordAuthenticationToken createAuthenticationToken(
			UserDetails userDetails) {
		return new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());
	}

	private String parseJwt(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
				.filter(header -> header.startsWith(BEARER_PREFIX))
				.map(header -> header.substring(BEARER_PREFIX.length())).orElse(null);
	}
}
