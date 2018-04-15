package com.example.android.bakingapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.bakingapp.data.RecipeContract;
import com.example.android.bakingapp.data.RecipeProvider;
import com.example.android.bakingapp.objects.Recipe;
import com.example.android.bakingapp.utils.FormattingUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecipeAdapter is responsible for populating recipe list
 * in the RecipesFragment (RecipesActivity) with recipes
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private List<Recipe> recipes;
    private Context context;
    private Toast toast;

    public RecipeAdapter(@NonNull Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recipe_card_list_item, viewGroup, false);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeAdapterViewHolder viewHolder, int position) {

        // Get current recipe
        final Recipe currentRecipe = recipes.get(position);

        // Get recipe image url
        String recipeImageString = currentRecipe.getRecipeImage();
        // Check if an image url is not null and is not empty
        if (recipeImageString != null && !TextUtils.isEmpty(recipeImageString)) {
            // If we have an image url, use it to show the recipe image
            Picasso.with(context).load(recipeImageString).fit()
                    .centerCrop().into(viewHolder.recipeImageView);
        } else {
            // If we do not have an image url, get recipe image resource id
            // to display a corresponding image from the drawables folder
            viewHolder.recipeImageView.setImageResource
                    (FormattingUtils.getRecipeImageResource(position));
        }

        // Get recipe name
        final String recipeName = currentRecipe.getRecipeName();
        // Display recipe name
        viewHolder.nameTextView.setText(recipeName);

        // Set OnClickListener on the TextView that says "COOK"
        viewHolder.cookTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // onClick create new intent to open DetailActivity
                Intent intentToOpenRecipeDetailActivity = new Intent(context,
                        RecipeDetailActivity.class);
                // Pass currentRecipe as an extra
                intentToOpenRecipeDetailActivity.putExtra("currentRecipe", currentRecipe);
                context.startActivity(intentToOpenRecipeDetailActivity);
            }
        });

        // Convert the Recipe object to a JSON string
        final String currentRecipeJsonString = new Gson().toJson(currentRecipe);

        // Query database and see if current Recipe exists in our database
        String[] detailMovieDataProjection = new String[]{
                RecipeContract.COLUMN_RECIPE_GSON_STRING,
        };
        String selection = RecipeContract.COLUMN_RECIPE_GSON_STRING + "=?";
        String[] selectionArgs = new String[]{currentRecipeJsonString};

        // Perform the query
        Cursor cursor = context.getContentResolver().query(
                RecipeProvider.Recipes.CONTENT_URI,
                detailMovieDataProjection,
                selection,
                selectionArgs,
                null);

        // Check if the cursor is null or empty
        if (null != cursor && cursor.getCount() != 0) {
            // If it is not null or empty, move cursor to the first row
            cursor.moveToFirst();
            // Get the index of the COLUMN_RECIPE_GSON_STRING of the favourite movies table
            int recipeGsonStringIdColumnIndex =
                    cursor.getColumnIndex(RecipeContract.COLUMN_RECIPE_GSON_STRING);
            // Get a recipe Json string from the recipes table
            String recipeJsonStringFromRecipesTable
                    = cursor.getString(recipeGsonStringIdColumnIndex);

            // Once more check that recipe Json string from the recipes table is equal
            // to the recipe Json string of the currently displayed recipe
            if (recipeJsonStringFromRecipesTable.equals(currentRecipeJsonString)) {
                // If equals, check the toggle button
                viewHolder.heartToggleButton.setChecked(true);
            }
            // Close the cursor to avoid memory leaks
            cursor.close();
        }

        // Set OnClickListener on the toggle button
        viewHolder.heartToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If the toggle button is checked
                if (viewHolder.heartToggleButton.isChecked()) {

                    // Add the recipe Json string to the recipes table
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(RecipeContract
                            .COLUMN_RECIPE_GSON_STRING, currentRecipeJsonString);
                    Uri uri = context.getContentResolver().insert
                            (RecipeProvider.Recipes.CONTENT_URI, contentValues);

                    // Update widget
                    RecipeIngredientsService.startActionUpdateIngredientsWidgets(context);

                    // If toast is not null
                    if (toast != null) {
                        // Cancel toast
                        toast.cancel();
                    }
                    // Inform the user with a toast that
                    // recipe ingredients were added to the widget
                    toast = Toast.makeText(context, recipeName
                            + " Ingredients Added to Widget", Toast.LENGTH_SHORT);
                    toast.show();
                } else {

                    // Creates selection and selection arguments
                    // to delete the Json string of the currently displayed recipe
                    String selection = RecipeContract.COLUMN_RECIPE_GSON_STRING + "=?";
                    String[] selectionArgs = new String[]{currentRecipeJsonString};

                    // Delete a single row of data using a ContentResolver
                    context.getContentResolver().delete(
                            RecipeProvider.Recipes.CONTENT_URI,
                            selection,
                            selectionArgs);

                    // Update widget
                    RecipeIngredientsService.startActionUpdateIngredientsWidgets(context);

                    // If toast is not null
                    if (toast != null) {
                        // Cancel toast
                        toast.cancel();
                    }
                    // Inform the user with a toast that
                    // recipe ingredients were added to the widget
                    toast = Toast.makeText(context, recipeName
                            + " Ingredients Removed from Widget", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (null == recipes) return 0;
        return recipes.size();
    }

    // Used to refresh recipes
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_recipe_image)
        ImageView recipeImageView;

        @BindView(R.id.tv_recipe_name)
        TextView nameTextView;

        @BindView(R.id.tv_cook)
        TextView cookTextView;

        @BindView(R.id.tb_heart)
        ToggleButton heartToggleButton;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
