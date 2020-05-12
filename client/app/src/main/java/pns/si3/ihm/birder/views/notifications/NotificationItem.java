package pns.si3.ihm.birder.views.notifications;

public class NotificationItem {

    // Name of bird
    private String notificationNameBird;


    // Drawable resource ID
    private int imageResourceId;


    public NotificationItem(String notificationNameBird, int imageResourceId){
        this.notificationNameBird = notificationNameBird;
        this.imageResourceId = imageResourceId;
    }


    public String getNotificationNameBird() {
        return notificationNameBird;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
