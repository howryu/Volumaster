package com.rey.material.demo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
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

import java.util.List;

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private MyDB myDB;
    private List <Rule> rules;

    public ListViewAdapter(Context mContext) {
        this.mContext = mContext;
        this.myDB = MyDB.getInstance(mContext);
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
                rules = myDB.select();
                Toast.makeText(mContext, "click delete, id = " + rules.get(position).getId(), Toast.LENGTH_SHORT).show();
                rules.remove(position);
                myDB.deleteById(rules.get(position).getId());
                swipeLayout.close();
//                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
                // TODO need to reload
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView t = (TextView)convertView.findViewById(R.id.position);
        Rule rule = rules.get(position);
        Log.d("listviewDebug", String.valueOf(position));
        String text;
        String str_new_line = "\n";
        String str_tab = "  |   ";
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

        t.setText(str);
    }

    @Override
    public int getCount() {
//        Log.d("liseviewDebug", "getCount call");
        rules = myDB.select();
        return rules.size();
//        return 5;
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
