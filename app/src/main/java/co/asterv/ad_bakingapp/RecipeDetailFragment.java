package co.asterv.ad_bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import co.asterv.ad_bakingapp.adapters.IngredientsAdapter;
import co.asterv.ad_bakingapp.adapters.StepsAdapter;
import co.asterv.ad_bakingapp.model.Recipe;
import co.asterv.ad_bakingapp.utils.Constant;

public class RecipeDetailFragment extends Fragment{
    private Recipe recipe;
    private static RecyclerView mIngredientsRecyclerView;
    private static RecyclerView.Adapter mIngredientsAdapter;
    private static RecyclerView mStepsRecyclerView;
    private static RecyclerView.Adapter mStepsAdapter;
    private RecyclerView.LayoutManager mIngredientLayoutManager;
    private RecyclerView.LayoutManager mStepLayoutManager;

    public RecipeDetailFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        recipe = getArguments().getParcelable (Constant.RECIPE_KEY);

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
        CardView cvIngredients = view.findViewById (R.id.ingredientsCardView);

        tvRecipeTitle.setText (recipe.getRecipeName ());
        tvRecipeServings.setText (String.valueOf (recipe.getServings ()));

        mIngredientLayoutManager = new LinearLayoutManager (getActivity ().getApplicationContext ());
        mStepLayoutManager = new LinearLayoutManager (getActivity ().getApplicationContext ());

        /*** SET UP INGREDIENTS RV ***/
        mIngredientsRecyclerView = view.findViewById (R.id.ingredientsRecyclerView);
        mIngredientsRecyclerView.setLayoutManager (mIngredientLayoutManager);
        view.findViewById(R.id.ingredientsRecyclerView).setVisibility(View.GONE);
        //specify adapter
        mIngredientsAdapter = new IngredientsAdapter (recipe.getIngredients (), getContext ());
        mIngredientsRecyclerView.setAdapter (mIngredientsAdapter);
        mIngredientsRecyclerView.setNestedScrollingEnabled (false);

        /*** SET UP STEPS RV ***/
        mStepsRecyclerView = view.findViewById (R.id.stepsRecyclerView);
        mStepsRecyclerView.setLayoutManager (mStepLayoutManager);
        //specify adapter
        mStepsAdapter = new StepsAdapter (recipe.getSteps (), getContext ());
        mStepsRecyclerView.setAdapter (mStepsAdapter);
        mStepsRecyclerView.setNestedScrollingEnabled (false);


        cvIngredients.setOnClickListener(v -> {
            if (v.findViewById (R.id.ingredientsRecyclerView).getVisibility () == View.VISIBLE) {
                v.findViewById(R.id.ingredientsRecyclerView).setVisibility(View.GONE);
            } else {
                v.findViewById(R.id.ingredientsRecyclerView).setVisibility(View.VISIBLE);
            }
        });

        // Return view
        return view;
    }
}
