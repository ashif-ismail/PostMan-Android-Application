package me.ashif.postman;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by almukthar on 27/3/16.
 */
public class DataAdapter extends BaseAdapter {

    private Activity thisActivity;
    private LayoutInflater inflater;
    private List<IncomingData> dataList;

    public DataAdapter(Activity thisActivity, List<IncomingData> dataList) {
        this.thisActivity = thisActivity;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) thisActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.single_data_row, null);
        TextView dataContent = (TextView) convertView.findViewById(R.id.txtDataContent);
        TextView senderID = (TextView) convertView.findViewById(R.id.txtSenderContent);
        TextView channel = (TextView) convertView.findViewById(R.id.txtChannelContent);
        TextView status = (TextView) convertView.findViewById(R.id.txtStatusContent);
        IncomingData data = dataList.get(position);
        dataContent.setText(data.getIncomingData());
        senderID.setText(data.getSenderID());
        channel.setText(data.getChannel());
        status.setText(data.getStatus());

        return convertView;
    }
}
