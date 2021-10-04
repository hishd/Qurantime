package com.hishd.multiselectpopup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hishd.multiselectpopup.databinding.CustomMultiSelectBinding;

import java.util.ArrayList;

public class MultiSelectDialog extends Dialog implements SearchView.OnQueryTextListener, View.OnClickListener {

    public static ArrayList<Integer> selectedIdsForCallback = new ArrayList<>();

    public ArrayList<MultiSelectModel> mainListOfAdapter = new ArrayList<>();
    private MutliSelectAdapter mutliSelectAdapter;
    //Default Values
    private String positiveText = "DONE";
    private String negativeText = "CANCEL";
    private TextView dialogTitle;
    Button dialogSubmit, dialogCancel;
    private ArrayList<Integer> previouslySelectedIdsList = new ArrayList<>();

    private ArrayList<Integer> tempPreviouslySelectedIdsList = new ArrayList<>();
    private ArrayList<MultiSelectModel> tempMainListOfAdapter = new ArrayList<>();

    private SubmitCallbackListener submitCallbackListener;

    private int minSelectionLimit = 1;
    private String minSelectionMessage = null;
    private int maxSelectionLimit = 0;
    private String maxSelectionMessage = null;

    Context context;
    RecyclerView recyclerView;

    public MultiSelectDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.custom_multi_select);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        SearchView searchView =  this.findViewById(R.id.search_view);
        dialogTitle =  this.findViewById(R.id.title);
        dialogSubmit =  this.findViewById(R.id.done);
        dialogCancel =  this.findViewById(R.id.cancel);

        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        dialogSubmit.setOnClickListener(this);
        dialogCancel.setOnClickListener(this);

        mutliSelectAdapter = new MutliSelectAdapter(mainListOfAdapter, getContext());
        recyclerView.setAdapter(mutliSelectAdapter);

        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
    }

    public MultiSelectDialog build() {
        mutliSelectAdapter.refreshData(setCheckedIDS(mainListOfAdapter, previouslySelectedIdsList));
        return this;
    }

    public MultiSelectDialog setTitle(String title) {
        if(this.dialogTitle == null)
            return this;
        this.dialogTitle.setText(title);
        return this;
    }

    public MultiSelectDialog setTitleTypeface(Typeface typeface) {
        if(this.dialogTitle == null)
            return this;
        this.dialogTitle.setTypeface(typeface);
        return this;
    }

    public MultiSelectDialog setPositiveText(String text) {
        if(this.dialogSubmit == null)
            return this;
        this.dialogSubmit.setText(text);
        return this;
    }

    public MultiSelectDialog setNegativeText(String text) {
        if(this.dialogCancel == null)
            return this;
        this.dialogCancel.setText(text);
        return this;
    }

    public MultiSelectDialog preSelectIDsList(ArrayList<Integer> list) {
        this.previouslySelectedIdsList = list;
        this.tempPreviouslySelectedIdsList = new ArrayList<>(previouslySelectedIdsList);
        return this;
    }

    public MultiSelectDialog multiSelectList(ArrayList<MultiSelectModel> list) {
        this.mainListOfAdapter = list;
        this.tempMainListOfAdapter = new ArrayList<>(mainListOfAdapter);
        if(maxSelectionLimit == 0)
            maxSelectionLimit = list.size();
        return this;
    }
	
    public MultiSelectDialog setMaxSelectionLimit(int limit){
        this.maxSelectionLimit = limit;
        return this;
    }
	
	public MultiSelectDialog setMaxSelectionMessage(String message) {
		this.maxSelectionMessage = message;
		return this;
	}
	
    public MultiSelectDialog setMinSelectionLimit(int limit){
        this.minSelectionLimit = limit;
        return this;
    }
	
	public MultiSelectDialog setMinSelectionMessage(String message) {
		this.minSelectionMessage = message;
		return this;
	}

    public MultiSelectDialog onSubmit(@NonNull SubmitCallbackListener callback) {
        this.submitCallbackListener = callback;
        return this;
    }



    private ArrayList<MultiSelectModel> setCheckedIDS(ArrayList<MultiSelectModel> multiselectdata, ArrayList<Integer> listOfIdsSelected) {
        for (int i = 0; i < multiselectdata.size(); i++) {
            multiselectdata.get(i).setSelected(false);
            for (int j = 0; j < listOfIdsSelected.size(); j++) {
                if (listOfIdsSelected.get(j) == (multiselectdata.get(i).getId())) {
                    multiselectdata.get(i).setSelected(true);
                }
            }
        }
        return multiselectdata;
    }

    private ArrayList<MultiSelectModel> filter(ArrayList<MultiSelectModel> models, String query) {
        query = query.toLowerCase();
        final ArrayList<MultiSelectModel> filteredModelList = new ArrayList<>();
        if (query.equals("") | query.isEmpty()) {
            filteredModelList.addAll(models);
            return filteredModelList;
        }

        for (MultiSelectModel model : models) {
            final String name = model.getName().toLowerCase();
            if (name.contains(query)) {
                filteredModelList.add(model);
            }
        }


        return filteredModelList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        selectedIdsForCallback = previouslySelectedIdsList;
        mainListOfAdapter = setCheckedIDS(mainListOfAdapter, selectedIdsForCallback);
        ArrayList<MultiSelectModel> filteredlist = filter(mainListOfAdapter, newText);
        mutliSelectAdapter.setData(filteredlist, newText.toLowerCase(), mutliSelectAdapter);
        return false;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.done) {
            ArrayList<Integer> callBackListOfIds = selectedIdsForCallback;

            if (callBackListOfIds.size() >= minSelectionLimit) {
                if (callBackListOfIds.size() <= maxSelectionLimit) {

                    //to remember last selected ids which were successfully done
                    tempPreviouslySelectedIdsList = new ArrayList<>(callBackListOfIds);

                    if(submitCallbackListener !=null) {
                        submitCallbackListener.onSelected(callBackListOfIds, getSelectNameList(), getSelectedDataString());
                    }
                    dismiss();
                } else {
                    String youCan = context.getString(R.string.you_can_only_select_upto);
                    String options = context.getString(R.string.options);
                    String option = context.getString(R.string.option);
                    String message = "";

                    if(this.maxSelectionMessage != null) {
                        message = maxSelectionMessage;
                    }
                    else {
                        if (maxSelectionLimit > 1)
                            message = youCan + " " + maxSelectionLimit + " " + options;
                        else
                            message = youCan + " " + maxSelectionLimit + " " + option;
                    }
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            } else {
                String pleaseSelect = context.getString(R.string.please_select_atleast);
                String options = context.getString(R.string.options);
                String option = context.getString(R.string.option);
                String message = "";

                if(this.minSelectionMessage != null) {
                    message = minSelectionMessage;
                }
                else {
                    if (minSelectionLimit > 1)
                        message = pleaseSelect + " " + minSelectionLimit + " " + options;
                    else
                        message = pleaseSelect + " " + minSelectionLimit + " " + option;
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }

        if (view.getId() == R.id.cancel) {
            if(submitCallbackListener!=null){
                selectedIdsForCallback.clear();
                selectedIdsForCallback.addAll(tempPreviouslySelectedIdsList);
                submitCallbackListener.onCancel();
            }
            dismiss();
        }
    }

    private String getSelectedDataString() {
        String data = "";
        for (int i = 0; i < tempMainListOfAdapter.size(); i++) {
            if (checkForSelection(tempMainListOfAdapter.get(i).getId())) {
                data = data + ", " + tempMainListOfAdapter.get(i).getName();
            }
        }
        if (data.length() > 0) {
            return data.substring(1);
        } else {
            return "";
        }
    }

    private ArrayList<String> getSelectNameList() {
        ArrayList<String> names = new ArrayList<>();
        for(int i=0;i<tempMainListOfAdapter.size();i++){
            if(checkForSelection(tempMainListOfAdapter.get(i).getId())){
                names.add(tempMainListOfAdapter.get(i).getName());
            }
        }
        return names;
    }

    private boolean checkForSelection(Integer id) {
        for (int i = 0; i < MultiSelectDialog.selectedIdsForCallback.size(); i++) {
            if (id.equals(MultiSelectDialog.selectedIdsForCallback.get(i))) {
                return true;
            }
        }
        return false;
    }

    public interface SubmitCallbackListener {
        void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String commonSeperatedData);
        void onCancel();
    }

}
