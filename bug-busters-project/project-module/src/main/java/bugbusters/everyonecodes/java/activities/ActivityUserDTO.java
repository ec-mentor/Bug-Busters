package bugbusters.everyonecodes.java.activities;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class ActivityUserDTO {

    @NotEmpty
    private String role;

    @NotEmpty
    private String username;

    @NotEmpty
    private double rating;

    public ActivityUserDTO() {

    }

    public ActivityUserDTO(String role, String username, double rating) {
        this.role = role;
        this.username = username;
        this.rating = rating;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityUserDTO that = (ActivityUserDTO) o;
        return Double.compare(that.rating, rating) == 0 && Objects.equals(role, that.role) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, username, rating);
    }

    @Override
    public String toString() {
        return "ActivityUserDTO{" +
                "role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", rating=" + rating +
                '}';
    }
}