package bugbusters.everyonecodes.java.notification;

public class NotificationDTO {

    private String creator;
    private String message;

    public NotificationDTO() {
    }

    public NotificationDTO(String creator, String message) {
        this.creator = creator;
        this.message = message;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
