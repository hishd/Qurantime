package com.hishd.qurantime.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hishd.qurantime.Model.WIfiScanModel;
import com.hishd.qurantime.R;

import java.util.ArrayList;

public class ConnectDeviceAdapter extends RecyclerView.Adapter<ConnectDeviceAdapter.ViewHolder> {

    final Context context;
    final ArrayList<WIfiScanModel> wIfiScanList;
    final OnScanResultActionListener listener;

    public ConnectDeviceAdapter(Context context, ArrayList<WIfiScanModel> wIfiScanList, OnScanResultActionListener listener) {
        this.context = context;
        this.wIfiScanList = wIfiScanList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtSSID.setText(wIfiScanList.get(position).getSsid());
        if(wIfiScanList.get(position).isPulseOx()) {
            holder.txtDeviceType.setTextColor(ContextCompat.getColor(context, R.color.blue));
            holder.txtDeviceType.setText(R.string.pulse_oximeter);
        } else {
            holder.txtDeviceType.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.txtDeviceType.setText(R.string.access_point);
        }
    }

    @Override
    public int getItemCount() {
        return wIfiScanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSSID;
        TextView txtDeviceType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSSID = itemView.findViewById(R.id.txtSSID);
            txtDeviceType = itemView.findViewById(R.id.txtDeviceType);
            itemView.setOnClickListener(v -> {
                listener.onSelectedResult(getAdapterPosition());
            });
        }
    }

    public interface OnScanResultActionListener {
        void onSelectedResult(int position);
    }
}
