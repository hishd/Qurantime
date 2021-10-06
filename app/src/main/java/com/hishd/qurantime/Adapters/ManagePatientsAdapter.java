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

public class ManagePatientsAdapter extends RecyclerView.Adapter<ManagePatientsAdapter.ViewHolder> {

    final Context context;
    final ArrayList<PatientModel> patientList;
    final OnPatientActionsListener listener;
    PatientModel patientModel;

    public ManagePatientsAdapter(Context context, ArrayList<PatientModel> patientList, OnPatientActionsListener listener) {
        this.context = context;
        this.patientList = patientList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_patients, parent, false);
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
        holder.txtContactNo.setText(patientModel.getContactNo());
    }

    @Override
    public int getItemCount() {
        return this.patientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPatientNameNIC;
        TextView txtStatus  ;
        TextView txtContactNo;
        Button btnDelete;
        RelativeLayout rootLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPatientNameNIC = itemView.findViewById(R.id.txtPatientNameNIC);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtContactNo = itemView.findViewById(R.id.txtContactNo);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            rootLayout = itemView.findViewById(R.id.rootLayout);

            btnDelete.setOnClickListener(v -> {
                listener.onRemovePressed(getAdapterPosition());
            });
        }
    }

    public interface OnPatientActionsListener {
        void onRemovePressed(int position);
    }
}
