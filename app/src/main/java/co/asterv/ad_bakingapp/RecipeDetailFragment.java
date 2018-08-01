package co.asterv.ad_bakingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import co.asterv.ad_bakingapp.adapters.IngredientsAdapter;
import co.asterv.ad_bakingapp.model.Recipe;
import co.asterv.ad_bakingapp.utils.Constant;
import co.asterv.ad_bakingapp.utils.JsonUtils;

public class RecipeDetailFragment extends Fragment{
    private Recipe recipe;
    private Recipe[] ingredientsData;
    private static RecyclerView mIngredientsRecyclerView;
    private static RecyclerView.Adapter mIngredientsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
        TextView tvRecipeServings = view.findViewById (R.id.servingsAmountTextView);

        tvRecipeTitle.setText (recipe.getRecipeName ());
        tvRecipeServings.setText (String.valueOf (recipe.getServings ()));

        mIngredientsRecyclerView = view.findViewById (R.id.ingredientsRecyclerView);
        mLayoutManager = new LinearLayoutManager (getActivity ().getApplicationContext ());
        mIngredientsRecyclerView.setLayoutManager (mLayoutManager);

        new IngredientsAsyncTask ().execute ();

        // Return view
        return view;
    }

    private class IngredientsAsyncTask extends AsyncTask<String, Void, Recipe[]> {
        @Override
        protected Recipe[] doInBackground(String... strings) {
            try {
                URL url = JsonUtils.buildBakingUrl ();
                String bakingResults = JsonUtils.getResponseFromHttpUrl(url);
                return setIngredientsDataToArray (bakingResults);
            } catch (IOException | JSONException e) {
                e.printStackTrace ();
            }
            return null;
        }

        protected void onPostExecute(Recipe[] recipes) {
            //specify adapter
            mIngredientsAdapter = new IngredientsAdapter (recipes, getContext ());
            mIngredientsRecyclerView.setAdapter (mIngredientsAdapter);
            mIngredientsRecyclerView.setNestedScrollingEnabled (false);
        }
    }

    /*** ADD REVIEW DATA TO MOVIE OBJECT ***/
    public Recipe[] setIngredientsDataToArray(String jsonResults) throws JSONException {
        JSONArray resultsArray = new JSONArray(jsonResults);

        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject recipe = resultsArray.getJSONObject(i);

            JSONArray ingredientsArray = recipe.getJSONArray(Constant.INGREDIENTS_KEY);

            ingredientsData = new Recipe[ingredientsArray.length ()];

            for (int j = 0; j < ingredientsArray.length(); j++) {
                JSONObject ingredient = ingredientsArray.getJSONObject(j);
                ingredientsData[j] = new Recipe();

                ingredientsData[j].setIngredientsQuantity (ingredient.getDouble (Constant.INGREDIENTS_QUANTITY_KEY));
                ingredientsData[j].setIngredientsMeasureType (ingredient.getString(Constant.INGREDIENTS_MEASURE_KEY).toLowerCase ());
                ingredientsData[j].setIngredientsName (ingredient.getString (Constant.INGREDIENT_NAME_KEY));
            }
        }

        return ingredientsData;
    }

}
