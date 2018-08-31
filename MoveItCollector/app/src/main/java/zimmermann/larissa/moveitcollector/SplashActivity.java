package zimmermann.larissa.moveitcollector;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    public static final int KILL_PARENT_ACTIVITY = 1;
    private static final int SPLASH_SCREEN_DELAY = 2000;

    @BindView(R.id.logo)
    ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @SuppressLint("RestrictedApi")
            @Override
            public void run() {
                ActivityOptionsCompat activityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this,
                                                                            ivLogo,
                                                                            ivLogo.getTransitionName());
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivityForResult(intent, KILL_PARENT_ACTIVITY, activityOptionsCompat.toBundle());
            }
        }, SPLASH_SCREEN_DELAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == KILL_PARENT_ACTIVITY) {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
