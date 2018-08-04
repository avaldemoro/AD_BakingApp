package co.asterv.ad_bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

import co.asterv.ad_bakingapp.model.Step;
import co.asterv.ad_bakingapp.utils.Constant;

public class RecipeInstructionFragment extends Fragment {
    RecipeDetailFragment.OnStepSelectedListener mCallback;
    private Step step, previousStep, nextStep;
    private ArrayList<Step> steps;
    private MediaController mediaController;
    VideoView vvStepVideo;
    Button previousButton;
    Button nextButton;
    TextView tvStepLongDesc;

    public RecipeInstructionFragment() { }


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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_instruction, container, false);

        setUpInstructionFragUI (view, step);

        previousButton.setOnClickListener (v -> {
            if (step.getStepId () == 0) {
                setUpInstructionFragUI (view, step);
                //mCallback.onStepSelected (steps, (step.getStepId ()));
            } else {
                mCallback.onStepSelected (steps, (step.getStepId ()) - 1);
            }
        });

        nextButton.setOnClickListener (v -> {
            if (step.getStepId () == (steps.size () - 1)) {
                setUpInstructionFragUI (view, step);

                //mCallback.onStepSelected (steps, (step.getStepId ()));
            } else {
                mCallback.onStepSelected (steps, (step.getStepId ()) + 1);
            }
        });

        // Return view
        return view;
    }

    public void setUpInstructionFragUI(View view, Step step) {
        // Inflate view
        vvStepVideo = view.findViewById (R.id.stepVideoView);
        previousButton = view.findViewById (R.id.previousButton);
        nextButton = view.findViewById (R.id.nextButton);
        tvStepLongDesc = view.findViewById (R.id.stepLongDescTextView);
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
