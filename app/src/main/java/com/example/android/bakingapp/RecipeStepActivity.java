package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.objects.Recipe;
import com.example.android.bakingapp.utils.TestingUtils;

public class RecipeStepActivity extends AppCompatActivity
        implements RecipeStepFragment.PreviousNextRecipeListener {

    private Recipe currentRecipe;
    int stepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        // If savedInstanceState is null
        if (savedInstanceState == null) {

            // Create a new RecipeStepFragment
            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

            // Get the intent that opened this activity
            Intent intentThatOpenedThisActivity = getIntent();
            // If the intent has extra with the "currentRecipe" key
            if (intentThatOpenedThisActivity.hasExtra("currentRecipe")) {
                // Get current recipe
                currentRecipe = intentThatOpenedThisActivity
                        .getParcelableExtra("currentRecipe");
                // If the intent has extra with the "stepId" key
                if (intentThatOpenedThisActivity.hasExtra("stepId")) {
                    // Get step id
                    stepId = intentThatOpenedThisActivity
                            .getIntExtra("stepId", 0);
                }
            // If the intent does not have extra with the "currentRecipe" key
            } else {

                // It means that this is a test, so provide recipe object for testing
                currentRecipe = TestingUtils.getRecipeForTesting(getApplicationContext());
            }

            // Create anew bundle
            Bundle fragmentArguments = new Bundle();
            // Put stepId
            fragmentArguments.putInt("stepId", stepId);
            // Put currentRecipe
            fragmentArguments.putParcelable("currentRecipe", currentRecipe);
            // Set fragment arguments
            recipeStepFragment.setArguments(fragmentArguments);

            // Get FragmentManager
            FragmentManager fragmentManager = getSupportFragmentManager();
            // Add fragment to the layout
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_fragment_container, recipeStepFragment)
                    .commit();
        }
    }

    // This method helps to handle navigation to previous and next steps
    // It passes stepId to the RecipeStepActivity
    @Override
    public void OnPreviousNextRecipeSelected(int stepId) {

        // Create a new RecipeStepFragment
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

        // Get the intent that opened this activity
        Intent intentThatOpenedThisActivity = getIntent();
        // If the intent has extra with the "currentRecipe" key
        if (intentThatOpenedThisActivity.hasExtra("currentRecipe")) {
            // Get current recipe
            currentRecipe = intentThatOpenedThisActivity.getParcelableExtra("currentRecipe");
        // If the intent does not have extra with the "currentRecipe" key
        } else {

            // It means that this is a test, so provide recipe object for testing
            currentRecipe = TestingUtils.getRecipeForTesting(getApplicationContext());
        }

        // Create a new bundle
        Bundle fragmentArguments = new Bundle();
        // Put stepId
        fragmentArguments.putInt("stepId", stepId);
        // Put currentRecipe
        fragmentArguments.putParcelable("currentRecipe", currentRecipe);
        // Set fragment arguments
        recipeStepFragment.setArguments(fragmentArguments);

        // Get FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Add fragment to the layout
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_step_fragment_container, recipeStepFragment)
                .commit();
    }
}
