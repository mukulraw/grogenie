package app.tuton.grocery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    Timer t;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);
        Log.d("TAG", "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        FirebaseMessaging.getInstance().subscribeToTopic("grc").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("task", task.toString());
            }
        });

        t = new Timer();

        t.schedule(new TimerTask() {
            @Override
            public void run() {

                Intent intent = new Intent(Splash.this , HomeActivity.class);
                startActivity(intent);
                finishAffinity();

            }
        } , 1200);

    }
}
