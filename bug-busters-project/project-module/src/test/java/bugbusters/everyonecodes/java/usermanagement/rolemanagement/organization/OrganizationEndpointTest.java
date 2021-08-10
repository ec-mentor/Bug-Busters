package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerPublicDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class OrganizationEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrganizationService organizationService;

    private final String url = "/organization";

    //tests for viewOrganizationPrivateData

    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_ORGANIZATION"})
    void viewOrganizationPrivateData_authorized() throws Exception{
        String username = "test";
        Mockito.when(organizationService.viewOrganisationPrivateData(username))
                .thenReturn(Optional.of(new ClientPrivateDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/login")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(organizationService, Mockito.times(1)).viewOrganisationPrivateData(username);
    }
    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_INDIVIDUAL"})
    void viewOrganizationPrivateData_forbidden() throws Exception{
        String username = "test";
        Mockito.when(organizationService.viewOrganisationPrivateData(username))
                .thenReturn(Optional.of(new ClientPrivateDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/login")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        Mockito.verify(organizationService, Mockito.never()).viewOrganisationPrivateData(username);
    }

    //tests for viewOrganizationPublicData
    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_ORGANIZATION"})
    void viewOrganizationPublicData() throws Exception{
        String username = "test";
        Mockito.when(organizationService.viewOrganisationPublicData(username))
                .thenReturn(Optional.of(new ClientPublicDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/view")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(organizationService, Mockito.times(1)).viewOrganisationPublicData(username);
    }


    //tests for viewVolunteerPublicData
    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_ORGANIZATION"})
    void viewVolunteerPublicData() throws Exception{
        String inputName = "input";
        Mockito.when(organizationService.viewVolunteerPublicData(inputName))
                .thenReturn(Optional.of(new VolunteerPublicDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/view/" + inputName )
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(organizationService, Mockito.times(1)).viewVolunteerPublicData(inputName);
    }

    //tests for editOrganizationData
    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_ORGANIZATION"})
    void editIndividualData_validInput() throws Exception {
        String username = "test";
        ClientPrivateDTO input = new ClientPrivateDTO(new UserPrivateDTO("test", "test", "test", null, null, "test.test@test.com", null));
        Mockito.when(organizationService.editOrganizationData(input, username))
                .thenReturn(Optional.of(input));
        mockMvc.perform(MockMvcRequestBuilders.put(url + "/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(input))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(organizationService).editOrganizationData(input, username);
    }
    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_ORGANIZATION"})
    void editIndividualData_invalidUserPrivateDTO() throws Exception {
        String username = "test";
        ClientPrivateDTO input = new ClientPrivateDTO(new UserPrivateDTO("test", "test", null, null, null, "test.test@test.com", null));
        Mockito.when(organizationService.editOrganizationData(input, username))
                .thenReturn(Optional.of(input));
        mockMvc.perform(MockMvcRequestBuilders.put(url + "/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(input))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(organizationService, Mockito.never()).editOrganizationData(input, username);
    }

}