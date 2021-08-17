package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VolunteerSearchResultDTO {

    private String username;
    private String skills;
    private Double rating;


    public VolunteerSearchResultDTO() {}

    public VolunteerSearchResultDTO(String username, String skills, Double rating) {
        this.username = username;
        this.skills = skills;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VolunteerSearchResultDTO that = (VolunteerSearchResultDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(skills, that.skills) && Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, skills, rating);
    }
}
