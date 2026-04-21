package br.com.joga_together.security;

import br.com.joga_together.exception.LoginExpiredOrBlockedException;
import br.com.joga_together.model.User;
import br.com.joga_together.model.enums.UserStatus;
import br.com.joga_together.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        if (
                user.getUserStatus().equals(UserStatus.EXPIRED) ||
                        user.getUserStatus().equals(UserStatus.BLOCKED) ||
                        user.getUserStatus().equals((UserStatus.INACTIVE)) ||
                        user.getUserStatus().equals(UserStatus.PENDING)
        ) {
            throw new LoginExpiredOrBlockedException("login expired ou blocked");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        // add default role if needed: authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

}

