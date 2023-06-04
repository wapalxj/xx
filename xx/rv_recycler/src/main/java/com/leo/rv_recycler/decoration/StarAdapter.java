package com.leo.rv_recycler.decoration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leo.rv_recycler.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarViewHolder> {

    private Context context;
    private List<Star> starList;

    public StarAdapter(Context context, List<Star> starList) {
        this.context = context;
        this.starList = starList;
    }

    // 是否是组的第一个Item
    public boolean isFirstItemOfGroup(int position) {
        if(position == 0){
            return true;
        } else {
            // 拿到当前位置的和前一个位置的 组名
            String currentItemGroupName = getGroupName(position);
            String preItemGroupName = getGroupName(position - 1);
            // 如果相等，则表示position的item不是第一个，否则是的
            if (preItemGroupName.equals(currentItemGroupName)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public String getGroupName(int position) {
        return starList.get(position).getGrounpName();
    }

    @NonNull
    @Override
    public StarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_star, null);
        return new StarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StarViewHolder holder, int position) {
        holder.tv.setText(starList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return starList == null ? 0 : starList.size();
    }

    public class StarViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public StarViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_star);
        }
    }
}
