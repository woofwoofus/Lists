package com.example.jgeestman.whattodo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class WhatToDo extends Activity {

    ArrayList<String> thingsToDo;
    ArrayAdapter<String> myAdapter;
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_to_do);

        thingsToDo = new ArrayList<String>();

        try {
            Scanner scan = new Scanner(openFileInput("todolist.txt"));
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                System.out.println(line);
                thingsToDo.add(line);
            }
            System.out.println("File found");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        myAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, thingsToDo);
        myList = (ListView) findViewById(R.id.listView);
        myList.setAdapter(myAdapter);

        myList.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        System.out.println("pos: " + position + " --- id: " + id);
                        thingsToDo.remove(position);
                        updateSaveFile();
                        myAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_what_to_do, menu);
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

    public void commitNewItem(View view) {
        String newItem = ((TextView) findViewById(R.id.newItemEditText)).getText().toString();
        if (! newItem.equals("")){
            thingsToDo.add(newItem);
            myAdapter.notifyDataSetChanged();

            updateSaveFile();
        }
    }

    public void updateSaveFile() {
        try {
            PrintStream saveFile = new PrintStream(openFileOutput("todolist.txt", MODE_PRIVATE));
            saveFile.flush();
            for (int i=0; i<thingsToDo.size(); i++) {
                saveFile.println(thingsToDo.get(i));
            }
            saveFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
