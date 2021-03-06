package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.activities.ActivityDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.Volunteer;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerSearchResultDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
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

    //tests for viewWebAppTree
    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_ORGANIZATION"})
    void viewWebAppTree() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/webapptree")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        String expected = "geh";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(url + "/webapptree")).andReturn();
        Assertions.assertEquals(expected, result.getResponse().getContentAsString());
    }

    //searchVolunteersByText Test
    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_ORGANIZATION"})
    void searchActivitiesByText_listNotEmpty() throws Exception {
        String text = "test";
        String username = "testUsername";
        VolunteerSearchResultDTO volunteerSearchResultDTO = new VolunteerSearchResultDTO("testUsername", null, null);
        List<VolunteerSearchResultDTO> volunteerSearchResultDTOList = List.of(volunteerSearchResultDTO);
        Mockito.when(organizationService.searchVolunteersByText(text)).thenReturn(volunteerSearchResultDTOList);
        var result = mockMvc.perform(MockMvcRequestBuilders.get(url + "/search/volunteers/" + text)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        var resultResponse = result.getResponse().getContentAsString();
        Assertions.assertTrue(resultResponse.contains(username));
        Assertions.assertFalse(resultResponse.contains("No results found for"));
    }

    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_ORGANIZATION"})
    void searchActivitiesByText_listEmpty() throws Exception {
        String text = "test";
        String expected = "No results found for \"test\"";
        Mockito.when(organizationService.searchVolunteersByText(text)).thenReturn(List.of());
        var result = mockMvc.perform(MockMvcRequestBuilders.get(url + "/search/volunteers/" + text)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        var resultResponse = result.getResponse().getContentAsString();
        Assertions.assertEquals(expected, resultResponse);
    }


}