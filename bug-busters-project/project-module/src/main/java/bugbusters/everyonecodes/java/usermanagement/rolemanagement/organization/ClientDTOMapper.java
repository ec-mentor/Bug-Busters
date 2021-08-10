package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.usermanagement.rolemanagement.Client;
import bugbusters.everyonecodes.java.usermanagement.service.UserDTOMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Service;

@Service
public class ClientDTOMapper {

    private final UserDTOMapper mapper;

    public ClientDTOMapper(UserDTOMapper mapper) {
        this.mapper = mapper;
    }

    public ClientPrivateDTO toClientPrivateDTO(Client client) {
        return new ClientPrivateDTO(mapper.toUserPrivateDTO(client.getUser()));
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ClientPublicDTO toClientPublicDTO(Client client) {
        return new ClientPublicDTO(mapper.toUserPublicDTO(client.getUser()));
    }

}
