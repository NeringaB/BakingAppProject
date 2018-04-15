package com.example.android.bakingapp.utils;

import android.content.Context;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.objects.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class TestingUtils {

    // This method is only for testing
    static public Recipe getRecipeForTesting(Context context) {
        // Get current recipe json string for testing
        String nutellaPieObjectString = context.getString(R.string.current_recipe_for_testing);
        // Create and return Recipe object
        Type type = new TypeToken<Recipe>(){}.getType();
        return new Gson().fromJson(nutellaPieObjectString, type);
    }
}
