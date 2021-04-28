package com.demo.ingredisearch.ui;

import android.view.Gravity;
import android.widget.ImageView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.DrawerMatchers;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.demo.ingredisearch.R;
import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.features.BaseUITest;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasSibling;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.demo.ingredisearch.util.CustomViewMatchers.withToolbarTitle;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

public class MainActivityTest extends BaseUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void activityInView() {
        // check toolbar main title displayed
        // TODO
//        onView(withText(R.string.main_title)).check(matches(isDisplayed()));
        onView(withToolbarTitle(R.string.main_title)).check(matches(isDisplayed()));

        checkMainView();
    }

    private void checkMainView() {
        // check whether 'searchButton' view displayed
        onView(withId(R.id.searchButton)).check(matches(isDisplayed()));
        onView(allOf(
                withText(R.string.search),
                withParent(withId(R.id.searchButton)),
                hasSibling(withContentDescription(R.string.search))
        )).check(matches(isDisplayed()));

        // check whether 'favButton' view displayed
        onView(withId(R.id.favButton)).check(matches(isDisplayed()));
        onView(allOf(
                withText(R.string.favorites),
                withParent(withId(R.id.favButton)),
                hasSibling(withContentDescription(R.string.favorites))
        )).check(matches(isDisplayed()));
    }

    @Test
    public void testOpenDrawer() {
        // Arrange (Given)

        // Act (When)
        // open drawer (drawer_layout)
        openDrawer();

        // Assert (Then)
        // check drawer Header displayed
        onView(withText(R.string.drawer_search_title)).check(matches(isDisplayed()));

        // check drawer Contents displayed
        onView(withText(R.string.home)).check(matches(isDisplayed()));

        onView(allOf(
                withText(R.string.search),
                withParent(withId(R.id.searchFragment))
        )).check(matches(isDisplayed()));

        onView(allOf(
                withText(R.string.favorites),
                withParent(withId(R.id.favoritesFragment))
        )).check(matches(isDisplayed()));
    }

    @Test
    public void testCloseDrawer() {
        // Arrange (Given)
        // open drawer
        openDrawer();

        // Act (When)
        // close drawer
        closeDrawer();

        // Assert (Then)
        // check drawer Header not displayed
        onView(withText(R.string.drawer_search_title)).check(matches(not(isDisplayed())));

        // check drawer Contents not displayed
        onView(withText(R.string.home)).check(matches(not(isDisplayed())));

        onView(allOf(
                withText(R.string.search),
                withParent(withId(R.id.searchFragment))
        )).check(matches(not(isDisplayed())));

        onView(allOf(
                withText(R.string.favorites),
                withParent(withId(R.id.favoritesFragment))
        )).check(matches(not(isDisplayed())));
    }

    @Test
    public void navigateToHomeScreen() {
        // Arrange (Given)
        // open drawer
        openDrawer();

        // Act (When)
        // click on Home menu item
        onView(withId(R.id.mainFragment)).perform(click());

        // Assert (Then)
        // check whether searchButton and favButton is displayed
        checkMainView();
    }

    @Test
    public void navigateToSearchScreen() {
        // Arrange (Given)
        // open drawer
        openDrawer();

        // Act (When)
        // click on Search menu item
        onView(withId(R.id.searchFragment)).perform(click());

        // Assert (Then)
        // check whether search_title text is displayed
        // TODO
//        onView(withText(R.string.search_title)).check(matches(isDisplayed()));
//        onView(isAssignableFrom(Toolbar.class)).check(matches(withToolbarTitle(R.string.search_title)));
        onView(withToolbarTitle(R.string.search_title)).check(matches(isDisplayed()));

        checkSearchView();
    }

    private void checkSearchView() {
        // check whether search_header text is displayed
        onView(withText(R.string.search_header)).check(matches(isDisplayed()));

        // check whether ingredients is displayed
        onView(withId(R.id.ingredients)).check(matches(isDisplayed()));

        // check whether searchActionButton is displayed
        onView(withId(R.id.searchActionButton)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToFavoriteScreenWithNoFavorites() {
        // Arrange (Given)
        // open drawer
        openDrawer();

        // Act (When)
        // click on Favorites menu item
        onView(withId(R.id.favoritesFragment)).perform(click());

        // Assert (Then)
        // TODO
        // check whether "Favorites" toolbar title is displayed
//        onView(allOf(
//                withText(R.string.favorites_title),
//                withParent(withId(R.id.toolbar)),
//                hasSibling(isAssignableFrom(ImageView.class))
//        )).check(matches(isDisplayed()));
        onView(withToolbarTitle(R.string.favorites_title)).check(matches(isDisplayed()));

        onView(withId(R.id.noresultsContainer)).check(matches(isDisplayed()));
        onView(withId(R.id.noresultsTitle)).check(matches(withText(R.string.nofavorites)));
    }

    @Test
    public void navigateToFavoriteScreenWithSomeFavorites() {
        // Arrange (Given)
        mFavoritesSource.addFavorites(TestData.mFavorites);
        // open drawer
        openDrawer();

        // Act (When)
        // click on Favorites menu item
        onView(withId(R.id.favoritesFragment)).perform(click());

        // Assert (Then)
        // check whether "Favorites" toolbar title is displayed
        onView(allOf(
                withText(R.string.favorites_title),
                withParent(withId(R.id.toolbar)),
                hasSibling(isAssignableFrom(ImageView.class))
        )).check(matches(isDisplayed()));

        onView(withId(R.id.list)).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToSearchScreen_and_backToHomeScreen() {
        // Arrange (Given)
        // click on Search button
        onView(withId(R.id.searchButton)).perform(click());
        checkSearchView();

        // Act (When)
        // press on the back button or navigateBack()
//        Espresso.pressBack();
        navigateBack();

        // Assert (Then)
        // check whether searchButton and favButton is displayed
        checkMainView();
    }

    @Test
    public void navigateToSearchScreenToSearchResults_and_backToMainScreen() {
        // Arrange (Given)
        // click on Search button
        onView(withId(R.id.searchButton)).perform(click());

        // enter query and press searchActionButton
        onView(withId(R.id.ingredients)).perform(typeText("eggs"), closeSoftKeyboard());
        onView(withId(R.id.searchActionButton)).perform(click());

        // Act (When)
        // press back button twice
        Espresso.pressBack();
        Espresso.pressBack();

        // Assert (Then)
        // check whether main screen in view
        checkMainView();
    }

    @Test
    public void navigateToSearchScreenToSearchResultsToRecipeDetails_and_backToMainScreen() {
        // Arrange (Given)
        // add test data to fake remote data source
        mRemoteDataSource.addRecipes(TestData.recipeDetails01);

        // click on Search button
        onView(withId(R.id.searchButton)).perform(click());

        // enter query and press searchActionButton
        onView(withId(R.id.ingredients)).perform(typeText("eggs"), closeSoftKeyboard());
        onView(withId(R.id.searchActionButton)).perform(click());

        // select a recipe
        onView(withId(R.id.list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Act (When)
        // press back button twice
        Espresso.pressBack();
        Espresso.pressBack();
        Espresso.pressBack();

        // Assert (Then)
        // check whether main screen in view
        checkMainView();
    }

    private void openDrawer() {
        // R.id.drawer_layout, Use DrawerMatchers and DrawerActions
        onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isClosed(Gravity.START)))
                .perform(DrawerActions.open(Gravity.START));
    }

    private void closeDrawer() {
        onView(withId(R.id.drawer_layout))
                .check(matches(DrawerMatchers.isOpen(Gravity.START)))
                .perform(DrawerActions.close(Gravity.START));
    }

    private void navigateBack() {
        // contentDescription "Navigate up" or R.string.abc_action_bar_up_description
//        onView(withContentDescription("Navigate up")).perform(click());
//        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());

        ViewInteraction hamburgerButton = onView(allOf(
                isAssignableFrom(ImageView.class),
                withParent(withId(R.id.toolbar))
        ));

        hamburgerButton.perform(click());
    }

}
