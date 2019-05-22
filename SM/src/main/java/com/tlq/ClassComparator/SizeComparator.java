/**
 * @program: SSM
 * @description: 文件大小排序
 * @author: TLQ
 * @create: 2019-05-14 23:21
 **/
package com.tlq.ClassComparator;

import java.util.Comparator;
import java.util.Hashtable;

public class SizeComparator implements Comparator {

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
            if (((Long) hashtable.get("filesize")) > ((Long) hashtable1
                    .get("filesize"))) {
                return 1;
            } else if (((Long) hashtable.get("filesize")) < ((Long) hashtable
                    .get("filesize"))) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}