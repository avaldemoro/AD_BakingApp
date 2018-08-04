package co.asterv.ad_bakingapp;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import co.asterv.ad_bakingapp.model.Recipe;
import co.asterv.ad_bakingapp.model.Step;
import co.asterv.ad_bakingapp.utils.Constant;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnRecipeSelectedListener, RecipeDetailFragment.OnStepSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        Bundle bundle = new Bundle();
        RecipeListFragment listFragment = new RecipeListFragment ();
        listFragment.setArguments (bundle);

        getSupportFragmentManager ().beginTransaction ()
                .replace (R.id.frame_container, listFragment)
                .commit ();
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable (Constant.RECIPE_KEY, recipe);
        RecipeDetailFragment fragment = new RecipeDetailFragment ();
        fragment.setArguments (bundle);

        getSupportFragmentManager ().addOnBackStackChangedListener (this::onBackStackChanged);
        getSupportFragmentManager ().beginTransaction ()
                .replace (R.id.frame_container, fragment)
                .addToBackStack (null)
                .commit ();
    }

    @Override
    public void onStepSelected(List<Step> steps, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList (Constant.STEPS_KEY,(ArrayList<? extends Parcelable>) steps);
        bundle.putParcelable (Constant.STEP_KEY, steps.get(position));

        RecipeInstructionFragment fragment = new RecipeInstructionFragment ();
        fragment.setArguments (bundle);

        getSupportFragmentManager ().beginTransaction ()
                .replace (R.id.frame_container, fragment)
                .addToBackStack (null)
                .commit ();
    }

    /***
     * Thanks to https://stackoverflow.com/questions/13086840/actionbar-up-navigation-with-fragments
     * for the Up Button methods
     ***/

    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canBack = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canBack);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }
}