package bugbusters.everyonecodes.java.profile.userprivate;

import bugbusters.everyonecodes.java.registration.data.User;
import bugbusters.everyonecodes.java.registration.repository.UserRepository;

public class ProfileEditor {
    private final UserRepository userRepository;

    public ProfileEditor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User editUserData(UserPrivateDTO input) {
        var userOptional = userRepository.findOneByUsername(input.getUsername());
        if (userOptional.isEmpty()) {
            return null;
        }
        var user = userOptional.get();

        user.setRole(input.getRole());
        user.setFullName(input.getFullName());
        user.setBirthday(input.getBirthday());
        user.setAddress(input.getAddress());
        user.setEmail(input.getEmail());
        user.setDescription(input.getDescription());

        userRepository.save(user);
        return user;
    }
}
