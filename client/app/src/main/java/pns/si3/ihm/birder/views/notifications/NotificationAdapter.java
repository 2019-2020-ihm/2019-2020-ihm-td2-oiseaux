package pns.si3.ihm.birder.views.notifications;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import etudes.fr.demoosm.R;

public class NotificationAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<NotificationItem> notifications;

    public NotificationAdapter(Context context, ArrayList<NotificationItem> notifications) {
        this.context = context;
        this.notifications = notifications;
        mInflater = LayoutInflater.from(context);

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

        return view;
    }
}