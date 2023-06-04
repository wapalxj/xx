package com.leo.rv_recycler.decoration;

import android.os.Bundle;

import com.leo.rv_recycler.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DecorationActivity extends AppCompatActivity {

    private List<Star> starList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dec);

        init();

        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new StarAdapter(this, starList));

        // 添加自定义ItemDecoration
        recyclerView.addItemDecoration(new StarDecoration(this));
    }

    private void init() {
        starList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 20; j++) {
                if (i % 2 == 0) {
                    starList.add(new Star("类型1" + j, "标题" + i));
                } else {
                    starList.add(new Star("类型2" + j, "title" + i));
                }
            }
        }
    }
}