package co.asterv.ad_bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.asterv.ad_bakingapp.model.Step;
import co.asterv.ad_bakingapp.utils.Constant;

public class RecipeInstructionFragment extends Fragment {
    @BindView (R.id.stepVideoView) VideoView vvStepVideo;
    @BindView (R.id.previousButton) Button previousButton;
    @BindView (R.id.nextButton) Button nextButton;
    @BindView (R.id.stepLongDescTextView) TextView tvStepLongDesc;
    RecipeDetailFragment.OnStepSelectedListener mCallback;
    private Step step;
    private ArrayList<Step> steps;
    private MediaController mediaController;
    private int screenHeight, screenWidth;

    public RecipeInstructionFragment() { }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle(step.getStepShortDescription ());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (RecipeDetailFragment.OnStepSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        step = getArguments().getParcelable (Constant.STEP_KEY);
        steps = getArguments ().getParcelableArrayList (Constant.STEPS_KEY);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity ().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_instruction, container, false);
        ButterKnife.bind(this, view);

        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle (step.getStepShortDescription ());
        }

        setUpInstructionFragUI (step);

        previousButton.setOnClickListener (v -> {
            if (step.getStepId () == 0) {
                setUpInstructionFragUI (step);
            } else {
                mCallback.onStepSelected (steps, (step.getStepId ()) - 1);
            }
        });

        nextButton.setOnClickListener (v -> {
            if (step.getStepId () == (steps.size () - 1)) {
                setUpInstructionFragUI (step);
            } else {
                mCallback.onStepSelected (steps, (step.getStepId ()) + 1);
            }
        });

        return view;
    }

    public void setUpInstructionFragUI(Step step) {
        String stepUrl = step.getStepVideoUrl ();

        tvStepLongDesc.setText (step.getStepLongDescription ());

        mediaController = new MediaController (getContext ());

        if (stepUrl.isEmpty ()) {
            vvStepVideo.setVisibility (View.GONE);
        } else {
            vvStepVideo.setMediaController (mediaController);

            mediaController.setAnchorView (vvStepVideo);
            vvStepVideo.setVideoURI (Uri.parse (step.getStepVideoUrl ()));
            vvStepVideo.seekTo (100);

            // Check orientation of the screen and set height of videoView accordingly
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // get layout parameters for that view
                ViewGroup.LayoutParams params = vvStepVideo.getLayoutParams();

                // change height of the params e.g. 480dp
                params.height = screenHeight;

                // initialize new parameters for my element
                vvStepVideo.setLayoutParams(params);
            }
        }

        if (step.getStepId () == 0) {
            previousButton.setVisibility (View.GONE);
        } else if (step.getStepId () == steps.size () - 1){
            previousButton.setVisibility (View.VISIBLE);
            nextButton.setVisibility (View.GONE);
        } else {
            previousButton.setVisibility (View.VISIBLE);
            nextButton.setVisibility (View.VISIBLE);
        }
    }
}
