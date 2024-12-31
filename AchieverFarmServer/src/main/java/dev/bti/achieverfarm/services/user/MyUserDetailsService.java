package dev.bti.achieverfarm.services.user;


import dev.bti.achieverfarm.exceptions.auth.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        dev.bti.achieverfarm.models.User user;

        try {
            user = userService.getUser(username, dev.bti.achieverfarm.models.User.class);
        } catch (AuthException e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        if (user.getId().equals(username)) {
            return User.withUsername(username)
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
