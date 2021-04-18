package com.demo.ingredisearch.features.details;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;

import com.demo.ingredisearch.R;
import com.demo.ingredisearch.TestData;
import com.demo.ingredisearch.features.BaseUITest;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class RecipeDetailsFragmentTest extends BaseUITest {

    @Test
    public void recipeDetailsFragmentInView() {
        // Arrange (Given)
        mRemoteDataSource.addRecipes(TestData.recipeDetails01);

        // Act (When)
        String recipeId = TestData.recipeDetails01.getRecipeId();
        Bundle args = new RecipeDetailsFragmentArgs.Builder(recipeId).build().toBundle();
        FragmentScenario.launchInContainer(RecipeDetailsFragment.class, args, R.style.AppTheme);

        // Assert (Then)
        onView(withId(R.id.recipe_image)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_title)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_social_score)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredients_title)).check(matches(isDisplayed()));
        onView(withId(R.id.ingredients_container)).check(matches(isDisplayed()));
    }

}