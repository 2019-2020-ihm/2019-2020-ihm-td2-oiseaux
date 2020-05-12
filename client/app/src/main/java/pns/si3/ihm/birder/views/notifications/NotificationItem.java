package pns.si3.ihm.birder.views.notifications;

import android.view.View;
import android.widget.Button;

import etudes.fr.demoosm.R;

public class NotificationItem {

    private String notificationNameBird;
    private String birdId;
    private int imageResourceId;

    public NotificationItem(String notificationNameBird, String birdId, int imageResourceId){
        this.notificationNameBird = notificationNameBird;
        this.birdId = birdId;
        this.imageResourceId = imageResourceId;
    }

    public String getBirdId() {
        return birdId;
    }

    public String getNotificationNameBird() {
        return notificationNameBird;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

}
