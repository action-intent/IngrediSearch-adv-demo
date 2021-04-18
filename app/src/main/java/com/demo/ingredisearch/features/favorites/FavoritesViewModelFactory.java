package com.demo.ingredisearch.features.favorites;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.demo.ingredisearch.repository.RecipeRepository;

public class FavoritesViewModelFactory implements ViewModelProvider.Factory {
    private final RecipeRepository recipeRepository;

    public FavoritesViewModelFactory(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!modelClass.isAssignableFrom(FavoritesViewModel.class))
            throw new IllegalArgumentException("No such viewmodel exists");

        return (T) new FavoritesViewModel(recipeRepository);
    }
}
