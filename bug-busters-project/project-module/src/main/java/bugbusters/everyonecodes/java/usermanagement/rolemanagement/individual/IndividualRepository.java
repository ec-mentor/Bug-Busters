package bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual;

import java.util.Optional;

public interface IndividualRepository {

    Optional<Individual> findOneByUser_username(String username);

}
