package com.hishd.qurantime.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hishd.qurantime.Model.MeasurementHistoryRecordModel;
import com.hishd.qurantime.R;

import java.util.ArrayList;
import java.util.Locale;

public class RecordHistoryAdapter extends RecyclerView.Adapter<RecordHistoryAdapter.ViewHolder> {

    final Context context;
    final ArrayList<MeasurementHistoryRecordModel> measurementRecords;
    MeasurementHistoryRecordModel measurementHistoryRecordModel;

    public RecordHistoryAdapter(Context context, ArrayList<MeasurementHistoryRecordModel> measurementRecords) {
        this.context = context;
        this.measurementRecords = measurementRecords;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_measurement_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        measurementHistoryRecordModel = measurementRecords.get(position);
        holder.txtTime.setText(measurementHistoryRecordModel.getTime());
        holder.txtSPO2.setText(String.format(Locale.ENGLISH,"%.1f%%", measurementHistoryRecordModel.getSpo2Level()));
        holder.txtBPM.setText(String.format(Locale.ENGLISH, "%.0f", measurementHistoryRecordModel.getBpmLevel()));
        holder.txtResult.setText(measurementHistoryRecordModel.getResult());
    }

    @Override
    public int getItemCount() {
        return measurementRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTime;
        TextView txtSPO2;
        TextView txtBPM;
        TextView txtResult;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtSPO2 = itemView.findViewById(R.id.txtSPO2);
            txtBPM = itemView.findViewById(R.id.txtBPM);
            txtResult = itemView.findViewById(R.id.txtResult);
        }
    }
}
