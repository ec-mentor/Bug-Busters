package bugbusters.everyonecodes.java.activities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findAllByStatusClient(Status status);

    List<Activity> findAllByCreator(String creator);

    List<Activity> findAllByCreatorAndStatusClient(String creator, Status statusClient);

    List<Activity> findAllByStatusVolunteerAndPostedDateGreaterThanEqual(Status statusVolunteer, LocalDateTime localDateTime);
}
