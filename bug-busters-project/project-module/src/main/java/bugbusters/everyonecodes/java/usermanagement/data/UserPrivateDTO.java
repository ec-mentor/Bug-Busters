package bugbusters.everyonecodes.java.usermanagement.data;

import java.time.LocalDate;
import java.util.Objects;

public class UserPrivateDTO {
    private String username;
    private String role;
    private String fullName;
    private LocalDate birthday;
    private String address;
    private String email;
    private String description;

    public UserPrivateDTO(String username, String role, String fullName, LocalDate birthday, String address, String email, String description) {
        this.username = username;
        this.role = role;
        this.fullName = fullName;
        this.birthday = birthday;
        this.address = address;
        this.email = email;
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        UserPrivateDTO that = (UserPrivateDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(role, that.role) && Objects.equals(fullName, that.fullName) && Objects.equals(birthday, that.birthday) && Objects.equals(address, that.address) && Objects.equals(email, that.email) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, role, fullName, birthday, address, email, description);
    }

    @Override
    public String toString() {
        return "UserPrivateDTO{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthday=" + birthday +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
