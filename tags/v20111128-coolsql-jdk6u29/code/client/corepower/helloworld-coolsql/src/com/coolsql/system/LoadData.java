/*
 * 创建日期 2006-11-9
 */
package com.coolsql.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.jdom.Document;
import org.jdom.Element;

import com.coolsql.action.framework.CsAction;
import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.loadlib.LoadJar;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.parse.xml.XMLBeanUtil;
import com.coolsql.pub.parse.xml.XMLConstant;
import com.coolsql.pub.parse.xml.XMLException;
import com.coolsql.view.ViewManage;
import com.coolsql.view.bookmarkview.RecentSQL;
import com.coolsql.view.bookmarkview.RecentSQLManage;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 程序启动时，装载数据信息
 */
public class LoadData {
    private static LoadData loader=null;
    
    
    public synchronized static LoadData getInstance()
    {
        if(loader==null)
            loader=new LoadData();
        return loader;
    }
    private LoadData() {

    }
    
    /**
     * 装载数据信息 1、装载书签信息 2、装载最近执行的sql语句 3、装载上次保存的sql编辑视图中的内容 4、装载图形界面的显示状态
     *  
     */
    public void loadData() {
        /**
         * 装载书签信息
         */
        loadBookmarks();

        try {
            /**
             * 装载最近执行的sql语句
             */
            loadRecentSQL();
        } catch (Exception e) {
            LogProxy.errorReport(e);
        }

        /**
         * 装载上次sql编辑视图的编辑内容
         */
        loadSqlEditor();

    }

    /**
     * 装载驱动类,及其他相关资源类库
     *
     */
    public void loadLibResource()
    {
        //装载类文件
        LoadJar.getInstance().getClassLoader();
    }
    /**
     * 装载书签信息 <bookmarks><bookmark></bookmark> </bookmarks>
     * 
     * @throws XMLException
     */
    public void loadBookmarks() {
        /**
         * 装载书签信息
         */
        XMLBeanUtil xml = new XMLBeanUtil();
        File file = new File(SystemConstant.bookmarkInfo);
        if (!file.exists())
            return;

        Document doc = null;
        try {
            doc = xml.importDocumentFromXML(file);
        } catch (XMLException e) {
            LogProxy.errorReport(e);
            return;
        } catch (MalformedURLException e) {
            LogProxy.errorReport(e);
            return;
        } catch (IOException e) {
            LogProxy.errorReport(e);
            return;
        }

        BookmarkManage bm = BookmarkManage.getInstance();
        Element root = doc.getRootElement();
        List children = root.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Element child = (Element) children.get(i);
            Object ob;
            try {
                ob = xml.getBean(child);

                if (ob instanceof Bookmark) {
                    Bookmark bookmark = (Bookmark) xml.getBean(child);
                    bm.addBookmark(bookmark);
                }
            } catch (XMLException e1) {
                LogProxy.errorReport(e1);
                continue;
            }
        }
        bm.nextBookmarkAsDefault();
    }

    /**
     * 装载上次保存的sql编辑视图中的内容
     */
    public void loadSqlEditor() {
        FileInputStream input = null;
        String content = "";
        File file = new File(SystemConstant.sqlEditeInfo);
        if (file.exists()) {
            try {

                input = new FileInputStream(file);
                byte[] b = new byte[1024];
                int count = -1;
                while ((count = input.read(b)) != -1) {
                    content += new String(b, 0, count);
                }
                CsAction undoAction=Setting.getInstance().getShortcutManager().
					getActionByClass(com.coolsql.system.menu.action.UndoMenuAction.class);
                CsAction redoAction=Setting.getInstance().getShortcutManager().
					getActionByClass(com.coolsql.system.menu.action.RedoMenuAction.class);
                if(content.length()!=0)
                {
                	ViewManage.getInstance().getSqlEditor().setEditorContent(
                            content);
					redoAction.setEnabled(false);
					undoAction.setEnabled(true);
                }else
                {
                	redoAction.setEnabled(false);
					undoAction.setEnabled(false);
                }
            } catch (Exception e) {
                LogProxy.errorMessage("load content of  sqlEditor view error:"
                        + e.getMessage());
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 解析格式： <recentsqls><bookmark><recentsql></recentsql> </bookmark>
     * </recentsqls>
     * 
     * @throws UnifyException
     * @throws XMLException
     * @throws IOException
     * @throws MalformedURLException
     */
    public void loadRecentSQL() throws UnifyException, MalformedURLException, XMLException, IOException {
        File file = new File(DoOnClosingSystem.getRecentSQLInfoFile());
        if (!file.exists())
            return;

        /**
         * 读取配置信息，返回文档对象
         */
        XMLBeanUtil xml = new XMLBeanUtil();
        Document doc = xml.importDocumentFromXML(file);

        RecentSQLManage manage = RecentSQLManage.getInstance();
        Element root = doc.getRootElement();
        List children = root.getChildren();
        Iterator it = children.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String aliasName = e
                    .getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME);//书签别名
            if (aliasName != null) {
                Bookmark bookmark = BookmarkManage.getInstance().get(aliasName);
                if (bookmark == null)
                    continue;

                //装载sql对象，组装成Collection类型的对象
                List linkListChild = e.getChildren();
                if (linkListChild.size() > 1)
                    throw new UnifyException(
                            PublicResource
                                    .getSQLString("system.loaddata.recentsql.linklisttoomuch"));
                else if (linkListChild.size() == 0) {
                    continue;
                }

                /**
                 * 一个书签只能对应一个linkedlist对象，如果不能一一对应，将会抛出异常或者过滤掉
                 */
                Object ob = xml.getBean((Element) linkListChild.get(0));
                if (ob == null) {
                    continue;
                } else if (!(ob instanceof LinkedList)) {
                    throw new UnifyException(
                            "attribute(datatype)  of tag(sqls) is incorrect");
                } else {
                    //对获取的链表进行遍历，同时对RecentSQL对象进行保存
                    LinkedList sqls = (LinkedList) ob;
                    Iterator tmpIt = sqls.iterator();
                    while (tmpIt.hasNext()) {
                        ob = tmpIt.next();
                        if (ob instanceof RecentSQL) {
                            ((RecentSQL) ob).setBookmark(bookmark);
                            manage.addSQL((RecentSQL) ob, bookmark);
                        }
                    }
                }
            }
        }
        manage.adjustAll();
    }

    /**
     * 装载指定书签和文件中最近执行sql的信息
     * 
     * @param bookmark
     *            --给定的书签别名
     * @param date
     *            --指定执行sql的日期
     * @return
     * @throws UnifyException
     * @throws XMLException
     * @throws IOException
     * @throws MalformedURLException
     */
    public Vector loadRecentSQL(String bookmark, String date)
            throws UnifyException, MalformedURLException, XMLException, IOException {
        File file = new File(DoOnClosingSystem.getRecentSQLInfoFileByDate(date)); //根据指定的日期获取文件名
        if (file == null || !file.exists()) {
            return new Vector();
        }

        /**
         * 读取配置信息，返回文档对象
         */
        XMLBeanUtil xml = new XMLBeanUtil();
        Document doc = xml.importDocumentFromXML(file);

        RecentSQLManage manage = RecentSQLManage.getInstance();
        Element root = doc.getRootElement();
        List children = root.getChildren();
        Iterator it = children.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String aliasName = e
                    .getAttributeValue(XMLConstant.TAG_ARRTIBUTE_NAME);//书签别名
            if (bookmark.equals(aliasName)) {
                //装载sql对象，组装成Collection类型的对象
                List linkListChild = e.getChildren();
                if (linkListChild.size() > 1)
                    throw new UnifyException(
                            PublicResource
                                    .getSQLString("system.loaddata.recentsql.linklisttoomuch"));
                else if (linkListChild.size() == 0) {
                    continue;
                }

                /**
                 * 一个书签只能对应一个linkedlist对象，如果不能一一对应，将会抛出异常或者过滤掉
                 */
                Object ob = xml.getBean((Element) linkListChild.get(0));
                if (ob == null) {
                    continue;
                } else if (!(ob instanceof LinkedList)) {
                    throw new UnifyException(
                            "attribute(datatype)  of tag(sqls) is incorrect");
                } else {
                    //对获取的链表进行遍历，同时对RecentSQL对象进行保存
                    Bookmark tmpBm=BookmarkManage.getInstance().get(bookmark);
                    if(tmpBm==null)
                        continue;
                    LinkedList sqls = (LinkedList) ob;
                    manage.sortRecentSQLs(sqls);
                    Iterator sqlIt = sqls.iterator();
                    Vector datas = new Vector();
                    int count = 0;
                    while (sqlIt.hasNext()) {
                        count++;
                        Object sqlObject = sqlIt.next();
                        
                        if (sqlObject instanceof RecentSQL) {
                            RecentSQL tmpSQL = (RecentSQL) sqlObject;
                            tmpSQL.setBookmark(tmpBm);
                            Vector tmpVector=tmpSQL.converToVector(count);
                            datas.add(tmpVector);
                        }
                    }
                    return datas;
                }
            } else {
                continue;
            }
        }
        return new Vector();
    }
}
