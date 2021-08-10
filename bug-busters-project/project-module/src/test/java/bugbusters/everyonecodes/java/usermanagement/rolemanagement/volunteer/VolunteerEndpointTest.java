package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class VolunteerEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    VolunteerService volunteerService;

    private final String url = "/volunteer";

    // viewOwnProfileTest
    @Test
    @WithMockUser(username = "test", password = "Testing1#", roles = {"ROLE_VOLUNTEER"})
    void viewUserProfile_methodCalled() throws Exception {
        String username = "test";
        Mockito.when(volunteerService.viewVolunteerPrivateData(username))
                .thenReturn(Optional.of(new VolunteerPrivateDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/login")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(volunteerService).viewVolunteerPrivateData(username);
    }

}