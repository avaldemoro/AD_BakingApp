package co.asterv.ad_bakingapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.asterv.ad_bakingapp.adapters.RecipeListNameAdapter;
import co.asterv.ad_bakingapp.model.Ingredient;
import co.asterv.ad_bakingapp.model.Recipe;
import co.asterv.ad_bakingapp.model.Step;
import co.asterv.ad_bakingapp.utils.Constant;
import co.asterv.ad_bakingapp.utils.JsonUtils;

public class RecipeListFragment extends Fragment {
    @BindView(R.id.recipeNameRecyclerView) RecyclerView mRecipeRecyclerView;
    private static RecyclerView.Adapter mRecipeAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Recipe[] recipes;
    private List<Ingredient> ingredientsList;
    private Ingredient[] ingredients;
    private List<Step> stepsList;
    private Step[] steps;
    OnRecipeSelectedListener mCallback;

    public interface OnRecipeSelectedListener {
        void onRecipeSelected(Recipe recipe);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnRecipeSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle (Constant.MAIN_TITLE);

        mLayoutManager = new LinearLayoutManager (getActivity ().getApplicationContext ());
        mRecipeRecyclerView.setLayoutManager(mLayoutManager);

        new RecipeAsyncTask().execute ();

        // Return view
        return view;
    }

    private class RecipeAsyncTask extends AsyncTask<String, Void, Recipe[]> {
        @Override
        protected Recipe[] doInBackground(String... strings) {
            try {
                URL url = JsonUtils.buildBakingUrl ();
                String bakingResults = JsonUtils.getResponseFromHttpUrl(url);
                return setRecipesArray (bakingResults);
            } catch (IOException | JSONException e) {
                e.printStackTrace ();
            }
            return null;
        }
        protected void onPostExecute(Recipe[] recipes) {
            // specify an adapter
            mRecipeAdapter = new RecipeListNameAdapter (recipes, getContext (), mCallback);
            mRecipeRecyclerView.setAdapter(mRecipeAdapter);
        }
    }
    /*** ADD REVIEW DATA TO MOVIE OBJECT ***/
    public Recipe[] setRecipesArray(String jsonResults) throws JSONException {
        JSONArray resultsArray = new JSONArray(jsonResults);
        recipes = new Recipe[resultsArray.length ()];

        for (int i = 0; i < resultsArray.length(); i++) {
            recipes[i] = new Recipe ();

            JSONObject recipe = resultsArray.getJSONObject(i);

            recipes[i].setRecipeName (recipe.getString(Constant.NAME_KEY));
            recipes[i].setRecipeId (recipe.getInt (Constant.ID_KEY));
            recipes[i].setServings (recipe.getInt (Constant.SERVINGS_KEY));

            // INGREDIENTS
            JSONArray ingredientsArray = recipe.getJSONArray(Constant.INGREDIENTS_KEY);
            ingredients = new Ingredient[ingredientsArray.length ()];
            ingredientsList = new ArrayList<Ingredient> ();

            // STEPS
            JSONArray stepsArray = recipe.getJSONArray (Constant.STEPS_KEY);
            steps = new Step[stepsArray.length ()];
            stepsList = new ArrayList<Step> ();

            // For loop for getting/setting ingredients
            for (int j = 0; j < ingredientsArray.length(); j++) {
                JSONObject ingredient = ingredientsArray.getJSONObject(j);

                ingredients[j] = new Ingredient ();

                ingredients[j].setIngredientsQuantity (ingredient.getDouble (Constant.INGREDIENTS_QUANTITY_KEY));
                ingredients[j].setIngredientsMeasureType (ingredient.getString(Constant.INGREDIENTS_MEASURE_KEY).toLowerCase ());
                ingredients[j].setIngredientsName (ingredient.getString (Constant.INGREDIENT_NAME_KEY));

                ingredientsList.add(ingredients[j]);
            }

            // For loop for getting/setting steps
            for (int k = 0; k < stepsArray.length (); k++) {
                JSONObject step = stepsArray.getJSONObject (k);

                steps[k] = new Step();

                steps[k].setStepId (step.getInt (Constant.ID_KEY));
                steps[k].setStepLongDescription (step.getString (Constant.STEP_LONG_DESC_KEY));
                steps[k].setStepShortDescription (step.getString (Constant.STEP_SHORT_DESC_KEY));

                if (step.getString (Constant.STEP_VIDEO_URL) == "") {
                    steps[k].setStepVideoUrl(null);
                } else {
                    steps[k].setStepVideoUrl (step.getString (Constant.STEP_VIDEO_URL));
                }
                stepsList.add(steps[k]);
            }

            recipes[i].setIngredients (ingredientsList);
            recipes[i].setSteps (stepsList);

        }
        return recipes;
    }
}
