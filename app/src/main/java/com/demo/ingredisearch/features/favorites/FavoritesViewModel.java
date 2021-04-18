package com.demo.ingredisearch.features.favorites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.RecipeRepository;
import com.demo.ingredisearch.util.Event;

import java.util.ArrayList;
import java.util.List;

public class FavoritesViewModel extends ViewModel {
    private final RecipeRepository mRecipeRepository;

    public FavoritesViewModel(RecipeRepository mRecipeRepository) {
        this.mRecipeRepository = mRecipeRepository;
    }

    public void load() {
        List<Recipe> favorites = new ArrayList<>(mRecipeRepository.getFavorites());
        mFavorites.setValue(favorites);
    }

    private final MutableLiveData<List<Recipe>> mFavorites = new MutableLiveData<>();

    public LiveData<List<Recipe>> getFavorites() {
        return mFavorites;
    }

    public void removeFavorite(Recipe recipe) {
        mRecipeRepository.removeFavorite(recipe);
        load();
    }

    public void clearFavorites() {
        mRecipeRepository.clearFavorites();
        load();
    }

    public void requestToNavToDetails(String recipeId) {
        mNavToDetails.setValue(new Event<>(recipeId));
    }

    private final MutableLiveData<Event<String>> mNavToDetails = new MutableLiveData<>();

    public LiveData<Event<String>> navToDetails() {
        return mNavToDetails;
    }
}
