package bugbusters.everyonecodes.java.usermanagement.rolemanagement.admin;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminRunner {

    @Bean
    ApplicationRunner prepareAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String username = "admin";
            if (!userRepository.existsByUsername(username)) {
                String password = passwordEncoder.encode("Coding12#");
                User admin = new User(
                        username,
                        password,
                        "ROLE_ADMIN",
                        username,
                        null,
                        null,
                        "admin@bugbusters.com",
                        null);
                userRepository.save(admin);
            }
        };
    }
}
