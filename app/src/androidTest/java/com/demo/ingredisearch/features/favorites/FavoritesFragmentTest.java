package com.demo.ingredisearch.features.favorites;

import android.view.View;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;

import com.demo.ingredisearch.R;
import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.features.BaseUITest;

import org.hamcrest.Matcher;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static com.demo.ingredisearch.util.CustomViewMatchers.withRecyclerView;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FavoritesFragmentTest extends BaseUITest {

    @Test
    public void favoritesFragmentInView_withFavorites() {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.mFavorites);

        // Act (When)
        FragmentScenario.launchInContainer(FavoritesFragment.class, null, R.style.AppTheme);

        // Assert (Then)
        onView(withId(R.id.list)).check(matches(isDisplayed()));
    }

    @Test
    public void favoritesFragmentInView_withNoFavorites() {
        // Given

        // When
        FragmentScenario.launchInContainer(FavoritesFragment.class, null, R.style.AppTheme);

        // Then - No favorites yet
        onView(withId(R.id.noresultsContainer)).check(matches(isDisplayed()));
        onView(withId(R.id.noresultsTitle)).check(matches(isDisplayed()));
        onView(withText(R.string.nofavorites)).check(matches(isDisplayed()));
    }

    @Test
    public void selectRecipeAsNonFavorite_removesTheRecipeFromView() throws Exception {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.mFavorites);
        FragmentScenario.launchInContainer(FavoritesFragment.class, null, R.style.AppTheme);

        // Act (When)
        onView(withRecyclerView(R.id.list).atPositionOnView(0, R.id.favButton)).perform(click());

        // Assert (Then)
        onView(withFavButton(TestData.recipe1_favored.getTitle(), R.drawable.ic_favorite_24dp))
                .check(doesNotExist());

        // or

        onView(withFavButton(TestData.recipe1_favored.getTitle(), R.drawable.ic_favorite_24dp))
                .check(doesNotExist());
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
    public void selectLastRecipeAsNonFavorite_displaysNoFavorites() throws Exception {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.recipe1);
        FragmentScenario.launchInContainer(FavoritesFragment.class, null, R.style.AppTheme);

        // Act (When)
        onView(withRecyclerView(R.id.list).atPositionOnView(0, R.id.favButton)).perform(click());

        // Assert (Then)
        onView(withId(R.id.noresultsContainer)).check(matches(isDisplayed()));
        onView(withId(R.id.noresultsTitle)).check(matches(isDisplayed()));
        onView(withText(R.string.nofavorites)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToRecipeDetailsView() {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.mFavorites);

        NavHostController navHostController = mock(NavHostController.class);
        FragmentScenario<FavoritesFragment> scenario = FragmentScenario.launchInContainer(FavoritesFragment.class, null, R.style.AppTheme);
        scenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navHostController));

        // Act (When)
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Assert (Then)
        verify(navHostController).navigate(
                FavoritesFragmentDirections.actionFavoritesFragmentToRecipeDetailsFragment(TestData.recipe1_favored.getRecipeId()));
    }
}