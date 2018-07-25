package co.asterv.ad_bakingapp;

import android.content.ClipData;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import co.asterv.ad_bakingapp.model.Recipe;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnRecipeClickListener {
    RecipeDetailFragment recipeDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
    }

    @Override
    public void onRecipeSelected(int position) {
        Log.e("TEXT", String.valueOf(position));
        // Fetch the item to display from bundle

    }
}
