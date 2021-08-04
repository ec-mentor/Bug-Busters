package bugbusters.everyonecodes.java.profile.userprivate;

import bugbusters.everyonecodes.java.registration.data.User;

public class PrivateDTOService {
    public UserPrivateDTO toDTO(User input) {
        return new UserPrivateDTO(
                input.getUsername(),
                input.getRole(),
                input.getFullName(),
                input.getBirthday(),
                input.getAddress(),
                input.getEmail(),
                input.getDescription()
        );
    }
}
