package com.demo.ingredisearch.ui;

import androidx.test.espresso.Espresso;

import org.junit.Test;

public class MainActivityTest {

    @Test
    public void activityInView() {
        // check toolbar main title displayed

        // check whether 'searchButton' view displayed

        // check whether 'favButton' view displayed
    }

    @Test
    public void testOpenDrawer() {
        // Arrange (Given)

        // Act (When)
        // open drawer (drawer_layout)
        openDrawer();

        // Assert (Then)
        // check drawer Header displayed

        // check drawer Contents displayed
    }

    @Test
    public void testCloseDrawer() {
        // Arrange (Given)

        // Act (When)
        // open drawer and close drawer

        // Assert (Then)
        // check drawer Header not displayed

        // check drawer Contents not displayed
    }

    @Test
    public void navigateToHomeScreen() {
        // Arrange (Given)
        // open drawer

        // Act (When)
        // click on Home menu item

        // Assert (Then)
        // check whether searchButton and favButton is displayed
    }

    @Test
    public void navigateToSearchScreen() {
        // Arrange (Given)
        // open drawer

        // Act (When)
        // click on Search menu item

        // Assert (Then)
        // check whether search_title text is displayed

        // check whether search_header text is displayed

        // check whether ingredients is displayed

        // check whether searchActionButton is displayed
    }

    @Test
    public void navigateToFavoriteScreen() {
        // Arrange (Given)
        // open drawer

        // Act (When)
        // click on Favorites menu item

        // Assert (Then)
        // check whether "Favorites" toolbar title is displayed
    }

    @Test
    public void navigateToSearchScreen_and_backToHomeScreen() {
        // Arrange (Given)
        // click on Search button

        // Act (When)
        // press on the back button or navigateBack()

        // Assert (Then)
        // check whether searchButton and favButton is displayed
    }

    @Test
    public void navigateToSearchScreenToSearchResults_and_backToMainScreen() {
        // Arrange (Given)
        // click on Search button

        // enter query and press searchActionButton

        // Act (When)
        // press back button twice

        // Assert (Then)
        // check whether main screen in view
    }

    @Test
    public void navigateToSearchScreenToSearchResultsToRecipeDetails_and_backToMainScreen() {
        // Arrange (Given)
        // add test data to fake remote data source

        // click on Search button

        // enter query and press searchActionButton

        // select a recipe

        // Act (When)
        // press back button twice
        Espresso.pressBack();
        Espresso.pressBack();
        Espresso.pressBack();

        // Assert (Then)
        // check whether main screen in view
    }

    private void openDrawer() {
        // R.id.drawer_layout, Use DrawerMatchers and DrawerActions

    }

    private void closeDrawer() {

    }

    private void navigateBack() {
        // contentDescription "Navigate up" or R.string.abc_action_bar_up_description
    }

}
