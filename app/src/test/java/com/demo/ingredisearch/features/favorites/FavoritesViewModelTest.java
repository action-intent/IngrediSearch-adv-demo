package com.demo.ingredisearch.features.favorites;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.features.BaseUnitTest;
import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.util.Event;
import com.demo.ingredisearch.util.LiveDataTestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class FavoritesViewModelTest extends BaseUnitTest {
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    // SUT
    FavoritesViewModel mViewModel;

    @Before
    public void init() {
        super.init();
        mViewModel = new FavoritesViewModel(mRecipeRepository);
    }

    @Test
    public void getFavorites_returnFavoriteRecipes() throws InterruptedException {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.recipe1, TestData.recipe2);

        // Act (When)
        mViewModel.load();
        List<Recipe> favorites = LiveDataTestUtil.getOrAwaitValue(mViewModel.getFavorites());

        // Assert (Then)
        assertThat(favorites, hasItems(TestData.recipe1_favored, TestData.recipe2_favored));
    }

    @Test
    public void removeFavorite_shouldRemoveFavorite() throws InterruptedException {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.recipe1, TestData.recipe2);
        mViewModel.load();

        List<Recipe> favorites = LiveDataTestUtil.getOrAwaitValue(mViewModel.getFavorites());
        assertThat(favorites, hasSize(2));

        // Act (When)
        mViewModel.removeFavorite(TestData.recipe1_favored);

        // Assert (Then)
        favorites = LiveDataTestUtil.getOrAwaitValue(mViewModel.getFavorites());
        assertThat(favorites, hasSize(1));
    }

    @Test
    public void clearFavorites_shouldResetFavoritesToEmpty() throws Exception {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.recipe1, TestData.recipe2);
        mViewModel.load();

        List<Recipe> favorites = LiveDataTestUtil.getOrAwaitValue(mViewModel.getFavorites());
        assertThat(favorites, hasSize(2));

        // Act (When)
        mViewModel.clearFavorites();

        // Assert (Then)
        favorites = LiveDataTestUtil.getOrAwaitValue(mViewModel.getFavorites());
        assertThat(favorites, hasSize(0));
    }

    @Test
    public void requestToRecipeDetails_shouldTriggerNavToRecipeDetails() throws Exception {
        // Arrange (Given)

        // Act (When)
        mViewModel.requestToNavToDetails(TestData.recipe1_favored.getRecipeId());

        // Assert (Then)
        Event<String> response = LiveDataTestUtil.getOrAwaitValue(mViewModel.navToDetails());
        assertThat(response.getContentIfNotHandled(), is(TestData.recipe1_favored.getRecipeId()));
    }

}
