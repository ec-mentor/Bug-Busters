package bugbusters.everyonecodes.java.usermanagement.rolemanagement.admin;

import bugbusters.everyonecodes.java.activities.Activity;
import bugbusters.everyonecodes.java.activities.Status;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AdminDTOMapperTest {

    @Autowired
    AdminDTOMapper adminDTOMapper;

    @Test
    void toAdminDTO_oneActivity() {
        User user = new User(
                "username",
                "password",
                "role",
                "fullname",
                LocalDate.now(),
                "address",
                "email",
                "description");
        user.setRatings(List.of(2));
        Activity activity = new Activity(
                "test", "test", "test", "test",
                null, null, LocalDateTime.now(), LocalDateTime.now(),
                false, Status.PENDING, Status.PENDING,
                null, null, "test", "test");
        user.setActivities(List.of(activity));
        AdminDTO result = adminDTOMapper.toAdminDTO(user);
        AdminDTO expected = new AdminDTO("username", 2.0, 1, 0, 0);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void toAdminDTO_moreActivities() {
        User user = new User(
                "username",
                "password",
                "role",
                "fullname",
                LocalDate.now(),
                "address",
                "email",
                "description");
        user.setRatings(List.of(2, 3));
        Activity a = new Activity(
                "test", "test", "test", "test",
                null, null, LocalDateTime.now(), LocalDateTime.now(),
                false, Status.PENDING, Status.PENDING,
                null, null, "test", "test");
        Activity b = new Activity(
                "test", "test", "test", "test",
                null, null, LocalDateTime.now(), LocalDateTime.now(),
                false, Status.PENDING, Status.IN_PROGRESS,
                null, null, "test", "test");
        Activity c = new Activity(
                "test", "test", "test", "test",
                null, null, LocalDateTime.now(), LocalDateTime.now(),
                false, Status.PENDING, Status.COMPLETED,
                null, null, "test", "test");
        user.setActivities(List.of(a, b, c));
        AdminDTO result = adminDTOMapper.toAdminDTO(user);
        AdminDTO expected = new AdminDTO("username", 2.5, 1, 1, 1);
        Assertions.assertEquals(expected, result);
    }

}