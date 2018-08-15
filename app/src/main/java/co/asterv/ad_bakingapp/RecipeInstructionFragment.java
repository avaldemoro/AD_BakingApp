package co.asterv.ad_bakingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.asterv.ad_bakingapp.model.Step;
import co.asterv.ad_bakingapp.utils.Constant;

public class RecipeInstructionFragment extends Fragment {
    @BindView(R.id.previousButton) Button previousButton;
    @BindView(R.id.nextButton) Button nextButton;
    @BindView(R.id.stepLongDescTextView) TextView tvStepLongDesc;
    RecipeDetailFragment.OnStepSelectedListener mCallback;
    private Step step;
    private ArrayList<Step> steps;
    private int screenHeight, screenWidth;
    @BindView(R.id.stepVideoPlayerView) PlayerView mPlayerView;
    SimpleExoPlayer player;
    // bandwidth meter to measure and estimate bandwidth
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter ();
    private DataSource.Factory mediaDataSourceFactory;


    private ComponentListener componentListener;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;


    public RecipeInstructionFragment() {
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState (outState);
        outState.putParcelable ("CURRENTSTEP", step);
        outState.putParcelableArrayList ("CURRENTSTEPARRAYLIST", steps);
    }

    @Override
    public void onStart() {
        super.onStart ();
        getActivity ().setTitle (step.getStepShortDescription ());

        if (Util.SDK_INT > 23) {
            initializePlayer(step.getStepVideoUrl ());
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach (context);
        try {
            mCallback = (RecipeDetailFragment.OnStepSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException (context.toString () + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        componentListener = new ComponentListener();
        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable ("CURRENTSTEP");
            steps = savedInstanceState.getParcelableArrayList ("CURRENTSTEPARRAYLIST");
        } else {
            step = getArguments ().getParcelable (Constant.STEP_KEY);
            steps = getArguments ().getParcelableArrayList (Constant.STEPS_KEY);
        }

        DisplayMetrics displaymetrics = new DisplayMetrics ();
        getActivity ().getWindowManager ().getDefaultDisplay ().getMetrics (displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        mediaDataSourceFactory = new DefaultDataSourceFactory(getContext (),
                Util.getUserAgent(getContext (), "ad_bakingapp"), (TransferListener<? super DataSource>) BANDWIDTH_METER);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_recipe_instruction, container, false);
        ButterKnife.bind (this, view);

        if (((AppCompatActivity) getActivity ()).getSupportActionBar () != null) {
            ((AppCompatActivity) getActivity ()).getSupportActionBar ().setTitle (step.getStepShortDescription ());
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

        if (stepUrl.isEmpty ()) {
            mPlayerView.setVisibility (View.GONE);
        } else {
            initializePlayer (step.getStepVideoUrl ());
            //vvStepVideo.setVideoURI (Uri.parse (step.getStepVideoUrl ()));

        }

        if (step.getStepId () == 0) {
            previousButton.setVisibility (View.GONE);
        } else if (step.getStepId () == steps.size () - 1) {
            previousButton.setVisibility (View.VISIBLE);
            nextButton.setVisibility (View.GONE);
        } else {
            previousButton.setVisibility (View.VISIBLE);
            nextButton.setVisibility (View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(step.getStepVideoUrl ());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    /**
     * Initialize ExoPlayer.
     */
    private void initializePlayer(String stepVideoUrl) {
        if (player == null) {
            // a factory to create an AdaptiveVideoTrackSelection
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            // using a DefaultTrackSelector with an adaptive video selection factory
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext ()),
                    new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());
            player.addListener(componentListener);
            mPlayerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
        }
        MediaSource mediaSource = new ExtractorMediaSource.Factory (mediaDataSourceFactory).createMediaSource (Uri.parse(step.getStepVideoUrl ()));

        player.prepare(mediaSource, true, false);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.removeListener(componentListener);
            player.release();
            player = null;
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}