package com.demo.ingredisearch.repository;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.models.Recipe;
import com.demo.ingredisearch.repository.sources.favorites.FakeFavoritesSource;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource.DataStatus;
import com.demo.ingredisearch.util.LiveDataTestUtil;
import com.demo.ingredisearch.util.Resource;
import com.demo.ingredisearch.util.SingleExecutors;
import com.demo.ingredisearch.util.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

public class RecipeRepositoryTest {
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    // SUT
    RecipeRepository mRecipeRepository;

    FakeRemoteDataSource mRemoteDataSource;
    FakeFavoritesSource mFavoritesSource;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);

        mRemoteDataSource = new FakeRemoteDataSource(new SingleExecutors());
        mFavoritesSource = new FakeFavoritesSource();
        mRecipeRepository = RecipeRepository.getInstance(mRemoteDataSource, mFavoritesSource);
    }

    @After
    public void tearDown() {
        mRecipeRepository.destroy();
    }

    @Test
    public void searchRecipes_whenFailedByNetworkError_returnsErrorResponse() {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.NetworkError);

        LiveData<Resource<List<Recipe>>> recipes = mRecipeRepository.getRecipes();
        Observer<Resource<List<Recipe>>> observer = new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(Resource<List<Recipe>> response) {
                // Assert (Then)
                if (response.status != Status.LOADING)
                    assertThat(response, is(Resource.error("Network Error", null)));
                recipes.removeObserver(this);
            }
        };
        recipes.observeForever(observer);

        // Act (When)
        mRecipeRepository.searchRecipes("some query");
    }

    @Test
    public void searchRecipes_whenFailedByNetworkError_returnsErrorResponse_with_Mock_observer() {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.NetworkError);

        LiveData<Resource<List<Recipe>>> recipes = mRecipeRepository.getRecipes();
        Observer<Resource<List<Recipe>>> observer = mock(Observer.class);
        recipes.observeForever(observer);

        // Act (When)
        mRecipeRepository.searchRecipes("some query");

        // Assert (Then)
        InOrder inOrder = Mockito.inOrder(observer);
        inOrder.verify(observer).onChanged(Resource.loading(null));
        inOrder.verify(observer).onChanged(Resource.error("Network Error", null));
    }

    @Test
    public void searchRecipes_whenFailedWithHTTPError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.HTTPError);

        // Act (When)
        mRecipeRepository.searchRecipes("some query");

        // Assert (Then)
        Resource<List<Recipe>> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipes());
        assertThat(response, is(Resource.error("HTTP Error", null)));
    }

    @Test
    public void searchRecipes_whenFailedWithAuthError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.AuthError);

        // Act (When)
        mRecipeRepository.searchRecipes("some query");

        // Assert (Then)
        Resource<List<Recipe>> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipes());
        assertThat(response, is(Resource.error("401 Unauthorized. Token may be invalid", null)));
    }

    @Test
    public void searchRecipes_whenSucceedWithNullResult_returnsEmptyList() throws InterruptedException {
        // Arrange (Given)

        // Act (When)
        mRecipeRepository.searchRecipes("some query");

        // Assert (Then)
        Resource<List<Recipe>> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipes());
        assertThat(response, is(Resource.success(emptyList())));
    }

    @Test
    public void searchRecipes_whenSucceed_returnsRecipesList() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        // Act (When)
        mRecipeRepository.searchRecipes("some query");

        // Assert (Then)
        Resource<List<Recipe>> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipes());
        assertThat(response, is(Resource.success(TestData.mRecipes)));
    }

    @Test
    public void searchRecipe_whenFailedByNetworkError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.NetworkError);

        // Act (When)
        mRecipeRepository.searchRecipe("some query id");

        // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipe());
        assertThat(response, is(Resource.error("Network Error", null)));
    }

    @Test
    public void searchRecipe_whenFailedWithHTTPError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.HTTPError);

        // Act (When)
        mRecipeRepository.searchRecipe("some query id");

        // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipe());
        assertThat(response, is(Resource.error("HTTP Error", null)));
    }

    @Test
    public void searchRecipe_whenFailedWithAuthError_returnsErrorResponse() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.setDataStatus(DataStatus.AuthError);

        // Act (When)
        mRecipeRepository.searchRecipe("some query id");

        // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipe());
        assertThat(response, is(Resource.error("401 Unauthorized. Token may be invalid", null)));
    }

    @Test
    public void searchRecipe_whenSucceedWithNullResult_returnsNull() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.recipeDetails01);

        // Act (When)
        mRecipeRepository.searchRecipe(TestData.recipeDetails02.getRecipeId());

        // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipe());
        assertThat(response, is(Resource.success(null)));
    }

    @Test
    public void searchRecipe_whenSucceed_returnsRecipe() throws InterruptedException {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.recipeDetails01);

        // Act (When)
        mRecipeRepository.searchRecipe(TestData.recipeDetails01.getRecipeId());

        // Assert (Then)
        Resource<Recipe> response = LiveDataTestUtil.getOrAwaitValue(mRecipeRepository.getRecipe());
        assertThat(response, is(Resource.success(TestData.recipeDetails01)));
    }

    @Test
    public void getFavorites_noFavorites_returnEmptyList() {
        // Arrange (Given)

        // Act (When)
        List<Recipe> favorites = mRecipeRepository.getFavorites();

        // Assert (Then)
        assertThat(favorites, hasSize(0));
    }

    @Test
    public void getFavorites_someFavorites_returnAll() {
        // Arrange (Given)
        mRecipeRepository.addFavorite(TestData.recipe1);

        // Act (When)
        List<Recipe> favorites = mRecipeRepository.getFavorites();

        // Assert (Then)
        assertThat(favorites, hasSize(1));
        assertThat(favorites, hasItems(TestData.recipe1_favored));
    }

    @Test
    public void addFavorites_noDuplicateId_addToFavoritesWithFavoriteStatusAsTrue() {
        // Arrange (Given)
        mFavoritesSource.addFavorite(TestData.recipe1);

        // Act (When)
        mRecipeRepository.addFavorite(TestData.recipe2);

        // Assert (Then)
        List<Recipe> favorites = mRecipeRepository.getFavorites();
        assertThat(favorites, hasSize(2));
        assertThat(favorites, hasItems(TestData.recipe1_favored, TestData.recipe2_favored));
    }

    @Test
    public void addFavorites_recipeWithSameIdAlreadyExists_rejectAddition() {
        // Arrange (Given)
        mFavoritesSource.addFavorite(TestData.recipe1);

        // Act (When)
        mRecipeRepository.addFavorite(TestData.recipe1);

        // Assert (Then)
        List<Recipe> favorites = mRecipeRepository.getFavorites();
        assertThat(favorites, hasSize(1));
        assertThat(favorites, hasItems(TestData.recipe1_favored));
    }

    @Test
    public void removeFavorite_removesRecipeFromFavorites() {
        // Arrange (Given)
        mFavoritesSource.addFavorite(TestData.recipe1);
        mFavoritesSource.addFavorite(TestData.recipe2);

        // Act (When)
        mRecipeRepository.removeFavorite(TestData.recipe1_favored);

        // Assert (Then)
        List<Recipe> favorites = mRecipeRepository.getFavorites();
        assertThat(favorites, hasSize(1));
        assertThat(favorites, hasItems(TestData.recipe2_favored));
    }

    @Test
    public void clearFavorites_removeAllFavorites() {
        // Arrange (Given)
        mFavoritesSource.addFavorite(TestData.recipe1);
        mFavoritesSource.addFavorite(TestData.recipe2);

        // Act (When)
        mRecipeRepository.clearFavorites();

        // Assert (Then)
        List<Recipe> favorites = mRecipeRepository.getFavorites();
        assertThat(favorites, hasSize(0));
    }

}