package com.vish.apps.dictionary.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.vish.apps.dictionary.fragments.DefinitionFragment;
import com.vish.apps.dictionary.fragments.SettingsFragment;
import com.vish.apps.dictionary.fragments.TranslationFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                DefinitionFragment definitionFragment = new DefinitionFragment();
                return definitionFragment;
            case 1:
                TranslationFragment translationFragment = new TranslationFragment();
                return translationFragment;
            case 2:
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
