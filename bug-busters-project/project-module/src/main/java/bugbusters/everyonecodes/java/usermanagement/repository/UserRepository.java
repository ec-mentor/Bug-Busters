package bugbusters.everyonecodes.java.usermanagement.repository;

import bugbusters.everyonecodes.java.usermanagement.data.EmailSchedule;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findOneByUsername(String username);
    Optional<User> findOneByEmail(String username);
    List<User> findByEmailSchedule(EmailSchedule schedule);
}
