package com.youshi.oldtv;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 典杰 on 2017/8/6.
 */

public class ListHelper implements Serializable {
    private List<HashMap<String,String>> items;
    public ListHelper(List<HashMap<String,String>> item){
        this.items = item;
    }
    public List<HashMap<String,String>> getList(){
        return items;
    }
}
