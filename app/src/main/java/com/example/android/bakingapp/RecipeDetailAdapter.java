package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.objects.Ingredient;
import com.example.android.bakingapp.objects.Recipe;
import com.example.android.bakingapp.objects.Step;
import com.example.android.bakingapp.utils.FormattingUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecipeAdapter is responsible for populating recipe list
 * in the RecipesFragment (RecipeActivity) with recipes
 */
public class RecipeDetailAdapter extends
        RecyclerView.Adapter<RecipeDetailAdapter.IngredientsAndStepAdapterViewHolder> {

    private static final int VIEW_TYPE_INGREDIENTS = 0;
    private static final int VIEW_TYPE_RECIPE_STEP = 1;

    private Context context;

    private Recipe currentRecipe;
    private List<Step> recipeSteps;

    private final RecipeStepOnClickHandler clickHandler;

    // RecipeStepOnClickHandler interface that handles clicks
    public interface RecipeStepOnClickHandler {
        void onClick(int stepId);
    }

    public RecipeDetailAdapter(@NonNull Context context, Recipe currentRecipe,
                               List<Step> recipeSteps, RecipeStepOnClickHandler clickHandler) {
        this.context = context;
        this.currentRecipe = currentRecipe;
        this.recipeSteps = recipeSteps;
        this.clickHandler = clickHandler;
    }

    @Override
    public IngredientsAndStepAdapterViewHolder onCreateViewHolder
            (ViewGroup viewGroup, int viewType) {

        int layoutId;

        switch (viewType) {

            // If it is VIEW_TYPE_INGREDIENTS, inflate layout for ingredients
            case VIEW_TYPE_INGREDIENTS: {
                layoutId = R.layout.ingredients_list_item;
                break;
            }

            // If it is VIEW_TYPE_RECIPE_STEP, inflate layout for a step
            case VIEW_TYPE_RECIPE_STEP: {
                layoutId = R.layout.step_list_item;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);

        view.setFocusable(true);

        return new IngredientsAndStepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final IngredientsAndStepAdapterViewHolder viewHolder, int position) {

        // Get all recipe information that will be displayed
        int recipeId = currentRecipe.getRecipeId() - 1;
        String recipeImage = currentRecipe.getRecipeImage();
        String recipeName = currentRecipe.getRecipeName();
        int recipeServings = currentRecipe.getRecipeServings();
        List<Ingredient> recipeIngredients = currentRecipe.getIngredients();
        recipeSteps = currentRecipe.getSteps();

        // If it is VIEW_TYPE_INGREDIENTS
        if (getItemViewType(position) == VIEW_TYPE_INGREDIENTS) {

            // Disable item that it would not be clickable
            viewHolder.ingredientsLinearLayout.setEnabled(false);

            // If recipeImage is not null and is not empty
            if (recipeImage != null && !recipeImage.equals("")) {
                // Show the recipe image
                Picasso.with(context).load(recipeImage).fit()
                        .centerCrop().into(viewHolder.recipeImageView);
            // If recipeImage is null or empty
            } else {
                // Get recipe image resource id for an image in drawables folder
                viewHolder.recipeImageView.setImageResource
                        (FormattingUtils.getRecipeImageResource(recipeId));
            }

            // Display recipe name
            viewHolder.nameTextView.setText(recipeName);

            // Display recipe servings
            String recipeServingsString = " (" + recipeServings + " servings)";
            viewHolder.servingsTextView.setText(recipeServingsString);

            // Get formatted ingredients list string
            String formattedIngredientsList = FormattingUtils
                    .getFormattedIngredientList(recipeIngredients);

            // Display formatted ingredients list string
            viewHolder.ingredientsTextView.setText(formattedIngredientsList);
        }

        // If it is VIEW_TYPE_RECIPE_STEP
        if (getItemViewType(position) == VIEW_TYPE_RECIPE_STEP) {

            // Get step short description
            String recipeStepShortDescription
                    = recipeSteps.get(position - 1).getStepShortDescription();
            // Display step short description
            viewHolder.stepTextView.setText(recipeStepShortDescription);
        }
    }

    @Override
    public int getItemCount() {
        if (null == recipeSteps) return 1;
        return recipeSteps.size() + 1;
    }

    public class IngredientsAndStepAdapterViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable
        @BindView(R.id.ingredients_linear_layout)
        LinearLayout ingredientsLinearLayout;

        @Nullable
        @BindView(R.id.iv_recipe_image)
        ImageView recipeImageView;

        @Nullable
        @BindView(R.id.tv_recipe_name)
        TextView nameTextView;

        @Nullable
        @BindView(R.id.tv_recipe_servings)
        TextView servingsTextView;

        @Nullable
        @BindView(R.id.tv_ingredients)
        TextView ingredientsTextView;

        @Nullable
        @BindView(R.id.tv_step)
        TextView stepTextView;

        public IngredientsAndStepAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            // Get adapter position which will help to indicate which step
            // the user has clicked on
            int position = getAdapterPosition() - 1;

            // If it is layout for tablet
            if (context.getResources().getBoolean(R.bool.two_pane)) {

                // Pass adapter position through the OnClickHandler
                // Because we do not need to open new activity
                clickHandler.onClick(position);

            // If it is layout for phone
            } else {

                // Create a new intent to open RecipeStepActivity
                Intent intentToOpenRecipeStepActivity = new Intent(context,
                        RecipeStepActivity.class);
                // Put adapter position and currentRecipe as extras
                intentToOpenRecipeStepActivity.putExtra("stepId", position);
                intentToOpenRecipeStepActivity.putExtra("currentRecipe", currentRecipe);
                // Start new activity
                context.startActivity(intentToOpenRecipeStepActivity);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        // If it is an item in the position 0
        if (position == 0) {
            // It is an ingredient item
            return VIEW_TYPE_INGREDIENTS;
        } else {
            // Otherwise, it is recipe step item
            return VIEW_TYPE_RECIPE_STEP;
        }
    }
}