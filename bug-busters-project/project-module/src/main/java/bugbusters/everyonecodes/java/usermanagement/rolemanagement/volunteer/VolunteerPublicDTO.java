package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VolunteerPublicDTO {
    private UserPublicDTO user;
    private String skills;

    VolunteerPublicDTO() {}

    public VolunteerPublicDTO(UserPublicDTO user, String skills) {
        this.user = user;
        this.skills = skills;
    }

    public UserPublicDTO getUser() {
        return user;
    }

    public void setUser(UserPublicDTO user) {
        this.user = user;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
