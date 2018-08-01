package co.asterv.ad_bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import co.asterv.ad_bakingapp.R;
import co.asterv.ad_bakingapp.RecipeListFragment;
import co.asterv.ad_bakingapp.model.Recipe;

public class RecipeListNameAdapter extends RecyclerView.Adapter<RecipeListNameAdapter.ViewHolder> {
    private static Recipe[] recipes;
    private TextView recipeNameTV;
    private Context context;
    RecipeListFragment.OnRecipeSelectedListener listener;


    public RecipeListNameAdapter(Recipe[] recipes, Context context, RecipeListFragment.OnRecipeSelectedListener listener) {
        this.context = context;
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeListNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_name_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListNameAdapter.ViewHolder holder, final int position) {
        holder.recipeNameTV.setText (String.valueOf(recipes[position].getRecipeName ()));

        holder.itemView.setOnClickListener (v -> {
            if (null != listener) {
                listener.onRecipeSelected (recipes[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (recipes == null || recipes.length == 0) {
            return -1;
        }
        return recipes.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView recipeNameTV;
        public ViewHolder(ConstraintLayout itemView) {
            super (itemView);

            recipeNameTV = itemView.findViewById (R.id.recipeName);
        }
    }
}