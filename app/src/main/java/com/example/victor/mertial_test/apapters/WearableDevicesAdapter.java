package com.example.victor.mertial_test.apapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.victor.mertial_test.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Victor on 2015/10/14.
 */
public class WearableDevicesAdapter  extends RecyclerView.Adapter< WearableDevicesAdapter.ViewHolderDevice>{

    private List<BluetoothDevice> listDevices = Collections.emptyList();
    private LayoutInflater layoutInflater;
    private Context context;

    public  WearableDevicesAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
    }

    public void setDeviceList(ArrayList<BluetoothDevice> listDevices){
        this.listDevices = listDevices;
        notifyItemRangeChanged(0, listDevices.size());
    }


    @Override
    public ViewHolderDevice onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_device_item,parent,false);
        ViewHolderDevice viewHolderDevice = new ViewHolderDevice(view);
        return viewHolderDevice;
    }

    @Override
    public void onBindViewHolder(ViewHolderDevice holder, int position) {
        BluetoothDevice currentDevice = listDevices.get(position);
        holder.device_image.setImageResource(R.drawable.bindingwd_icon);
        holder.device_text.setText(currentDevice.getName());
    }

    @Override
    public int getItemCount() {
        return listDevices.size();
    }

    static class ViewHolderDevice extends RecyclerView.ViewHolder{

        private ImageView device_image;
        private TextView device_text;

        public ViewHolderDevice(View itemView) {
            super(itemView);
            device_image = (ImageView)itemView.findViewById(R.id.deivice_image);
            device_text = (TextView)itemView.findViewById(R.id.device_name);
        }
    }

}
