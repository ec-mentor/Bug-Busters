package bugbusters.everyonecodes.java.profile.userpublic;

import bugbusters.everyonecodes.java.registration.data.User;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;

@Service
public class UserPublicDTOService {

    private final LocalDateNowProvider provider;

    public UserPublicDTOService(LocalDateNowProvider provider) {
        this.provider = provider;
    }


    public UserPublicDTO transformUserToUserPublicDTO(User user) {
        LocalDate birthday = user.getBirthday();
        String age = null;
        if (birthday != null) {
            age = String.valueOf((calculateAge(birthday, provider.getDateNow())));
        }
        return new UserPublicDTO(user.getUsername(), user.getFullName(), age, user.getDescription());
    }


    int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        return Period.between(birthDate, currentDate).getYears();
    }

}
