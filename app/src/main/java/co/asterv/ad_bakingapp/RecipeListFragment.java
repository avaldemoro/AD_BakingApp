package co.asterv.ad_bakingapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import co.asterv.ad_bakingapp.adapters.RecipeListNameAdapter;
import co.asterv.ad_bakingapp.model.Recipe;
import co.asterv.ad_bakingapp.utils.Constant;
import co.asterv.ad_bakingapp.utils.JsonUtils;

public class RecipeListFragment extends Fragment {
    private static RecyclerView mRecipeRecyclerView;
    private static RecyclerView.Adapter mRecipeAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Recipe[] recipes;
    RecipeListFragment.OnRecipeClickListener mCallback;

    public interface OnRecipeClickListener {
        void onRecipeSelected(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        mRecipeRecyclerView = view.findViewById(R.id.recipeNameRecyclerView);
        mLayoutManager = new LinearLayoutManager (getActivity ().getApplicationContext ());
        mRecipeRecyclerView.setLayoutManager(mLayoutManager);

        new RecipeAsyncTask().execute ();

        // Return view
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnRecipeClickListener)context;
        }catch (ClassCastException e){
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach ();
        mCallback = null;
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
        }
        return recipes;
    }
}
