package co.asterv.ad_bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import java.util.List;
import co.asterv.ad_bakingapp.R;
import co.asterv.ad_bakingapp.model.Ingredient;

public class IngredientsListService extends RemoteViewsService {
    public ListViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewsFactory (this.getApplicationContext ());
    }
}

class ListViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<Ingredient> ingredientList;

    public ListViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() { }

    //Very Important,this is the place where the data is being changed each time by the adapter.
    @Override
    public void onDataSetChanged() {
        ingredientList = RecipeWidgetProvider.mIngredientsList;
    }

    @Override
    public void onDestroy() { }

    @Override
    public int getCount() {
        if (ingredientList == null || ingredientList.size () == 0) {
            return -1;
        }
        return ingredientList.size ();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_list_item);
        views.setTextViewText (R.id.widgetIngredientText,
                String.valueOf(ingredientList.get(position).getIngredientsQuantity ()) + " " +
                String.valueOf (ingredientList.get(position).getIngredientsMeasureType ()) + " " +
                String.valueOf(ingredientList.get(position).getIngredientsName ()));
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

