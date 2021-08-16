package bugbusters.everyonecodes.java.activities;

import bugbusters.everyonecodes.java.usermanagement.rolemanagement.Client;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.Volunteer;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Objects;

public class ActivityDTO {

    private Client creator;

    private Volunteer volunteer;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private Status statusClient;

    @NotEmpty
    private Status statusVolunteer;

    @NotEmpty
    private LocalDateTime startDate;

    @NotEmpty
    private LocalDateTime endDate;

    @NotEmpty
    private String role;

    @NotEmpty
    private String username;

    @NotEmpty
    private double rating;

    private int myRatingToThem;
    private String myFeedbackToThem;
    private int theirRatingToMe;
    private String theirFeedbackToMe;

    public ActivityDTO(String title, String description, Status statusVolunteer, LocalDateTime startTime, LocalDateTime endTime, String role, String username, Double aDouble, Integer ratingFromVolunteer, String feedbackFromVolunteer, Integer ratingFromClient, String feedbackFromClient) {

    }

    public ActivityDTO(String title, String description, Status statusClient, Status statusVolunteer, LocalDateTime startDate, LocalDateTime endDate, String role, String username, double rating, int myRatingToThem, String myFeedbackToThem, int theirRatingToMe, String theirFeedbackToMe) {
        this.title = title;
        this.description = description;
        this.statusClient = statusClient;
        this.statusVolunteer = statusVolunteer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.role = role;
        this.username = username;
        this.rating = rating;
        this.myRatingToThem = myRatingToThem;
        this.myFeedbackToThem = myFeedbackToThem;
        this.theirRatingToMe = theirRatingToMe;
        this.theirFeedbackToMe = theirFeedbackToMe;
    }

    public ActivityDTO(Client creator, String title, String description, Status statusClient, LocalDateTime startDate, LocalDateTime endDate, String role, String username, double rating, int myRatingToThem, String myFeedbackToThem, int theirRatingToMe, String theirFeedbackToMe) {
        this.creator = creator;
        this.title = title;
        this.description = description;
        this.statusClient = statusClient;
        this.startDate = startDate;
        this.endDate = endDate;
        this.role = role;
        this.username = username;
        this.rating = rating;
        this.myRatingToThem = myRatingToThem;
        this.myFeedbackToThem = myFeedbackToThem;
        this.theirRatingToMe = theirRatingToMe;
        this.theirFeedbackToMe = theirFeedbackToMe;
    }

    public ActivityDTO(Client creator, Volunteer volunteer, String title, String description, Status statusVolunteer, LocalDateTime startDate, LocalDateTime endDate, String role, String username, double rating, int myRatingToThem, String myFeedbackToThem, int theirRatingToMe, String theirFeedbackToMe) {
        this.creator = creator;
        this.volunteer = volunteer;
        this.title = title;
        this.description = description;
        this.statusVolunteer = statusVolunteer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.role = role;
        this.username = username;
        this.rating = rating;
        this.myRatingToThem = myRatingToThem;
        this.myFeedbackToThem = myFeedbackToThem;
        this.theirRatingToMe = theirRatingToMe;
        this.theirFeedbackToMe = theirFeedbackToMe;
    }

    public Client getCreator() {
        return creator;
    }

    public void setCreator(Client creator) {
        this.creator = creator;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
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

    public Status getStatusClient() {
        return statusClient;
    }

    public void setStatusClient(Status statusClient) {
        this.statusClient = statusClient;
    }

    public Status getStatusVolunteer() {
        return statusVolunteer;
    }

    public void setStatusVolunteer(Status statusVolunteer) {
        this.statusVolunteer = statusVolunteer;
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

    public int getMyRatingToThem() {
        return myRatingToThem;
    }

    public void setMyRatingToThem(int myRatingToThem) {
        this.myRatingToThem = myRatingToThem;
    }

    public String getMyFeedbackToThem() {
        return myFeedbackToThem;
    }

    public void setMyFeedbackToThem(String myFeedbackToThem) {
        this.myFeedbackToThem = myFeedbackToThem;
    }

    public int getTheirRatingToMe() {
        return theirRatingToMe;
    }

    public void setTheirRatingToMe(int theirRatingToMe) {
        this.theirRatingToMe = theirRatingToMe;
    }

    public String getTheirFeedbackToMe() {
        return theirFeedbackToMe;
    }

    public void setTheirFeedbackToMe(String theirFeedbackToMe) {
        this.theirFeedbackToMe = theirFeedbackToMe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityDTO that = (ActivityDTO) o;
        return Double.compare(that.rating, rating) == 0 && myRatingToThem == that.myRatingToThem && theirRatingToMe == that.theirRatingToMe && Objects.equals(creator, that.creator) && Objects.equals(volunteer, that.volunteer) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && statusClient == that.statusClient && statusVolunteer == that.statusVolunteer && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(role, that.role) && Objects.equals(username, that.username) && Objects.equals(myFeedbackToThem, that.myFeedbackToThem) && Objects.equals(theirFeedbackToMe, that.theirFeedbackToMe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creator, volunteer, title, description, statusClient, statusVolunteer, startDate, endDate, role, username, rating, myRatingToThem, myFeedbackToThem, theirRatingToMe, theirFeedbackToMe);
    }

    @Override
    public String toString() {
        return "ActivityDTO{" +
                "creator=" + creator +
                ", volunteer=" + volunteer +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", statusClient=" + statusClient +
                ", statusVolunteer=" + statusVolunteer +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", rating=" + rating +
                ", myRatingToThem=" + myRatingToThem +
                ", myFeedbackToThem='" + myFeedbackToThem + '\'' +
                ", theirRatingToMe=" + theirRatingToMe +
                ", theirFeedbackToMe='" + theirFeedbackToMe + '\'' +
                '}';
    }
}