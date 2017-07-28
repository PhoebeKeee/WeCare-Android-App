package com.example.victor.mertial_test.apapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.victor.mertial_test.R;
import com.example.victor.mertial_test.pojo.Monitor;

import java.util.Collections;
import java.util.List;

/**
 * Created by Victor on 2015/10/14.
 */
public class MonitorAdapter extends RecyclerView.Adapter<MonitorAdapter.ViewHolderMonitor>{

    List<Monitor> monitorList = Collections.emptyList();

    public MonitorAdapter(List<Monitor> monitorList) {
        this.monitorList = monitorList;
    }

    @Override
    public ViewHolderMonitor onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bindingmonitorchoose_item, parent, false);
        ViewHolderMonitor mvh = new ViewHolderMonitor(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(ViewHolderMonitor holder, int position) {
       holder.monitor_id.setText(monitorList.get(position).getMonitor_name());
    }

    @Override
    public int getItemCount() {
        return monitorList.size();
    }

    public static class ViewHolderMonitor extends RecyclerView.ViewHolder {

        CardView monitorcv; //cardview
        TextView monitor_id; //血壓計

        ViewHolderMonitor(View itemView) {
            super(itemView);
            monitorcv = (CardView)itemView.findViewById(R.id.monitorcv);
            monitor_id = (TextView)itemView.findViewById(R.id.monitor_id);

        }
    }


}
