package com.yvesmatta.hindgametracker;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.yvesmatta.hindgametracker.Fragments.HindListFragment;
import com.yvesmatta.hindgametracker.Fragments.HindScoreboardFragment;
import com.yvesmatta.hindgametracker.Fragments.HindSetupFragment;
import com.yvesmatta.hindgametracker.Models.Game;

public class MainActivity extends AppCompatActivity {

    // Fragments
    private static FragmentManager fragmentManager;
    private static HindListFragment hindListFragment;
    private static HindSetupFragment hindSetupFragment;
    private static HindScoreboardFragment hindScoreboardFragment;

    // Game
    public static Game game = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get instance of the FragmentManager to add/remove fragments
        fragmentManager = getFragmentManager();

        // Add fragment to frgContainer and commit the transaction
        hindListFragment = new HindListFragment();
        fragmentManager.beginTransaction()
                .add(R.id.frgContainer, hindListFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Store the id that was clicked
        int id = item.getItemId();

        // Switch to find which item was clicked
        switch (id) {
            case android.R.id.home:
                // Call the override method
                onBackPressed();
                break;
        }

        // Return the supers class method call
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Skip the hindSetupFragment when the hindScoreboardFragment is visible on back pressed
        if (hindScoreboardFragment != null && hindScoreboardFragment.isVisible()) {
            if (hindScoreboardFragment.onBackPressed()) {
                fragmentManager.beginTransaction()
                        .replace(R.id.frgContainer, hindListFragment)
                        .commit();
            }
        } else {
            super.onBackPressed();
        }
    }

    // Load the GameSetupFragment from list view
    public void loadGameSetupFragment(View view) {
        // Replace fragment in frgContainer with new fragment and commit the transaction
        hindSetupFragment = new HindSetupFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.frgContainer, hindSetupFragment)
                .addToBackStack(hindListFragment.getClass().getName())
                .commit();
    }

    // Load the GamePlayFragment from setup view
    public void loadScoreboardFragment(View view) {
        // Assign the game
        game = hindSetupFragment.setupGame();

        // Ensure the game is not null before continuing
        if (game != null) {
            // Replace fragment in frgContainer with new fragment and commit the transaction
            hindScoreboardFragment = new HindScoreboardFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.frgContainer, hindScoreboardFragment)
                    .commit();
        }
    }

    // Call the hindScoreboardFragment method from the scoreboard view
    public void fragmentAddRound(View view) {
        hindScoreboardFragment.addRound(view);
    }
}
