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
import co.asterv.ad_bakingapp.model.Step;
import co.asterv.ad_bakingapp.utils.Constant;

public class RecipeInstructionFragment extends Fragment {
    RecipeDetailFragment.OnStepSelectedListener mCallback;
    private Step step;
    private MediaController mediaController;


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

        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle (step.getStepShortDescription ());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_recipe_instruction, container, false);

        VideoView vvStepVideo = view.findViewById (R.id.stepVideoView);
        Button previousButton = view.findViewById (R.id.previousButton);
        Button nextButton = view.findViewById (R.id.nextButton);
        TextView tvStepLongDesc = view.findViewById (R.id.stepLongDescTextView);
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
        // Return view
        return view;
    }
}
