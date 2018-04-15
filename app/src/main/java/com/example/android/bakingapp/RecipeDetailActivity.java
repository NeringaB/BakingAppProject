package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.android.bakingapp.objects.Recipe;
import com.example.android.bakingapp.utils.TestingUtils;

import butterknife.BindView;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeDetailFragment.OnStepSelectedClickListener,
        RecipeStepFragment.PreviousNextRecipeListener {

    @Nullable
    @BindView(R.id.recipe_detail_linear_layout)
    LinearLayout recipeDetailLinearLayout;

    private Recipe currentRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Get the intent that opened this activity
        Intent intentThatOpenedThisActivity = getIntent();
        // If intent has currentRecipe
        if (intentThatOpenedThisActivity.hasExtra("currentRecipe")) {
            // Get current recipe
            currentRecipe = intentThatOpenedThisActivity.getParcelableExtra("currentRecipe");
        } else {
            // If the intent that opened this activity does not have "currentRecipe" extra,
            // it means that this is a test, so provide recipe object for testing
            currentRecipe = TestingUtils.getRecipeForTesting(getApplicationContext());
        }

        if (savedInstanceState == null) {

            // Get fragment manager
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Create a new RecipeDetailFragment
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            // If currentRecipe is not null
            if (currentRecipe != null) {
                // Create a new bundle
                Bundle detailFragmentArguments = new Bundle();
                // Add currentRecipe to the bundle
                detailFragmentArguments.putParcelable("currentRecipe", currentRecipe);
                // Set fragment arguments
                recipeDetailFragment.setArguments(detailFragmentArguments);
            }

            // Use fragment manager to add RecipeDetailFragment
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_fragment_container, recipeDetailFragment)
                    .commit();

            // Determine if it is a two-pane or single-pane layout
            if (getResources().getBoolean(R.bool.two_pane)) {
                // It is a two-pane layout so create and add RecipeStepFragment

                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                // If the intent that opened this activity has currentRecipe
                if (currentRecipe != null) {
                    // Create a new bundle
                    Bundle stepFragmentArguments = new Bundle();
                    // Add currentRecipe to the bundle
                    stepFragmentArguments.putParcelable("currentRecipe", currentRecipe);
                    // Set fragment arguments
                    recipeStepFragment.setArguments(stepFragmentArguments);
                }

                // Use fragment manager to add RecipeStepFragment
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_step_fragment_container, recipeStepFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onStepSelected(int stepId) {

        // Create new RecipeStepFragment
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

        // Create a new bundle
        Bundle fragmentArguments = new Bundle();
        // Add currentRecipe to the bundle
        fragmentArguments.putParcelable("currentRecipe", currentRecipe);
        // Add stepId to the bundle
        fragmentArguments.putInt("stepId", stepId);
        // Set fragment arguments
        recipeStepFragment.setArguments(fragmentArguments);

        // Get FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Use fragment manager to add RecipeStepFragment
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_step_fragment_container, recipeStepFragment)
                .commit();
    }

    @Override
    public void OnPreviousNextRecipeSelected(int stepId) {

        // Create new RecipeStepFragment
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

        // Create a new bundle
        Bundle fragmentArguments = new Bundle();
        // Add currentRecipe to the bundle
        fragmentArguments.putParcelable("currentRecipe", this.currentRecipe);
        // Add stepId to the bundle
        fragmentArguments.putInt("stepId", stepId);
        // Set fragment arguments
        recipeStepFragment.setArguments(fragmentArguments);

        // Get FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Use fragment manager to add RecipeStepFragment
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_step_fragment_container, recipeStepFragment)
                .commit();
    }
}
