package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;

public class ClientPublicDTO {

    private final UserPublicDTO userPublicDTO;

    public ClientPublicDTO(UserPublicDTO userPublicDTO) {
        this.userPublicDTO = userPublicDTO;
    }

    public UserPublicDTO getUserPublicDTO() {
        return userPublicDTO;
    }
}
