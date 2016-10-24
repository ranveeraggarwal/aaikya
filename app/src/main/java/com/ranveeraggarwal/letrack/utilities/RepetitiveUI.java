package com.ranveeraggarwal.letrack.utilities;

import android.content.Context;
import android.widget.Toast;


public class RepetitiveUI {
    public static void shortToastMaker(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
