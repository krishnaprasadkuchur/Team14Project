package group14.mc.asu.edu.brainnetteam14;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class SplashScreen extends AppCompatActivity {

    private static final String imageUrl = "https://intimacyheals.files.wordpress.com/2017/03/energyflow.gif?w=788";
    //private static final String imageUrl  = "https://billybeyondblog.files.wordpress.com/2017/02/tumblr_okedamfony1vfo0eko1_1280.gif?w=584&h=584";
    ImageView gifImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        gifImageView = findViewById(R.id.imageView);
        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.background))
                .into(gifImageView);

        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished){}

            @Override
            public void onFinish(){
                Intent i = new Intent(getApplicationContext(), VideoActivity.class);
                startActivity(i);



            }
        }.start();
    }
}
