package com.demo.ingredisearch.repository.sources.favorites;

import android.content.Context;
import android.content.SharedPreferences;

import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.sources.FavoritesSource;
import com.demo.ingredisearch.repository.util.JsonConverter;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesFavoritesSource implements FavoritesSource {
    public static final String FAVORITES_KEY = "FAVORITE_KEY";

    private final SharedPreferences mPreferences;

    public SharedPreferencesFavoritesSource(Context context) {
        mPreferences = context.getSharedPreferences(FAVORITES_KEY, Context.MODE_PRIVATE);
    }

    @Override
    public List<Recipe> getFavorites() {
        String stringifiedFavorites = mPreferences.getString(FAVORITES_KEY, null);
        return stringifiedFavorites == null ? new ArrayList<>() : JsonConverter.toRecipes(stringifiedFavorites);
    }

    @Override
    public void addFavorite(Recipe recipe) {
        List<Recipe> favorites = getFavorites();
        if (contains(favorites, recipe)) return;

        Recipe temp = new Recipe(recipe);
        temp.setFavorite(true);
        favorites.add(temp);

        save(favorites);
    }

    private boolean contains(List<Recipe> favorites, Recipe recipe) {
        return favorites.stream().anyMatch(recipe::isSameAs);
    }

    private void save(List<Recipe> favorites) {
        mPreferences.edit().putString(FAVORITES_KEY, JsonConverter.toJson(favorites)).apply();
    }

    @Override
    public void removeFavorite(Recipe recipe) {
        List<Recipe> favorites = getFavorites();
        if (favorites.removeIf(recipe::isSameAs)) {
            save(favorites);
        }
    }

    @Override
    public void clearFavorites() {
        mPreferences.edit().clear().apply();
    }

}

