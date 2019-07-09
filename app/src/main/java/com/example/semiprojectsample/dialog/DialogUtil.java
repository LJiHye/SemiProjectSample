package com.example.semiprojectsample.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtil {

    private static Dialog dialog;

    public static void showDialog(Context context, String title, String msg,
                                  String okMsg, DialogInterface.OnClickListener okListener,
                                  String cancelMsg, DialogInterface.OnClickListener cancelListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);

        if(okListener != null) {
            builder.setPositiveButton(okMsg, okListener);
        }

        if(cancelListener != null) {
            builder.setNegativeButton(cancelMsg, cancelListener);
        }

        dialog = builder.create();
        dialog.show();
    }

    public static void dismiss() {
        if(dialog != null) dialog.dismiss();
    }
}
