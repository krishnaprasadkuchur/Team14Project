package group14.mc.asu.edu.brainnetteam14;

import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VideoActivity extends AppCompatActivity {
    VideoView videoView;
    Button PlayVideo;
    MediaController mediaController;
    EditText userName;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = findViewById(R.id.loginView);
        PlayVideo = findViewById(R.id.playVideo);
        userName = findViewById(R.id.userName);
//        parseEDF = new ParseEDF();

        copyDataToSDCard();

       mediaController = new MediaController(this);
       videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
           @Override
           public void onCompletion(MediaPlayer mediaPlayer) {
               Intent loginintent = new Intent(VideoActivity.this, MainActivity.class);
               loginintent.putExtra("username", userName.getText().toString());
               startActivity(loginintent);

           }
       });



    }

    public void copyDataToSDCard(){

        createFolders();
        String[] filenames = {"s001r14.edf", "s002r14.edf","s003r14.edf", "s004r14.edf","s005r14.edf"};
        int[] resources = {R.raw.s001r14, R.raw.s002r14, R.raw.s003r14, R.raw.s004r14, R.raw.s005r14};
        int i = 0;
        for(String filename:filenames) {

            File file = new File(ApplicationConstants.EDF_FOLDER_PATH + filename);
            if (file.exists()) {
                Log.d(TAG, "Edf file on sdcard");
            } else {
                InputStream in = null;
                OutputStream out = null;
                try {

                    in = getResources().openRawResource(resources[i]);
                    i++;
                    String newFileName = ApplicationConstants.EDF_FOLDER_PATH + filename;
                    out = new FileOutputStream(newFileName);

                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                    Log.d(TAG, "Finished copying");
                } catch (Exception e) {
                    Log.e(TAG, "Error while copying" + e.toString());
                }

            }
        }

    }

    public void createFolders(){

        File file = new File(ApplicationConstants.EDF_FOLDER_PATH);
        if(!file.exists()){
            Log.d(TAG, "Folders created");
            file.mkdir();
        }
        else Log.d(TAG, "Folder exists");
    }


    public void playVideo(View v){

        String username = userName.getText().toString();
        if(username.equals("")){
            Toast.makeText(getApplicationContext(), "Please enter user name", Toast.LENGTH_SHORT).show();
        }

        else {
            videoView.setVideoURI(Uri.parse(ApplicationConstants.VIDEO_PATH));
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            videoView.start();

        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG,"Back button pressed, No action taken");
    }
}

