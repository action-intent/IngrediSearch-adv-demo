package com.demo.ingredisearch.features.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.demo.ingredisearch.R;
import com.demo.ingredisearch.util.EventObserver;
import com.demo.ingredisearch.util.ViewHelper;
import com.google.android.material.snackbar.Snackbar;

public class SearchFragment extends Fragment {
    private Button searchActionButton;
    private EditText ingredients;

    private SearchViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_search, container, false);
        getViews(root);

        searchActionButton.setOnClickListener(view -> {
            String query = ingredients.getText().toString();
            ViewHelper.hideKeyboard(this);
            mViewModel.search(query);
        });

        ViewHelper.showSubtitle(this, null);
        return root;
    }

    private void getViews(View root) {
        ingredients = root.findViewById(R.id.ingredients);
        searchActionButton = root.findViewById(R.id.searchActionButton);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createViewModel();
        subscribeObservers();
    }

    private void createViewModel() {
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory())
                .get(SearchViewModel.class);
    }

    private void subscribeObservers() {
        mViewModel.isEmpty().observe(getViewLifecycleOwner(),
                new EventObserver<>(isEmpty -> showEmptyQueryAlert()));

        mViewModel.navToSearchResults().observe(getViewLifecycleOwner(),
                new EventObserver<>(this::navigateToSearchResults));
    }

    private void showEmptyQueryAlert() {
        Snackbar.make(requireView(), R.string.search_query_required, Snackbar.LENGTH_LONG).show();
    }

    private void navigateToSearchResults(String query) {
        Navigation.findNavController(requireView()).navigate(
                SearchFragmentDirections.actionSearchFragmentToSearchResultsFragment(query));
    }

}
