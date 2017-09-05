package com.youshi.oldtv;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 典杰 on 2017/8/5.
 */

public class ParseXml implements ItemParser {

    @Override
    public List<HashMap<String, String>> parseTvitems(InputStream isr) throws Exception {
        List<HashMap<String,String>> items = null;
        HashMap<String,String> map=null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(isr,"utf-8");
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                   items = new ArrayList<HashMap<String, String>>();
                    break;
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("item")) {
                        map = new HashMap<String,String>();
                    } else if (parser.getName().equals("number")) {
                        //eventType = parser.next();
                        map.put("number",parser.nextText());
                    } else if (parser.getName().equals("title")) {
                       // eventType = parser.next();
                       map.put("title",parser.nextText());
                    } else if (parser.getName().equals("url")) {
                      //  eventType = parser.next();
                       map.put("url",parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("item")) {
                        assert items!=null;
                        items.add(map);
                        map = null;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return items;
    }

    @Override
    public List<HashMap<String, String>> parseTvitems(InputStreamReader isr) throws Exception {
        List<HashMap<String,String>> items = null;
        HashMap<String,String> map=null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(isr);
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    items = new ArrayList<HashMap<String, String>>();
                    break;
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("item")) {
                        map = new HashMap<String,String>();
                    } else if (parser.getName().equals("number")) {
                        //eventType = parser.next();
                        map.put("number",parser.nextText());
                    } else if (parser.getName().equals("title")) {
                        // eventType = parser.next();
                        map.put("title",parser.nextText());
                    } else if (parser.getName().equals("url")) {
                        //  eventType = parser.next();
                        map.put("url",parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("item")) {
                        assert items!=null;
                        items.add(map);
                        map = null;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return items;
    }
}
