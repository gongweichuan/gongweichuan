/*
 * Created on 2007-2-2
 */
package com.coolsql.modifydatabase.insert;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.component.BaseDialog;
import com.coolsql.pub.display.EditorSQLArea;
import com.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin
 *预览sql对话框
 */
public class PreviewSQLDialog extends BaseDialog {

    private EditorSQLArea sqlArea;  //sql显示区域

    public PreviewSQLDialog(JDialog dialog,boolean isModel)
    {
        this(null,dialog,isModel);
    }
    public PreviewSQLDialog(Bookmark bookmark,JDialog dialog,boolean isModel)
    {
        super(dialog,isModel);
        createGUI(bookmark);
    }
    /**
     * 创建图形界面
     *
     */
    protected void createGUI(Bookmark bookmark)
    {
        setTitle(PublicResource.getSQLString("previewsql.dialog.title"));
        
        JPanel pane=(JPanel)getContentPane();
        sqlArea=new EditorSQLArea(bookmark)
        {
            public boolean isExecuteSQL() {
                return false;
            }
        };
        pane.add(sqlArea,BorderLayout.CENTER);
        
        setHighlight(false);
        addWindowListener(new WindowAdapter()
                {
            		public void windowClosing(WindowEvent e)
            		{
            		    closeDialog();
            		}
                }
        );
        setSize(600,470);
        toCenter();
    }
    private void closeDialog()
    {
        removeAll();
        sqlArea.dispose();
        dispose();
    }
    /**
     * 设置显示区域的内容信息
     * @param text  --需要显示的文本内容
     */
    public void setContent(String text)
    {
        sqlArea.setText(text);
    }
    /**
     * 设置状态栏的信息
     * @param txt  --状态栏需要显示的信息
     */
    public void setStatueInfo(String txt)
    {
        sqlArea.setStatueInfo(txt);
    }
    /**
     * 向显示区域追加文本信息
     * @param txt  --追加的文本信息
     * @throws BadLocationException
     */
    public void append(String txt) throws BadLocationException
    {
        sqlArea.append(txt);
    }
    /**
     * 是否进行高亮处理
     * @return  --true：高亮处理 false：不处理
     */
    public boolean isHighlight()
    {
        return sqlArea.isHighlight();
    }
    /**
     * 设置是否高亮处理文档对象中的内容
     * @param isHighlight true:高亮处理 false:不作处理
     */
    public void setHighlight(boolean isHighlight)
    {
        sqlArea.setHighlight(isHighlight);
    }
    /**
     * @return Returns the bookmark.
     */
    public Bookmark getBookmark() {
        return sqlArea.getBookmark();
    }
    /**
     * @param bookmark The bookmark to set.
     */
    public void setBookmark(Bookmark bookmark) {
        sqlArea.setBookmark(bookmark);
    }
}
