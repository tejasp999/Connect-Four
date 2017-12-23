package com.example.teja.connect4ai;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridLayout gridBoard;
    public Board board;
    public TextView playerScoreText, cpuScoreText;
    public int playerScr, cpuScr;
    public static AlertDialog.Builder builder;
    public static boolean inLoop = false;
    public static ImageButton button;
    public int count = 0;
    public static int algorithmSelection;
    List<ImageButton> buttons = new ArrayList<ImageButton>();
    List<ImageButton> checkButtons = new ArrayList<ImageButton>();
    public static int Player1 = 1;
    public static int Player2 = 2;
    public static int player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerScoreText = findViewById(R.id.playerscore);
        cpuScoreText = findViewById(R.id.cpuscore);
        gridBoard = (GridLayout) findViewById(R.id.board);
        if (getIntent().getExtras()!=null){
            algorithmSelection = getIntent().getIntExtra("Algorithm",0);
        }
        if (algorithmSelection==0){
            board = new Board(6,7,4, MainActivity.this,false);
        }else {
            board = new Board(6,7,4, MainActivity.this,true);
        }
        player = Player2;
        for(int i = 0; i < gridBoard.getChildCount(); i++) {
            ImageButton button = (ImageButton) gridBoard.getChildAt(i);
            button.setImageResource(R.mipmap.empty_tile);
            String tag = (String)button.getTag();
            int row = Character.digit(tag.charAt(0), 10);
            int col = Character.digit(tag.charAt(1), 10);
            buttons.add(button);
        }
    }
    //On click of the tiles in the grid layout
    public void setImage(View view){
        ImageButton getButton = (ImageButton) view;
        String tag = (String)view.getTag();
        int row = Character.digit(tag.charAt(0), 10);
        Log.d("The row is","coming"+row);
        int col = Character.digit(tag.charAt(1), 10);
        Log.d("The col is","coming"+col);
        board.PlaceCoin(col-1, MainActivity.Player2);
        board.CPUPlayer();
    }
    //Used to check the Win condition and display the winner
    public void DisplayMessage(int winner) {
        if(winner == MainActivity.Player1) {
            Toast.makeText(this, "CPU wins",Toast.LENGTH_LONG).show();
            for (ImageButton b: buttons){
                b.setClickable(false);
            }
            int cpuSc = Integer.parseInt(cpuScoreText.getText().toString());
            cpuSc++;
            cpuScoreText.setText(String.valueOf(cpuSc));
        } else if(winner == MainActivity.Player2) {
            Toast.makeText(this, "Player wins",Toast.LENGTH_LONG).show();
            for (ImageButton b: buttons){
                b.setClickable(false);
            }
            int playerSc = Integer.parseInt(playerScoreText.getText().toString());
            playerSc++;
            playerScoreText.setText(String.valueOf(playerSc));
        } else if (winner == 0) {
            Toast.makeText(this, "Game Draw",Toast.LENGTH_LONG).show();
        }
    }
    //Set Image from the Board
    public void setImageView(int row, int col, int ply){
        String tag = row + "" + col;
        for (ImageButton b : buttons) {
            if (b.getTag().toString().equals(tag)) {
                button = b;
            }
        }
        if (ply == MainActivity.Player1) {
            button.setImageResource(R.mipmap.yellow);
        }else if(ply == MainActivity.Player2){
            button.setImageResource(R.mipmap.red);
        }
    }
    //Reset the UI once a player or CPU wins
    public void resetUI(View view){
        playerScr = Integer.parseInt(playerScoreText.getText().toString());
        cpuScr = Integer.parseInt(cpuScoreText.getText().toString());
        for (ImageButton b : buttons) {
            b.setImageResource(R.mipmap.empty_tile);
            b.setClickable(true);
        }
        if (algorithmSelection==0){
            board = new Board(6,7,4, MainActivity.this,false);
        }else {
            board = new Board(6,7,4, MainActivity.this,true);
        }
    }
    //Quit the game as per the user wish
    public void quitGame(View view){
        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Do you really want to quit the game?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MainActivity.this, LogActivity.class);
                startActivity(i);
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("delete","do not delete");
            }
        }).setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
