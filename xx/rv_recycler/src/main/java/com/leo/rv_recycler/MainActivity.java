package com.leo.rv_recycler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.LinearLayout;

import com.leo.rv_recycler.decoration.DecorationActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.TraceCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView缓存机制
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Debug.startMethodTracing("test_rv");
        TraceCompat.beginSection("systrace_rv");
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.rv);

        rv.setLayoutManager(new GridLayoutManager(this, 1));
        rv.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add("" + i);
        }

        final CustomAdapter adapter = new CustomAdapter(this, list);
        rv.setAdapter(adapter);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        Debug.stopMethodTracing();
        TraceCompat.endSection();
    }

    public void next(View view) {
        Intent intent=new Intent(this, DecorationActivity.class);
        startActivity(intent);
    }
}