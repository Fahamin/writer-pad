package fam.doa.writerpad;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ProgressBar Progressbar;
    TextView ShowText;
    int progressBarValue = 0;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.hide();
        }
        Progressbar = (ProgressBar)findViewById(R.id.progressBar1);
        ShowText = (TextView)findViewById(R.id.textView1);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while(progressBarValue < 100)
                {
                    progressBarValue++;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                            Progressbar.setProgress(progressBarValue);
                            ShowText.setText(progressBarValue +"%");
                            if(progressBarValue==99)
                            {
                                startActivity(new Intent(MainActivity.this,second.class));
                                finish();
                            }
                        }
                    });try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }
            }
        }).start();
    }

}

