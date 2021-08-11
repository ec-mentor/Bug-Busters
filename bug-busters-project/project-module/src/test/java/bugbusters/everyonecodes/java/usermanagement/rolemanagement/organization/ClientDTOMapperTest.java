package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.Client;
import bugbusters.everyonecodes.java.usermanagement.service.UserDTOMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ClientDTOMapperTest {

    @Autowired
    ClientDTOMapper clientDTOMapper;

    @MockBean
    UserDTOMapper userDTOMapper;

    //toClientPrivateDTO test
    @Test
    void toClientPrivateDTO() {
        Client client = new Client() {
            @Override
            public User getUser() {
                return new User(
                        "username",
                        "password",
                        "role",
                        "fullname",
                        LocalDate.now(),
                        "address",
                        "email",
                        "description");
            }
        };
        UserPrivateDTO privateDTO = new UserPrivateDTO(
                "username",
                "role",
                "fullname",
                LocalDate.now(),
                "address",
                "email",
                "description");
        Mockito.when(userDTOMapper.toUserPrivateDTO(client.getUser())).thenReturn(privateDTO);
        ClientPrivateDTO result = clientDTOMapper.toClientPrivateDTO(client);
        ClientPrivateDTO expected = new ClientPrivateDTO(privateDTO);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void toClientPrivateDTO_nullValues() {
        Client client = new Client() {
            @Override
            public User getUser() {
                return new User(
                        "username",
                        "password",
                        "role",
                        null,
                        null,
                        null,
                        null,
                        null);
            }
        };
        UserPrivateDTO privateDTO = new UserPrivateDTO(
                "username",
                "role",
                null,
                null,
                null,
                null,
                null);
        Mockito.when(userDTOMapper.toUserPrivateDTO(client.getUser())).thenReturn(privateDTO);
        ClientPrivateDTO result = clientDTOMapper.toClientPrivateDTO(client);
        ClientPrivateDTO expected = new ClientPrivateDTO(privateDTO);
        Assertions.assertEquals(expected, result);
    }

    //toClientPublicDTO test
    @Test
    void toClientPublicDTO() {
        Client client = new Client() {
            @Override
            public User getUser() {
                return new User(
                        "username",
                        "password",
                        "role",
                        "fullname",
                        LocalDate.now(),
                        "address",
                        "email",
                        "description");
            }
        };
        UserPublicDTO publicDTO = new UserPublicDTO(
                "username",
                "fullname",
                0,
                "description",
                null
        );
        Mockito.when(userDTOMapper.toUserPublicDTO(client.getUser())).thenReturn(publicDTO);
        ClientPublicDTO result = clientDTOMapper.toClientPublicDTO(client);
        ClientPublicDTO expected = new ClientPublicDTO(publicDTO);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void toClientPublicDTO_nullValues() {
        Client client = new Client() {
            @Override
            public User getUser() {
                return new User(
                        "username",
                        "password",
                        "role",
                        null,
                        null,
                        null,
                        null,
                        null);
            }
        };
        UserPublicDTO publicDTO = new UserPublicDTO(
                "username",
                null,
                null,
                null,
                null
        );
        Mockito.when(userDTOMapper.toUserPublicDTO(client.getUser())).thenReturn(publicDTO);
        ClientPublicDTO result = clientDTOMapper.toClientPublicDTO(client);
        ClientPublicDTO expected = new ClientPublicDTO(publicDTO);
        Assertions.assertEquals(expected, result);
    }

}