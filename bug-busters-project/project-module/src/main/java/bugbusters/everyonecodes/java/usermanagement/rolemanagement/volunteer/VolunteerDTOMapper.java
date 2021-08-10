package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.usermanagement.service.UserDTOMapper;
import org.springframework.stereotype.Service;

@Service
public class VolunteerDTOMapper {
    private final UserDTOMapper userDTOMapper;
    private final SetToStringMapper setToStringMapper;

    public VolunteerDTOMapper(UserDTOMapper userDTOMapper, SetToStringMapper setToStringMapper) {
        this.userDTOMapper = userDTOMapper;
        this.setToStringMapper = setToStringMapper;
    }

    public VolunteerPrivateDTO toVolunteerPrivateDTO(Volunteer input) {
        var user = userDTOMapper.toUserPrivateDTO(input.getUser());
        var skills = setToStringMapper.convertToString(input.getSkills());
        return new VolunteerPrivateDTO(user, skills);
    }

    public VolunteerPublicDTO toVolunteerPublicDTO(Volunteer input) {
        var user = userDTOMapper.toUserPublicDTO(input.getUser());
        var skills = setToStringMapper.convertToString(input.getSkills());
        return new VolunteerPublicDTO(user, skills);
    }
}
