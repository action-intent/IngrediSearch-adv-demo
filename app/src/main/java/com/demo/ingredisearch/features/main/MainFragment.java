package com.demo.ingredisearch.features.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.demo.ingredisearch.R;
import com.demo.ingredisearch.util.ViewHelper;

public class MainFragment extends Fragment {
    private LinearLayout searchButton;
    private LinearLayout favButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        getViews(root);
        ViewHelper.showSubtitle(this, null);
        ViewHelper.hideKeyboard(this); // necessary when back from SearchFragment

        setupNavigation();

        return root;
    }

    private void getViews(View root) {
        searchButton = root.findViewById(R.id.searchButton);
        favButton = root.findViewById(R.id.favButton);
    }

    private void setupNavigation() {
        searchButton.setOnClickListener(view -> navigateTo(R.id.action_mainFragment_to_searchFragment));

        favButton.setOnClickListener(view -> navigateTo(R.id.action_mainFragment_to_favoritesFragment));
    }

    private void navigateTo(int destination) {
        Navigation.findNavController(requireView()).navigate(destination);
    }

}
