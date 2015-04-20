package com.rey.material.demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.widget.Button;

import java.util.ArrayList;

/**
 * Created by howryu on 4/19/15.
 */
public class SelectImportActivity extends ActionBarActivity {
    private ArrayList<String> allEvents;

    private ArrayList<Boolean> checked;
    private SelectImportActivity ac;

     
    MyCustomAdapter dataAdapter = null;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ac = this;

        setContentView(R.layout.select_import);
        Intent intent = getIntent();
        allEvents = intent.getStringArrayListExtra(MainActivity.EVENTSLIST);
        checked = new ArrayList<Boolean>(allEvents.size());
        for(int i=0; i<allEvents.size(); i++){
            checked.add(Boolean.TRUE);
        }
        Log.d("Select", "check size is" + checked.size());
        Log.d("Select", "event size is" + allEvents.size());
         
          //Generate list View from ArrayList
        displayListView();
         
        checkButtonClick();
         
    }
     
    private void displayListView() {
         
        //Array list of countries

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.eventinfo, allEvents);
        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new OnItemClickListener() {
           public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                // When clicked, show a toast with the TextView text
                String event = (String) parent.getItemAtPosition(position);
                /*Toast.makeText(getApplicationContext(),
                          "Clicked on Row: " + event,
                          Toast.LENGTH_LONG).show();*/


               }
          });

        }
     
        private class MyCustomAdapter extends ArrayAdapter<String> {

            private ArrayList<String> eventList;

            public MyCustomAdapter(Context context, int textViewResourceId,
                                     ArrayList<String> allEvents) {
                super(context, textViewResourceId, allEvents);
                this.eventList = new ArrayList<String>();
                this.eventList.addAll(allEvents);
            }

            private class ViewHolder {
                TextView code;
                CheckBox name;
            }
         
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
             
               ViewHolder holder = null;
               Log.v("ConvertView", String.valueOf(position));
             
               if (convertView == null) {
                   LayoutInflater vi = (LayoutInflater)getSystemService(
                             Context.LAYOUT_INFLATER_SERVICE);
                   convertView = vi.inflate(R.layout.eventinfo, null);
                 
                   holder = new ViewHolder();
                   holder.code = (TextView) convertView.findViewById(R.id.code);
                   holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                   convertView.setTag(holder);
                 
                   holder.name.setOnClickListener( new View.OnClickListener() {
                             public void onClick(View v) { 
                          CheckBox cb = (CheckBox) v ; 
                          int eventPosition = (int) cb.getTag();
                          /*Toast.makeText(getApplicationContext(),
                                   "Clicked on Checkbox: " + cb.getText() +
                                   " is " + cb.isChecked(),
                                   Toast.LENGTH_LONG).show();*/
                          checked.set(eventPosition, cb.isChecked());
                          //String.setSelected(cb.isChecked());
                         } 
                   });
               }
               else {
                   holder = (ViewHolder) convertView.getTag();
               }
             
               String event = allEvents.get(position);


                String[] info = event.split("#");
                String summary = info[0];
                String date = info[1].substring(0, 10);
                String start = info[1].substring(11, 16);
                String end = info[2].substring(11, 16);
                Rule rule = new Rule(summary, date, start, end, "0");

                String text;
                String str_new_line = "\n";
                String str_tab = "  ";
                text = rule.getStart_time() + str_tab + rule.getTitle() + str_new_line + rule.getEnd_time() + str_tab + rule.getDate();
                final SpannableStringBuilder str = new SpannableStringBuilder(text);

                int wordStart = rule.getStart_time().length() + str_tab.length();
                int wordEnd = rule.getStart_time().length() + str_tab.length() + rule.getTitle().length();

                str.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        wordStart,
                        wordEnd,
                        SpannableStringBuilder.SPAN_EXCLUSIVE_INCLUSIVE
                );
                str.setSpan(
                        new RelativeSizeSpan(2f),
                        wordStart,
                        wordEnd,
                        SpannableStringBuilder.SPAN_EXCLUSIVE_INCLUSIVE
                );
                holder.code.setText(str);
               holder.name.setText("");

               holder.name.setChecked(true);
               holder.name.setTag(position);
             
               return convertView;
             
            }
         
        }
     
        private void checkButtonClick() {

            Button myButton = (Button) findViewById(R.id.button_import_selected);
            myButton.setOnClickListener(new View.OnClickListener() {
             
            @Override
            public void onClick(View v) {
                String responseText = "";
                ArrayList<String> checkedEvent = new ArrayList<String>();
                for (int i=0; i<checked.size(); i++){
                    if (checked.get(i).equals(Boolean.TRUE)){
                        responseText += allEvents.get(i);
                        responseText += "is checked\n";
                        checkedEvent.add(allEvents.get(i));
                    }
                }


                Intent intent = new Intent(ac, MainActivity.class);
                intent.putStringArrayListExtra(MainActivity.CHECKED, checkedEvent);
                startActivity(intent);

               }
            });
         
         }
}
