package co.asterv.ad_bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import java.util.List;
import co.asterv.ad_bakingapp.MainActivity;
import co.asterv.ad_bakingapp.R;
import co.asterv.ad_bakingapp.model.Ingredient;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    public static List<Ingredient> mIngredientsList;
    public static String mRecipeName;

    public RecipeWidgetProvider() { }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetIds[], String recipeName, List<Ingredient> ingredientList) {

        mIngredientsList = ingredientList;
        mRecipeName = recipeName;

        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, IngredientsListService.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
            views.setRemoteAdapter(R.id.appwidget_ingredientsList, intent);
            ComponentName component = new ComponentName(context, RecipeWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_ingredientsList);
            views.setTextViewText (R.id.appwidget_recipeTitle, recipeName);

            Intent openAppIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity (context, 0, openAppIntent, 0);
            views.setOnClickPendingIntent (R.id.appwidget_recipeTitle, pendingIntent);

            appWidgetManager.updateAppWidget(component, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate (context, appWidgetManager, appWidgetIds);
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