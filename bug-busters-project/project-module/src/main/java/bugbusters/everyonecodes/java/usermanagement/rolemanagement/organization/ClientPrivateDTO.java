package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;

public class ClientPrivateDTO {

    private final UserPrivateDTO userPrivateDTO;

    public ClientPrivateDTO(UserPrivateDTO userPrivateDTO) {
        this.userPrivateDTO = userPrivateDTO;
    }

    public UserPrivateDTO getUserPrivateDTO() {
        return userPrivateDTO;
    }
}
