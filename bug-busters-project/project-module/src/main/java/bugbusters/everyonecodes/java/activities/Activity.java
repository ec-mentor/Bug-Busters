package bugbusters.everyonecodes.java.activities;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Activity {
    @Id
    @GeneratedValue
    private Long id;


    //ToDO: Refactor field to role classes, because "Client" is not an entity
    @NotEmpty
    private String creator;

    private String Volunteer;

    @NotEmpty
    @Size(min = 3, max = 40)
    private String title;

    @NotEmpty
    private String description;

    @NotNull
    @ElementCollection
    private Set<String> recommendedSkills = new HashSet<>();

    @NotNull
    @ElementCollection
    private Set<String> categories = new HashSet<>();

    @NotEmpty
    private LocalDateTime startTime;

    @NotEmpty
    private LocalDateTime endTime;

    private boolean isOpenEnd;

    @NotEmpty
    private Status statusVolunteer;

    @NotEmpty
    private Status statusClient;

    @Min(1) @Max(5)
    private Integer ratingFromVolunteer;

    @Min(1) @Max(5)
    private Integer ratingFromClient;

    private String feedbackFromVolunteer;
    private String feedbackFromClient;

    public Activity(){}

    public Activity(String creator, String title, String description, Set<String> recommendedSkills, Set<String> categories, LocalDateTime startTime, LocalDateTime endTime, boolean isOpenEnd, Status statusVolunteer, Status statusClient, Integer ratingFromVolunteer, Integer ratingFromClient, String feedbackFromVolunteer, String feedbackFromClient) {
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.recommendedSkills = recommendedSkills;
        this.categories = categories;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isOpenEnd = isOpenEnd;
        this.statusVolunteer = statusVolunteer;
        this.statusClient = statusClient;
        this.ratingFromVolunteer = ratingFromVolunteer;
        this.ratingFromClient = ratingFromClient;
        this.feedbackFromVolunteer = feedbackFromVolunteer;
        this.feedbackFromClient = feedbackFromClient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getVolunteer() {
        return Volunteer;
    }

    public void setVolunteer(String volunteer) {
        Volunteer = volunteer;
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

    public Set<String> getRecommendedSkills() {
        return recommendedSkills;
    }

    public void setRecommendedSkills(Set<String> recommendedSkills) {
        this.recommendedSkills = recommendedSkills;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isOpenEnd() {
        return isOpenEnd;
    }

    public void setOpenEnd(boolean openEnd) {
        isOpenEnd = openEnd;
    }

    public Status getStatusVolunteer() {
        return statusVolunteer;
    }

    public void setStatusVolunteer(Status statusVolunteer) {
        this.statusVolunteer = statusVolunteer;
    }

    public Status getStatusClient() {
        return statusClient;
    }

    public void setStatusClient(Status statusClient) {
        this.statusClient = statusClient;
    }

    public Integer getRatingFromVolunteer() {
        return ratingFromVolunteer;
    }

    public void setRatingFromVolunteer(Integer ratingFromVolunteer) {
        this.ratingFromVolunteer = ratingFromVolunteer;
    }

    public Integer getRatingFromClient() {
        return ratingFromClient;
    }

    public void setRatingFromClient(Integer ratingFromClient) {
        this.ratingFromClient = ratingFromClient;
    }

    public String getFeedbackFromVolunteer() {
        return feedbackFromVolunteer;
    }

    public void setFeedbackFromVolunteer(String feedbackFromVolunteer) {
        this.feedbackFromVolunteer = feedbackFromVolunteer;
    }

    public String getFeedbackFromClient() {
        return feedbackFromClient;
    }

    public void setFeedbackFromClient(String feedbackFromClient) {
        this.feedbackFromClient = feedbackFromClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return isOpenEnd == activity.isOpenEnd && Objects.equals(id, activity.id) && Objects.equals(creator, activity.creator) && Objects.equals(Volunteer, activity.Volunteer) && Objects.equals(title, activity.title) && Objects.equals(description, activity.description) && Objects.equals(recommendedSkills, activity.recommendedSkills) && Objects.equals(categories, activity.categories) && Objects.equals(startTime, activity.startTime) && Objects.equals(endTime, activity.endTime) && statusVolunteer == activity.statusVolunteer && statusClient == activity.statusClient && Objects.equals(ratingFromVolunteer, activity.ratingFromVolunteer) && Objects.equals(ratingFromClient, activity.ratingFromClient) && Objects.equals(feedbackFromVolunteer, activity.feedbackFromVolunteer) && Objects.equals(feedbackFromClient, activity.feedbackFromClient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creator, Volunteer, title, description, recommendedSkills, categories, startTime, endTime, isOpenEnd, statusVolunteer, statusClient, ratingFromVolunteer, ratingFromClient, feedbackFromVolunteer, feedbackFromClient);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", creator='" + creator + '\'' +
                ", Volunteer='" + Volunteer + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", recommendedSkills=" + recommendedSkills +
                ", categories=" + categories +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", isOpenEnd=" + isOpenEnd +
                ", statusVolunteer=" + statusVolunteer +
                ", statusClient=" + statusClient +
                ", ratingFromVolunteer=" + ratingFromVolunteer +
                ", ratingFromClient=" + ratingFromClient +
                ", feedbackFromVolunteer='" + feedbackFromVolunteer + '\'' +
                ", feedbackFromClient='" + feedbackFromClient + '\'' +
                '}';
    }
}