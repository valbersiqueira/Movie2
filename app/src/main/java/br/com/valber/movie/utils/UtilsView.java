package br.com.valber.movie.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class UtilsView {

    public static int getNumberColumns(DisplayMetrics metrics, Activity activity) {
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int wintDriver = 400;
        int width = metrics.widthPixels;
        int nColuns = (width / wintDriver);
        if (nColuns < 2) return 2;
        return nColuns;
    }
}
