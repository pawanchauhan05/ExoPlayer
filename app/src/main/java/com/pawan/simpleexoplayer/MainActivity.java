package com.pawan.simpleexoplayer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends Activity implements ExoPlayer.EventListener, SimpleExoPlayer.VideoListener, SurfaceHolder.Callback {

    public static String ARG_URL = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";

    private SurfaceView mSurfaceView;
    private SimpleExoPlayer mPlayer;
    //private AspectRatioFrameLayout mAspectRatioLayout;

    private PlaybackControlView mPlaybackControlView;
    private PlayerControl playerControl;
    private ExtractorsFactory extractor;
    private DefaultHttpDataSourceFactory dataSourceFactory;
    private SimpleExoPlayerView mAspectRatioLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialize view
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceView.getHolder().addCallback(this);
        //mAspectRatioLayout = (AspectRatioFrameLayout) findViewById(R.id.aspect_ratio_layout);
        mAspectRatioLayout = (SimpleExoPlayerView) findViewById(R.id.aspect_ratio_layout);
        String videoUrl = ARG_URL;

        // initialize player
        Handler handler = new Handler();
        extractor = new DefaultExtractorsFactory();
        dataSourceFactory = new DefaultHttpDataSourceFactory("ExoPlayer Demo");

        mPlayer = ExoPlayerFactory.newSimpleInstance(
                this,
                new DefaultTrackSelector(),
                new DefaultLoadControl()
        );
        mPlaybackControlView = (PlaybackControlView) findViewById(R.id.player_view);
        mPlaybackControlView.requestFocus();
        mPlaybackControlView.setPlayer(mPlayer);

        // initialize source
        /*MediaSource videoSource = new ExtractorMediaSource(
                Uri.parse(videoUrl),
                dataSourceFactory,
                extractor,
                null,
                null
        );*/

        MediaSource firstSource = new ExtractorMediaSource(Uri.parse(videoUrl), dataSourceFactory, extractor, null, null);
        MediaSource secondSource = new ExtractorMediaSource(Uri.parse("http://demos.webmproject.org/exoplayer/glass_vp9_vorbis.webm"),dataSourceFactory, extractor, null, null);
        MediaSource thirdSource = new ExtractorMediaSource(Uri.parse("http://techslides.com/demos/sample-videos/small.mp4"),dataSourceFactory, extractor, null, null);
        // Plays the first video twice.
        LoopingMediaSource firstSourceTwice = new LoopingMediaSource(firstSource, 2);
        LoopingMediaSource secondSourceTwice = new LoopingMediaSource(secondSource, 3);
        LoopingMediaSource thirdSourceTwice = new LoopingMediaSource(thirdSource, 3);
        // Plays the first video twice, then the second video.
        ConcatenatingMediaSource concatenatedSource = new ConcatenatingMediaSource(firstSourceTwice, secondSourceTwice, thirdSourceTwice);
        // Loops the sequence indefinitely.
        LoopingMediaSource compositeSource = new LoopingMediaSource(concatenatedSource);


        playerControl = new PlayerControl(mPlayer);
        mPlayer.prepare(compositeSource);
        mPlayer.setPlayWhenReady(true);
        mPlayer.addListener(this);
        mPlayer.setVideoListener(this);
        //playerControl.pause();

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        //mAspectRatioLayout.setAspectRatio(pixelWidthHeightRatio);
    }

    @Override
    public void onRenderedFirstFrame() {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (mPlayer != null) {
            mPlayer.setVideoSurfaceHolder(surfaceHolder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mPlayer != null) {
            mPlayer.setVideoSurfaceHolder(null);
        }
    }

    public void pauseVideo(View view) {
        playerControl.pause();
    }

    public void resumeVideo(View view) {
        playerControl.start();
    }

    public void backwardVideo(View view) {
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse("http://techslides.com/demos/sample-videos/small.mp4"), dataSourceFactory, extractor, null, null);
        LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource, 3);
        // Prepare the player with the source.
        mPlayer.prepare(loopingSource);
        mPlayer.setPlayWhenReady(true);
        mPlaybackControlView.setPlayer(mPlayer);
    }

    public void forwardVideo(View view) {
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse("http://demos.webmproject.org/exoplayer/glass_vp9_vorbis.webm"), dataSourceFactory, extractor, null, null);
        LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource, 3);
        // Prepare the player with the source.
        mPlayer.prepare(loopingSource);
        mPlayer.setPlayWhenReady(true);
        mPlaybackControlView.setPlayer(mPlayer);
    }

    private int count = 0;

}
