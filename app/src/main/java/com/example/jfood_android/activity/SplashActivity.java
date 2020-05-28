package com.example.jfood_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jfood_android.R;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {
    ProgressBar pbSplash;
    RelativeLayout rlSplash;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pbSplash = findViewById(R.id.pbSplash);
        startAsyncTask(this.getWindow().getDecorView());
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100){
                    progressStatus++;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pbSplash.setProgress(progressStatus);
                        }
                    });
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
         */

    }

    public void startAsyncTask(View v){
        SplashAsyncTask task = new SplashAsyncTask(this);
        task.execute(100);
    }

    private static class SplashAsyncTask extends AsyncTask<Integer, Integer, String>{
        private WeakReference<SplashActivity> activityWeakReference;

        SplashAsyncTask(SplashActivity activity) {
            activityWeakReference = new WeakReference<SplashActivity>(activity);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SplashActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.pbSplash.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Integer... integers) {
            for (int i = 0; i < integers[0]; i++) {
                publishProgress((i * 100) / integers[0]);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Finished!";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            SplashActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.pbSplash.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SplashActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            //activity.pbSplash.setProgress(0);
            //activity.pbSplash.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(activity,LoginActivity.class);
            activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
        }
    }
}
