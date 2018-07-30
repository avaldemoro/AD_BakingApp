package co.asterv.ad_bakingapp;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import co.asterv.ad_bakingapp.model.Recipe;
import co.asterv.ad_bakingapp.utils.Constant;

public class RecipeDetailFragment extends Fragment{
    private Recipe recipe;

    //public RecipeDetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        recipe = (Recipe) this.getArguments().getParcelable (Constant.RECIPE_KEY);
        Log.e("Recipe Detail Fragment1", String.valueOf(recipe.getRecipeName ()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        TextView tvRecipeTitle = view.findViewById (R.id.recipeTitle);
        tvRecipeTitle.setText (recipe.getRecipeName ());

        // Return view
        return view;
    }

    public static RecipeDetailFragment newInstance(Recipe recipe1) {
        RecipeDetailFragment fragment = new RecipeDetailFragment ();
        Bundle args = new Bundle();
        args.putParcelable (Constant.RECIPE_KEY, recipe1);
        Log.e("Recipe Detail Fragment", String.valueOf(recipe1.getRecipeName ()));
        fragment.setArguments (args);
        return fragment;
    }
}
