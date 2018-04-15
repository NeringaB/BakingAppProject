package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.data.RecipeContract;
import com.example.android.bakingapp.data.RecipeProvider;
import com.example.android.bakingapp.objects.Ingredient;
import com.example.android.bakingapp.objects.Recipe;
import com.example.android.bakingapp.utils.FormattingUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Cursor cursor;

    public ListRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        // Get recipes from database
        if (cursor != null) cursor.close();
        cursor = context.getContentResolver().query(
                RecipeProvider.Recipes.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        // If cursor is null and it has 0 rows, return null
        if (cursor == null || cursor.getCount() == 0) return null;

        // Otherwise, move cursor to position of the view
        cursor.moveToPosition(position);
        // Get COLUMN_RECIPE_GSON_STRING column index
        int idIndex = cursor.getColumnIndex(RecipeContract.COLUMN_RECIPE_GSON_STRING);
        // Get recipe Json string
        String recipeJsonString = cursor.getString(idIndex);
        Type type = new TypeToken<Recipe>(){}.getType();
        // Create Recipe from the string
        Recipe recipe = new Gson().fromJson(recipeJsonString, type);

        // Get recipe name
        String recipeName = recipe.getRecipeName();

        // Get recipe ingredients
        List<Ingredient> ingredients = recipe.getIngredients();

        // Format recipe ingredients list
        String formattedIngredientsList = FormattingUtils.getFormattedIngredientList(ingredients);

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.recipe_ingredients_widget_provider);

        // Display recipe name
        views.setTextViewText(R.id.widget_recipe_name, recipeName);

        // Display formatted ingredients list string
        views.setTextViewText(R.id.widget_text, formattedIngredientsList);

        // Create new intent
        Intent fillInIntent = new Intent();
        // Put current recipe as extra
        fillInIntent.putExtra("currentRecipe", recipe);
        // Set filling intent
        views.setOnClickFillInIntent(R.id.widget_linear_layout, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
