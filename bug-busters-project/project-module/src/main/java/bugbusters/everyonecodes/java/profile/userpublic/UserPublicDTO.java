package bugbusters.everyonecodes.java.profile.userpublic;

import java.util.Objects;

public class UserPublicDTO {

    private String username;
    private String fullName;
    private String age;
    private String description;

    public UserPublicDTO(String username, String fullName, String age, String description) {
        this.username = username;
        this.fullName = fullName;
        this.age = age;
        this.description = description;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
