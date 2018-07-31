package co.asterv.ad_bakingapp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import co.asterv.ad_bakingapp.model.Recipe;
import co.asterv.ad_bakingapp.utils.Constant;

public class RecipeDetailFragment extends Fragment{
    private Recipe recipe;

    public RecipeDetailFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        recipe = getArguments().getParcelable (Constant.RECIPE_KEY);
        Log.e("Recipe Detail Fragment1", String.valueOf(recipe.getRecipeName ()));

        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle (Constant.DETAILS_TITLE);
        }
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

}
