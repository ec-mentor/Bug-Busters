package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

public class VolunteerPrivateDTO {

    @Valid
    private UserPrivateDTO user;
    @Pattern(regexp = "^[a-zA-Z\s;]*$")
    private String skills;

    public VolunteerPrivateDTO() {
    }

    public VolunteerPrivateDTO(UserPrivateDTO user, String skills) {
        this.user = user;
        this.skills = skills;
    }

    public UserPrivateDTO getUser() {
        return user;
    }

    public void setUser(UserPrivateDTO user) {
        this.user = user;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
