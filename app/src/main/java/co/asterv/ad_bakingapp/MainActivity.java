package co.asterv.ad_bakingapp;

import android.content.ClipData;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import co.asterv.ad_bakingapp.model.Recipe;
import co.asterv.ad_bakingapp.utils.Constant;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnRecipeClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Bundle bundle = new Bundle();
        RecipeDetailFragment fragment = new RecipeDetailFragment ();

        bundle.putParcelable(Constant.RECIPE_KEY, recipe);
        fragment.setArguments (bundle);

        final Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtras(bundle);

        Log.e("Main Activity", String.valueOf (recipe.getRecipeName ()));

        startActivity(intent);
    }
}
