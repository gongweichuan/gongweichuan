﻿/*
 * Created on 2007-2-7
 */
package com.coolsql.view.sqleditor.pop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * @author liu_xlin
 * 弹出List控件，展示相关数据。
 */
public class PopListModel extends AbstractListModel {

	private static final long serialVersionUID = 1L;
	private List<?> data;
    public PopListModel(Object[] data)
    {
        this.data=Arrays.asList(data);
    }
    public PopListModel(List<Object> data)
    {
        this.data=data;
    }
    /* (non-Javadoc)
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        
        return data.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index) {
        
        return data.get(index);
    }
    /**
     * 整体更新模型中的数据
     * @param data  --新数据
     */
    public void setData(Object[] data)
    {
        this.data=Arrays.asList(data);
        fireContentsChanged(this,0,data.length-1);
    }
    public void setData(List<?> data)
    {
        this.data=data;
        fireContentsChanged(this,0,data.size()-1);
    }
    /**
     * 清空数据
     *
     */
    public void clear()
    {
        if(data!=null&&data.size()>0)
        {
            
            fireContentsChanged(this,0,data.size()-1);
            data=new ArrayList<Object>();
        }
    }
}
