package com.example.teja.connect4ai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class LogActivity extends AppCompatActivity {
    Spinner spinner;
    public int algo = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.algorithms_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MainActivity.algorithmSelection = 0;
        spinner.setAdapter(adapter);
    }
    public void startGame(View view){
        Intent i = new Intent(LogActivity.this, MainActivity.class);
        String text = spinner.getSelectedItem().toString();
        if (text.equals("Alpha Beta")){
            algo = 1;
        }else {
            algo = 0;
        }
        i.putExtra("Algorithm",algo);
        startActivity(i);
    }
}
