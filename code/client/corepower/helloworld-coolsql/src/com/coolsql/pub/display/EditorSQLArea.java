/*
 * Created on 2007-1-25
 */
package com.coolsql.pub.display;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.resultset.ResultSetDataProcess;
import com.coolsql.view.sqleditor.ScriptParser;

/**
 * @author liu_xlin 可编辑sql显示组件
 */
public class EditorSQLArea extends JPanel {

    private JCheckBoxMenuItem wrapItem = null; //是否换行

    private JMenuItem runItem = null; //执行sql

    private SQLArea sqlArea = null; //sql显示区域

    private StatusBar statueBar = null; //状态栏

    private Bookmark bookmark = null;

    public EditorSQLArea(Bookmark bookmark) {
        super();
        this.bookmark = bookmark;
        sqlArea = new SQLArea();
        sqlArea.setWrap(false);
        
        initPanel();
        addMenu();
    }

    protected void initPanel() {
        this.setLayout(new BorderLayout());
        add(new JScrollPane(sqlArea), BorderLayout.CENTER);
        statueBar = new StatusBar();
        statueBar.setMinimumSize(new Dimension(30, 50));
        add(statueBar, BorderLayout.SOUTH);
    }

    /**
     * 创建菜单
     *  
     */
    protected void addMenu() {

        //是否自动换行菜单项
        wrapItem = sqlArea.insertCheckBoxMenuItem(PublicResource
                .getString("logView.popmenu.iswrap"), null, null);
        ActionListener wrapListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                sqlArea.setWrap(wrapItem.getState());
            }

        };
        wrapItem.addActionListener(wrapListener);
        sqlArea.getAreaMenuManage().addMenuCheck(new MenuCheckable() {

            public void check(Component table) {
                wrapItem.setSelected(sqlArea.isWrap());

            }

        });

        if (isExecuteSQL()) {  //如果需要添加执行sql的功能菜单项
            //执行sql菜单项

            ActionListener runListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (bookmark == null) {
                        LogProxy
                                .errorMessage(
                                        EditorSQLArea.this,
                                        PublicResource
                                                .getSQLString("sql.execute.nobookmark"));
                        return;
                    }
                    if (!bookmark.isConnected()) {
                        LogProxy.errorMessage(PublicResource
                                .getSQLString("database.notconnected")
                                + bookmark.getAliasName());
                        return;
                    }
                    ScriptParser sqlParser=new ScriptParser(sqlArea.getSelectedText());
//                    List list = SQLParser.parse(sqlArea.getSelectedText());

                    if (sqlParser.getSize() < 1) {
                        LogProxy.message(PublicResource
                                .getSQLString("sql.execute.nosql"),
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (bookmark == null) {
                        LogProxy.message(PublicResource
                                .getSQLString("sql.execute.nobookmark"),
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Operatable operator = null;
                    try {
                        operator = OperatorFactory
                                .getOperator(com.coolsql.sql.commonoperator.SQLResultProcessOperator.class);
                    } catch (Exception e1) {
                        LogProxy.internalError(e1);
                        return;
                    }
                    for (int i = 0; i <sqlParser.getSize(); i++) {
                        List argList = new ArrayList();
                        argList.add(bookmark);
                        argList.add(sqlParser.getCommand(i));
                        argList.add(new Integer(ResultSetDataProcess.EXECUTE));//设置sql的执行处理类型：首次执行
                        try {
                            operator.operate(argList);
                        } catch (UnifyException e2) {
                            LogProxy.errorReport(e2);
                        } catch (SQLException e2) {
                            LogProxy.SQLErrorReport(e2);
                        }
                    }

                }

            };
            runItem = sqlArea.insertMenuItem(PublicResource
                    .getSQLString("sqlarea.popmenu.execute.label"),
                    PublicResource.getIcon("TextMenu.icon.execute"),
                    runListener);
            sqlArea.getAreaMenuManage().addMenuCheck(new MenuCheckable() {

                public void check(Component table) {
                    String txt = StringUtil.trim(sqlArea.getSelectedText());
                    if (txt.equals(""))
                        GUIUtil.setComponentEnabled(false, runItem);
                    else
                        GUIUtil.setComponentEnabled(true, runItem);
                }

            });
        }
    }

    /**
     *  
     */
    public void dispose() {
        sqlArea.dispose();
    }

    /**
     * 设置状态栏信息
     * 
     * @param info
     */
    public void setStatueInfo(String info) {
        statueBar.setText(info);
    }

    /**
     * 在文档模型最后添加文本内容
     * 
     * @param txt
     *            --追加的文本内容
     * @throws BadLocationException
     */
    public void append(String txt) throws BadLocationException {
        sqlArea.append(txt);
    }

    /**
     * @param t
     */
    public void setText(String t) {
        sqlArea.setText(t);
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
        return bookmark;
    }

    /**
     * @param bookmark
     *            The bookmark to set.
     */
    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    /**
     * 定义“执行sql”菜单项是否显示
     * 
     * @return true：显示 false:不显示
     */
    public boolean isExecuteSQL() {
        return true;
    }
}
