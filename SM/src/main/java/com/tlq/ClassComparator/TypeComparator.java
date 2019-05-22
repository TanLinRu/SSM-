/**
 * @program: SSM
 * @description: 类型排序
 * @author: TLQ
 * @create: 2019-05-14 23:23
 **/
package com.tlq.ClassComparator;

import java.util.Comparator;
import java.util.Hashtable;

public class TypeComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Hashtable hashtable = (Hashtable) o1;
        Hashtable hashtable1 = (Hashtable) o2;
        if (((Boolean) hashtable.get("is_dir"))
                && !((Boolean) hashtable1.get("is_dir"))) {
            return -1;
        } else if (!((Boolean) hashtable.get("is_dir"))
                && ((Boolean) hashtable1.get("is_dir"))) {
            return 1;
        } else {
            return ((String) hashtable.get("filetype"))
                    .compareTo((String) hashtable1.get("filetype"));
        }
    }
}