package com.hishd.qurantime.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.hishd.qurantime.R;

public class UIUtil {

    public Dialog getProgress(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.popup_progressbar);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(activity.getResources().getColor(R.color.blue_light), PorterDuff.Mode.MULTIPLY);
        dialog.setCancelable(false);
        return dialog;
    }

    public Dialog getProgress(Activity activity, String message) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.popup_progressbar);
        if(dialog.findViewById(R.id.txtMessage)!=null)
            ((TextView)dialog.findViewById(R.id.txtMessage)).setText(message);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(activity.getResources().getColor(R.color.blue_light), PorterDuff.Mode.MULTIPLY);
        dialog.setCancelable(false);
        return dialog;
    }


    public Toast CustomToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.blue_light), PorterDuff.Mode.SRC_IN);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(ContextCompat.getColor(context, R.color.white));
        return toast;
    }

}
