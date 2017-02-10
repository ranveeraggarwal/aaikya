package com.ranveeraggarwal.letrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.ranveeraggarwal.letrack.R;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        int primaryColor = ContextCompat.getColor(this, R.color.colorPrimary);

        addSlide(AppIntroFragment.newInstance("Hello!", "Welcome to Aaikya!\n Your everyday leave tracker.",
                R.drawable.human_greeting, primaryColor));
        addSlide(AppIntroFragment.newInstance("Getting Started", "Add a new person to keep track of.",
                R.drawable.plus_box, primaryColor));
        addSlide(AppIntroFragment.newInstance("Add A Leave", "For the day from the main screen itself.\n\n Modify leaves for other days by selecting the person.",
                R.drawable.list_view, primaryColor));

        // Override bar/separator color.
        setBarColor(primaryColor);
        setSeparatorColor(ContextCompat.getColor(this, R.color.colorPrimaryLight));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}