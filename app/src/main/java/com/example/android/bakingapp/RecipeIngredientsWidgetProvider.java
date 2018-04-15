package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of Recipe Ingredients Widget functionality.
 */
public class RecipeIngredientsWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.recipe_ingredients_widget_list_view);

        // Set the ListWidgetService intent to act as the adapter for the ListView
        Intent serviceIntent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.recipe_ingredients_widget_list_view, serviceIntent);

        // Set the RecipeDetailActivity intent to launch when clicked
        Intent openDetailActivityIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0,
                openDetailActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.recipe_ingredients_widget_list_view, appPendingIntent);

        // Handle empty ingredients list
        views.setEmptyView(R.id.recipe_ingredients_widget_list_view, R.id.empty_view);

        // Update App Widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Start the intent service update widget action,
        // the service takes care of updating the widget's UI
        RecipeIngredientsService.startActionUpdateIngredientsWidgets(context);
    }

    public static void updateIngredientsWidgets(Context context, AppWidgetManager appWidgetManager,
                                                int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
