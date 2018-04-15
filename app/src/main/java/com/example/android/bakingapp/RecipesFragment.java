package com.example.android.bakingapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.android.bakingapp.app.AppController;
import com.example.android.bakingapp.objects.Ingredient;
import com.example.android.bakingapp.objects.Recipe;
import com.example.android.bakingapp.objects.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecipesFragment is a fragment displaying a recipe list
 * It will be displayed in the RecipesActivity
 * Here we also query server and get recipe information
 */
public class RecipesFragment extends Fragment {

    private static final String TAG = RecipesFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RecipeAdapter recipeAdapter;

    private List<Recipe> recipes;

    private Parcelable layoutManagerSavedState;

    public RecipesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            // Restores the state of the LayoutManager
            layoutManagerSavedState = savedInstanceState
                    .getParcelable("RecyclerViewLayoutManager");
        }

        // Inflate the RecipesFragment layout
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);

        // If it is portrait layout for tablet
        if (getActivity().getResources().getBoolean(R.bool.two_pane)
                && getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {

            // Create GridLayout with 2 columns
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2,
                    LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);

            // If it is landscape layout for tablet
        } else if (getActivity().getResources().getBoolean(R.bool.two_pane)
                && getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {

            // Create GridLayout with 3 columns
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3,
                    LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(gridLayoutManager);

            // If it is layout for phone
        } else if (!getActivity().getResources().getBoolean(R.bool.two_pane)) {

            // Create LinearLayout
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }

        recyclerView.setHasFixedSize(true);

        recipeAdapter = new RecipeAdapter(getContext(), recipes);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Set RecipeAdapter on the RecyclerView
        recyclerView.setAdapter(recipeAdapter);

        return rootView;
    }

    // Get and parse Json for recipe information
    // Here Volley library is used and its JsonArrayRequest
    public void getRecipeListFromJson() {

        String tagJsonRecipeListArrayRequest = "json_recipe_list_array_request";

        JsonArrayRequest request = new JsonArrayRequest(
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d(TAG, response.toString());

                        // Create an empty array list that recipes will be added to
                        List<Recipe> recipes = new ArrayList<>();

                        try {
                            // Loop through each json object
                            for (int i = 0; i < response.length(); i++) {

                                // Get JSONObject which represent a recipe
                                JSONObject recipe = (JSONObject) response.get(i);

                                // From this object get needed information:
                                // recipeId, recipeName and recipeImage
                                int recipeId = recipe.getInt("id");
                                String recipeName = recipe.getString("name");
                                String recipeImage = recipe.getString("image");
                                int recipeServings = recipe.getInt("servings");

                                // Get ingredients array
                                JSONArray ingredientsArray = recipe
                                        .getJSONArray("ingredients");

                                // Create an empty array list that ingredients will be added to
                                List<Ingredient> ingredients = new ArrayList<>();

                                for (int a = 0; a < ingredientsArray.length(); a++) {

                                    // Get JSONObject which represent an ingredient
                                    JSONObject ingredient = (JSONObject) ingredientsArray.get(a);

                                    // From this object get needed information:
                                    // ingredientQuantity, ingredientName and ingredientMeasure
                                    double ingredientQuantity =
                                            ingredient.getDouble("quantity");
                                    String ingredientName =
                                            ingredient.getString("ingredient");
                                    String ingredientMeasure =
                                            ingredient.getString("measure");

                                    // Create a new Ingredient object
                                    Ingredient newIngredient =
                                            new Ingredient(ingredientQuantity,
                                                    ingredientName, ingredientMeasure);
                                    // Add the new Ingredient object to the list
                                    ingredients.add(newIngredient);
                                }

                                // Get steps array
                                JSONArray stepArray = recipe.getJSONArray("steps");

                                // Creates an empty array list that videos will be added to
                                List<Step> steps = new ArrayList<>();

                                for (int b = 0; b < stepArray.length(); b++) {

                                    // Get JSONObject which represent a step
                                    JSONObject step = (JSONObject) stepArray.get(b);

                                    // From this object get needed information:
                                    // stepId, stepShortDescription, stepDescription,
                                    // stepVideoUrl and stepThumbnailUrl
                                    int stepId = step.getInt("id");
                                    String stepShortDescription =
                                            step.getString("shortDescription");
                                    String stepDescription = step.getString("description");
                                    String stepVideoUrl = step.getString("videoURL");
                                    String stepThumbnailUrl = step.getString("thumbnailURL");

                                    // Create a new Step object
                                    Step newStep = new Step(stepId, stepShortDescription,
                                            stepDescription, stepVideoUrl, stepThumbnailUrl);

                                    // Add the new step object to the list
                                    steps.add(newStep);
                                }

                                // Create a new Recipe object with the recipeId, recipeName,
                                // recipeImage, recipeServings, ingredients, steps
                                Recipe newRecipe = new Recipe(recipeId, recipeName, recipeImage,
                                        recipeServings, ingredients, steps);

                                // Add the new Recipe to the list of Recipes.
                                recipes.add(newRecipe);
                            }

                            // Pass the list of Recipe objects to the RecipeAdapter
                            recipeAdapter.setRecipes(recipes);

                            recyclerView.getLayoutManager()
                                    .onRestoreInstanceState(layoutManagerSavedState);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, " + e.getMessage()) " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request, tagJsonRecipeListArrayRequest);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Call getRecipeListFromJson method which queries the server and gets Recipe data
        getRecipeListFromJson();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("RecyclerViewLayoutManager",
                recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }
}
