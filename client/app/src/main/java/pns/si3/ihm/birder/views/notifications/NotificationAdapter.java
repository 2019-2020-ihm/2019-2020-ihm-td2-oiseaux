package pns.si3.ihm.birder.views.notifications;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import etudes.fr.demoosm.R;

public class NotificationAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<NotificationItem> notifications;
    private Button buttonSuppression;
    private NotificationActivity notificationActivity;

    public NotificationAdapter(Context context, ArrayList<NotificationItem> notifications, NotificationActivity activity) {
        this.context = context;
        this.notifications = notifications;
        mInflater = LayoutInflater.from(context);
        notificationActivity = new NotificationActivity();
        this.notificationActivity = activity;

    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = mInflater.inflate(R.layout.list_item_notification, null);

        ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        TextView birdName = (TextView) view.findViewById(R.id.bird_name);

        NotificationItem currentNotification = (NotificationItem) getItem(position);

        iconView.setImageResource(currentNotification.getImageResourceId());
        birdName.setText(currentNotification.getNotificationNameBird());

        buttonSuppression = (Button) view.findViewById(R.id.button_supprimer_notif);
        buttonSuppression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO io
                //Récupérer id de l'oiseau et l'enlever de la liste associé à l'user
                //mettre à jour la liste affiché
            }
        });

        return view;
    }



}