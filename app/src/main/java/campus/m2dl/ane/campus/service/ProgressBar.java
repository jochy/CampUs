package campus.m2dl.ane.campus.service;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;

/**
 * Created by edouard on 23/01/16.
 */
public class ProgressBar {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(Resources r, final View v, final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = r.getInteger(android.R.integer.config_shortAnimTime);

            v.setVisibility(show ? View.VISIBLE : View.GONE);
            v.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    v.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            v.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}

