package com.example.administrator.mymz;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    public ArrayList<IMG> list;
    public int pager=1;
    public RecyclerView recyclerView;
    private MyAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private boolean isRefresh=false;
    private boolean isLoading=false;
    ArrayList<IMG> datalist=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog=new ProgressDialog(this);
        dialog.setTitle("正在找妹子");
        dialog.show();
        recyclerView= (RecyclerView) findViewById(R.id.recycle);
        refreshLayout= (SwipeRefreshLayout) findViewById(R.id.refresh);
        recyclerView.setHasFixedSize(true);
        final StaggeredGridLayoutManager layoutManager= new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MyAdapter(MainActivity.this,datalist);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSlidingToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState==RecyclerView.SCROLL_STATE_IDLE){
                    int [] lastVisiblePositions=layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);

                    int lastVisiblePos = getMaxElem(lastVisiblePositions);
                    int totalItemCount=layoutManager.getItemCount();

                    // 判断是否滚动到底部
                    if (lastVisiblePos == (totalItemCount -1) && isSlidingToLast&&!isLoading) {
                        //加载更多功能的代码
                        pager++;
                        setList(pager);
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if(dy > 0){
                    //大于0表示，正在向下滚动
                    isSlidingToLast = true;
                }else{
                    //小于等于0 表示停止或向上滚动
                    isSlidingToLast = false;
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                ReFresh();
            }
        });

        setList(pager);

    }

    public void setList(final int pager){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<IMG>list=new ArrayList<>();
                    String URL="http://www.dbmeinv.com/dbgroup/show.htm?pager_offset="+pager;
                    Log.d("----",">>>>>>>>>"+URL);
                    isLoading=true;
                    try {
                        Document document= Jsoup.connect(URL).timeout(10000).get();//10s的请求超时
                        Elements es=document.select("[src]");
                        for (Element src:es){
                            if (src.tagName().equals("img")){
                                Log.d("-----",src.attr("abs:src"));
                                IMG img=new IMG();
                                img.setUrl(src.attr("abs:src"));
                                list.add(img);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Message message=Message.obtain();
                    message.what=1;
                    message.obj=list;
                    handler.sendMessage(message);
                }
            }).start();
    }

    public void ReFresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<IMG>list=new ArrayList<>();
                String URL="http://www.dbmeinv.com/dbgroup/show.htm?pager_offset=1";
                Log.d("----",">>>>>>>>>"+URL);
                isRefresh=true;
                try {
                    Document document= Jsoup.connect(URL).timeout(10000).get();//10s的请求超时
                    Elements es=document.select("[src]");
                    for (Element src:es){
                        if (src.tagName().equals("img")){
                            Log.d("-----",src.attr("abs:src"));
                            IMG img=new IMG();
                            img.setUrl(src.attr("abs:src"));
                            list.add(img);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message message=Message.obtain();
                message.what=2;
                message.obj=list;
                handler.sendMessage(message);
            }
        }).start();
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    isLoading=false;
                    Log.d("----","<<<<<<<<<<setAdapter>>>>>>>");
                    for (int i=0;i<((ArrayList<IMG>) msg.obj).size();i++){
                        datalist.add(((ArrayList<IMG>) msg.obj).get(i));
                    }
                    //list= (ArrayList<IMG>) msg.obj;
                    adapter.notifyDataSetChanged();
                    if (dialog.isShowing()){
                        dialog.dismiss();
                    }
//                    SpacesItemDecoration decoration=new SpacesItemDecoration(16);
//                    recyclerView.addItemDecoration(decoration);
                    break;
                case 2:
                    isRefresh=false;
                    datalist.clear();
                    for (int i= 0; i<((ArrayList<IMG>) msg.obj).size(); i++){
                        datalist.add(((ArrayList<IMG>) msg.obj).get(i));
                    }
                    refreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };


    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i]>maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }
}
