package com.yvesmatta.hindgametracker;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.zip.Inflater;

public class HindListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = HindListFragment.class.getSimpleName();
    private CursorAdapter cursorAdapter;
    private boolean firstVisit;

    public HindListFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.hind_list_fragment, container, false);

        // Hide the back button
        hideBackButton();

        // Initialize the custom adapter
        cursorAdapter = new HindCursorAdapter(getActivity(), null, 0);

        // Attach the CursorAdapter to the list
        ListView list = (ListView) view.findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);
        // Initialize the loader
        getLoaderManager().initLoader(0, null, this);

        // Set firstVisit to this fragment to true
        firstVisit = true;

        // Return the new created view
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete_all:
                deleteAllGames();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllGames() {
        // Create a DialogInterface OnClickListener
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int button) {
                if (button == DialogInterface.BUTTON_POSITIVE) {
                    // Delete all games and players
                    getActivity().getContentResolver().delete(GameProvider.CONTENT_URI, null, null);
                    getActivity().getContentResolver().delete(PlayerProvider.CONTENT_URI, null, null);

                    // Restart the loader
                    restartLoader();

                    // Display a toast to the user
                    Toast.makeText(getActivity(), R.string.all_games_deleted, Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.are_you_sure)
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    public void hideBackButton() {
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firstVisit) {
            restartLoader();
        }
    }

    public void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), GameProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
