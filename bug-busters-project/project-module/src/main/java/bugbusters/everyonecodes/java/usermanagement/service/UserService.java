package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper mapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTOMapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    //regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$^&+=])(?=\\S+$).{6,}")
    //password must contain at least 1 digit, 1 lower and upper case character, 1 special character, no whitespaces and must be at least 6 characters long
    public User saveUser(User user) throws IllegalArgumentException {
        if (!user.getPassword().matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$^&+=/_-])(?=\\S+$).{6,100}")) throw new IllegalArgumentException();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
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

    public Optional<UserPrivateDTO> viewUserPrivateData(String username) {
        var oUser = getUserByUsername(username);
        return oUser.map(mapper::toUserPrivateDTO);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }

    public Optional<UserPublicDTO> viewUserPublicDTO(String username){
        return userRepository.findOneByUsername(username).map(mapper::toUserPublicDTO);
    }
}

