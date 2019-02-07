package com.mua.mua.cuwallpaperchanger;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartActivity extends AppCompatActivity {


    int ser=0;

    List<String> imagesList=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        try {
            imagesList=getImage(getBaseContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Button button=findViewById(R.id.click);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {

                    ser=(++ser)%imagesList.size();

                    setNextWallpaper();
                    //changeWallpaper(imagesList.get(ser));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        final GridView gridView = (GridView) findViewById(R.id.grid);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, imagesList);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
            }
        });



        startRepeatingTask();



    }



    //private final static int INTERVAL = 1000 * 60 * 2; //2 minutes
    private final static int INTERVAL = 1000 * 10 ; //2 minutes
    Handler mHandler = new Handler();

    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {

            ser=(++ser)%imagesList.size();
            setNextWallpaper();
            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };

    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    void stopRepeatingTask()
    {
        mHandler.removeCallbacks(mHandlerTask);
    }






    private void setNextWallpaper(){
        try
        {
            Toast.makeText(getBaseContext(),imagesList.get(ser),Toast.LENGTH_SHORT).show();
            changeWallpaper(imagesList.get(ser));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private List<String> getImage(Context context) throws IOException {
        /*
        AssetManager assetManager = context.getAssets();
        String[] files = assetManager.list("assets");
        List<String> it = Arrays.asList(files);
        return it;
        */
        String[] images =getAssets().list("");
        List<String> imgs=new ArrayList<>();
        for(String x:images)
        {
            if(x.endsWith(".jpg"))
            {
                imgs.add(x);
            }
        }
        /*
        for (String x:images)
        {
            Log.v("images get",x);
        }
        */
        return imgs;
    }

    private void changeWallpaper(String name){
        try {

            WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(getApplicationContext());
            InputStream ims = getAssets().open(name);
            myWallpaperManager.setStream(ims);
        } catch (IOException e) {

            //Toast.makeText(getBaseContext(),ser+"  "+imagesList.size(),Toast.LENGTH_LONG).show();
            //Toast.makeText(getBaseContext(),"error",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


}
