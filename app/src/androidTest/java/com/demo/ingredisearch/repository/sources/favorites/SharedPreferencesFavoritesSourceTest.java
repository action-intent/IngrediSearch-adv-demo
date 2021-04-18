package com.demo.ingredisearch.repository.sources.favorites;

import androidx.test.core.app.ApplicationProvider;

import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.sources.FavoritesSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

public class SharedPreferencesFavoritesSourceTest {
    // SUT
    FavoritesSource mFavoritesSource;

    @Before
    public void init() {
        mFavoritesSource = new SharedPreferencesFavoritesSource(
                ApplicationProvider.getApplicationContext()
        );
        mFavoritesSource.clearFavorites();
    }

    @After
    public void tearDown() {
        mFavoritesSource.clearFavorites();
    }

    @Test
    public void getFavorites_noFavorites_returnEmptyList() {
        // Arrange (Given)

        // Act (When)
        List<Recipe> favorites = mFavoritesSource.getFavorites();

        // Assert (Then)
        assertThat(favorites, hasSize(0));
    }

    @Test
    public void getFavorites_someFavorites_returnAll() {
        // Arrange (Given)

        // Act (When)
        mFavoritesSource.addFavorite(TestData.recipe1);

        // Assert (Then)
        List<Recipe> favorites = mFavoritesSource.getFavorites();
        assertThat(favorites, hasSize(1));
    }

    @Test
    public void addFavorites_noDuplicateId_addToFavoritesWithFavoriteStatusAsTrue() {
        // Arrange (Given)
        mFavoritesSource.addFavorite(TestData.recipe1);

        // Act (When)
        mFavoritesSource.addFavorite(TestData.recipe2);

        // Assert (Then)
        List<Recipe> favorites = mFavoritesSource.getFavorites();
        assertThat(favorites, hasSize(2));
        assertThat(favorites, hasItems(TestData.recipe1_favored, TestData.recipe2_favored));
    }

    @Test
    public void addFavorites_recipeWithSameIdAlreadyExists_rejectAddition() {
        // Arrange (Given)
        mFavoritesSource.addFavorite(TestData.recipe1);

        // Act (When)
        mFavoritesSource.addFavorite(TestData.recipe1);

        // Assert (Then)
        List<Recipe> favorites = mFavoritesSource.getFavorites();
        assertThat(favorites, hasSize(1));
    }

    @Test
    public void removeFavorite_removesRecipeFromFavorites() {
        // Arrange (Given)
        mFavoritesSource.addFavorite(TestData.recipe1);
        mFavoritesSource.addFavorite(TestData.recipe2);

        // Act (When)
        mFavoritesSource.removeFavorite(TestData.recipe1_favored);

        // Assert (Then)
        List<Recipe> favorites = mFavoritesSource.getFavorites();
        assertThat(favorites, hasSize(1));
    }

    @Test
    public void clearFavorites_removeAllFavorites() {
        // Arrange (Given)
        mFavoritesSource.addFavorite(TestData.recipe1);
        mFavoritesSource.addFavorite(TestData.recipe2);

        // Act (When)
        mFavoritesSource.clearFavorites();

        // Assert (Then)
        List<Recipe> favorites = mFavoritesSource.getFavorites();
        assertThat(favorites, hasSize(0));
    }

}
