package com.vish.apps.dictionary.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vish.apps.dictionary.fragments.AllSetFragment;
import com.vish.apps.dictionary.fragments.CameraPageFragment;
import com.vish.apps.dictionary.fragments.DefPageFragment;
import com.vish.apps.dictionary.fragments.MainActivityFragment;
import com.vish.apps.dictionary.fragments.QueryFragment;
import com.vish.apps.dictionary.fragments.SetFragment;
import com.vish.apps.dictionary.fragments.TextSpeechFragment;
import com.vish.apps.dictionary.fragments.TransPageFragment;
import com.vish.apps.dictionary.fragments.VoiceRecFragment;
import com.vish.apps.dictionary.fragments.UserGuideFragment;
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
                UserGuideFragment userGuideFragment = new UserGuideFragment();
                return userGuideFragment;
            case 4:
                MainActivityFragment mainActivityFragment = new MainActivityFragment();
                return mainActivityFragment;
            case 5:
                QueryFragment queryFragment = new QueryFragment();
                return queryFragment;
            case 6:
                DefPageFragment defPageFragment = new DefPageFragment();
                return defPageFragment;
            case 7:
                SetFragment settingsFragment = new SetFragment();
                return settingsFragment;
            case 8:
                TransPageFragment transPageFragment = new TransPageFragment();
                return transPageFragment;
            case 9:
                CameraPageFragment cameraPageFragment = new CameraPageFragment();
                return cameraPageFragment;
            case 10:
                TextSpeechFragment ttsFragment = new TextSpeechFragment();
                return ttsFragment;
            case 11:
                VoiceRecFragment voiceRecFragment = new VoiceRecFragment();
                return voiceRecFragment;
            case 12:
                AllSetFragment allSetFragment = new AllSetFragment();
                return allSetFragment;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 13;
    }


}