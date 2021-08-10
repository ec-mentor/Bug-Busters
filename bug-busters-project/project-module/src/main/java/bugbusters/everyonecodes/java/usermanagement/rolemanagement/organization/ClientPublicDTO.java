package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientPublicDTO {

    private UserPublicDTO userPublicDTO;

    public ClientPublicDTO() {
    }

    public ClientPublicDTO(UserPublicDTO userPublicDTO) {
        this.userPublicDTO = userPublicDTO;
    }

    public UserPublicDTO getUserPublicDTO() {
        return userPublicDTO;
    }

    public void setUserPublicDTO(UserPublicDTO userPublicDTO) {
        this.userPublicDTO = userPublicDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientPublicDTO that = (ClientPublicDTO) o;
        return Objects.equals(userPublicDTO, that.userPublicDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userPublicDTO);
    }

    @Override
    public String toString() {
        return "ClientPublicDTO{" +
                "userPublicDTO=" + userPublicDTO +
                '}';
    }
}
