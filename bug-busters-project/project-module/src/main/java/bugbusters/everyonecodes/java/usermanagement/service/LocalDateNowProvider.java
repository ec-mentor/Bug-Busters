package bugbusters.everyonecodes.java.usermanagement.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class LocalDateNowProvider {

    //need this class to test LocalDate.now(), so I can mock it

    public LocalDate getDateNow() {
        return LocalDate.now();
    }

    public LocalDateTime getLocalDateTimeNow() {return LocalDateTime.now();}
}
