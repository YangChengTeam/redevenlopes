package com.yc.redevenlopes.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yc.redevenlopes.R;




public class LoadDialog extends AlertDialog {
    private Context ctx;
    private TextView textView;
    private String text;

    public LoadDialog(Context context) {
        super(context, R.style.LoadingDialogTheme);
        this.ctx = context;
    }

    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_view);
        this.textView = findViewById(R.id.loading_view_tv);
        this.setCancelable(true);
        this.initView();
    }

    private void initView() {
    }

    public void showLoadingDialog() {
        try {

            if (ctx != null) {
                Activity activity = (Activity) ctx;
                if (!activity.isFinishing()) {
                    dismiss();
                    if (!isShowing()) {
                        show();
                    }
                }
            }
        } catch (Exception var2) {
            Log.d("mylog", "LoadDialog --showLoadingDialog: " + var2);
        }

    }

    public void dismissLoadingDialog() {
        try {
            dismiss();
        } catch (Exception var2) {
            Log.d("mylog", "LoadDialog --dismissDialog: " + var2);
        }

    }

    public void setText(String text) {
        this.text = text;

        if (textView != null) {

            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }

    }
}
