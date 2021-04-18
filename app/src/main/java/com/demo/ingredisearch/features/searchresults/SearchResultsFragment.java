package com.demo.ingredisearch.features.searchresults;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.ingredisearch.R;
import com.demo.ingredisearch.RecipeApplication;
import com.demo.ingredisearch.adapters.RecipeAdapter;
import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.RecipeRepository;
import com.demo.ingredisearch.util.EventObserver;
import com.demo.ingredisearch.util.ViewHelper;

import java.util.List;

public class SearchResultsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;

    private TextView mRetry;
    private ViewHelper mViewHelper;
    private String mQuery;

    private SearchResultsViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_list, container, false);
        getViews(root);

        SearchResultsFragmentArgs arguments = SearchResultsFragmentArgs.fromBundle(requireArguments());
        mQuery = arguments.getQuery();
        mRetry.setOnClickListener(view -> searchRecipes(mQuery)); // May use LiveData on ViewModel
        ViewHelper.showSubtitle(this, mQuery);

        setHasOptionsMenu(true);
        setupRecyclerView();

        return root;
    }

    private void getViews(View root) {
        mRecyclerView = root.findViewById(R.id.list);
        LinearLayout mLoadingContainer = root.findViewById(R.id.loadingContainer);
        LinearLayout mErrorContainer = root.findViewById(R.id.errorContainer);
        LinearLayout mNoResultsContainer = root.findViewById(R.id.noresultsContainer);
        mRetry = root.findViewById(R.id.retry);

        mViewHelper = new ViewHelper(mRecyclerView, mLoadingContainer,
                mErrorContainer, mNoResultsContainer);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createViewModel();
        subscribeObservers();

        searchRecipes(mQuery);
    }

    private void createViewModel() {
        RecipeApplication app = (RecipeApplication) requireActivity().getApplication();
        mViewModel = new ViewModelProvider(this, new SearchResultsViewModelFactory(
                app.getInjection().getRecipeRepository()
        )).get(SearchResultsViewModel.class);
    }

    private void subscribeObservers() {
        mViewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null) {
                handleRecipes(recipes);
            }
        });

        mViewModel.isError().observe(getViewLifecycleOwner(),
                new EventObserver<>(isError -> showError()));

        mViewModel.navToDetails().observe(getViewLifecycleOwner(), new EventObserver<>(
                this::navigateToRecipeDetails
        ));

        mViewModel.isLoading().observe(getViewLifecycleOwner(), new EventObserver<>(isLoading -> handleLoading()));
    }

    private void handleLoading() {
        mViewHelper.showLoading();
    }

    private void showError() {
        mViewHelper.showError();
    }

    private void handleRecipes(List<Recipe> recipes) {
        if (recipes.isEmpty()) {
            mViewHelper.showNoResults();
        } else {
            mViewHelper.hideOthers();
            mAdapter.setRecipes(recipes);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.favoritesFragment) {
            Navigation.findNavController(requireView()).navigate(R.id.action_searchResultsFragment_to_favoritesFragment);
            return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_recipes, menu);
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new RecipeAdapter(new RecipeAdapter.Interaction() {
            @Override
            public void onRemoveFavorite(@NonNull Recipe recipe) {
                mViewModel.unMarkFavorite(recipe);
            }

            @Override
            public void onAddFavorite(@NonNull Recipe recipe) {
                mViewModel.markFavorite(recipe);
            }

            @Override
            public void onClickItem(@NonNull Recipe recipe) {
                mViewModel.requestNavToDetails(recipe);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void navigateToRecipeDetails(@NonNull String recipeId) {
        Navigation.findNavController(requireView()).navigate(
                SearchResultsFragmentDirections.actionSearchResultsFragmentToRecipeDetailsFragment(recipeId));
    }

    public void searchRecipes(String query) {
        mViewModel.searchRecipes(query);
    }
}
