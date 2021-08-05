package bugbusters.everyonecodes.java.usermanagement.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class LocalDateNowProvider {

    //need this class to test LocalDate.now(), so I can mock it

    public LocalDate getDateNow() {
        return LocalDate.now();
    }
}
