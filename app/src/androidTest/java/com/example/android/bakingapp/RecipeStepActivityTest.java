package com.example.android.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * This test demos a user clicking on the next step TextView
 * in RecipeStepActivity which replaces RecipeStepFragment with
 * the updated RecipeStepFragment displaying the next step (for phones)
 */
@RunWith(AndroidJUnit4.class)
public class RecipeStepActivityTest {

    // RecipeStepActivity will be tested
    @Rule
    public ActivityTestRule<RecipeStepActivity> activityTestRule =
            new ActivityTestRule<>(RecipeStepActivity.class);

    // Initialize fragments
    @Before
    public void initializeFragment(){
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
    }

    // Clicks on the next step TextView and checks if the previous step TextView exists
    // because if it exists, it means that the next (second) step is displayed
    @Test
    public void clickNextRecipeButton_NextRecipeStepIsDisplayed() {

        // Click on the next button
        onView((withId(R.id.tv_next))).perform(click());

        // Verify that the next recipe step is displayed which contains the text
        onView(withId(R.id.tv_previous)).check(matches(isDisplayed()));
    }
}
