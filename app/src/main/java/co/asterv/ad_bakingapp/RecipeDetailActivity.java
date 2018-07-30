package co.asterv.ad_bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import co.asterv.ad_bakingapp.model.Recipe;
import co.asterv.ad_bakingapp.utils.Constant;

public class RecipeDetailActivity extends AppCompatActivity {
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        //recipe = getIntent ().getParcelableExtra (Constant.RECIPE_KEY);
        RecipeDetailFragment fragment;

        if (savedInstanceState == null) {
            Bundle bundle = getIntent ().getExtras ();
            recipe = bundle.getParcelable (Constant.RECIPE_KEY);

            fragment = RecipeDetailFragment.newInstance (recipe);
            Log.e("Recipe Detail Activity", String.valueOf (recipe.getRecipeName ()));

            getSupportFragmentManager ().beginTransaction ()
                    .replace (R.id.recipeDetailContainer, fragment)
                    .commit ();
        }

    }
}
