package com.demo.ingredisearch.features.searchresults;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.test.espresso.contrib.RecyclerViewActions;

import com.demo.ingredisearch.R;
import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.features.BaseUITest;
import com.demo.ingredisearch.repository.sources.remote.FakeRemoteDataSource.DataStatus;

import org.hamcrest.Matcher;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.demo.ingredisearch.util.CustomViewMatchers.withRecyclerView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SearchResultsFragmentTest extends BaseUITest {

    Bundle args = new SearchResultsFragmentArgs.Builder("eggs").build().toBundle();

    @Before
    public void init() {
       super.init();
    }

    @Test
    public void SearchResultsFragmentInView() {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        // Act (When)
        FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme);

        // Assert (Then)
        onView(withId(R.id.list)).check(matches(isDisplayed()));
    }

    @Test
    public void searchRecipes_queryWithNoResults_displayNoRecipesView() {
        // Arrange (Given)

        // Act (When)
        FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme);

        // Assert (Then)
        onView(withId(R.id.noresultsContainer)).check(matches(isDisplayed()));
        onView(withId(R.id.noresultsTitle)).check(matches(isDisplayed()));
        onView(withText(R.string.noresults)).check(matches(isDisplayed()));
    }

    @Test
    public void searchRecipes_errorOnConnection_displayRetryView() {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);
        mRemoteDataSource.setDataStatus(DataStatus.NetworkError);

        // Act (When)
        FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme);

        // Assert (Then)
        onView(withText(R.string.error)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(isDisplayed()));
        onView(withText(R.string.retry)).check(matches(isDisplayed()));
    }

    @Test
    public void searchRecipes_errorOnConnection_displayRetryView_andThen_retry() {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);
        mRemoteDataSource.setDataStatus(DataStatus.NetworkError);
        FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme);

        // Strange error if omitted!@#$!
        onView(withId(R.id.retry)).check(matches(isDisplayed()));

        // Act (When)
        mRemoteDataSource.setDataStatus(DataStatus.Success);
        onView(withId(R.id.retry)).perform(click());

        // Assert (Then)
        onView(withId(R.id.list)).check(matches(isDisplayed()));
    }

    @Test
    public void selectRecipeAsFavorite_markItsStatusAsFavorite() throws Exception {
        // Given
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme);

        // Act (When)
        onView(withFavButton(TestData.recipe1.getTitle(), R.drawable.ic_favorite_border_24dp)).perform(click());

        // Assert (Then)
        onView(withFavButton(TestData.recipe1.getTitle(), R.drawable.ic_favorite_24dp)).check(matches(isDisplayed()));
    }

    @NotNull
    protected Matcher<View> withFavButton(String title, int tag) {
        return allOf(
                hasSibling(withText(title)),
                withId(R.id.favButton),
                withTagValue(is(tag))
        );
    }

    @Test
    public void selectRecipeAsUnFavorite_markItsStatusAsUnFavorite() throws Exception {
        // Given
        mRemoteDataSource.addRecipes(TestData.mRecipes);
        mFavoritesSource.addFavorites(TestData.recipe1);

        FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme);

        // Act (When)
        onView(withRecyclerView(R.id.list).atPositionOnView(0, R.id.favButton))
                .perform(click());

        // Assert (Then)
        onView(withRecyclerView(R.id.list).atPositionOnView(0, R.id.favButton))
                .check(matches(withTagValue(is(R.drawable.ic_favorite_border_24dp))));
    }

    @Test
    public void navigateToRecipeDetailsView() {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.mRecipes);

        NavHostController navHostController = mock(NavHostController.class);
        FragmentScenario<SearchResultsFragment> scenario =
                FragmentScenario.launchInContainer(SearchResultsFragment.class, args, R.style.AppTheme);
        scenario.onFragment(fragment ->
                Navigation.setViewNavController(fragment.requireView(), navHostController));

        // Act (When)
        onView(withId(R.id.list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click())
        );

        // Assert (Then)
        verify(navHostController).navigate(
                SearchResultsFragmentDirections.actionSearchResultsFragmentToRecipeDetailsFragment(TestData.recipe1.getRecipeId()));
    }
}