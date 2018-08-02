package co.asterv.ad_bakingapp.adapters;

import android.app.ActionBar;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;
import co.asterv.ad_bakingapp.R;
import co.asterv.ad_bakingapp.model.Step;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {
    private Context context;
    private static List<Step> steps;
    private TextView stepShortDescTV;
    private TextView stepLongDescTV;
    private VideoView stepVideoVV;
    MediaController mediaController;
    private FrameLayout stepVideoFL;

    public StepsAdapter(List<Step> steps, Context context) {
        this.context = context;
        this.steps = steps;

    }

    @NonNull
    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from (parent.getContext ())
                .inflate (R.layout.step_list_item, parent, false);

        ViewHolder vh = new ViewHolder (v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.ViewHolder holder, int position) {
        String longDesc = steps.get(position).getStepLongDescription ();
        String shortDesc = steps.get(position).getStepShortDescription ();
        String stepUrl = steps.get(position).getStepVideoUrl ();
        holder.stepShortDescTV.setText (shortDesc);
        holder.stepLongDescTV.setText (longDesc);

        mediaController = new MediaController (context);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams (ActionBar.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        if (stepUrl.isEmpty ()) {
            holder.stepVideoVV.setVisibility (View.GONE);
            holder.stepVideoFL.setVisibility (View.GONE);
        } else {
            holder.stepVideoVV.setVideoURI (Uri.parse(stepUrl));
            holder.stepVideoVV.seekTo(100);
            holder.stepVideoVV.setMediaController (mediaController);

            lp.gravity = Gravity.BOTTOM;
            mediaController.setLayoutParams (lp);
            ((ViewGroup) mediaController.getParent()).removeView(mediaController);
            holder.stepVideoFL.addView (mediaController);
            mediaController.setAnchorView (holder.stepVideoFL);

            if (holder.stepVideoVV.isPlaying ()) {
                mediaController.hide ();
            }

        }
    }


    @Override
    public int getItemCount() {
        if (steps == null || steps.size() == 0) {
            return -1;
        }
        return steps.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView stepShortDescTV;
        TextView stepLongDescTV;
        VideoView stepVideoVV;
        FrameLayout stepVideoFL;


        public ViewHolder(View itemView) {
            super (itemView);

            stepLongDescTV = itemView.findViewById (R.id.longDescTextView);
            stepShortDescTV = itemView.findViewById (R.id.shortDescTextView);
            stepVideoVV = itemView.findViewById (R.id.stepVideoView);
            stepVideoFL = itemView.findViewById (R.id.stepVideoFrameLayout);

        }
    }
}
