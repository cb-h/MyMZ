package com.example.administrator.mymz;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<IMG>list;
    public MyAdapter(Context context, ArrayList<IMG> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.img_layout,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(context).
                load(list.get(position).getUrl())
                .asBitmap()
                .error(R.mipmap.error)
                .placeholder(R.mipmap.loading)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.img);
            int width = ((Activity) imageView.getContext()).getWindowManager().getDefaultDisplay().getWidth();
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            //设置图片的相对于屏幕的宽高比
            params.width = width/2;
            params.height =  (int) (width/2 + Math.random() * 100) ;
            imageView.setLayoutParams(params);

        }
    }
}
