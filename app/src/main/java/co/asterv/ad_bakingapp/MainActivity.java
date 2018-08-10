package co.asterv.ad_bakingapp;

import android.os.Parcelable;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import co.asterv.ad_bakingapp.model.Recipe;
import co.asterv.ad_bakingapp.model.Step;
import co.asterv.ad_bakingapp.utils.Constant;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnRecipeSelectedListener, RecipeDetailFragment.OnStepSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        // Check if online
        if (isOnline ()) {
            Bundle bundle = new Bundle();
            RecipeListFragment listFragment = new RecipeListFragment ();
            listFragment.setArguments (bundle);

            getSupportFragmentManager ().beginTransaction ()
                    .replace (R.id.frame_container, listFragment)
                    .commit ();
        } else {
            Toast.makeText(getApplicationContext(), Constant.NO_INTERNET_TEXT, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        // Check if online
        if (isOnline ()) {
            Bundle bundle = new Bundle();
            bundle.putParcelable (Constant.RECIPE_KEY, recipe);
            RecipeDetailFragment fragment = new RecipeDetailFragment ();
            fragment.setArguments (bundle);

            getSupportFragmentManager ().addOnBackStackChangedListener (this::onBackStackChanged);
            getSupportFragmentManager ().beginTransaction ()
                    .replace (R.id.frame_container, fragment)
                    .addToBackStack (null)
                    .commit ();
        } else {
            Toast.makeText(getApplicationContext(), Constant.NO_INTERNET_TEXT, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStepSelected(List<Step> steps, int position) {
        // Check if online
        if (isOnline ()) {
            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            if (tabletSize) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList (Constant.STEPS_KEY,(ArrayList<? extends Parcelable>) steps);
                bundle.putParcelable (Constant.STEP_KEY, steps.get(position));

                RecipeInstructionFragment fragment = new RecipeInstructionFragment ();
                fragment.setArguments (bundle);

                getSupportFragmentManager ().beginTransaction ()
                        .replace (R.id.recipe_detail_container, fragment)
                        .addToBackStack (null)
                        .commit ();
            } else {
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
        } else {
            Toast.makeText(getApplicationContext(), Constant.NO_INTERNET_TEXT, Toast.LENGTH_LONG).show();
        }
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

    /*** CHECKS IF ONLINE
     * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out ***/
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec(Constant.INTERNET_CHECK_COMMAND);
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}