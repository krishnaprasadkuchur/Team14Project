package group14.mc.asu.edu.brainnetteam14;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
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
import java.util.Random;

public class VideoActivity extends AppCompatActivity {
    VideoView videoView;
    Button PlayVideo;
    MediaController mediaController;
    EditText userName;
    String ser ;
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = findViewById(R.id.loginView);
        PlayVideo = findViewById(R.id.playVideo);
        userName = findViewById(R.id.userName);
        copyDataToSDCard();
        int[] arr = {313, 492};
        int[] arr2 = {292, 512};
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

        dlgAlert.setTitle("Round Trip times");
        Random rmd = new Random();
        int i = rmd.nextInt(1-0)+0;
        ser = (arr[i] > arr2[i])? "Fog":"Cloud";
        dlgAlert.setMessage("RTT for Cloud:" +arr[i]+"ms"+" \n RTT for Fog:"+ arr2[i]+"ms");
        dlgAlert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "On Ok clicked");
                    }
                });
        dlgAlert.create().show();
       mediaController = new MediaController(this);
       videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
           @Override
           public void onCompletion(MediaPlayer mediaPlayer) {
//               Intent loginintent = new Intent(VideoActivity.this, MainActivity.class);
//               loginintent.putExtra("username", userName.getText().toString());
//               startActivity(loginintent);
               Intent welcomeIntent = new Intent(VideoActivity.this, WelcomeUserBack.class);
               welcomeIntent.putExtra("username", userName.getText().toString());
               welcomeIntent.putExtra("serverused",ser);
               startActivity(welcomeIntent);
            //           getServerResponse("0");
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

    public void getServerResponse(String response){
        try {
            if (Integer.valueOf(response) == 1) {

                Intent loginintent = new Intent(VideoActivity.this, MainActivity.class);
                loginintent.putExtra("username", userName.getText().toString());
                startActivity(loginintent);

            } else {

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

                dlgAlert.setMessage("wrong password or username");
                dlgAlert.setTitle("Authentication Error...");
                //dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "On Ok clicked");
                                Intent intent = getIntent();
                                startActivity(intent);
                            }
                        });
                dlgAlert.create().show();


            }
        }
        catch (NumberFormatException ex){

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

            dlgAlert.setTitle("server error");
            dlgAlert.setMessage("Please retry");
            //dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "On Ok clicked");
                            Intent intent = getIntent();
                            startActivity(intent);
                        }
                    });
            dlgAlert.create().show();
        }

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG,"Back button pressed, No action taken");
    }
}

