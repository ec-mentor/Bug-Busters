package bugbusters.everyonecodes.java.activities;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Objects;

public class ActivityDTO {

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private Status status;

    @NotEmpty
    private LocalDate startDate;

    @NotEmpty
    private LocalDate endDate;

    @NotEmpty
    private ActivityUserDTO activityUserDTO;

    private int myRatingToThem;
    private String myFeedbackToThem;
    private int theirRatingToMe;
    private String theirFeedbackToMe;

    public ActivityDTO() {

    }

    public ActivityDTO(String title, String description, Status status, LocalDate startDate, LocalDate endDate, ActivityUserDTO activityUserDTO, int myRatingToThem, String myFeedbackToThem, int theirRatingToMe, String theirFeedbackToMe) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.activityUserDTO = activityUserDTO;
        this.myRatingToThem = myRatingToThem;
        this.myFeedbackToThem = myFeedbackToThem;
        this.theirRatingToMe = theirRatingToMe;
        this.theirFeedbackToMe = theirFeedbackToMe;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ActivityUserDTO getActivityUserDTO() {
        return activityUserDTO;
    }

    public void setActivityUserDTO(ActivityUserDTO activityUserDTO) {
        this.activityUserDTO = activityUserDTO;
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
        return myRatingToThem == that.myRatingToThem && theirRatingToMe == that.theirRatingToMe && Objects.equals(title, that.title) && Objects.equals(description, that.description) && status == that.status && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(activityUserDTO, that.activityUserDTO) && Objects.equals(myFeedbackToThem, that.myFeedbackToThem) && Objects.equals(theirFeedbackToMe, that.theirFeedbackToMe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, status, startDate, endDate, activityUserDTO, myRatingToThem, myFeedbackToThem, theirRatingToMe, theirFeedbackToMe);
    }

    @Override
    public String toString() {
        return "ActivityDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", activityUserDTO=" + activityUserDTO +
                ", myRatingToThem=" + myRatingToThem +
                ", myFeedbackToThem='" + myFeedbackToThem + '\'' +
                ", theirRatingToMe=" + theirRatingToMe +
                ", theirFeedbackToMe='" + theirFeedbackToMe + '\'' +
                '}';
    }
}
