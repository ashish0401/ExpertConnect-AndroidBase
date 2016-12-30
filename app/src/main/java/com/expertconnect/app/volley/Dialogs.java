package com.expertconnect.app.volley;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.expertconnect.app.R;


/**
 * @author bhushan
 * class for Showing the Global Dialogs
 */
public class Dialogs {

    /**
     * Display Common Alert Dialog
     *
     * @param context context
     * @param title title
     * @param message message
     */
    public static void showAlert(Context context, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    public static Dialog showCircleLoading(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.iphone_progressdailog, null);
        Dialog addCommentDialog = new Dialog(context);
        addCommentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addCommentDialog.setContentView(R.layout.iphone_progressdailog);
        addCommentDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,

                ViewGroup.LayoutParams.MATCH_PARENT);
        addCommentDialog.setCancelable(true);
        addCommentDialog.setCanceledOnTouchOutside(true);
        addCommentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        // initialiseViewsInDialog(addCommentDialog);
        v.findViewById(R.id.ldProgressBar1);
//        addCommentDialog.show();
        return addCommentDialog;
    }


}
