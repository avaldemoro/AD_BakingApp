package co.asterv.ad_bakingapp;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import co.asterv.ad_bakingapp.model.Recipe;

public class RecipeDetailFragment extends Fragment{
    private Recipe recipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipe = (Recipe) getArguments().getParcelable ("recipe");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        TextView recipeDetailNameTV = view.findViewById (R.id.recipeTitle);
        recipeDetailNameTV.setText (recipe.getRecipeName ());

        Log.e("TEXT", String.valueOf(recipeDetailNameTV.getText ()));

        // Return view
        return view;
    }
}
