package com.example.android.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.objects.Recipe;
import com.example.android.bakingapp.objects.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecipeStepFragment is a detail fragment describing a recipe step
 * It will be displayed in the RecipeStepActivity for phones
 * and in the RecipesDetailActivity for tablets
 */
public class RecipeStepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = RecipeStepFragment.class.getSimpleName();
    private static MediaSessionCompat mediaSession;
    @Nullable
    @BindView(R.id.tv_short_description)
    TextView shortDescriptionTextView;
    @Nullable
    @BindView(R.id.tv_description)
    TextView descriptionTextView;
    @Nullable
    @BindView(R.id.tv_previous)
    TextView previousRecipeTextView;
    @Nullable
    @BindView(R.id.tv_next)
    TextView nextRecipeTextView;
    @Nullable
    @BindView(R.id.tv_player_empty_view)
    TextView emptyView;
    @Nullable
    @BindView(R.id.player_view)
    SimpleExoPlayerView playerView;
    @Nullable
    @BindView(R.id.iv_thumbnail_image_view)
    ImageView thumbnailImageView;
    long exoPlayerPosition;
    private String stepShortDescription;
    // Step description
    private String stepDescription;

    // Step video URL
    private String stepVideoUrl;
    // Step thumbnail URL
    private String stepThumbnailUrl;
    private Uri mediaUri;

    private List<Step> recipeSteps;
    private int stepId;
    private SimpleExoPlayer exoPlayer;
    private PlaybackStateCompat.Builder stateBuilder;
    private PreviousNextRecipeListener listener;

    public RecipeStepFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate
                (R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, rootView);

        exoPlayerPosition = C.TIME_UNSET;
        // Check if saved instance state is not null
        if (savedInstanceState != null) {
            // If it is not null, get saved ExoPlayer position
            exoPlayerPosition = savedInstanceState.getLong("exoPlayerPosition", C.TIME_UNSET);
            // Get media uri which will be used to display a video
            String mediaUriString = savedInstanceState.getString("mediaUri");
            if (!TextUtils.isEmpty(mediaUriString)) {
                mediaUri = Uri.parse(mediaUriString);
            }
        }

        // Get fragment arguments
        Bundle fragmentArguments = this.getArguments();
        // If fragments arguments are not null
        if (fragmentArguments != null) {
            // Get current recipe
            Recipe currentRecipe = fragmentArguments.getParcelable("currentRecipe");
            // Get step id
            stepId = fragmentArguments.getInt("stepId", 0);
            // Get recipe steps
            recipeSteps = currentRecipe.getSteps();
            // Get one step with the stepId
            Step step = recipeSteps.get(stepId);
            // Get short step description
            stepShortDescription = step.getStepShortDescription();
            // Get step description
            stepDescription = step.getStepDescription();
            // Get step video url
            stepVideoUrl = step.getStepVideoUrl();
            // Get step thumbnail url
            stepThumbnailUrl = step.getStepThumbnailUrl();
        }

        // If mediaUri is null
        if (mediaUri == null) {

            // If stepVideoUrl is not null or empty
            if (stepVideoUrl != null && !TextUtils.isEmpty(stepVideoUrl)) {

                // Assign stepVideoUrl to mediaUri
                mediaUri = Uri.parse(stepVideoUrl);

                // If stepVideoUrl is null or empty (then we do not have a video to display)
                // and stepThumbnailUrl is null or empty
            } else if (stepVideoUrl == null || TextUtils.isEmpty(stepVideoUrl)
                    && stepThumbnailUrl != null && !TextUtils.isEmpty(stepThumbnailUrl)) {

                // If stepThumbnailUrl is of mp4 format, it is a video
                if (stepThumbnailUrl.endsWith(".mp4")) {

                    // Assign stepThumbnailUrl to mediaUri
                    mediaUri = Uri.parse(stepThumbnailUrl);

                    // Otherwise, it is an image
                } else {

                    // Display ImageView
                    displayImageView();

                    // Use Picasso to display the recipe image
                    Picasso.with(getActivity().getApplicationContext())
                            .load(stepThumbnailUrl)
                            .centerCrop()
                            .into(thumbnailImageView);
                }

                // If stepVideoUrl and thumbnailImageView is null or empty
            } else {

                // Display empty view
                displayEmptyView();
            }
        }

        // If layout is for tablet or
        // layout is for phone in portrait orientation
        if (getActivity().getResources().getBoolean(R.bool.two_pane) ||
                !getActivity().getResources().getBoolean(R.bool.two_pane)
                        && getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_PORTRAIT) {

            // Display step short description
            shortDescriptionTextView.setText(stepShortDescription);

            // Display step description
            descriptionTextView.setText(stepDescription);

            // If it is the first step, do not display "Previous" button
            if (stepId > 0) {
                previousRecipeTextView.setVisibility(View.VISIBLE);
                previousRecipeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.OnPreviousNextRecipeSelected(stepId - 1);
                    }
                });
            }

            // If it the last step, do not display "Next" button
            if (stepId < recipeSteps.size() - 1) {
                nextRecipeTextView.setVisibility(View.VISIBLE);
                nextRecipeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.OnPreviousNextRecipeSelected(stepId + 1);
                    }
                });
            }
            // If it is landscape phone layout
        } else {

            // Display player view full screen
            hideSystemUI();
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Check if the PreviousNextRecipeListener is implemented
        try {
            listener = (PreviousNextRecipeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnPreviousNextRecipeSelected");
        }
    }

    private void initializePlayer(Uri mediaUri) {
        // If ExoPlayer is null
        if (exoPlayer == null) {
            // Create an instance of the ExoPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);
            // Prepare the MediaSource
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(), null, null);
            // If we had ExoPlayer position saved in the saved instance state,
            // seek to the saved position
            if (exoPlayerPosition != C.TIME_UNSET) exoPlayer.seekTo(exoPlayerPosition);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Initialize the Media Session
        initializeMediaSession();
        // If stepVideoUrl is not null
        if (mediaUri != null) {

            // Show display player view
            displayPlayerView();

            // Show step video
            initializePlayer(mediaUri);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Release the player
        releasePlayer();
    }

    private void releasePlayer() {
        // If ExoPlayer is not null
        if (exoPlayer != null) {
            // Get current ExoPlayer position
            exoPlayerPosition = exoPlayer.getCurrentPosition();
            exoPlayer.stop();
            exoPlayer.release();
            // This is to ensure that ExoPlayer is released
            exoPlayer = null;
        }
    }

    // Initialize the Media Session to be enabled with media buttons,
    // transport controls, callbacks and media controller.
    private void initializeMediaSession() {

        // Create a MediaSessionCompat
        mediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButton and TransportControls
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart player when the app is not visible
        mediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(stateBuilder.build());

        // VideoSessionCallback has methods that handle callbacks from a media controller.
        mediaSession.setCallback(new VideoSessionCallback());

        mediaSession.setActive(true);
    }

    // ExoPlayer Event Listeners
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    // This method is called when the ExoPlayer state changes.
    // Used to update the MediaSession PlayBackState to keep in sync.
    //
    // boolean playWhenReady: true - playing, false - paused
    // int playbackState: STATE_IDLE, STATE_BUFFERING, STATE_READY, STATE_ENDED
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(), 1f);
        }

        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        // SYSTEM_UI_FLAG_HIDE_NAVIGATION hides navigation bar
        // SYSTEM_UI_FLAG_FULLSCREEN hides status bar
        playerView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {

        // Save EcoPlayer position
        currentState.putLong("exoPlayerPosition", exoPlayerPosition);

        // If stepVideoUrl is not null
        if (mediaUri != null) {
            // Save stepVideoUrl
            currentState.putString("mediaUri", mediaUri.toString());
        }

        super.onSaveInstanceState(currentState);
    }

    private void displayPlayerView() {

        // Hide empty view, thumbnail image view, display player view
        emptyView.setVisibility(View.INVISIBLE);
        thumbnailImageView.setVisibility(View.INVISIBLE);
        playerView.setVisibility(View.VISIBLE);
    }

    private void displayImageView() {

        // Hide empty view, player view, display image view
        emptyView.setVisibility(View.INVISIBLE);
        playerView.setVisibility(View.INVISIBLE);
        thumbnailImageView.setVisibility(View.VISIBLE);
    }

    private void displayEmptyView() {

        // Hide player view, thumbnail image view
        // Display empty view explaining that there is no video
        playerView.setVisibility(View.INVISIBLE);
        thumbnailImageView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.VISIBLE);
    }

    // PreviousNextRecipeListener to handle navigation to previous and next steps
    // It passes stepId to the RecipeStepActivity
    public interface PreviousNextRecipeListener {
        void OnPreviousNextRecipeSelected(int stepId);
    }

    // Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mediaSession, intent);
        }
    }

    // Media Session Callbacks, where all external clients control the player.
    private class VideoSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            exoPlayer.seekTo(0);
        }
    }
}
