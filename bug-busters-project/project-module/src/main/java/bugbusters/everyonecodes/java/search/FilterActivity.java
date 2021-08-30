package bugbusters.everyonecodes.java.search;

import java.time.LocalDateTime;
import java.util.Objects;

public class FilterActivity {
    private LocalDateTime date;
    private String category;
    private String skills;
    private String creator;
    private Integer rating;

    public FilterActivity(){}

    public FilterActivity(LocalDateTime date, String category, String skills, String creator, Integer rating) {
        this.date = date;
        this.category = category;
        this.skills = skills;
        this.creator = creator;
        this.rating = rating;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterActivity that = (FilterActivity) o;
        return Objects.equals(date, that.date) && Objects.equals(category, that.category) && Objects.equals(skills, that.skills) && Objects.equals(creator, that.creator) && Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, category, skills, creator, rating);
    }
}
