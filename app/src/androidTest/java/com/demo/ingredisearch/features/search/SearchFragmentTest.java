package com.demo.ingredisearch.features.search;

import android.os.RemoteException;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.demo.ingredisearch.R;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SearchFragmentTest {

    @Test
    public void searchFragmentInView() {
        // Arrange (Given)

        // Act (When)
        FragmentScenario.launchInContainer(SearchFragment.class, null, R.style.AppTheme);

        // Assert (Then)
        onView(withText(R.string.search_header)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredients)).check(matches(isDisplayed()));
        onView(withId(R.id.searchActionButton)).check(matches(isDisplayed()));
    }

    @Test
    public void search_validQuery_navigateToSearchResultsView() {
        // Arrange (Given)
        NavHostController navHostController = mock(NavHostController.class);
        FragmentScenario<SearchFragment> scenario =
                FragmentScenario.launchInContainer(SearchFragment.class, null, R.style.AppTheme);
        scenario.onFragment(fragment -> {
            Navigation.setViewNavController(fragment.requireView(), navHostController);
        });

        // Act (When)
        onView(withId(R.id.ingredients)).perform(typeText("eggs"), closeSoftKeyboard());
        onView(withId(R.id.searchActionButton)).perform(click());

        // Assert (Then)
        verify(navHostController).navigate(SearchFragmentDirections.actionSearchFragmentToSearchResultsFragment("eggs"));
    }

    @Test
    public void search_emptyQuery_displayWarningSnackBar() {
        // Arrange (Given)
        FragmentScenario.launchInContainer(SearchFragment.class, null, R.style.AppTheme);

        // Act (When)
        onView(withId(R.id.ingredients)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.searchActionButton)).perform(click());

        // Assert (Then)
        // check snackbar is displayed: search_query_required
        onView(withText(R.string.search_query_required)).check(matches(isDisplayed()));
    }

    @Test
    public void search_emptyQuery_displayWarningSnackBar_shouldNotDisplayAgain_whenRotated() throws RemoteException {
        // Arrange (Given)
        FragmentScenario.launchInContainer(SearchFragment.class, null, R.style.AppTheme);

        // Act (When)
        onView(withId(R.id.ingredients)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.searchActionButton)).perform(click());

        // Assert (Then)
        // check snackbar is displayed
        onView(withText(R.string.search_query_required)).check(matches(isDisplayed()));

        // Rotate screen
        UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        uiDevice.setOrientationLeft();

        // check snackbar does not exist
        onView(withText(R.string.search_query_required)).check(doesNotExist());
    }
}