package com.vish.apps.dictionary.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vish.apps.dictionary.fragments.LanguageFragment;
import com.vish.apps.dictionary.fragments.TranslateFragment;
import com.vish.apps.dictionary.fragments.VoiceFragment;
import com.vish.apps.dictionary.fragments.WelcomeFragment;

public class WalkthroughPagerAdapter extends FragmentStateAdapter {

    public WalkthroughPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                WelcomeFragment welcomeFragment = new WelcomeFragment();
                return welcomeFragment;
            case 1:
                TranslateFragment translateFragment = new TranslateFragment();
                return translateFragment;
            case 2:
                VoiceFragment voiceFragment = new VoiceFragment();
                return voiceFragment;
            case 3:
                LanguageFragment languageFragment = new LanguageFragment();
                return languageFragment;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }


}