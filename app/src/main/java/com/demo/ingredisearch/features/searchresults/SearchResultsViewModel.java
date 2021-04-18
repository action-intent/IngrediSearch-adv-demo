package com.demo.ingredisearch.features.searchresults;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.RecipeRepository;
import com.demo.ingredisearch.util.Event;
import com.demo.ingredisearch.util.Status;

import java.util.List;
import java.util.stream.Collectors;

public class SearchResultsViewModel extends ViewModel {
    private final RecipeRepository mRecipeRepository;

    public SearchResultsViewModel(RecipeRepository mRecipeRepository) {
        this.mRecipeRepository = mRecipeRepository;
    }

    public void searchRecipes(String query) {
        mRecipeRepository.searchRecipes(query);
        reload();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return Transformations.switchMap(mIsReload(), isReload ->
                Transformations.map(mRecipeRepository.getRecipes(), resource -> {
                    if (resource.status == Status.SUCCESS) {
                        return markFavorites(resource.data);
                    } else if (resource.status == Status.ERROR) {
                        mIsError.setValue(new Event<>(new Object()));
                        return null;
                    } else if (resource.status == Status.LOADING) {
                        mIsLoading.setValue(new Event<>(new Object()));
                        return null;
                    } else {
                        throw new IllegalStateException();
                    }
                }));
    }

    private MutableLiveData<Event<Object>> mIsLoading = new MutableLiveData<>();

    public LiveData<Event<Object>> isLoading() {
        return mIsLoading;
    }

    private MutableLiveData<Event<Object>> mIsError = new MutableLiveData<>();

    public LiveData<Event<Object>> isError() {
        return mIsError;
    }

    private List<Recipe> markFavorites(List<Recipe> recipes) {
        List<Recipe> favorites = mRecipeRepository.getFavorites();
        return recipes.stream().map(recipe -> {
            if (favorites.stream().anyMatch(recipe::isSameAs)) {
                Recipe temp = new Recipe(recipe);
                temp.setFavorite(true);
                return temp;
            } else {
                return recipe;
            }
        }).collect(Collectors.toList());
    }

    public void markFavorite(Recipe recipe) {
        mRecipeRepository.addFavorite(recipe);
        reload();
    }

    public void unMarkFavorite(Recipe recipe) {
        mRecipeRepository.removeFavorite(recipe);
        reload();
    }

    MutableLiveData<Event<Object>> mReload = new MutableLiveData<>();

    private LiveData<Event<Object>> mIsReload() {
        return mReload;
    }

    private void reload() {
        mReload.setValue(new Event<>(new Object()));
    }

    public void requestNavToDetails(Recipe recipe) {
        mNavToDetails.setValue(new Event<>(recipe.getRecipeId()));
    }

    private MutableLiveData<Event<String>> mNavToDetails = new MutableLiveData<>();

    public LiveData<Event<String>> navToDetails() {
        return mNavToDetails;
    }

    /*
     * Currently, nav to favorites is handled by the fragment directly.
     */
    public void requestNavToFavorites() {
        mNavToFavorites.setValue(new Event<>(new Object()));
    }

    private MutableLiveData<Event<Object>> mNavToFavorites = new MutableLiveData<>();

    public LiveData<Event<Object>> navToFavorites() {
        return mNavToFavorites;
    }
}
