package bugbusters.everyonecodes.java.profile.userpublic;

import bugbusters.everyonecodes.java.registration.data.User;
import bugbusters.everyonecodes.java.registration.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
public class UserPublicDTOService {

    private final UserRepository userRepository;
    private final LocalDateNowProvider provider;

    public UserPublicDTOService(UserRepository userRepository, LocalDateNowProvider provider) {
        this.userRepository = userRepository;
        this.provider = provider;
    }


    public UserPublicDTO viewUserPublicDTO(String username){
        Optional<User> result = userRepository.findOneByUsername(username);
        if (result.isEmpty()){
            return null;
        }
        return transformUserToUserPublicDTO(result.get());
    }

    UserPublicDTO transformUserToUserPublicDTO(User user) {
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
