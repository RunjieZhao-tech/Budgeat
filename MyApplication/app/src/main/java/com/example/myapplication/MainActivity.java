package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setSupportActionBar(binding.toolbar);

        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        //appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        LinearLayout ll = findViewById(R.id.recipes);
        ImageView iv = findViewById(R.id.image1);
        Button btnTag = new Button(this);
        btnTag.setText("Button 1");
        Button btntag1 = new Button(this);
        btntag1.setText("Button 2");
//        ll.addView(btntag1);
//        ll.addView(btnTag);
        Glide.with(this).load("https://spoonacular.com/recipeImages/632925-636x393.jpg").into(iv);
        //myview.setText("This is life");
        //Initialize database and open it
        SQLiteOpenHelper dbh = DatabaseHelper.getmInstance(this);
        SQLiteDatabase db = dbh.getReadableDatabase();

        //read cvs and input data into database if the data is already inputted, then we will not input the data
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select * from persons",null);
            if(!cursor.moveToNext()){
                input_data(db);
            }
        }

        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from persons", null);
            while (cursor.moveToNext()) {
                //String name = cursor.getString(1);
                int _id = cursor.getInt(0);
                String title = cursor.getString(1);
                Log.d("id-->","id " + _id+ " Name " + title);
            }
            cursor.close();
            db.close();
        }


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

    public void input_data(SQLiteDatabase db){
        try {
            InputStreamReader is = null;
            InputStreamReader is_ingre = null;
            InputStream input = getResources().openRawResource(R.raw.recipes);
            InputStream ingre_input = getResources().openRawResource(R.raw.recipe_ingredients);
            is = new InputStreamReader(input, Charset.forName("UTF-8"));
            is_ingre = new InputStreamReader(ingre_input, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(is);
            BufferedReader reader_ingre = new BufferedReader(is_ingre);
            reader.readLine();//读取每行
            String line;
            //input recipe data;
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

            //input recipe ingredient data
            int i = 0;
            while ((line = reader_ingre.readLine()) != null) {
                if(i == 0){
                    i++;
                    continue;
                }
                line = line.replace("\"","");
                String token[] = line.split(",");
                int recipe_id = Integer.parseInt(token[0]);
                int ingre_id = Integer.parseInt(token[1]);
                String name = token[2];
                Log.d("token 3 -->",token[3]);
                double quantity = Double.parseDouble(token[3]);
                String measure = token[4];
                String sql = "insert into ingredients VALUES(" + recipe_id + "," +ingre_id+ ",'" + name + "'," + quantity + ",'" + measure + "')";
                db.execSQL(sql);
                Log.d("line -->",line);
                Log.d("line -->",""+token.length);
            }
            db.close();
        } catch (IOException e) {
            System.out.println("error information");
            e.printStackTrace();
        };
    }
}