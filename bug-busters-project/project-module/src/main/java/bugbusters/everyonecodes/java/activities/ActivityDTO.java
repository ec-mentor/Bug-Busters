package bugbusters.everyonecodes.java.activities;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityDTO {

    private String usernameOfOtherParty;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private Status status;

    @NotEmpty
    private LocalDateTime startDate;

    @NotEmpty
    private LocalDateTime endDate;

    private String roleOfOtherParty;

    private Double ratingOfOtherParty;

    private Integer ratingFromClient;
    private Integer ratingFromVolunteer;
    private String feedbackFromClient;
    private String feedbackFromVolunteer;

    public ActivityDTO() {

    }

    public ActivityDTO(String usernameOfOtherParty, String title, String description, Status status, LocalDateTime startDate, LocalDateTime endDate, String roleOfOtherParty, Double ratingOfOtherParty, Integer ratingFromClient, String feedbackFromClient, Integer ratingFromVolunteer, String feedbackFromVolunteer) {
        this.usernameOfOtherParty = usernameOfOtherParty;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roleOfOtherParty = roleOfOtherParty;
        this.ratingOfOtherParty = ratingOfOtherParty;
        this.ratingFromClient = ratingFromClient;
        this.ratingFromVolunteer = ratingFromVolunteer;
        this.feedbackFromClient = feedbackFromClient;
        this.feedbackFromVolunteer = feedbackFromVolunteer;
    }

    public String getUsernameOfOtherParty() {
        return usernameOfOtherParty;
    }

    public void setUsernameOfOtherParty(String usernameOfOtherParty) {
        this.usernameOfOtherParty = usernameOfOtherParty;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getRoleOfOtherParty() {
        return roleOfOtherParty;
    }

    public void setRoleOfOtherParty(String roleOfOtherParty) {
        this.roleOfOtherParty = roleOfOtherParty;
    }

    public Double getRatingOfOtherParty() {
        return ratingOfOtherParty;
    }

    public void setRatingOfOtherParty(Double ratingOfOtherParty) {
        this.ratingOfOtherParty = ratingOfOtherParty;
    }

    public Integer getRatingFromClient() {
        return ratingFromClient;
    }

    public void setRatingFromClient(Integer ratingFromClient) {
        this.ratingFromClient = ratingFromClient;
    }

    public Integer getRatingFromVolunteer() {
        return ratingFromVolunteer;
    }

    public void setRatingFromVolunteer(Integer ratingFromVolunteer) {
        this.ratingFromVolunteer = ratingFromVolunteer;
    }

    public String getFeedbackFromClient() {
        return feedbackFromClient;
    }

    public void setFeedbackFromClient(String feedbackFromClient) {
        this.feedbackFromClient = feedbackFromClient;
    }

    public String getFeedbackFromVolunteer() {
        return feedbackFromVolunteer;
    }

    public void setFeedbackFromVolunteer(String feedbackFromVolunteer) {
        this.feedbackFromVolunteer = feedbackFromVolunteer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityDTO that = (ActivityDTO) o;
        return Objects.equals(usernameOfOtherParty, that.usernameOfOtherParty) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && status == that.status && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(roleOfOtherParty, that.roleOfOtherParty) && Objects.equals(ratingOfOtherParty, that.ratingOfOtherParty) && Objects.equals(ratingFromClient, that.ratingFromClient) && Objects.equals(ratingFromVolunteer, that.ratingFromVolunteer) && Objects.equals(feedbackFromClient, that.feedbackFromClient) && Objects.equals(feedbackFromVolunteer, that.feedbackFromVolunteer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usernameOfOtherParty, title, description, status, startDate, endDate, roleOfOtherParty, ratingOfOtherParty, ratingFromClient, ratingFromVolunteer, feedbackFromClient, feedbackFromVolunteer);
    }

    @Override
    public String toString() {
        return "ActivityDTO{" +
                "usernameOfOtherParty='" + usernameOfOtherParty + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", statusVolunteer=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", roleOfOtherParty='" + roleOfOtherParty + '\'' +
                ", ratingOfOtherParty=" + ratingOfOtherParty +
                ", ratingFromClient=" + ratingFromClient +
                ", ratingFromVolunteer=" + ratingFromVolunteer +
                ", feedbackFromClient='" + feedbackFromClient + '\'' +
                ", feedbackFromVolunteer='" + feedbackFromVolunteer + '\'' +
                '}';
    }
}
