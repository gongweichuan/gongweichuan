/*
 * 创建日期 2006-11-9
 */
package com.coolsql.system;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.main.frame.MainFrame;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.loadlib.LoadJar;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.parse.xml.XMLBeanUtil;
import com.coolsql.pub.parse.xml.XMLConstant;
import com.coolsql.pub.parse.xml.XMLException;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.view.ViewManage;
import com.coolsql.view.bookmarkview.RecentSQLManage;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 在关闭程序之前,将需要保存的信息进行处理,具体保存的信息,以及保存顺序见代码中的注释
 */
public class DoOnClosingSystem implements CloseSystem {
    private static DoOnClosingSystem system=null;
    
    private MainFrame main = null;

    private CloseProgressDialog progressDialog=null;
    
    public synchronized static DoOnClosingSystem getInstance()
    {
        if(system==null)
            system=new DoOnClosingSystem((MainFrame)GUIUtil.getMainFrame());
        return system;
    }
    private DoOnClosingSystem(MainFrame main) {
        this.main = main;
        
        /**
         * 初始化关闭进度窗口
         */
        progressDialog=new CloseProgressDialog();
        progressDialog.setTasks(TaskFactory.getCloseTasks());         
        
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.system.CloseSystem#close()
     */
    public void close() throws UnifyException {

        main.removeAll();
        main.dispose();
    }

    /*
     * 1、断开所有数据库连接 2、保存书签信息 3、保存驱动信息 4、保存执行的sql信息 5、保存sql编辑的内容
     * 
     * @see com.coolsql.system.CloseSystem#save()
     */
    public void save() throws UnifyException {
//        disconnectAllDB();
//
//        saveBookmarkInfo();
//
//        /**
//         * 保存驱动信息
//         */
//        LoadJar.newInstance().writeClasspath();
//
//        try {
//            /**
//             * 保存执行的sql信息
//             */
//            saveRecentSQL();
//        } catch (XMLException e1) {
//            LogProxy.errorReport(e1);
//        } catch (Exception e) {
//            LogProxy.outputErrorLog(e);
//            LogProxy.errorReport(e);
//        }
//
//        /**
//         * 保存sql编辑内容
//         */
//        saveContentOfSQLEditor();
    }
    /**
     * 断开所有书签的连接
     */
    public void disconnectAllDB()
    {
        BookmarkManage bm = BookmarkManage.getInstance();
        Iterator it = bm.getBookmarks().iterator();
        while (it.hasNext()) {
            Bookmark bookmark = (Bookmark) it.next();
            if (bookmark.isConnected()) {
                try {
                    bookmark.disconnect();
                } catch (SQLException e2) {
                    LogProxy.outputErrorLog(e2);
                }
            }
        }
    }
    /**
     * 保存书签信息 <bookmarks><bookmark></bookmark> <bookmark></bookmark>
     * </bookmarks>
     */
    public void saveBookmarkInfo()
    {
        XMLBeanUtil xml = new XMLBeanUtil();
        Document doc = new Document(xml.createRootElement("bookmarks"));
        Element element = doc.getRootElement();

        BookmarkManage bm = BookmarkManage.getInstance();
        Iterator it = bm.getBookmarks().iterator();
        while (it.hasNext()) {
            Bookmark bookmark = (Bookmark) it.next();
            try {
                element.addContent(xml.parseBean(bookmark, "bookmark"));
            } catch (XMLException e) {
                LogProxy.errorMessage("(" + bookmark.getAliasName() + ")"
                        + PublicResource.getString("save.bookmark.erroroccur")
                        + e.getMessage());
                LogProxy.outputErrorLog(e);
            }
        }
        File file = new File(SystemConstant.bookmarkInfo);
        try {
            xml.saveDocumentToFile(doc, file);
        } catch (XMLException e) {
            LogProxy.errorMessage(e.getMessage());
        } catch (Exception e) {
            LogProxy.errorReport(e);
        }
    }
    /**
     * 保存最近执行的sql信息，保存格式如下 <recentSQL><bookmark datatype> <sql datatype> </sql>
     * </bookmark> </recentSQL>
     * 
     * @throws XMLException
     */
    public void saveRecentSQL() throws XMLException {
        XMLBeanUtil xml = new XMLBeanUtil();
        Document doc = new Document(new Element("recentSQL"));
        Element element = doc.getRootElement();

        RecentSQLManage manage = RecentSQLManage.getInstance();
        BookmarkManage bm = BookmarkManage.getInstance();
        Iterator it = bm.getBookmarks().iterator();
        while (it.hasNext()) {
            Bookmark bookmark = (Bookmark) it.next();

            /**
             * 构建书签标签，并设置name属性为书签别名
             */
            Element e = new Element("bookmark");
            e.setAttribute(XMLConstant.TAG_ARRTIBUTE_NAME, bookmark
                    .getAliasName());

            List list = manage.getRecentSQLList(bookmark);
            if (list == null)
                continue;

            Element sqlsElement = e.addContent(xml.parseBean(list, "sqls"));
            element.addContent(e);
        }

        xml.saveDocumentToFile(doc, new File(getRecentSQLInfoFile()));
    }
    public void deleteNoUseFile()
    {
    	int saveDays=Setting.getInstance().getIntProperty(PropertyConstant.PROPERTY_SYSTEM_HISTORYDAYS, 15);
    	String fileName=getPreviousRecentSQLInfoFile(saveDays);
    	new File(fileName).delete();
    }
    /**
     * 保存sql编辑内容
     */
    public void saveContentOfSQLEditor()
    {
        String sqlContent = ViewManage.getInstance().getSqlEditor()
                .getEditorContent();
        File file = new File(SystemConstant.sqlEditeInfo);
        FileOutputStream out = null;
        try {
            GUIUtil.createDir(file.getAbsolutePath(),false, false);
            out = new FileOutputStream(file);
            if(Setting.getInstance().getBoolProperty(PropertyConstant.PROPERTY_VIEW_SQLEDITOR_ISSAVEEDITORCONTENT, true))
            	out.write(sqlContent.getBytes());
        } catch (Exception e) {
            LogProxy.errorMessage("save content of  sqlEditor view error:"
                    + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }
    /**
     * 获取当前日期，然后生成当前日期所对应的最近执行sql的保存文件名
     * @return --生成的文件名
     */
    protected static String getRecentSQLInfoFile()
    {
        return getPreviousRecentSQLInfoFile(0);
    }
    /**
     * 获取当前日期前days天的日期，然后生成所对应的最近执行sql的保存文件名
     * @param days  --往前追溯的天数
     * @return --生成的文件名
     */
    protected static String getPreviousRecentSQLInfoFile(int days)
    {
        long timeGap = 24 * 60 * 60 * 1000*days;
        long currentTime=System.currentTimeMillis();
        
        String deaultDateTimeFormat = "yyyy-MM-dd";      
        String date=StringUtil.transDate(new Date(currentTime-timeGap),deaultDateTimeFormat);
        
        return SystemConstant.recentSqlInfo+"("+date+").xml";
    }
    /**
     * 获取指定日期对应的文件
     * @param date  --给定的日期字符串
     * @return  --生成的文件名
     * @throws UnifyException  --如果格式不正确，抛出此异常
     */
    protected static String getRecentSQLInfoFileByDate(String date) throws UnifyException
    {
        String msg=StringUtil.checkDateFormat(date);

        if(msg.equals(""))
            return SystemConstant.recentSqlInfo+"("+date+").xml";
        else
            throw new UnifyException(msg);
    }
    public CloseProgressDialog getProgressDialog() {
        return progressDialog;
    }
}
