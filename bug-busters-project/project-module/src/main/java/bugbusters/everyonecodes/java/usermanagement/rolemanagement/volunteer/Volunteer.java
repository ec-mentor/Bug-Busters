package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.usermanagement.data.EmailSchedule;
import bugbusters.everyonecodes.java.usermanagement.data.User;

import javax.persistence.*;
import java.util.*;

@Entity
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private Set<String> skills = new HashSet<>();

    @ElementCollection
    private Map<String, EmailSchedule> registeredKeywords = new HashMap<>();

    @OneToOne @MapsId
    private User user;

    public Volunteer() {
    }

    public Volunteer(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = skills;
    }

    public Map<String, EmailSchedule> getRegisteredKeywords() {
        return registeredKeywords;
    }

    public void setRegisteredKeywords(Map<String, EmailSchedule> registeredKeywords) {
        this.registeredKeywords = registeredKeywords;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        return Objects.equals(id, volunteer.id) && Objects.equals(skills, volunteer.skills) && Objects.equals(user, volunteer.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, skills, user);
    }

    @Override
    public String toString() {
        return "Volunteer{" +
                "id=" + id +
                ", skills=" + skills +
                ", user=" + user +
                '}';
    }
}
