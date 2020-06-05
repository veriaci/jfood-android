package com.example.jfood_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.jfood_android.R;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {
    ProgressBar pbSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pbSplash = findViewById(R.id.pbSplash);
        startAsyncTask(this.getWindow().getDecorView());
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

        // dilakukan sebelum task dimulai: memunculkan progress bar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SplashActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.pbSplash.setVisibility(View.VISIBLE);
        }

        // dilakukan selama task berlangsung: menghitung 1 sampai 100 selama sekitar 2 detik
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

        // dilakukan selama task berlangsung: memperbaharui progressbar sesuai dengan angka yg dihitung pada background
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            SplashActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.pbSplash.setProgress(values[0]);
        }

        // dilakukan setelah task selesai (perhitungan mencapai 100): pindah ke LoginActivity
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SplashActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            Intent intent = new Intent(activity,LoginActivity.class);
            activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
        }
    }
}
