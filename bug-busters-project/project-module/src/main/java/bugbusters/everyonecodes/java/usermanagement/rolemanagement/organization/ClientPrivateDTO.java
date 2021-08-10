package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;

import javax.validation.Valid;

public class ClientPrivateDTO {

    @Valid
    private UserPrivateDTO userPrivateDTO;

    public ClientPrivateDTO() {
    }

    public ClientPrivateDTO(UserPrivateDTO userPrivateDTO) {
        this.userPrivateDTO = userPrivateDTO;
    }

    public UserPrivateDTO getUserPrivateDTO() {
        return userPrivateDTO;
    }

    public void setUserPrivateDTO(UserPrivateDTO userPrivateDTO) {
        this.userPrivateDTO = userPrivateDTO;
    }
}
