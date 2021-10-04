package com.hishd.lightpopup;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hishd.lightpopup.databinding.ChangePasswordBinding;
import com.hishd.lightpopup.databinding.DualActionDialogBinding;
import com.hishd.lightpopup.databinding.SingleActionDialogBinding;
import com.hishd.lightpopup.databinding.UploadCvBinding;

public class LightPopup extends Dialog {

    RelativeLayout rootLayout;
    TextView txtTitle;
    TextView txtMessage;
    Button btn1;
    Button btn2;
    Context context;

    //Change Password
    EditText txtNewPassword;
    EditText txtConfPassword;

    //Upload CV
    EditText txtFileName;

    private ChangePasswordBinding changePasswordBinding;
    private UploadCvBinding uploadCvBinding;
    private SingleActionDialogBinding singleActionDialogBinding;
    private DualActionDialogBinding dualActionDialogBinding;

    public LightPopup(@NonNull Context context) {
        super(context);
        this.context = context;
        this.setCanceledOnTouchOutside(false);
        changePasswordBinding = ChangePasswordBinding.inflate(getLayoutInflater());
        uploadCvBinding = UploadCvBinding.inflate(getLayoutInflater());
        singleActionDialogBinding = SingleActionDialogBinding.inflate(getLayoutInflater());
        dualActionDialogBinding = DualActionDialogBinding.inflate(getLayoutInflater());
    }

    public LightPopup setCancelledOnOutside(boolean cancellable) {
        this.setCanceledOnTouchOutside(cancellable);
        return this;
    }

    public LightPopup createChangePasswordDialog() {
        this.rootLayout = changePasswordBinding.getRoot();
        setContentView(rootLayout);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.txtTitle = changePasswordBinding.txtTitle;
        this.btn1 = changePasswordBinding.btn1;
        this.txtNewPassword = changePasswordBinding.txtNewPassword;
        this.txtConfPassword = changePasswordBinding.txtConfPassword;
        return this;
    }

    public LightPopup createCVUploadDialog() {
        this.rootLayout = uploadCvBinding.getRoot();
        setContentView(rootLayout);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.btn1 = uploadCvBinding.btn1;
        this.btn2 = uploadCvBinding.btn2;
        this.txtFileName = uploadCvBinding.txtFileName;
        btn2.setOnClickListener(v -> {
            dismiss();
        });
        return this;
    }

    public LightPopup createSingleActionDialog() {
        this.rootLayout = singleActionDialogBinding.getRoot();
        setContentView(rootLayout);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.txtTitle = singleActionDialogBinding.txtTitle;
        this.txtMessage = singleActionDialogBinding.txtMessage;
        this.btn1 = singleActionDialogBinding.btn1;
        return this;
    }

    public LightPopup createDualActionDialog() {
        this.rootLayout = dualActionDialogBinding.getRoot();
        setContentView(rootLayout);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.txtTitle = dualActionDialogBinding.txtTitle;
        this.txtMessage = dualActionDialogBinding.txtMessage;
        this.btn1 = dualActionDialogBinding.btn1;
        this.btn2 = dualActionDialogBinding.btn2;
        return this;
    }

    public LightPopup setTitle(String title) {
        if (this.txtTitle == null)
            return this;
        txtTitle.setText(title);
        return this;
    }

    public LightPopup setMessage(String message) {
        if (this.txtMessage == null)
            return this;
        txtMessage.setText(message);
        return this;
    }

    public LightPopup setBackgroundColor(int color) {
        Drawable background = rootLayout.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(context.getColor(color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(context.getColor(color));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(context.getColor(color));
        }
        return this;
    }

    public LightPopup setTitleColor(int color) {
        if (txtTitle != null)
            txtTitle.setTextColor(context.getColor(color));
        return this;
    }

    public LightPopup setMessageColor(int color) {
        if (txtMessage != null)
            txtMessage.setTextColor(context.getColor(color));
        return this;
    }

    public LightPopup setBtn1Caption(String text) {
        if (btn1 != null)
            btn1.setText(text);

        return this;
    }

    public LightPopup setBtn2Caption(String text) {
        if (btn2 != null)
            btn2.setText(text);

        return this;
    }

    public LightPopup setBtn1CaptionColor(int textColor) {
        if (btn1 != null)
            btn1.setTextColor(context.getColor(textColor));

        return this;
    }

    public LightPopup setBtn2CaptionColor(int textColor) {
        if (btn2 != null)
            btn2.setTextColor(context.getColor(textColor));

        return this;
    }

    public LightPopup setBtn1AllCaps(boolean allCaps) {
        if (btn1 != null)
            btn1.setAllCaps(allCaps);

        return this;
    }

    public LightPopup setBtn2AllCaps(boolean allCaps) {
        if (btn2 != null)
            btn2.setAllCaps(allCaps);

        return this;
    }

    public LightPopup setBtn1Color(int color) {
        if (btn1 == null)
            return this;

        Drawable background = btn1.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(context.getColor(color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(context.getColor(color));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(context.getColor(color));
        }
        return this;
    }

    public LightPopup setBtn2Color(int color) {
        if (btn2 == null)
            return this;

        Drawable background = btn2.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(context.getColor(color));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(context.getColor(color));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(context.getColor(color));
        }
        return this;
    }

    public LightPopup setBtn1Action(LightPopupDialogListener listener) {
        if (btn1 == null)
            return this;
        btn1.setOnClickListener(v -> {
            listener.onClicked(LightPopup.this);
        });

        return this;
    }

    public LightPopup setCVDialogBtn1Action(LightCVPopupDialogListener listener) {
        if (btn1 == null)
            return this;

        btn1.setOnClickListener(v -> {
            this.txtFileName.setError(null);
            if(txtFileName.getText().toString().isEmpty()) {
                this.txtFileName.setError("Enter a valid Name for the CV");
                return;
            }

            listener.onSelectFileClicked(txtFileName.getText().toString(), LightPopup.this);
            this.txtFileName.setText(null);
            this.txtFileName.setError(null);
            this.txtFileName.clearFocus();
        });

        return this;
    }

    public LightPopup setPwdDialogBtn1Action(LightPwdPopupDialogListener listener, String passwordPattern, String invalidPasswordText) {
        if (btn1 == null)
            return this;
        btn1.setOnClickListener(v -> {
            if (this.txtNewPassword == null || this.txtConfPassword == null) {
                Toast.makeText(this.context, "Password popup requires correct setup!", Toast.LENGTH_LONG).show();
                listener.onChangePasswordClicked(null,LightPopup.this);
                return;
            }

            this.txtNewPassword.setError(null);
            this.txtConfPassword.setError(null);

            if (passwordPattern != null) {
                if (!this.txtNewPassword.getText().toString().matches(passwordPattern)) {
                    this.txtNewPassword.setError(invalidPasswordText);
                    return;
                }
            }

            if (!this.txtNewPassword.getText().toString().equals(this.txtConfPassword.getText().toString())) {
                this.txtNewPassword.setError("Passwords does not match");
                this.txtConfPassword.setError("Passwords does not match");
                return;
            }

            listener.onChangePasswordClicked(this.txtNewPassword.getText().toString(),LightPopup.this);
            this.txtNewPassword.setText(null);
            this.txtNewPassword.clearFocus();
            this.txtNewPassword.setError(null);
            this.txtConfPassword.setText(null);
            this.txtConfPassword.clearFocus();
            this.txtConfPassword.setError(null);
        });

        return this;
    }


    public LightPopup setBtn2Action(LightPopupDialogListener listener) {
        if (btn2 == null)
            return this;
        btn2.setOnClickListener(v -> {
            listener.onClicked(LightPopup.this);
        });

        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (this.txtNewPassword != null) {
            this.txtNewPassword.clearFocus();
            this.txtNewPassword.setText(null);
            this.txtNewPassword.setError(null);
        }
        if (this.txtConfPassword != null) {
            this.txtConfPassword.clearFocus();
            this.txtConfPassword.setText(null);
            this.txtConfPassword.setError(null);
        }
    }

    public interface LightPopupDialogListener {
        void onClicked(Dialog dialog);
    }

    public interface LightPwdPopupDialogListener {
        void onChangePasswordClicked(String password, Dialog dialog);
    }

    public interface LightCVPopupDialogListener {
        void onSelectFileClicked(String fileName, Dialog dialog);
    }
}
