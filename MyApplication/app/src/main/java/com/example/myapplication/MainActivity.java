package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

import android.util.*;
import android.os.*;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TextView myview = (TextView)findViewById(R.id.textview_first);
        myview.setText("This is life");
        //Initialize database and open it
        SQLiteOpenHelper dbh = DatabaseHelper.getmInstance(this);
        SQLiteDatabase db = dbh.getReadableDatabase();

        InputStreamReader is = null;
        try {
            InputStream input = getResources().openRawResource(R.raw.recipes);

//Get the text file
            //File file = new File(sdcard,"NewTextFile.txt");
            is = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();//读取每行
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replace("\"","");
                String token[] = line.split(",");
                int i = 0;
                int id = Integer.parseInt(token[0]);
                String title = token[1];
                if(token.length > 7){
                    title += "," + token[2];
                    i++;
                }
                String subtitle = token[2+i];
                Log.d("token 3 -->",token[3+i]);
                int ppl = Integer.parseInt(token[3+i]);
                double price = Double.parseDouble(token[4+i]);
                String web = token[5+i];
                String type = token[6+i];
                String sql = "insert into persons VALUES(" + id + ",'" +title+ "','" + subtitle + "'," + ppl + "," + price + ",'" + web + "','" + type + "')";
                db.execSQL(sql);
                Log.d("line -->",line);
                Log.d("line -->",""+token.length);
            }
            db.close();
        } catch (IOException e) {
            System.out.println("error information");
            e.printStackTrace();
        };


//        if (db.isOpen()) {
//            String sql = "insert into persons VALUES(6,'Asparagus & Asiago Frittata','',1,1.79,'https://spoonacular.com/recipeImages/632925-636x393.jpg','Breakfast')";
//            db.execSQL(sql);
//            Cursor cursor = db.rawQuery("select * from persons", null);
//            while (cursor.moveToNext()) {
//                //String name = cursor.getString(1);
//                int _id = cursor.getInt(0);
//                String title = cursor.getString(1);
//                Log.d("id-->","id " + _id+ " Name " + title);
//            }
//            cursor.close();
//            db.close();
//        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}