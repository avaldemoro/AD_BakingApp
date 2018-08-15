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

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
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

    // Exoplayer related variables
    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";

    @BindView(R.id.stepVideoPlayerView) PlayerView mPlayerView;
    SimpleExoPlayer player;

    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;

    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;

    public RecipeInstructionFragment() { }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        updateStartPosition ();
        outState.putParcelable ("CURRENTSTEP", step);
        outState.putParcelableArrayList ("CURRENTSTEPARRAYLIST", steps);
        updateStartPosition();

        outState.putBoolean(KEY_PLAY_WHEN_READY, playWhenReady);
        outState.putInt(KEY_WINDOW, currentWindow);
        outState.putLong(KEY_POSITION, playbackPosition);
        super.onSaveInstanceState (outState);
    }

    @Override
    public void onStart() {
        super.onStart ();
        getActivity ().setTitle (step.getStepShortDescription ());
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

        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable ("CURRENTSTEP");
            steps = savedInstanceState.getParcelableArrayList ("CURRENTSTEPARRAYLIST");
            playWhenReady = savedInstanceState.getBoolean (KEY_PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(KEY_WINDOW);
            playbackPosition = savedInstanceState.getLong(KEY_POSITION);

        } else {
            step = getArguments ().getParcelable (Constant.STEP_KEY);
            steps = getArguments ().getParcelableArrayList (Constant.STEPS_KEY);
            playWhenReady = true;
            currentWindow = 0;
            playbackPosition = 0;
        }

        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter ();
        mediaDataSourceFactory = new DefaultDataSourceFactory(getContext (),
                Util.getUserAgent(getContext (), "ad_bakingapp"), (TransferListener<? super DataSource>) bandwidthMeter);

        window = new Timeline.Window ();

        DisplayMetrics displaymetrics = new DisplayMetrics ();
        getActivity ().getWindowManager ().getDefaultDisplay ().getMetrics (displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
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
            initializePlayer ();
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

    private void initializePlayer() {
        mPlayerView.requestFocus ();

        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory (bandwidthMeter);
        trackSelector = new DefaultTrackSelector (videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance (getContext (), trackSelector);
        mPlayerView.setPlayer (player);

        player.setPlayWhenReady (shouldAutoPlay);
        MediaSource mediaSource = new ExtractorMediaSource.Factory (mediaDataSourceFactory).createMediaSource (Uri.parse(step.getStepVideoUrl ()));

        boolean haveStartPosition = currentWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo (currentWindow, playbackPosition);
        }

        player.prepare (mediaSource, !haveStartPosition, false);
    }

    private void releasePlayer() {
        if (player != null) {
            updateStartPosition();
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    private void updateStartPosition() {
        playbackPosition = player.getCurrentPosition();
        currentWindow = player.getCurrentWindowIndex();
        playWhenReady = player.getPlayWhenReady();
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
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
}