package com.ranveeraggarwal.letrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.ranveeraggarwal.letrack.R;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Hello!", "Welcome to Aaikya!\n Your everyday leave tracker.",
                R.drawable.human_greeting, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Getting Started", "Add a new person to keep track of.",
                R.drawable.plus_box, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Add A Leave", "For the day from the main screen itself.\n\n Modify leaves for other days by selecting the person.",
                R.drawable.list_view, getResources().getColor(R.color.colorPrimary)));

        // Override bar/separator color.
        setBarColor(getResources().getColor(R.color.colorPrimary));
        setSeparatorColor(getResources().getColor(R.color.colorPrimaryLight));

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

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}