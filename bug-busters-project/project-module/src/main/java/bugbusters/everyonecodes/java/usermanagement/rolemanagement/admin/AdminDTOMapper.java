package bugbusters.everyonecodes.java.usermanagement.rolemanagement.admin;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.service.UserDTOMapper;
import org.springframework.stereotype.Service;

@Service
public class AdminDTOMapper {

    //TODO: implement calculating activities for mapper
    //TODO: add activity repository


    private final UserDTOMapper userDTOMapper;

    public AdminDTOMapper(UserDTOMapper userDTOMapper) {
        this.userDTOMapper = userDTOMapper;
    }

    public AdminDTO toAdminDTO(User user) {
        Double rating = userDTOMapper.calculateRating(user.getRatings());
        int pending = 0;
        int inProgress = 0;
        int completed = 0;
        return new AdminDTO(user.getUsername(), rating, pending, inProgress, completed);
    }
}
