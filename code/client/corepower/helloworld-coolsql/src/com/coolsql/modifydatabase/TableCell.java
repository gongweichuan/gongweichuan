/*
 * Created on 2007-1-12
 */
package com.coolsql.modifydatabase;

import java.awt.Color;

import javax.swing.JTable;

/**
 * @author liu_xlin
 *表格元素接口，用来生成insert或update类型的sql语句
 */
public interface TableCell {

    /**
     * 如果该表格的显示,在前景色上需要渲染,使用此方法获取前景色.如果返回null,表示该表格前景色不需要渲染
     * @return  --表格需要渲染的前景色
     */
    public abstract Color getRenderOfForeground();
    
    /**
     * 获取背景色的渲染颜色,如果返回null,表示该表格被景色不需要渲染
     * @return --表格被景色的渲染颜色
     */
    public abstract Color getRenderOfBackground();
    /**
     * 表格元素是否可以编辑
     * @return --true：可编辑  false:不可编辑
     */
    public abstract boolean isEditable();
    
    /**
     * 获取表格元素所包含的对象值
     * @return
     */
    public abstract Object getValue();
    
    /**
     * 定义该表格所对应的编辑组件类型
     * @return 该返回值与类EditorFactory中组件类型的定义一致
     */
    public abstract String getEditorType();
    
    /**
     * 该方法返回表格对象所属的表控件
     * @return
     */
    public abstract JTable getTable();
    
    /**
     * 定义了该表格元素所对应的字段对象是否需要被更该
     * 1、如果在进行创建插入sql时，该方法反映了对应字段是否需要被作为一个有效字段（被赋予一个合法值）插入数据库中
     * 2、如果在进行创建更新sql时，该方法反映了需要被更新的字段。
     * @return true：该字段将被作为插入或者更新的字段加入sql中。 false：不做任何的调整
     */
    public abstract boolean isNeedModify();
    
    /**
     * 当鼠标放置在该表格之上时，需要相应的提示信息，该方法提供了需要显示的文本内容
     * @return  --提示文本信息
     */
    public abstract String getToolTip();
    
    /**
     * 展示表格元素时的风格文本,通常以静态html格式进行返回
     * @return --带有html格式的字符串
     */
    public abstract String getStyleString();
    
    /**
     * 是否作为更新条件。如果在更新行数据时，需要将某一字段作为更新条件，可定制该方法
     * @return  --true：将作为更新条件  false:不作为更新条件
     */
    public abstract boolean isAsTerm();
    
    /**
     * 表格元素对象有效值是否是null
     * @return  true:null  false:非null
     */
    public abstract boolean isNullValue();
}
