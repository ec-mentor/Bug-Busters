package bugbusters.everyonecodes.java.usermanagement.rolemanagement.admin;

import java.util.Objects;

public class AdminDTO {

    private String username;
    private double rating;
    private int activitiesPending;
    private int activitiesInProgress;
    private int activitiesCompleted;

    public AdminDTO(String username, double rating, int activitiesPending, int activitiesInProgress, int activitiesCompleted) {
        this.username = username;
        this.rating = rating;
        this.activitiesPending = activitiesPending;
        this.activitiesInProgress = activitiesInProgress;
        this.activitiesCompleted = activitiesCompleted;
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

    public int getActivitiesPending() {
        return activitiesPending;
    }

    public void setActivitiesPending(int activitiesPending) {
        this.activitiesPending = activitiesPending;
    }

    public int getActivitiesInProgress() {
        return activitiesInProgress;
    }

    public void setActivitiesInProgress(int activitiesInProgress) {
        this.activitiesInProgress = activitiesInProgress;
    }

    public int getActivitiesCompleted() {
        return activitiesCompleted;
    }

    public void setActivitiesCompleted(int activitiesCompleted) {
        this.activitiesCompleted = activitiesCompleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminDTO adminDTO = (AdminDTO) o;
        return Double.compare(adminDTO.rating, rating) == 0 && activitiesPending == adminDTO.activitiesPending && activitiesInProgress == adminDTO.activitiesInProgress && activitiesCompleted == adminDTO.activitiesCompleted && Objects.equals(username, adminDTO.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, rating, activitiesPending, activitiesInProgress, activitiesCompleted);
    }

    @Override
    public String toString() {
        return "AdminDTO{" +
                "username='" + username + '\'' +
                ", rating=" + rating +
                ", activitiesPending=" + activitiesPending +
                ", activitiesInProgress=" + activitiesInProgress +
                ", activitiesCompleted=" + activitiesCompleted +
                '}';
    }
}
