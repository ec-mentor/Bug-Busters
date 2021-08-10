package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;

import javax.validation.Valid;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientPrivateDTO that = (ClientPrivateDTO) o;
        return Objects.equals(userPrivateDTO, that.userPrivateDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userPrivateDTO);
    }

    @Override
    public String toString() {
        return "ClientPrivateDTO{" +
                "userPrivateDTO=" + userPrivateDTO +
                '}';
    }
}
