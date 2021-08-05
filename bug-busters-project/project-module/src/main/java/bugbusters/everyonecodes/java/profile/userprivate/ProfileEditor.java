package bugbusters.everyonecodes.java.profile.userprivate;

import bugbusters.everyonecodes.java.registration.data.User;
import bugbusters.everyonecodes.java.registration.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileEditor {

    private final UserRepository userRepository;

    public ProfileEditor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User editUserData(UserPrivateDTO input) {
        var userOptional = userRepository.findOneByUsername(input.getUsername());
        if (userOptional.isEmpty()) {
            return null;
        }
        var user = userOptional.get();

        user.setFullName(input.getFullName());
        user.setBirthday(input.getBirthday());
        user.setAddress(input.getAddress());
        user.setEmail(input.getEmail());
        user.setDescription(input.getDescription());

        userRepository.save(user);
        return user;
    }

    public User getUser(String username) {
        var userOptional = userRepository.findOneByUsername(username);
        if (userOptional.isEmpty()) {
            return null;
        }
        return userOptional.get();
    }

}
