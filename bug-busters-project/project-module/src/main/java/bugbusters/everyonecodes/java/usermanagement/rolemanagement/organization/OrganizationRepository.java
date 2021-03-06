package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findOneByUser_username(String username);

}
