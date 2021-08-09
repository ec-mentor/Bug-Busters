package bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IndividualRepository extends JpaRepository<Individual, Long> {

    Optional<Individual> findOneByUser_username(String username);

}
