package com.example.android.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
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
 * This test demos a user clicking on the first step in the LinearLayout item
 * in RecipeDetailActivity which opens up the corresponding RecipeStepActivity (for phones)
 */
@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    // RecipeDetailActivity will be tested
    @Rule
    public ActivityTestRule<RecipeDetailActivity> activityTestRule =
            new ActivityTestRule<>(RecipeDetailActivity.class);

    // Initialize fragments
    @Before
    public void initializeFragment(){
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
    }

    // Clicks on the first step item in the LinearLayout RecyclerView
    // and checks if RecipeStepActivity opens
    @Test
    public void clickRecyclerViewStepItem_OpensRecipeStepActivity() {

        // Click on the first recipe step item in a RecyclerView LinearLayout
        onView(withId(R.id.recycler_view_recipe_detail))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // Check if RecipeStepFragment is displayed
        onView(withId(R.id.recipe_step_fragment_linear_layout)).check(matches(isDisplayed()));
    }
}
