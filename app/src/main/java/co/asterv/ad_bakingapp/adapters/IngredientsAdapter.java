package co.asterv.ad_bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import co.asterv.ad_bakingapp.R;
import co.asterv.ad_bakingapp.model.Ingredient;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private Context context;
    private static List<Ingredient> ingredients;
    private TextView ingredientQuantityTV;
    private TextView ingredientNameTV;
    private TextView ingredientMeasureTV;

    public IngredientsAdapter(List<Ingredient> ingredients, Context context) {
        this.context = context;
        this.ingredients = ingredients;
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
        int quantity = (int) Math.round(ingredients.get (position).getIngredientsQuantity ());
        String measureType = ingredients.get(position).getIngredientsMeasureType ();
        String ingredientName = ingredients.get(position).getIngredientsName ();

        holder.ingredientQuantityTV.setText (String.valueOf (quantity));
        holder.ingredientMeasureTV.setText (measureType);
        holder.ingredientNameTV.setText (ingredientName);
    }

    @Override
    public int getItemCount() {
        if (ingredients == null || ingredients.size () == 0) {
            return -1;
        }
        return ingredients.size ();
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
