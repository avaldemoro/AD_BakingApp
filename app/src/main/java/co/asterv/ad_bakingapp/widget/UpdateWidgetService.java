package co.asterv.ad_bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import co.asterv.ad_bakingapp.model.Ingredient;
import co.asterv.ad_bakingapp.model.Recipe;
import co.asterv.ad_bakingapp.utils.Constant;

public class UpdateWidgetService extends IntentService {
    public static final String WIDGET_UDPATE_ACTION = "co.asterv.ad_bakingapp.update_widget";
    private List<Ingredient> ingredientsList;
    private String recipeName;

    public UpdateWidgetService() {
        super ("UpdateWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            final String action = intent.getAction ();
            if (WIDGET_UDPATE_ACTION.equals (action)) {
                Bundle bundle = intent.getBundleExtra (Constant.BUNDLE_KEY);
                ingredientsList = bundle.getParcelableArrayList (Constant.GET_RECIPE_INFO_KEY);
                recipeName = bundle.getString (Constant.NAME_KEY);
            }
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance (this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds (new ComponentName (this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateAppWidget (this, appWidgetManager, appWidgetIds, recipeName, ingredientsList);
    }
    public static void startUpdateWidgetService(Context context, Recipe recipe) {
        Intent intent = new Intent(context, UpdateWidgetService.class);
        Bundle bundle = new Bundle();
        bundle.putString (Constant.NAME_KEY, (String) String.valueOf (recipe.getRecipeName ()));
        bundle.putParcelableArrayList (Constant.GET_RECIPE_INFO_KEY, (ArrayList<? extends Parcelable>) recipe.getIngredients ());
        intent.putExtra (Constant.BUNDLE_KEY, bundle);
        intent.setAction(WIDGET_UDPATE_ACTION);
        context.startService(intent);
    }
}
