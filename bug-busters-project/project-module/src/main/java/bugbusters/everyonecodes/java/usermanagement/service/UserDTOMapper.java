package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class UserDTOMapper {

    private final LocalDateNowProvider provider;

    public UserDTOMapper(LocalDateNowProvider provider) {
        this.provider = provider;
    }

    public UserPrivateDTO toUserPrivateDTO(User user) {
        return new UserPrivateDTO(
                user.getUsername(),
                user.getRole(),
                user.getFullName(),
                user.getBirthday(),
                user.getAddress(),
                user.getEmail(),
                user.getDescription()
        );
    }

    public UserPublicDTO toUserPublicDTO(User user) {
        if (user == null) {
            return null;
        }
        LocalDate birthday = user.getBirthday();
        Integer age = null;
        if (birthday != null) {
            age = calculateAge(birthday, provider.getDateNow());
        }
        return new UserPublicDTO(user.getUsername(), user.getFullName(), age, user.getDescription());
    }

    Integer calculateAge(LocalDate birthDate, LocalDate currentDate) {
        return Period.between(birthDate, currentDate).getYears();
    }

}
