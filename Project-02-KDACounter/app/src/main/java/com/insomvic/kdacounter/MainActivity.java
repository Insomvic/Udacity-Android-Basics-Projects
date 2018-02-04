package com.insomvic.kdacounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    // Global variables
    float team_a_kda = 0;
    int team_a_kills = 0;
    int team_a_assists = 0;
    int team_a_deaths = 0;
    float team_b_kda = 0;
    int team_b_kills = 0;
    int team_b_assists = 0;
    int team_b_deaths = 0;

    // Keep consistent decimal format
    DecimalFormat decimalFormat = new DecimalFormat("0.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Team A Kills
    public void teamAKillIncrease(View view) {
        team_a_kills ++;
        displayTeamAKills(team_a_kills);
    }

    public void teamAKillDecrease(View view) {
        if (team_a_kills > 0) {
            team_a_kills--;
            displayTeamAKills(team_a_kills);
        }
    }

    private void displayTeamAKills(int kills) {
        TextView quantityTextView = findViewById(R.id.team_a_kills);
        quantityTextView.setText("Kills: " + kills);
        displayTeamAKDA();
    }

    // Team A Assists
    public void teamAAssistIncrease(View view) {
        team_a_assists ++;
        displayTeamAAssists(team_a_assists);
    }

    public void teamAAssistDecrease(View view) {
        if (team_a_assists > 0) {
            team_a_assists--;
            displayTeamAAssists(team_a_assists);
        }
    }

    private void displayTeamAAssists(int assists) {
        TextView quantityTextView = findViewById(R.id.team_a_assists);
        quantityTextView.setText("Assists: " + assists);
        displayTeamAKDA();
    }

    // Team A Deaths
    public void teamADeathIncrease(View view) {
        team_a_deaths ++;
        displayTeamADeaths(team_a_deaths);
    }

    public void teamADeathDecrease(View view) {
        if (team_a_deaths > 0) {
            team_a_deaths--;
            displayTeamADeaths(team_a_deaths);
        }
    }

    private void displayTeamADeaths(int deaths) {
        TextView quantityTextView = findViewById(R.id.team_a_deaths);
        quantityTextView.setText("Deaths: " + deaths);
        displayTeamAKDA();
    }

    // Team A KDA
    private void displayTeamAKDA() {
        if (team_a_deaths > 0) {
            team_a_kda = (team_a_kills + team_a_assists) / ((float) team_a_deaths);
        } else {
            team_a_kda = (team_a_kills + team_a_assists);
        }
        TextView quantityTextView = findViewById(R.id.team_a_kda);
        String output = decimalFormat.format(team_a_kda);
        quantityTextView.setText("" + output);
    }

    // Team B Kills
    public void teamBKillIncrease(View view) {
        team_b_kills ++;
        displayTeamBKills(team_b_kills);
    }

    public void teamBKillDecrease(View view) {
        if (team_b_kills > 0) {
            team_b_kills--;
            displayTeamBKills(team_b_kills);
        }
    }

    private void displayTeamBKills(int kills) {
        TextView quantityTextView = findViewById(R.id.team_b_kills);
        quantityTextView.setText("Kills: " + kills);
        displayTeamBKDA();
    }

    // Team B Assists
    public void teamBAssistIncrease(View view) {
        team_b_assists ++;
        displayTeamBAssists(team_b_assists);
    }

    public void teamBAssistDecrease(View view) {
        if (team_b_assists > 0) {
            team_b_assists--;
            displayTeamBAssists(team_b_assists);
        }
    }

    private void displayTeamBAssists(int assists) {
        TextView quantityTextView = findViewById(R.id.team_b_assists);
        quantityTextView.setText("Assists: " + assists);
        displayTeamBKDA();
    }

    // Team B Deaths
    public void teamBDeathIncrease(View view) {
        team_b_deaths ++;
        displayTeamBDeaths(team_b_deaths);
    }

    public void teamBDeathDecrease(View view) {
        if (team_b_deaths > 0) {
            team_b_deaths--;
            displayTeamBDeaths(team_b_deaths);
        }
    }

    private void displayTeamBDeaths(int deaths) {
        TextView quantityTextView = findViewById(R.id.team_b_deaths);
        quantityTextView.setText("Deaths: " + deaths);
        displayTeamBKDA();
    }

    // Team B KDA
    private void displayTeamBKDA() {
        if (team_b_deaths > 0) {
            team_b_kda = (team_b_kills + team_b_assists) / ((float) team_b_deaths);
        } else {
            team_b_kda = (team_b_kills + team_b_assists);
        }
        TextView quantityTextView = findViewById(R.id.team_b_kda);
        String output = decimalFormat.format(team_b_kda);
        quantityTextView.setText("" + output);
    }

    // Reset Scores
    public void resetScores(View view) {
        team_a_kda = 0;
        team_a_kills = 0;
        team_a_assists = 0;
        team_a_deaths = 0;
        displayTeamAKills(team_a_kills);
        displayTeamAAssists(team_a_assists);
        displayTeamADeaths(team_a_deaths);
        team_b_kda = 0;
        team_b_kills = 0;
        team_b_assists = 0;
        team_b_deaths = 0;
        displayTeamBKills(team_b_kills);
        displayTeamBAssists(team_b_assists);
        displayTeamBDeaths(team_b_deaths);
    }
}
