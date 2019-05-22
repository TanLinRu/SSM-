/**
 * @program: SSM
 * @description:Logger日志工具类
 * @author: TLQ
 * @create: 2019-03-29 15:37
 **/
package com.tlq.util;


import sun.reflect.Reflection;

import java.util.logging.Logger;

public class Log4JUtils {
    private static Logger  logger = null;

    public static Logger getLogger(){
        if (logger == null) {
            logger = Logger.getLogger(Reflection.getCallerClass().getName());
        }
        return logger;
    }
}