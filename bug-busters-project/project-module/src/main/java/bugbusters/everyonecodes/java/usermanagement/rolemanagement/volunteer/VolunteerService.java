package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.activities.Status;
import bugbusters.everyonecodes.java.activities.*;
import bugbusters.everyonecodes.java.search.ActivityTextSearchService;
import bugbusters.everyonecodes.java.search.FilterActivity;
import bugbusters.everyonecodes.java.search.FilterActivityService;
import bugbusters.everyonecodes.java.usermanagement.data.EmailSchedule;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.Client;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.Individual;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.IndividualRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientDTOMapper;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.Organization;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.OrganizationRepository;
import bugbusters.everyonecodes.java.usermanagement.service.EmailService;
import bugbusters.everyonecodes.java.usermanagement.service.LocalDateNowProvider;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VolunteerService {
    private final UserService userService;
    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;
    private final IndividualRepository individualRepository;
    private final ClientDTOMapper clientMapper;
    private final VolunteerDTOMapper volunteerMapper;
    private final SetToStringMapper setToStringMapper;
    private final ActivityDTOMapper activityDTOMapper;
    private final ActivityTextSearchService activityTextSearchService;
    private final ActivityRepository activityRepository;
    private final FilterActivityService filterActivityService;
    private final LocalDateNowProvider localDateNowProvider;
    private final EmailService emailService;

    public VolunteerService(UserService userService, VolunteerRepository volunteerRepository, OrganizationRepository organizationRepository, IndividualRepository individualRepository, ClientDTOMapper clientMapper, VolunteerDTOMapper volunteerMapper, SetToStringMapper setToStringMapper, ActivityDTOMapper activityDTOMapper, ActivityTextSearchService activityTextSearchService, ActivityRepository activityRepository, FilterActivityService filterActivityService, LocalDateNowProvider localDateNowProvider, EmailService emailService) {
        this.userService = userService;
        this.volunteerRepository = volunteerRepository;
        this.organizationRepository = organizationRepository;
        this.individualRepository = individualRepository;
        this.clientMapper = clientMapper;
        this.volunteerMapper = volunteerMapper;
        this.setToStringMapper = setToStringMapper;
        this.activityDTOMapper = activityDTOMapper;
        this.activityTextSearchService = activityTextSearchService;
        this.activityRepository = activityRepository;
        this.filterActivityService = filterActivityService;
        this.localDateNowProvider = localDateNowProvider;
        this.emailService = emailService;
    }

    public Optional<VolunteerPrivateDTO> editVolunteerData(VolunteerPrivateDTO edits, String username) {
        var oVolunteer = getVolunteerByUsername(username);
        if (oVolunteer.isEmpty()) return Optional.empty();

        var oUser = userService.editUserData(edits.getUser(), username);

        oVolunteer = getVolunteerByUsername(username);
        if (oVolunteer.isEmpty()) return Optional.empty();

        var volunteer = oVolunteer.get();
        volunteer.setSkills(setToStringMapper.convertToSet(edits.getSkills()));
        volunteer = volunteerRepository.save(volunteer);
        return Optional.of(volunteerMapper.toVolunteerPrivateDTO(volunteer));
    }

    public Optional<Volunteer> getVolunteerByUsername(String username) {
        return volunteerRepository.findOneByUser_username(username);
    }

    public Optional<VolunteerPrivateDTO> viewVolunteerPrivateData(String username) {
        return getVolunteerByUsername(username).map(volunteer -> volunteerMapper.toVolunteerPrivateDTO(volunteer));
    }

    public Optional<VolunteerPublicDTO> viewVolunteerPublicData(String username) {
        return getVolunteerByUsername(username).map(volunteer -> volunteerMapper.toVolunteerPublicDTO(volunteer));
    }

    private Optional<Client> getClientByUsername(String username) {
        Optional<Organization> oOrganization = organizationRepository.findOneByUser_username(username);
        if (oOrganization.isPresent()) {
            return oOrganization.map(Client.class::cast);
        }
        Optional<Individual> oIndividual = individualRepository.findOneByUser_username(username);
        return oIndividual.map(Client.class::cast);
    }

    public Optional<ClientPublicDTO> viewClientPublicData(String name) {
        return getClientByUsername(name).map(client -> clientMapper.toClientPublicDTO(client));
    }

    public List<ActivityDTO> listAllPendingActivities() {
        return activityRepository.findAllByStatusClient(Status.PENDING).stream()
                .map(activityDTOMapper::toVolunteerActivityDTO)
                .collect(Collectors.toList());
    }

    public List<ActivityDTO> searchPendingActivitiesByText(String text) {
        List<Activity> filteredList = activityTextSearchService.searchActivitiesByText(activityRepository.findAllByStatusClient(Status.PENDING), text);
        return filteredList.stream()
                .map(activityDTOMapper::toVolunteerActivityDTO)
                .collect(Collectors.toList());
    }

    public List<ActivityDTO> searchPendingActivitiesByTextFiltered(String text, FilterActivity filterActivity) {
        List<Activity> filteredList = activityTextSearchService.searchActivitiesByText(activityRepository.findAllByStatusClient(Status.PENDING), text);
        filteredList = filterActivityService.filterSearchResults(filteredList, filterActivity);
        return filteredList.stream()
                .map(activityDTOMapper::toVolunteerActivityDTO)
                .collect(Collectors.toList());
    }

    public List<ActivityDTO> listAllActivitiesOfVolunteer(String username) {
        var oResult = volunteerRepository.findOneByUser_username(username);
        if(oResult.isEmpty()) return List.of();
        return oResult.get().getUser().getActivities().stream()
                .map(activityDTOMapper::toVolunteerActivityDTO)
                .collect(Collectors.toList());
    }

    public void registerNewKeyword(String keyword, EmailSchedule schedule, String username) {
        if (EmailSchedule.DAILY.equals(schedule) || EmailSchedule.WEEKLY.equals(schedule) || EmailSchedule.MONTHLY.equals(schedule)) {
            var oVolunteer = volunteerRepository.findOneByUser_username(username);
            if (oVolunteer.isPresent()) {
                Volunteer volunteer = oVolunteer.get();
                volunteer.getRegisteredKeywords().put(keyword, schedule);
                volunteerRepository.save(volunteer);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    void sendDailyEmail() {
        List<Activity> activitiesOfLastDay = activityRepository.findAllByStatusVolunteerAndPostedDateGreaterThanEqual(Status.PENDING, localDateNowProvider.getLocalDateTimeNow().minusDays(1));
        sendEmailsForMatchingKeywords(activitiesOfLastDay, EmailSchedule.DAILY);
    }

    @Scheduled(cron = "0 0 0 * * 1")
    void sendWeeklyEmail() {
        List<Activity> activitiesOfLastWeek = activityRepository.findAllByStatusVolunteerAndPostedDateGreaterThanEqual(Status.PENDING, localDateNowProvider.getLocalDateTimeNow().minusWeeks(1));
        sendEmailsForMatchingKeywords(activitiesOfLastWeek, EmailSchedule.WEEKLY);
    }

    @Scheduled(cron = "0 0 0 1 * *")
    void sendMonthlyEmail() {
        List<Activity> activitiesOfLastMonth = activityRepository.findAllByStatusVolunteerAndPostedDateGreaterThanEqual(Status.PENDING, localDateNowProvider.getLocalDateTimeNow().minusMonths(1));
        sendEmailsForMatchingKeywords(activitiesOfLastMonth, EmailSchedule.MONTHLY);
    }

    private void sendEmailsForMatchingKeywords(List<Activity> activities, EmailSchedule emailSchedule) {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        volunteers.forEach(volunteer -> volunteer.getRegisteredKeywords().forEach((key, value) -> {
            if (emailSchedule.equals(value)) {
                var matchingActivities = activityTextSearchService.searchActivitiesByText(activities, key);
                var matchingActivitiesAsString = matchingActivities.stream()
                        .map(this::toEmailString).collect(Collectors.joining("\n"));
                String message = "The following Activities have been posted since the last notification: \n\n"
                        + matchingActivitiesAsString;
                emailService.sendListOfActivityMailForKeyword(volunteer.getUser().getEmail(), key,  message);
            }
        }));
    }

    private String toEmailString(Activity activity) {
        return "Title: \"" + activity.getTitle() + "\"\n" +
                "Creator: \"" + activity.getCreator() + "\"\n" +
                "Description: \"" + activity.getDescription() + "\"\n" +
                "Start Time: \"" + activity.getStartTime() + "\"\n" +
                "End Time: \"" + activity.getEndTime() + "\"\n";
    }

    public Map<String, EmailSchedule> viewKeywordRegistrations(String username) {
        var oVolunteer = volunteerRepository.findOneByUser_username(username);
        if (oVolunteer.isPresent()) {
            return oVolunteer.get().getRegisteredKeywords();
        }
        return Map.of();
    }

    public void deleteKeywordRegistration(String keyword, String username) {
        var oVolunteer = volunteerRepository.findOneByUser_username(username);
        if (oVolunteer.isPresent()) {
            Volunteer volunteer = oVolunteer.get();
            volunteer.getRegisteredKeywords().remove(keyword);
            volunteerRepository.save(volunteer);
        }
    }
}
