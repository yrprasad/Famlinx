package com.nglinx.pulse.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;


/**
 * Created by yelisetr on 1/26/2017.
 */

public class ProgressbarUtil {

    public static ProgressDialog startProgressBar(final Context context, final String message) {

        final ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        return mProgressDialog;
    }

    public static ProgressDialog startProgressBar(final Context context) {
        return startProgressBar(context, "Loading...");
    }

    public static void stopProgressBar(ProgressDialog mProgressDialog) {
//        if (mProgressDialog.isShowing())
//            mProgressDialog.dismiss();
        dismissProgressDialog(mProgressDialog);
    }


    /**
     * Dismiss {@link ProgressDialog} with check for nullability and SDK version
     *
     * @param dialog instance of {@link ProgressDialog} to dismiss
     */
    public static void dismissProgressDialog(ProgressDialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {

                //get the Context object that was used to great the dialog
                Context context = ((ContextWrapper) dialog.getContext()).getBaseContext();

                // if the Context used here was an activity AND it hasn't been finished or destroyed
                // then dismiss it
                if (context instanceof Activity) {

                    // Api >=17
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                            dismissWithExceptionHandling(dialog);
                        }
                    } else {

                        // Api < 17. Unfortunately cannot check for isDestroyed()
                        if (!((Activity) context).isFinishing()) {
                            dismissWithExceptionHandling(dialog);
                        }
                    }
                } else
                    // if the Context used wasn't an Activity, then dismiss it too
                    dismissWithExceptionHandling(dialog);
            }
            dialog = null;
        }
    }

    /**
     * Dismiss {@link ProgressDialog} with try catch
     *
     * @param dialog instance of {@link ProgressDialog} to dismiss
     */
    public static void dismissWithExceptionHandling(ProgressDialog dialog) {
        try {
            dialog.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            dialog = null;
        }
    }
}
