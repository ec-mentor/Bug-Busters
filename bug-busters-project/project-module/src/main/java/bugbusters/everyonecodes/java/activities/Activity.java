package bugbusters.everyonecodes.java.activities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Activity {
    @Id
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    @Size(min = 3, max = 40)
    private String Description;

    @NotEmpty
    private String recommendedSkills;

    @NotEmpty
    private List<String> categories;

    @NotEmpty
    private LocalDate startDate;

    @NotEmpty
    private LocalTime startTime;

    @NotEmpty
    private LocalDate endDate;

    @NotEmpty
    private LocalTime endTime;

    @NotEmpty
    private boolean isOpenEnd;

    public Activity(){}

    public Activity(String title, String description, String recommendedSkills, List<String> categories, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, boolean isOpenEnd) {
        this.title = title;
        Description = description;
        this.recommendedSkills = recommendedSkills;
        this.categories = categories;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.isOpenEnd = isOpenEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getRecommendedSkills() {
        return recommendedSkills;
    }

    public void setRecommendedSkills(String recommendedSkills) {
        this.recommendedSkills = recommendedSkills;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean isOpenEnd() {
        return isOpenEnd;
    }

    public void setOpenEnd(boolean openEnd) {
        isOpenEnd = openEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return isOpenEnd == activity.isOpenEnd && Objects.equals(id, activity.id) && Objects.equals(title, activity.title) && Objects.equals(Description, activity.Description) && Objects.equals(recommendedSkills, activity.recommendedSkills) && Objects.equals(categories, activity.categories) && Objects.equals(startDate, activity.startDate) && Objects.equals(startTime, activity.startTime) && Objects.equals(endDate, activity.endDate) && Objects.equals(endTime, activity.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, Description, recommendedSkills, categories, startDate, startTime, endDate, endTime, isOpenEnd);
    }
}
