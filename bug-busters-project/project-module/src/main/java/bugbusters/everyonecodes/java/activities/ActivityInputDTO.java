package bugbusters.everyonecodes.java.activities;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

public class ActivityInputDTO {
    @NotEmpty
    @Size(min = 3, max = 40)
    private String title;
    @NotEmpty
    private String description;
    private String recommendedSkills;
    private String categories;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;

    private boolean isOpenEnd;

    @NotNull
    private Status statusClient;



    public ActivityInputDTO(String title, String description, String recommendedSkills, String categories, LocalDateTime startTime, LocalDateTime endTime, boolean isOpenEnd, Status statusClient) {
        this.title = title;
        this.description = description;
        this.recommendedSkills = recommendedSkills;
        this.categories = categories;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isOpenEnd = isOpenEnd;
        this.statusClient = statusClient;
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

    public String getRecommendedSkills() {
        return recommendedSkills;
    }

    public void setRecommendedSkills(String recommendedSkills) {
        this.recommendedSkills = recommendedSkills;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
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

    public Status getStatusClient() {
        return statusClient;
    }

    public void setStatusClient(Status statusClient) {
        this.statusClient = statusClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityInputDTO that = (ActivityInputDTO) o;
        return isOpenEnd == that.isOpenEnd && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(recommendedSkills, that.recommendedSkills) && Objects.equals(categories, that.categories) && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, recommendedSkills, categories, startTime, endTime, isOpenEnd);
    }

    @Override
    public String toString() {
        return "ActivityEditDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", recommendedSkills='" + recommendedSkills + '\'' +
                ", categories='" + categories + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", isOpenEnd=" + isOpenEnd +
                '}';
    }
}
