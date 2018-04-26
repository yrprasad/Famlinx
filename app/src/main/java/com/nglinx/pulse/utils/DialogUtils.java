package com.nglinx.pulse.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yelisetr on 1/26/2017.
 */

public class DialogUtils {

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date());
    }

    public static void diaplayErrorDialog(Context context, final String errorMsg) {
        diaplayDialog(context, "Error", errorMsg);
    }

    public static void diaplayFailureDialog(Context context, final String errorMsg) {
        diaplayDialog(context, "Failure", errorMsg);
    }

    public static void diaplaySuccessDialog(Context context, final String errorMsg) {
        diaplayDialog(context, "Success", errorMsg);
    }

    public static void diaplayInfoDialog(Context context, final String errorMsg) {
        diaplayDialog(context, "Info", errorMsg);
    }

    public static void diaplayDialog(Context context, final String title, final String errorMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle(title);
        builder.setCancelable(false);
        builder.setMessage(errorMsg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public static void diaplayDialogAndStartIntentOnOk(final Context context, final String title, final String message, final Class clazz) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent = new Intent(context, clazz);
                        context.startActivity(intent);
                        Log.e("info", "OK");
                    }
                });
        builder.show();
    }
}
