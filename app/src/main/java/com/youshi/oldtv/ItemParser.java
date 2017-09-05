package com.youshi.oldtv;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 典杰 on 2017/8/5.
 */

public interface ItemParser {
    public  List<HashMap<String,String>> parseTvitems(InputStream isr) throws Exception;
    public  List<HashMap<String,String>> parseTvitems(InputStreamReader isr) throws Exception;
}
