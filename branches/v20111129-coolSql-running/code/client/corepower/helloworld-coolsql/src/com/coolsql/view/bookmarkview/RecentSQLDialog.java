/*
 * 创建日期 2006-12-5
 */
package com.coolsql.view.bookmarkview;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.bookmarkBean.BookmarkUpdateOfComboBoxListener;
import com.coolsql.pub.component.BaseDialog;
import com.coolsql.pub.component.DateSelector;
import com.coolsql.pub.component.RenderButton;
import com.coolsql.pub.display.CheckModel;
import com.coolsql.pub.display.CommonDataTable;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.display.SQLArea;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.system.LoadData;
import com.coolsql.view.View;
import com.coolsql.view.log.LogProxy;
import com.coolsql.view.resultset.ResultSetDataProcess;

/**
 * @author liu_xlin 显示了最近执行的sql的历史记录。
 */
public class RecentSQLDialog extends BaseDialog implements ItemListener {

	private static final long serialVersionUID = 1L;

	private static boolean isDisplayed = false;

    //书签选择控件
    private JComboBox bookmarkSelect = null;

    //日期选择控件
    private DateSelector dateSelector = null;

    //信息显示表控件
    private CommonDataTable table = null;

    //sql显示区域
    private SQLArea sqlArea = null;

    //保存对应书签的查询条件
    //key:aliasname value:date(String)
    private Map lastQuery = null;

    //对sql管理器的监听器
    private HistorySQLAddListener sqlListener = null;

    private BookmarkUpdateOfComboBoxListener listener = null; //书签更新监听器

    public RecentSQLDialog(Frame con) {
        this(con, false);
    }

    public RecentSQLDialog(Dialog con) {
        this(con, false);
    }

    public RecentSQLDialog(Dialog con, boolean isModel) {
        super(con, isModel);
        initPane();
    }

    public RecentSQLDialog(Frame con, boolean isModel) {
        super(con, isModel);
        initPane();
    }

    private void initPane() {
        setTitle(PublicResource.getSQLString("recentsql.dialog.title"));
        JPanel pane = (JPanel) this.getContentPane();
        pane.setLayout(new BorderLayout());

        //初始化
        lastQuery = Collections.synchronizedMap(new HashMap());

        /**
         * 条件选择面板
         */
        JPanel topPane = new JPanel();
        topPane.setLayout(new BoxLayout(topPane, BoxLayout.X_AXIS));
        //书签选择
        topPane.add(new JLabel(PublicResource
                .getSQLString("recentsql.dialog.bookmark.label")));
        bookmarkSelect = new JComboBox();
        bookmarkSelect.setPreferredSize(new Dimension(130, 25));
        bookmarkSelect.setEditable(false);
        topPane.add(bookmarkSelect);
        topPane.add(Box.createHorizontalStrut(15));

        /**
         * 添加对各书签的监听
         */
        listener = new BookmarkUpdateOfComboBoxListener(bookmarkSelect);
        BookmarkManage.getInstance().addBookmarkListener(listener);
        Bookmark[] bookmarks = (Bookmark[]) BookmarkManage.getInstance()
                .getBookmarks().toArray(new Bookmark[0]);
        for (int i = 0; i < bookmarks.length; i++) {
            bookmarks[i].addPropertyListener(listener);
        }
        //上一日按钮
        RenderButton preBtn = new RenderButton(PublicResource
                .getSQLString("recentsql.dialog.prebtn"));
        preBtn.setToolTipText(PublicResource
                .getSQLString("recentsql.dialog.prebtn.tip"));
        preBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    Date date = dateSelector.getSelectedDate();
                    dateSelector.setSelectedItem(StringUtil
                            .getPreviousDay(date));
                } catch (ParseException e1) {
                    LogProxy.errorReport(RecentSQLDialog.this, e1);
                }

            }
        });
        topPane.add(preBtn);

        topPane.add(Box.createHorizontalStrut(10));

        //下一日按钮
        RenderButton nextBtn = new RenderButton(PublicResource
                .getSQLString("recentsql.dialog.nextbtn"));
        nextBtn.setToolTipText(PublicResource
                .getSQLString("recentsql.dialog.nextbtn.tip"));
        nextBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    Date date = dateSelector.getSelectedDate();
                    dateSelector.setSelectedItem(StringUtil.getNextDay(date));
                } catch (ParseException e1) {
                    LogProxy.errorReport(RecentSQLDialog.this, e1);
                }

            }
        });
        topPane.add(nextBtn);
        //日期选择
        topPane.add(Box.createHorizontalStrut(15));
        topPane.add(new JLabel(PublicResource
                .getSQLString("recentsql.dialog.dateselect.label")));
        dateSelector = new DateSelector();
        dateSelector.setEditable(false);
        dateSelector.setPreferredSize(new Dimension(130, 25));
        dateSelector.addItemListener(this);
        topPane.add(dateSelector);

        pane.add(topPane, BorderLayout.NORTH);

        /**
         * 初始化最近执行sql的表显示控件 设置表头信息
         */
        Vector header = new Vector();
        for (int i = 0; i < 5; i++) {
            header.add(PublicResource
                    .getSQLString("recentsql.dialog.table.header" + i));
        }
        table = new CommonDataTable(null, header) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            public String getToolTipText(MouseEvent event) {
                Point p = event.getPoint();
                int column = columnAtPoint(p);
                if (column == 2) //如果移动到sql列位置
                {
                    int row = rowAtPoint(p);
                    return getValueAt(row, column).toString();
                }
                return null;
            }
            public boolean isDisplayToolTipSelectMenu()
            {
            	return false;
            }
            //            /**
            //             * 重写JTree的getToolTipLocation()方法
            //             * 将tip位置与组件对齐
            //             */
            //            public Point getToolTipLocation(MouseEvent e)
            //            {
            //                return new Point(e.getX(),e.getY()+20);
            //            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(View.getThemeColor());
        table.getSelectionModel().addListSelectionListener(
                new SqlSelectListener());
        adjustWidth();
        //添加属性查看菜单项
        JMenuItem item = table.getPopMenuManage().addMenuItem(
                PublicResource.getSQLString("recentsql.dialog.queryselect"),
                new QueryMenuListener(),
                PublicResource.getIcon("TextMenu.icon.execute"), true);
        table.getPopMenuManage().addMenuCheck(new QueryMenuCheck(item));//添加该菜单项的可用规则

        table.setEnableToolTip(false); //不作信息提示
        ToolTipManager.sharedInstance().registerComponent(table); //注册信息提示

        sqlArea = new SQLArea();
        sqlArea.setMinimumSize(new Dimension(420, 200));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(table), new JScrollPane(sqlArea));
        splitPane.setDividerSize(4);
        splitPane.setDividerLocation(350);
        pane.add(splitPane, BorderLayout.CENTER);
        /**
         * 退出按钮面板
         */
        JPanel quitPane = new JPanel();
        quitPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        RenderButton quitBtn = new RenderButton(PublicResource
                .getSQLString("recentsql.dialog.quitbtn.label"));
        quitBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }

        });
        quitPane.add(quitBtn);
        pane.add(quitPane, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
        });
//        this.setSize(620, 550);
        GUIUtil.setFrameSizeToScreen(this,(float)7/8,(float)3/4);
        this.toCenter();
        loadAliasData();
        bookmarkSelect.addItemListener(this);

        //初始化当前选择书签、日期下的sql信息
        try {
            loadRecentSQLInfo();
        } catch (UnifyException e1) {
            LogProxy.errorReport(e1);
        }
        sqlListener = new HistorySQLAddListener();
        RecentSQLManage.getInstance().addPropertyChangeListener(sqlListener);
    }

    /**
     * 关闭对话框时，释放相关资源
     *  
     */
    public void closeDialog() {
        /**
         * 除去对各书签的监听
         */
        Bookmark[] bookmarks = (Bookmark[]) BookmarkManage.getInstance()
                .getBookmarks().toArray(new Bookmark[0]);
        for (int i = 0; i < bookmarks.length; i++) {
            bookmarks[i].removePropertyListener(listener);
        }
        BookmarkManage.getInstance().removeBookmarkListener(listener);

        RecentSQLManage.getInstance().removePropertyChangeListener(sqlListener);

        sqlArea.dispose();//关闭sql显示组件的相关资源

        //删除各个组件
        this.removeAll();
        dispose();
        RecentSQLDialog.isDisplayed = false;
    }

    /**
     * 重写父类方法
     */
    public void setVisible(boolean isVisible) {
        if (RecentSQLDialog.isDisplayed) {
            this.removeAll();
            return;
        }
        RecentSQLDialog.isDisplayed = true;
        super.setVisible(isVisible);

    }

    /**
     * 获取历史纪录窗口的显示状态
     * 
     * @return
     */
    public static boolean getDisplayState() {
        return RecentSQLDialog.isDisplayed;
    }

    /**
     * 将已有的数据库配置添加到数据库下拉选择控件中
     *  
     */
    private void loadAliasData() {
//        DefaultComboBoxModel model = (DefaultComboBoxModel) bookmarkSelect
//                .getModel();
//        Set set = BookmarkManage.getInstance().getAliases();
//        Iterator it = set.iterator();
//        while (it.hasNext()) {
//            model.addElement(it.next());
//        }
//
//        //将数签选择设置成sql编辑视图所选择的书签
//        SqlEditorView view = ViewManage.getInstance().getSqlEditor();
//        Bookmark bookmark = (Bookmark) view.getDefaultBookMark();
//        model.setSelectedItem(bookmark.getAliasName());
        GUIUtil.loadBookmarksToComboBox(bookmarkSelect);
    }

    /**
     * 调整相关列的宽度
     *  
     */
    private void adjustWidth() {
        if (table == null)
            return;
        table.getColumnModel().getColumn(0).setPreferredWidth(50); //调整显示sql列的宽度
        table.getColumnModel().getColumn(0).setWidth(50); //调整显示sql列的宽度
        table.getColumnModel().getColumn(2).setPreferredWidth(300); //调整显示sql列的宽度
        table.getColumnModel().getColumn(2).setWidth(300); //调整显示sql列的宽度

        table.getColumnModel().getColumn(3).setWidth(120); //调整显示sql列的宽度
        table.getColumnModel().getColumn(3).setPreferredWidth(120); //调整显示sql列的宽度
    }

    /**
     * 根据界面选择的数签和日期，查找对应的sql信息
     * 
     * @throws UnifyException
     *             --如果查看条件格式不正确，抛出异常
     */
    public void loadRecentSQLInfo() throws UnifyException {
        String bookmark = StringUtil.trim((String) bookmarkSelect
                .getSelectedItem());
        if (bookmark.equals(""))
            throw new UnifyException(PublicResource
                    .getSQLString("recentsql.dialog.load.nobookmarkselected"));

        String date = (String) dateSelector.getSelectedItem();
        Vector data = null; //数据向量
        if (date.equals(StringUtil.getCurrentDate())) //如果为当天日期，直接从内存中获取信息
        {
            List tmpList = RecentSQLManage.getInstance().getRecentSQLList(
                    bookmark);
            data = new Vector();
            if (tmpList != null) { //存在执行的sql信息

                Iterator sqlIt = tmpList.iterator();
                int count = 0;
                while (sqlIt.hasNext()) {
                    count++;
                    Object sqlObject = sqlIt.next();

                    if (sqlObject instanceof RecentSQL) {
                        RecentSQL tmpSQL = (RecentSQL) sqlObject;
                        Vector tmpVector = tmpSQL.converToVector(count);
                        data.add(tmpVector);
                    }
                }
            }
        } else {
            try {
                data = LoadData.getInstance().loadRecentSQL(bookmark, date);
            } catch (Exception e) {
                throw new UnifyException(PublicResource
                        .getSQLString("recentsql.dialog.load.xmlerror")+e.getMessage());
            }
        }

        DefaultTableModel model = (DefaultTableModel) table.getModel();

        model.setDataVector(data, GUIUtil.getColumnIdenfiers(table));
        adjustWidth();

    }

    /**
     * 将给定的sql对象添加至记录显示控件中
     * 
     * @param sqlOb
     */
    private void addSQLToTable(RecentSQL sqlOb) {
        updateSeqNo();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Vector tmpVector = sqlOb.converToVector(1);
        model.insertRow(0, tmpVector);
    }

    /**
     * 将表控件中没行数据的序号列加一
     *  
     */
    private void updateSeqNo() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            int seqNo = Integer.parseInt(model.getValueAt(i, 0).toString());//获取该行的第一列：序号
            seqNo++;
            model.setValueAt(seqNo + "", i, 0);
        }
    }

    /*
     * 如果查询条件更改，调用该方法重新装入信息
     * 
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) { //如果指定项被选中以后,才作相应的调整
            try {
                loadRecentSQLInfo();
            } catch (UnifyException e1) {
                LogProxy.errorReport(this, e1);
            }
        }
    }

    /**
     * 
     * @author liu_xlin 最近执行sql信息显示表控件的选择监听类
     */
    protected class SqlSelectListener implements ListSelectionListener {

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent e) {
            javax.swing.ListSelectionModel model = (javax.swing.ListSelectionModel) e
                    .getSource();
            int row=model.getMinSelectionIndex();
            if (row == -1)
                return;
            if (!e.getValueIsAdjusting() && row < table.getRowCount()) {
                String sql = (String) table.getValueAt(row, 2); //获取sql
                sqlArea.setText(sql);
            }
        }

    }

    /**
     * 
     * @author liu_xlin 该监听器用于执行新的sql后，如果历史窗口显示日期为当天，并且书签一致，那么向历史纪录窗口中相应添加一条记录
     */
    private class HistorySQLAddListener implements PropertyChangeListener {

        /*
         * （非 Javadoc）
         * 
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            if (name.equals("addsql")) {
                RecentSQL sqlObject = (RecentSQL) evt.getNewValue();
                String currentSelectBookmark = bookmarkSelect.getSelectedItem()
                        .toString();
                if (currentSelectBookmark.equals(sqlObject.bookmark()
                        .getAliasName())) //书签一致
                {
                    String currentDate = StringUtil.getCurrentDate();
                    String selectDate = dateSelector.getSelectedItem()
                            .toString();
                    if (currentDate.equals(selectDate)) //日期一致
                        addSQLToTable(sqlObject);
                }
            }
        }

    }

    /**
     * 
     * @author liu_xlin 查询菜单项是否可用的校验类
     */
    private class QueryMenuCheck extends CheckModel {
        public QueryMenuCheck(JMenuItem item) {
            super(item);
        }

        /*
         * （非 Javadoc）
         * 
         * @see com.coolsql.data.display.MenuCheckable#check(java.awt.Component)
         */
        public void check(Component table) {
            if (!(table instanceof JTable))
                return;
            JTable tmpTable = (JTable) table;
            int[] selected = tmpTable.getSelectedRows();
            if (selected == null || selected.length == 0) //如果未选中，该菜单项不可用
            {
                if (this.getMenu().isEnabled())
                    this.getMenu().setEnabled(false);
            } else {
                if (!this.getMenu().isEnabled())
                    this.getMenu().setEnabled(true);
            }

        }

    }

    /**
     * 
     * @author liu_xlin 执行菜单事件监听处理类.可选择多行进行执行
     */
    private class QueryMenuListener implements ActionListener {

        /*
         * （非 Javadoc）
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            int selectedCount = table.getSelectedRowCount();
            if (selectedCount < 1)
                return;
            Operatable operator = null;
            try {
                operator = OperatorFactory
                        .getOperator(com.coolsql.sql.commonoperator.SQLResultProcessOperator.class);
            } catch (Exception e1) {
                LogProxy.internalError(e1);
                return;
            }
            int[] selectRows = table.getSelectedRows();
            for (int i = 0; i < selectedCount; i++) {
                String bookmark = (String) table.getValueAt(selectRows[i], 1); //获取数签别名
                String sql = (String) table.getValueAt(selectRows[i], 2); //获取sql
                Bookmark bm = BookmarkManage.getInstance().get(
                        StringUtil.trim(bookmark));
                if (bm == null)
                    continue;
                List argList = new ArrayList();
                argList.add(bm);
                argList.add(sql);
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

    }
}
