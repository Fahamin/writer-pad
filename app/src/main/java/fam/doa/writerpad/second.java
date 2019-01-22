package fam.doa.writerpad;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;



public class second extends AppCompatActivity {
    GestureOverlayView gestureOverlayView;
    InterstitialAd mInterstitialAd;
    boolean doubleBackToExitPressedOnce = false;
    private Button redrawButton = null;
    private Button saveButton = null;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    Button b;
    // private AdView adView;
    AdRequest adRequest;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);


        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


      //  MobileAds.initialize(getApplicationContext(), getString(R.string.appID));


        //  adView = (AdView) findViewById(R.id.ad_view);
       // adRequest = new AdRequest.Builder().build();
        // adView.loadAd(adRequest);*/

        init();
        gestureOverlayView.addOnGesturePerformedListener(new gesturecustom());

        redrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gestureOverlayView.clear(false);

              // addsShow();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionAndSaveSignature();
                //addsShow();
            }

        });
        b = findViewById(R.id.gallery);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //  addsShow();

                Intent iE = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                startActivity(iE);
            }
        });
    }

    private void addsShow() {
        count++;
        if (count % 3 == 0) {
            mInterstitialAd = new InterstitialAd(second.this);
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
            AdRequest adRequest = new AdRequest.Builder().build();//.addTestDevice("93448558CC721EBAD8FAAE5DA52596D3").build();
            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });
        }
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    /* @Override
     public void onBackPressed() {
         //System.exit(0);
         Toast.makeText(this,"tap again for exit",Toast.LENGTH_SHORT).show();
        System.exit(1);
        this.finish();
     }
 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Thanks for using my app")
                        .setMessage("Do you want to exit from here?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                AppExit();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;
            case R.id.share:
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Install now");
                String app_url = "";
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
                break;
            case R.id.rate:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
                startActivity(intent);
                break;
            case R.id.review:
                Intent inter = new Intent(Intent.ACTION_VIEW, Uri.parse(
                       ""));
                startActivity(inter);
                break;
            case R.id.moreapp:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
                startActivity(i);
                break;
            case R.id.exit:
                finish();
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void AppExit() {
        System.exit(1);
        finish();
    }

    private void init() {
        if (gestureOverlayView == null) {
            gestureOverlayView = (GestureOverlayView) findViewById(R.id.sign_pad);
        }
        if (redrawButton == null) {
            redrawButton = (Button) findViewById(R.id.redraw_button);
        }
        if (saveButton == null) {
            saveButton = (Button) findViewById(R.id.save_button);
        }
    }

    private void checkPermissionAndSaveSignature() {
        try {
            // Check whether this app has write external storage permission or not.
            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            // If do not grant write external storage permission.
            if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                // Request user to grant write external storage permission.
                ActivityCompat.requestPermissions(second.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
            } else {
                saveSignature();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveSignature() {
        try {
            // First destroy cached image.
            gestureOverlayView.destroyDrawingCache();
            // Enable drawing cache function.
            gestureOverlayView.setDrawingCacheEnabled(true);
            // Get drawing cache bitmap.
            Bitmap drawingCacheBitmap = gestureOverlayView.getDrawingCache();
            // Create a new bitmap
            Bitmap bitmap = Bitmap.createBitmap(drawingCacheBitmap);
            // Get image file save path and name.
            String filePath = Environment.getExternalStorageDirectory().toString();
            filePath += File.separator;
            filePath += "sign.png";
            File file = new File(filePath);
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            // Compress bitmap to png image.
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            // Flush bitmap to image file.
            fileOutputStream.flush();
            // Close the output stream.
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "Signature file is saved to " + filePath, Toast.LENGTH_LONG).show();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
            int grantResultsLength = grantResults.length;
            if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveSignature();
            } else {
                Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
