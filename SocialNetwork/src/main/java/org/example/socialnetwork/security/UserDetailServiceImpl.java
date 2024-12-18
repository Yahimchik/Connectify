package org.example.socialnetwork.security;

import lombok.RequiredArgsConstructor;
import org.example.socialnetwork.entities.User;
import org.example.socialnetwork.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private static final String USER_NOT_FOUND_EXCEPTION = "Specified user is not found";
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_EXCEPTION));
        return CustomSecurityUser.fromUser(user);
    }
}
