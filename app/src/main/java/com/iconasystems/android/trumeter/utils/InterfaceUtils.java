package com.iconasystems.android.trumeter.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.iconasystems.android.trumeter.R;

/**
 * Created by MUSHABE on 6/21/2016.
 */
public class InterfaceUtils {
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(Context context, final boolean show, final View view, final ProgressBar progressBar) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

            view.setVisibility(show ? View.GONE : View.VISIBLE);
            view.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            view.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static void resetUI(View itemView){
        ImageView redIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_red);
        ImageView darkBlueIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_dark_blue);
        ImageView lightBlueIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_light_blue);
        ImageView blackIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_black);
        ImageView pinkIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_pink);
        ImageView defaultIndicator = (ImageView) itemView.findViewById(R.id.reading_indicator_default);

        redIndicator.setVisibility(View.GONE);
        darkBlueIndicator.setVisibility(View.GONE);
        lightBlueIndicator.setVisibility(View.GONE);
        blackIndicator.setVisibility(View.GONE);
        pinkIndicator.setVisibility(View.GONE);

        if (defaultIndicator.getVisibility() == View.GONE){
            defaultIndicator.setVisibility(View.VISIBLE);
        }
    }

}
