package org.example.basic.services;

import lombok.RequiredArgsConstructor;
import org.example.basic.entities.User;
import org.example.basic.repositories.UserRepository;
import org.example.basic.security.SecurityUser;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public SecurityUser loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
    }

    public User processOauthUser(String email, String username) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        User user = new User();
        user.setUserName(username);
        user.setEmail(email);

        return userRepository.save(user);
    }
}


