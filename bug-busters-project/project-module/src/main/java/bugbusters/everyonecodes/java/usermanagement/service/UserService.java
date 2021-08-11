package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.RoleFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper mapper;
    private final RoleFactory roleFactory;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTOMapper mapper, RoleFactory roleFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.roleFactory = roleFactory;
    }

    //regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$^&+=])(?=\\S+$).{6,}")
    //password must contain at least 1 digit, 1 lower and upper case character, 1 special character, no whitespaces and must be at least 6 characters long
    public User saveUser(User user) throws IllegalArgumentException {
        if (!user.getPassword().matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$^&+=/_-])(?=\\S+$).{6,100}")) throw new IllegalArgumentException();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user = userRepository.save(user);
        roleFactory.createRole(user);
        return user;
    }

    public Optional<UserPrivateDTO> editUserData(UserPrivateDTO input, String username) {
        var oUser = getUserByUsername(username);
        if (oUser.isEmpty()) return Optional.empty();
        var user = oUser.get();
        user.setFullName(input.getFullName());
        user.setBirthday(input.getBirthday());
        user.setAddress(input.getAddress());
        user.setEmail(input.getEmail());
        user.setDescription(input.getDescription());
        return Optional.of(mapper.toUserPrivateDTO(userRepository.save(user)));
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }

    public Optional<UserPrivateDTO> viewUserPrivateData(String username) {
        return getUserByUsername(username).map(user -> mapper.toUserPrivateDTO(user));
    }

    public Optional<UserPublicDTO> viewUserPublicData(String username) {
        return userRepository.findOneByUsername(username).map(user -> mapper.toUserPublicDTO(user));
    }
}

