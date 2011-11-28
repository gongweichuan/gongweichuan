/*
 * 创建日期 2006-10-15
 */
package com.coolsql.pub.display;

import java.util.Vector;

/**
 * @author liu_xlin
 * 完成数据的转换
 */
public class DataTran {
    /**
     * 将二维数组转化为向量集合
     * @param anArray
     * @return
     */
    public static Vector<Object> convertToVector(Object[][] anArray) {
        if (anArray == null) {
            return null;
	}
        Vector<Object> v = new Vector<Object>(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.addElement(convertToVector(anArray[i]));
        }
        return v;
    }
    /**
     * 将一维数组转化为向量集合
     * @param anArray
     * @return
     */
    protected static Vector<Object> convertToVector(Object[] anArray) {
        if (anArray == null) { 
            return null;
	}
        Vector<Object> v = new Vector<Object>(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.addElement(anArray[i]);
        }
        return v;
    }
}
