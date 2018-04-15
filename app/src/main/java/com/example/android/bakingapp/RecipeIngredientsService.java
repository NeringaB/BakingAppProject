package com.example.android.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * An {@link IntentService} subclass for handling asynchronous
 * task requests in a service on a separate handler thread.
 */
public class RecipeIngredientsService extends IntentService {

    // Actions that RecipeIngredientsService executes
    public static final String ACTION_UPDATE_INGREDIENTS_WIDGETS =
            "com.example.android.bakingapp.action.update_ingredients_widgets";

    public RecipeIngredientsService() {
        super("RecipeIngredientsService");
    }

    // Start this service to perform UpdateIngredientsWidgets action with the given parameters.
    // If the service is already performing a task this action will be queued.
    public static void startActionUpdateIngredientsWidgets(Context context) {
        Intent intent = new Intent(context, RecipeIngredientsService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENTS_WIDGETS);
        context.startService(intent);
    }

    // Handle intent by redirecting which method should be executed
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_INGREDIENTS_WIDGETS.equals(action)) {
                handleActionUpdateIngredientsWidgets(getApplicationContext());
            }
        }
    }

    // Handle action UpdatePlantWidgets in the provided background thread
    private void handleActionUpdateIngredientsWidgets(Context context) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds
                (new ComponentName(this, RecipeIngredientsWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged
                (appWidgetIds, R.id.recipe_ingredients_widget_list_view);

        RecipeIngredientsWidgetProvider.updateIngredientsWidgets
                (this, appWidgetManager, appWidgetIds);
    }
}
