package bugbusters.everyonecodes.java.usermanagement.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPublicDTO {

    private String username;
    private String fullName;
    private Integer age;
    private String description;
    private Double rating;

    public UserPublicDTO() {}

    public UserPublicDTO(String username, String fullName, Integer age, String description, List<Integer> ratings) {
        this.username = username;
        this.fullName = fullName;
        this.age = age;
        this.description = description;
        this.rating = calculateRating(ratings);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRating() {
        return rating;
    }

    void setRating(Double rating) {
        this.rating = rating;
    }

    private Double calculateRating(List<Integer> ratings) {
        if (ratings.size() == 0) return null;
        return ratings.stream()
                .mapToDouble(Double::valueOf)
                .sum() / ratings.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPublicDTO that = (UserPublicDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(fullName, that.fullName) && Objects.equals(age, that.age) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, fullName, age, description);
    }

    @Override
    public String toString() {
        return "UserPublicDTO{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", age='" + age + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
