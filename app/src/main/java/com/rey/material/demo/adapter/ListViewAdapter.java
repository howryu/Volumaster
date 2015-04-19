package com.rey.material.demo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.rey.material.demo.MyDB;
import com.rey.material.demo.R;
import com.rey.material.demo.Rule;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private MyDB myDB;
    private List<Long> ruleIDs;
    private List <Rule> rules;

    public ListViewAdapter(Context mContext) {
        this.mContext = mContext;
        this.myDB = MyDB.getInstance(mContext);;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
//        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
//            @Override
//            public void onOpen(SwipeLayout layout) {
//                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
//            }
//        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click delete, position is " + position, Toast.LENGTH_SHORT).show();
                myDB.deleteById(ruleIDs.get(position).longValue());
                swipeLayout.close();
                // TODO need to reload
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView t = (TextView)convertView.findViewById(R.id.position);
//        Log.d("listview", "text = " + t.getText().toString());
        Rule rule = rules.get(position);
        String text;
        String str_volume_is = "Volume is ";
        String str_in = " in ";
        String str_from = " \nfrom ";
        String str_to = " to ";
        text = str_volume_is + rule.getVolume() + str_in + rule.getTitle() + str_from + rule.getStart_time() + str_to + rule.getEnd_time() + " " + rule.getDate();
        final SpannableStringBuilder str = new SpannableStringBuilder(text);
        int wordStart = str_volume_is.length() + rule.getVolume().length() + str_in.length();
        int wordEnd = str_volume_is.length() + rule.getVolume().length() + str_in.length() + rule.getTitle().length();

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

        wordStart = str_volume_is.length() + rule.getVolume().length() + str_in.length() + rule.getTitle().length() + str_from.length();
        wordEnd = str_volume_is.length() + rule.getVolume().length() + str_in.length() + rule.getTitle().length() + str_from.length() + rule.getStart_time().length();
        str.setSpan(
                new StyleSpan(Typeface.BOLD),
                wordStart,
                wordEnd,
                SpannableStringBuilder.SPAN_EXCLUSIVE_INCLUSIVE
        );
        str.setSpan(
                new RelativeSizeSpan(1.5f),
                wordStart,
                wordEnd,
                SpannableStringBuilder.SPAN_EXCLUSIVE_INCLUSIVE
        );
        t.setText(str);
    }

    @Override
    public int getCount() {
        rules = myDB.select();
        ruleIDs = new ArrayList<Long>();
        for (Rule r : rules) {
            ruleIDs.add(r.getId());
            Log.d("listview", r.getId() + ", " + r.getStart_time() + ", " + r.getEnd_time() + ", " + r.getVolume());
        }
        return rules.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
