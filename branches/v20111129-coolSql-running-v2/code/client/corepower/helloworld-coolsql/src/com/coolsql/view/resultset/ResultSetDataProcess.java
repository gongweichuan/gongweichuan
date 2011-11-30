/*
 * 创建日期 2006-10-20
 */
package com.coolsql.view.resultset;

import java.sql.SQLException;

import javax.swing.SwingUtilities;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.sql.SQLResultSetResults;
import com.coolsql.sql.SQLResults;
import com.coolsql.sql.SQLUpdateResults;
import com.coolsql.view.ResultSetView;
import com.coolsql.view.ViewManage;
import com.coolsql.view.bookmarkview.RecentSQL;
import com.coolsql.view.bookmarkview.RecentSQLManage;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 结果集的获取、编辑处理类，该类主要用来负责处理结果集的获取、刷新、下一页，上一页的数据处理
 */
public class ResultSetDataProcess {
    public static int EXECUTE = 0; //正常的第一次执行

    public static int PREVIOUS = 1; //前一页数据处理

    public static int NEXT = 2; //下一页的数据处理

    public static int REFRESH = 3;//刷新处理

    private int processType;

    private DataSetPanel dataPane; //数据面板

    private String sql = null;

    private Bookmark bookmark;

    //    private JTable tmpTable = null; //临时表控件，该变量对于数据的页处理有用
    public ResultSetDataProcess(DataSetPanel dataPane, String sql,
            Bookmark bookmark, int processType) {
        super();
        if (!checkProcessType(processType))
            throw new IllegalArgumentException(
                    "process type is not in [0,3],error type:" + processType);

        this.processType = processType;
        this.dataPane = dataPane;
        this.sql = sql;
        this.bookmark = bookmark;

        //        dataReuse();
        dataPane.setPromptContent();
        dataPane.setReady(false); //将数据面把的状态置为未就绪
//        dataPane.setSqlResult(dataPane.getSqlResult())
//        dataPane.firePanelPropertyUpdate("sqlResult", null, );
    }

    /**
     * 校验处理类型
     * 
     * @param type
     * @return true if in part of the normal route,false if not in
     */
    private boolean checkProcessType(int type) {
        if (type < 0 || type > 3)
            return false;
        else
            return true;
    }
    /**
     * 执行处理
     *  
     */
    public void process() {
        if (processType == EXECUTE) //首次执行
        {
            firstExcecute();
        } else //再次处理
        {
            pageProcess();
        }
    }

    /**
     * 首次获取sql执行结果,同时将执行的sql语句加入书签节点中
     *  
     */
    protected void firstExcecute() {
        //第一步提示正在处理
        dataPane.setPromptText(PublicResource
                .getSQLString("sql.execute.process.start"));
        SQLResults set = null;
        try {
            set = bookmark.getDbInfoProvider().execute(bookmark.getConnection(), sql);          
            
            /**
             * 创建sql对象
             */
            RecentSQL sqlData=new RecentSQL(sql,set.getCostTime(),set.getTime(),bookmark);
            RecentSQLManage.getInstance().addSQL(sqlData,bookmark);
            
            //将sql执行时间显示在日志中
            LogProxy log = LogProxy.getProxy();
            log.debug(PublicResource
                    .getSQLString("sql.execute.process.costtime")
                    + ((float) set.getTime()) / 1000);
        } catch (SQLException e) {
            removeProcessingTab();
            LogProxy.SQLErrorReport(e);
            return;
        } catch (UnifyException e) {
            removeProcessingTab();
            LogProxy.errorReport(e);
            return;
        }

        //第二步提示初始化界面数据
        dataPane.setPromptText(PublicResource
                .getSQLString("sql.execute.process.initdata"));

        
        if (set.isResultSet()) //如果是查询结果集
        {
            SQLResultSetResults querySet = (SQLResultSetResults) set;
                       
            //创建表控件,同时初始化数据
            DataSetTable dataTable = new DataSetTable(querySet
                    .getVectorDataOfRow(), DataSetPanel
                    .getHeaderDefinition(querySet.getArrayDataOfColumn()));
            ResultSetView view=ViewManage.getInstance().getResultView();
            view.installDataSetTableSelectionListener(dataTable);
            dataPane.addTableToContent(dataTable); //更新内容部件,将数据展示在界面上
            
            /**
             * 将查询的结果集解析之后，展示在界面上
             */
            dataPane.setSqlResult(querySet);

        } else //如果是更新或者删除语句
        {
            /**
             * 将更新sql和处理时间，以及处理的纪录数展示在界面中（建立新类）
             */
            SQLUpdateResults updateResult = (SQLUpdateResults) set;
            
            UpdateResultPane updatePane = new UpdateResultPane(this.sql,
                    updateResult.getCostTime(), updateResult.getUpdateCount());
            dataPane.setReady(true); //设置状态为已就绪
            dataPane.setContent(updatePane);
            dataPane.setSqlResult(updateResult);
        }
    }

    /**
     * 数据的再处理，包含了刷新、上一页、下一页数据处理
     *  
     */
    protected void pageProcess() {

//        dataPane.setPromptContent();//添加提示标签

        dataPane.setPromptText(PublicResource
                .getSQLString("sql.execute.process.start"));

        boolean isError = false;
        try {
            if (processType == PREVIOUS)
                dataPane.previousPage();

            else if (processType == NEXT)
                dataPane.nextPage();
            else if (processType == REFRESH)
                dataPane.refreshPage();
        } catch (SQLException e) {
            isError = true;
            LogProxy.SQLErrorReport(e);
        } catch (UnifyException e) {
            isError = true;
            LogProxy.errorReport(e);
        }

        dataPane.setPromptText(PublicResource
                .getSQLString("sql.execute.process.initdata"));
        SQLResults data = dataPane.getSqlResult();
        if (data.isResultSet()) {
            SQLResultSetResults querySet = (SQLResultSetResults) data;
            //创建表控件,同时初始化数据
            DataSetTable dataTable = new DataSetTable(querySet
                    .getVectorDataOfRow(), DataSetPanel
                    .getHeaderDefinition(querySet.getArrayDataOfColumn()));
            ResultSetView view=ViewManage.getInstance().getResultView();
            view.installDataSetTableSelectionListener(dataTable);
            dataPane.addTableToContent(dataTable); //更新内容部件,将数据展示在界面上
            //给数据面板添加监听事件
            //            ResultSetView view=ViewManage.getInstance().getResultView();
            //            dataPane.addDataChangeListener(view.getResultSetListener());

            dataPane.setReady(true); //设置状态为已就绪
            if (!isError) { //如果没有错误，那么更新界面上的信息
                dataPane.firePanelPropertyUpdate("sqlResult", null, data);
                dataPane.updateResultInfo((SQLResultSetResults) data);
            }
        }
    }

    /**
     * 删除结果视图中对应组件的tab
     *  
     */
    private void removeProcessingTab() {
       	SwingUtilities.invokeLater(new Runnable()
    	{
    		public void run()
    		{
    			ResultSetView view = ViewManage.getInstance().getResultView();
    			view.removeTab(dataPane);
    			view.updateUI();
    		}
    	});
    }
}
