package com.demo.ingredisearch.features.details;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.features.BaseUnitTest;
import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource.DataStatus;
import com.demo.ingredisearch.util.LiveDataTestUtil;
import com.demo.ingredisearch.util.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RecipeDetailsViewModelTest extends BaseUnitTest {
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    // SUT
    RecipeDetailsViewModel mViewModel;

    @Before
    public void init() {
        super.init();

        mViewModel = new RecipeDetailsViewModel(mRecipeRepository);
    }

    @Test
    public void searchRecipe_returnsThatRecipe() throws Exception {
        // Given
        mRemoteDataSource.addRecipes(TestData.recipeDetails01);
        mViewModel.searchRecipe(TestData.recipeDetails01.getRecipeId());

        // When
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipe());

        // Then
        assertThat(response, is(Resource.success(TestData.recipeDetails01)));
    }

    @Test
    public void searchRecipe_noMatch_returnsNull() throws Exception {
        // Given
        mViewModel.searchRecipe(TestData.recipeDetails01.getRecipeId());

        // When
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipe());

        // Then
        assertThat(response, is(Resource.success(null)));
    }

    @Test
    public void searchRecipe_error_returnsError() throws Exception {
        // Given
        mRemoteDataSource.setDataStatus(DataStatus.NetworkError);
        mViewModel.searchRecipe(TestData.recipeDetails01.getRecipeId());

        // When
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(mViewModel.getRecipe());

        // Then
        assertThat(response, is(Resource.error("Network Error", null)));
    }
}