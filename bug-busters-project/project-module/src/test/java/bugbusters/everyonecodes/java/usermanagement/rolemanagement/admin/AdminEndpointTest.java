package bugbusters.everyonecodes.java.usermanagement.rolemanagement.admin;

import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.Volunteer;
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

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AdminEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AdminService service;

    private final String url = "/admin";

    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_ADMIN"})
    void getVolunteers() throws Exception {
        Mockito.when(service.listAllVolunteers()).thenReturn(List.of(new AdminDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/volunteers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(service, Mockito.times(1)).listAllVolunteers();
    }

    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_ADMIN"})
    void getOrganizations() throws Exception {
        Mockito.when(service.listAllOrganizations()).thenReturn(List.of(new AdminDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/organizations")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(service, Mockito.times(1)).listAllOrganizations();
    }

    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_ADMIN"})
    void getIndividuals() throws Exception {
        Mockito.when(service.listAllIndividuals()).thenReturn(List.of(new AdminDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/individuals")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(service, Mockito.times(1)).listAllIndividuals();
    }

    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_ADMIN"})
    void getAccount() throws Exception {
        String username = "test";
        Mockito.when(service.getAccountDataByUsername(username)).thenReturn(new Volunteer());
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/account/" + username)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(service).getAccountDataByUsername(username);
    }



}