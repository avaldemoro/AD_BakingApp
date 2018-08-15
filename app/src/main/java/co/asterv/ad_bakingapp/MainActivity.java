package co.asterv.ad_bakingapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

        if (savedInstanceState == null) {
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
        shouldDisplayHomeUp ();
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

    /*** CREDIT TO: https://medium.com/@bherbst/managing-the-fragment-back-stack-373e87e4ff62
     * for managing fragment backstack
     * ***/

    @Override
    public void onStepSelected(List<Step> steps, int position) {
        // Check if online
        if (isOnline ()) {
            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

            if (tabletSize) {
                openFragment (R.id.recipe_detail_container, steps, position);
            } else {
                openFragment (R.id.frame_container, steps, position);
            }

        } else {
            Toast.makeText(getApplicationContext(), Constant.NO_INTERNET_TEXT, Toast.LENGTH_LONG).show();
        }
        shouldDisplayHomeUp ();
    }

    public void openFragment(int container, List<Step> steps, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList (Constant.STEPS_KEY,(ArrayList<? extends Parcelable>) steps);
        bundle.putParcelable (Constant.STEP_KEY, steps.get(position));

        RecipeInstructionFragment fragment = new RecipeInstructionFragment ();
        fragment.setArguments (bundle);
        getSupportFragmentManager ().popBackStack("BACK_STACK_ROOT_TAG", getSupportFragmentManager ().POP_BACK_STACK_INCLUSIVE);

        getSupportFragmentManager ().beginTransaction ()
                .replace (container, fragment)
                .addToBackStack ("BACK_STACK_ROOT_TAG")
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

    /*** CHECKS IF ONLINE
     * https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out ***/
    public static boolean isOnline() {
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