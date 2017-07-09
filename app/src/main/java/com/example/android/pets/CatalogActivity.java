/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetsContract;
import com.example.android.pets.data.PetsContract.PetsEntry;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = CatalogActivity.class.getName();

    private static final int PET_LOADER = 0;

    PetCursorAdapter petCursorAdapter;

    private PetDbHelper mDbHelper;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        mDbHelper = new PetDbHelper(this);
        listView = (ListView)findViewById(R.id.petsList);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        petCursorAdapter = new PetCursorAdapter(this, null);
        listView.setAdapter(petCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(PetsEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });
        //displayDatabaseInfo();
//        PetDbHelper mDbHelper = new PetDbHelper(this);
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //Kick off the loader
        getLoaderManager().initLoader(PET_LOADER, null, this);
    }

    /*public void onStart(){
        super.onStart();
        displayDatabaseInfo();
    }*/

  /*  private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.


        // Create and/or open a database to read from it
       // SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {PetsEntry._ID, PetsEntry.COLUMN_PET_NAME, PetsEntry.COLUMN_PET_BREED, PetsEntry.COLUMN_PET_GENDER, PetsEntry.COLUMN_PET_WEIGHT};


        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        //Cursor cursor = db.query(PetsEntry.TABLE_NAME, null, null, null, null, null, null);
        //Cursor cursor = db.query(PetsEntry.TABLE_NAME, projection, null, null, null, null, null);
        Cursor cursor = getContentResolver().query(PetsEntry.CONTENT_URI, projection, null, null, null);

        petCursorAdapter = new PetCursorAdapter(this, cursor);
        listView.setAdapter(petCursorAdapter);

//        try {
//            PetCursorAdapter petCursorAdapter = new PetCursorAdapter(this, cursor);
//            listView.setAdapter(petCursorAdapter);
//        }
//            // Display the number of rows in the Cursor (which reflects the number of rows in the
//            // pets table in the database).
//
////            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
////            displayView.setText("Number of rows in pets database table: " + cursor.getCount());
//            // Create a header in the Text View that looks like this:
//
//            // The pets table contains <number of rows in Cursor> pets.
//            // _id - name - breed - gender - weight
//
//            // In the while loop below, iterate through the rows of the cursor and display
//            // the information from each column in this order.
////            displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");
////            displayView.append(PetsEntry._ID + " - " +
////                    PetsEntry.COLUMN_PET_NAME + " - " +
////                    PetsEntry.COLUMN_PET_BREED + " - " +
////                    PetsEntry.COLUMN_PET_GENDER + " - " +
////                    PetsEntry.COLUMN_PET_WEIGHT + "\n");
////
////            // Figure out the index of each column
////            int idColumnIndex = cursor.getColumnIndex(PetsEntry._ID);
////            int nameColumnIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PET_NAME);
////            int breedColumnIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PET_BREED);
////            int genderColumnIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PET_GENDER);
////            int weightColumnIndex = cursor.getColumnIndex(PetsEntry.COLUMN_PET_WEIGHT);
////
////            // Iterate through all the returned rows in the cursor
////                    while (cursor.moveToNext()) {
////                        // Use that index to extract the String or Int value of the word
////                        // at the current row the cursor is on.
////                        int currentID = cursor.getInt(idColumnIndex);
////                        String currentName = cursor.getString(nameColumnIndex);
////                        String currentBreed = cursor.getString(breedColumnIndex);
////                        int currentGender = cursor.getInt(genderColumnIndex);
////                        int currentWeight = cursor.getInt(weightColumnIndex);
////                        // Display the values from each column of the current row in the cursor in the TextView
////                        displayView.append(("\n" + currentID + " - " +
////                                currentName + " - " +
////                                currentBreed + " - " +
////                                currentGender + " - " +
////                                currentWeight));
////                    }       }
//
//          finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }
    } */



    private void insertPet(){
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PetsEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetsEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetsEntry.COLUMN_PET_GENDER, PetsEntry.GENDER_MALE);
        values.put(PetsEntry.COLUMN_PET_WEIGHT, "7");

        //long newRowId = db.insert(PetsEntry.TABLE_NAME, null, values);
        //Log.v(LOG_TAG, Long.toString(newRowId));

        Uri newUri = getContentResolver().insert(PetsEntry.CONTENT_URI, values);
       // Log.v(LOG_TAG, )
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                PetsEntry._ID,
                PetsEntry.COLUMN_PET_NAME,
                PetsEntry.COLUMN_PET_BREED };

        //This Loader will execute the ContentProvider's query method on a background thread.
        return new CursorLoader(this,
                PetsEntry.CONTENT_URI,
                projection,
                null,
                null,
                null );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //update {@link PetCursorAdapter} with this new cursor containing updated pet data
        petCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Callback called when the data needs to be deleted
        petCursorAdapter.swapCursor(null);

    }
    /**
     * Helper method to delete all pets in the database.
     */
    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(PetsEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }
}
