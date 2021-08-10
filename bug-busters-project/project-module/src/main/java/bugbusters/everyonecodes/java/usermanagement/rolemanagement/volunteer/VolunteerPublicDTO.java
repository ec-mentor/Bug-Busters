package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.Valid;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VolunteerPublicDTO {

    private UserPublicDTO user;
    private String skills;

    public VolunteerPublicDTO() {}

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VolunteerPublicDTO that = (VolunteerPublicDTO) o;
        return Objects.equals(user, that.user) && Objects.equals(skills, that.skills);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, skills);
    }

    @Override
    public String toString() {
        return "VolunteerPublicDTO{" +
                "user=" + user +
                ", skills='" + skills + '\'' +
                '}';
    }
}
