package com.example.administrator.mymz;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/14.
 */
public class Parseurl {

    public static final String url="http://www.dbmeinv.com/dbgroup/show.htm?pager_offset=";
    //解析图片的URL 以list集合的形式返回  传参是网页地址和页码pager_offset

    public static ArrayList<IMG> parseurl (final int pager){
        ArrayList<IMG>list=new ArrayList<>();
        String URL=url+pager;
        Log.d("----",">>>>>>>>>"+URL);
        try {
            Document document= Jsoup.connect(URL).timeout(10000).get();//10s的请求超时
            Elements es=document.select("[src]");

//            for (int i=1;i<es.size();i++){
//                if (es.get(i).tagName().equals("img")){
//                    Log.d("-----",es.get(i).attr("abs:src"));
//                    IMG img=new IMG();
//                    img.setUrl(es.get(i).attr("abs:src"));
//                    list.add(img);
//                }
//            }

            for (Element src:es){
                if (src.tagName().equals("img")){
                    Log.d("-----",src.attr("abs:src"));
                    IMG img=new IMG();
                    img.setUrl(src.attr("abs:src"));
                    list.add(img);
                }
            }



//            Element ele=document.select("div.main-container container").first();
//            Elements elements=ele.select("thumbnails");
//            for (int i=0;i<elements.size();i++){
//                IMG img=new IMG();
//                Element element=elements.get(i);
//                Element urlelement=null;
//                if (element.select("img").size()!=0){
//                    urlelement=element.select("img").first();
//                }
//                String myurl="";
//                if (urlelement!=null){
//                    myurl=urlelement.attr("src");
//                }
//                img.setUrl(myurl);
//                list.add(img);
//
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
