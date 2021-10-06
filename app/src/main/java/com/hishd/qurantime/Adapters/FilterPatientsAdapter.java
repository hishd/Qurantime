package com.hishd.qurantime.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hishd.qurantime.Model.PatientModel;
import com.hishd.qurantime.R;

import java.util.ArrayList;

public class FilterPatientsAdapter extends RecyclerView.Adapter<FilterPatientsAdapter.ViewHolder> {

    final Context context;
    final ArrayList<PatientModel> patientList;
    final OnFilterPatientActionsListener listener;
    PatientModel patientModel;

    public FilterPatientsAdapter(Context context, ArrayList<PatientModel> patientList, OnFilterPatientActionsListener listener) {
        this.context = context;
        this.patientList = patientList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_patient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        patientModel = patientList.get(position);
        if(patientModel.getHealthStatus().equals("Normal Condition")) {
            holder.rootLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.patient_bg_normal));
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.blue));
        }
        else {
            holder.rootLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.patient_bg_critical));
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        holder.txtStatus.setText(patientModel.getHealthStatus());
        holder.txtPatientNameNIC.setText(
                String.format("%s (%s)",
                        patientModel.getFullName().length() > 20 ? patientModel.getFullName().split(" ")[0] : patientModel.getFullName(),
                        patientModel.getNicNo()
                )
        );
        holder.txtTime.setText(patientModel.getLastUpdate());
        holder.txtLatestCondition.setText(patientModel.getLatestCondition());
        if(patientModel.getLatestCondition().equals("Severe")) {
            holder.txtLatestCondition.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else {
            holder.txtLatestCondition.setTextColor(ContextCompat.getColor(context, R.color.blue));
        }
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPatientNameNIC;
        TextView txtStatus;
        TextView txtLatestCondition;
        TextView txtTime;
        Button btnCall;
        RelativeLayout rootLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPatientNameNIC = itemView.findViewById(R.id.txtPatientNameNIC);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtLatestCondition = itemView.findViewById(R.id.txtLatestCondition);
            txtTime = itemView.findViewById(R.id.txtTime);
            btnCall = itemView.findViewById(R.id.btnCall);
            rootLayout = itemView.findViewById(R.id.rootLayout);

            btnCall.setOnClickListener(v -> {
                listener.onCallClicked(getAdapterPosition());
            });
        }
    }

    public interface OnFilterPatientActionsListener {
        void onCallClicked(int position);
    }
}
