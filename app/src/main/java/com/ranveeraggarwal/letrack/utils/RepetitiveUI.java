package com.ranveeraggarwal.letrack.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by raagga on 13-10-2016.
 */

public class RepetitiveUI {
    public static void shortToastMaker(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
