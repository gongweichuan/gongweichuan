/*
 * 创建日期 2006-12-11
 */
package com.coolsql.bookmarkBean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * @author liu_xlin 该类针对装载了书签列表的下拉框控件，当书签删除、添加、别名调整时，分别对控件的显示进行调整
 */
public class BookmarkUpdateOfComboBoxListener implements BookmarkListener,
        PropertyChangeListener {

    /**
     * 装载书签别名的下拉框控件
     */
    private JComboBox box = null;

    public BookmarkUpdateOfComboBoxListener(JComboBox box) {
        super();
        this.box = box;
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkAdded(com.coolsql.bookmarkBean.BookMarkEvent)
     */
    public void bookmarkAdded(BookmarkEvent e) {
        e.getBookmark().addPropertyListener(this);
        DefaultComboBoxModel model = (DefaultComboBoxModel) box.getModel();
        model.addElement(e.getBookmark().getAliasName());
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkDeleted(com.coolsql.bookmarkBean.BookMarkEvent)
     */
    public void bookmarkDeleted(BookmarkEvent e) {
        e.getBookmark().removePropertyListener(this);
        DefaultComboBoxModel model = (DefaultComboBoxModel) box.getModel();
        model.removeElement(e.getBookmark().getAliasName());
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.bookmarkBean.BookMarkListener#bookMarkUpdated(com.coolsql.bookmarkBean.BookMarkEvent)
     */
    public void bookMarkUpdated(BookmarkEvent e) {

    }

    /*
     * 查询过程中，数据库别名发生变化时的监听处理
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("aliasName")) {
            String oldValue = (String) evt.getOldValue();
            String newValue = (String) evt.getNewValue();
            if (oldValue.equals(newValue))
                return;
            DefaultComboBoxModel model = (DefaultComboBoxModel) box.getModel();
            int selectIndex = box.getSelectedIndex();
            int index = model.getIndexOf(oldValue);
            model.removeElement(oldValue);
            model.insertElementAt(newValue, index);
            box.setSelectedIndex(selectIndex);
        }

    }

}
