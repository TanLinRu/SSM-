/**
 * @program: SSM
 * @description: 名称外部比较器
 * @author: TLQ
 * @create: 2019-05-14 23:09
 **/
package com.tlq.ClassComparator;

import java.util.Comparator;
import java.util.Hashtable;

public class NameComparator implements Comparator {


    @Override
    public int compare(Object o1, Object o2) {
        Hashtable hashtable = (Hashtable) o1;
        Hashtable hashtable1 = (Hashtable) o2;
        if (((Boolean) hashtable.get("is_dir"))
                && !((Boolean)hashtable1.get("is_dir"))) {
            return -1;
        } else if (!((Boolean) hashtable.get("is_dir"))
                && ((Boolean)hashtable1.get("is_dir"))) {
            return 1;
        } else {
            return ((String) hashtable.get("filename"))
                    .compareTo((String) hashtable1.get("filename"));
        }
    }
}