/*
 * Created on 2007-4-26
 */
package com.coolsql.pub.component;

import java.util.Vector;

import javax.swing.ComboBoxModel;

/**
 * @author liu_xlin
 *该下拉框编辑组件将提供历史纪录，即将先前的输入值进行保存，便于以后的直接选择。
 */
public class RecordEditCombBox extends EditComboBox {

    /**
     * 允许保存历史输入的最大数。
     */
    private int maxItems;
    public RecordEditCombBox()
    {
        super();
        init();
    }
    public RecordEditCombBox(ComboBoxModel model)
    {
        super(model);
        init();
    }
    public RecordEditCombBox(Object[] items)
    {
        super(items);
        init();
    }
    public RecordEditCombBox(Vector items)
    {
        super(items);
        init();
    }
    /**
     * 初始化
     *
     */
    protected void init()
    {
        maxItems=10; //初始值允许保存10项
        setEditable(true);
    }
    /**
     * 将当前选择值添加入选择列表中，如果已经存在，则将其放置在第一个位置
     *
     */
    public void record()
    {
        String str=getSelectedItem().toString();
        if(str.equals(""))  //如果输入值为空串将不作保存
            return;
        
        removeItem(str);
        
        if(getItemCount()>=maxItems)  //如果超出最大保存数量，删除最后一项
            removeItemAt(getItemCount()-1);
        
        insertItemAt(str,0);
        
        setSelectedIndex(0);
    }

    /**
     * @return Returns the maxItems.
     */
    public int getMaxItems() {
        return maxItems;
    }
    /**
     * @param maxItems The maxItems to set.
     */
    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }
}
