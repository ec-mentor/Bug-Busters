package bugbusters.everyonecodes.java.search;

import java.util.Objects;

public class FilterVolunteer {
    private String skills;
    private Integer rating;

    public FilterVolunteer(){}

    public FilterVolunteer(String skills, Integer rating) {
        this.skills = skills;
        this.rating = rating;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
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
        FilterVolunteer that = (FilterVolunteer) o;
        return Objects.equals(skills, that.skills) && Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skills, rating);
    }

    @Override
    public String toString() {
        return "FilterVolunteer{" +
                "skills='" + skills + '\'' +
                ", rating=" + rating +
                '}';
    }
}
