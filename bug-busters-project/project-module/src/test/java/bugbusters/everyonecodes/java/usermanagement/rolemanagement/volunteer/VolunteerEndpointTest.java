package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.activities.ActivityDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientPublicDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class VolunteerEndpointTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    VolunteerService volunteerService;

    private final String url = "/volunteer";

    // viewVolunteerPrivateData Test
    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_VOLUNTEER"})
    void viewVolunteerPrivateData_authorized() throws Exception {
        String username = "test";
        Mockito.when(volunteerService.viewVolunteerPrivateData(username))
                .thenReturn(Optional.of(new VolunteerPrivateDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/login")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(volunteerService, Mockito.times(1)).viewVolunteerPrivateData(username);
    }

    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_ORGANIZATION"})
    void viewVolunteerPrivateData_forbidden() throws Exception {
        String username = "test";
        Mockito.when(volunteerService.viewVolunteerPrivateData(username))
                .thenReturn(Optional.of(new VolunteerPrivateDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/login")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        Mockito.verify(volunteerService, Mockito.never()).viewVolunteerPrivateData(username);
    }

    //editVolunteerData Test
    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_VOLUNTEER"})
    void editUserProfile_validInput() throws Exception {
        String username = "test";
        VolunteerPrivateDTO input = new VolunteerPrivateDTO(new UserPrivateDTO("test", "test", "test", null, null, "test.test@test.com", null), "cooking;testing;ability to not scream out of frustration");
        Mockito.when(volunteerService.editVolunteerData(input, username))
                .thenReturn(Optional.of(input));
        mockMvc.perform(MockMvcRequestBuilders.put(url + "/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(input))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(volunteerService).editVolunteerData(input, username);
    }

    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_VOLUNTEER"})
    void editUserProfile_invalidUserPrivateDTO() throws Exception {
        String username = "test";
        VolunteerPrivateDTO input = new VolunteerPrivateDTO(new UserPrivateDTO("test", "test", null, null, null, null, null), "testing");
        mockMvc.perform(MockMvcRequestBuilders.put(url + "/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(input))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(volunteerService, Mockito.never()).editVolunteerData(input, username);
    }

    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_VOLUNTEER"})
    void editUserProfile_invalidSkills() throws Exception {
        String username = "test";
        VolunteerPrivateDTO input = new VolunteerPrivateDTO(new UserPrivateDTO("test", "test", "test", null, null, "test.test@test.com", null), "200 years");
        mockMvc.perform(MockMvcRequestBuilders.put(url + "/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(input))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(volunteerService, Mockito.never()).editVolunteerData(input, username);
    }

    //viewVolunteerPublicData Test
    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_VOLUNTEER"})
    void viewVolunteerPublicData() throws Exception {
        String username = "test";
        Mockito.when(volunteerService.viewVolunteerPublicData(username))
                .thenReturn(Optional.of(new VolunteerPublicDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/view")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(volunteerService, Mockito.times(1)).viewVolunteerPublicData(username);
    }

    //viewClientPublicData Test
    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_VOLUNTEER"})
    void viewClientPublicData() throws Exception {
        String targetName = "target";
        Mockito.when(volunteerService.viewClientPublicData(targetName))
                .thenReturn(Optional.of(new ClientPublicDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/view/" + targetName)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(volunteerService, Mockito.times(1)).viewClientPublicData(targetName);
    }

    //searchActivitiesByText Test
    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_VOLUNTEER"})
    void searchActivitiesByText_listNotEmpty() throws Exception {
        String text = "test";
        String description = "descriptionTest";
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setDescription(description);
        List<ActivityDTO> activityDTOList = List.of(activityDTO);
        Mockito.when(volunteerService.searchPendingActivitiesByText(text)).thenReturn(activityDTOList);
        var result = mockMvc.perform(MockMvcRequestBuilders.get(url + "/activities/search/" + text)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        var resultResponse = result.getResponse().getContentAsString();
        Assertions.assertTrue(resultResponse.contains(description));
        Assertions.assertFalse(resultResponse.contains("No results found for"));
    }

    @Test
    @WithMockUser(username = "test", password = "Testing1#", authorities = {"ROLE_VOLUNTEER"})
    void searchActivitiesByText_listEmpty() throws Exception {
        String text = "test";
        String expected = "No results found for \"test\"";
        Mockito.when(volunteerService.searchPendingActivitiesByText(text)).thenReturn(List.of());
        var result = mockMvc.perform(MockMvcRequestBuilders.get(url + "/activities/search/" + text)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        var resultResponse = result.getResponse().getContentAsString();
        Assertions.assertEquals(expected, resultResponse);
    }


}