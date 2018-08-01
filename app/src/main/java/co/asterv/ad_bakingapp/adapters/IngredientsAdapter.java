package co.asterv.ad_bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import co.asterv.ad_bakingapp.R;
import co.asterv.ad_bakingapp.model.Recipe;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private Context context;
    private static Recipe[] recipes;
    private TextView ingredientQuantityTV;
    private TextView ingredientNameTV;
    private TextView ingredientMeasureTV;

    public IngredientsAdapter(Recipe[] recipes, Context context) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext ())
                .inflate (R.layout.ingredient_list_item, parent, false);
        ViewHolder vh = new ViewHolder (v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdapter.ViewHolder holder, int position) {
        holder.ingredientQuantityTV.setText (String.valueOf (recipes[position].getIngredientsQuantity ()));
        holder.ingredientMeasureTV.setText (recipes[position].getIngredientsMeasureType ());
        holder.ingredientNameTV.setText (recipes[position].getIngredientsName ());
    }

    @Override
    public int getItemCount() {
        if (recipes == null || recipes.length == 0) {
            return -1;
        }
        return recipes.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientQuantityTV;
        TextView ingredientNameTV;
        TextView ingredientMeasureTV;

        public ViewHolder(ConstraintLayout itemView) {
            super (itemView);

            ingredientQuantityTV = itemView.findViewById (R.id.ingredientQuantityTextView);
            ingredientNameTV = itemView.findViewById (R.id.ingredientNameTextView);
            ingredientMeasureTV = itemView.findViewById (R.id.ingredientMeasureTextView);
        }
    }
}
