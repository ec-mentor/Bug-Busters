package bugbusters.everyonecodes.java.registration.service;

import bugbusters.everyonecodes.java.registration.Validation.Password.PasswordConstraintValidator;
import bugbusters.everyonecodes.java.registration.data.User;
import bugbusters.everyonecodes.java.registration.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordConstraintValidator validator;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordConstraintValidator validator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    public User saveUser(User user) throws IllegalArgumentException {
        //Need more info on context, currently implemented as null, as it is not needed
        if (!validator.isValid(user.getPassword(), null)) throw new IllegalArgumentException();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }
}
