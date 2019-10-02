package com.goaffilate.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class NewsActivity extends AppCompatActivity {
    String newtitle, newdesi;
    TextView newdesi_text,newtitle_text;
    Toolbar toolbar;
int padding=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        newdesi_text = findViewById(R.id.news_des);
        newtitle_text = findViewById(R.id.title_new);

        newtitle = getIntent().getStringExtra("newsheading");
        newdesi = getIntent().getStringExtra("newsdesi");

        newtitle_text.setText(newtitle);
        newdesi_text.setText(newdesi);

    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;
        }
        return true;
    }

}
