package org.cws.web.software.engineer.task.security.service;

import org.cws.web.software.engineer.task.persistence.model.User;
import org.cws.web.software.engineer.task.persistence.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //@formatter:off
        User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found. User name: %s", username)));
        //@formatter:on

        return UserDetailsImpl.build(user);
    }

}
