/*
 * 创建日期 2006-12-1
 */
package com.coolsql.system;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.coolsql.pub.component.BaseDialog;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin 该类展示为一个对话框。 在结束程序时弹出，显示关闭资源的进度信息
 */
public class CloseProgressDialog extends BaseDialog implements SystemProcess {
    private JLabel label = null; //用于提示当前正在执行的动作

    private JProgressBar progressBar = null; //整体执行的进度条

    /**
     * 用于保存是否启动任务的标志。防止任务启动后，继续添加任务
     */
    private boolean isStart = false;

    /**
     * 保存所有需要执行的任务
     */
    private Vector tasks = null;

    public CloseProgressDialog() {
        this(null);

    }

    public CloseProgressDialog(List tasks) {
        super(GUIUtil.getMainFrame(), PublicResource
                .getString("system.closedialog.title"), false);
        if (tasks != null)
            this.tasks = new Vector(tasks);
        else
            this.tasks = new Vector();
        initialize();

    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        JPanel pane = (JPanel) this.getContentPane();
        pane.setLayout(new GridLayout(0, 1));

        //文本提示面板
        JPanel txtPane = new JPanel();
        txtPane.setLayout(new FlowLayout(SwingConstants.CENTER));
        label = new JLabel();
        label.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        txtPane.add(label);
        pane.add(txtPane);

        //进度条所在面板
        JPanel progressPane = new JPanel();
        progressPane.setLayout(new FlowLayout(SwingConstants.CENTER));
        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(480, 26));
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(false);
        setProgressValue(0);
        setProgressLength(getTotalLength()); //设置任务总量

        progressPane.add(progressBar);
        pane.add(progressPane);

        setSize(500, 159);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int result=JOptionPane.showConfirmDialog(CloseProgressDialog.this, PublicResource
                        .getString("system.closedialog.forcecloseconfirm"),
                        "force close confirm", JOptionPane.YES_NO_OPTION);
                if(result==JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * 设置提示信息
     * 
     * @param info
     *            --需要显示的文本信息
     */
    public void setPrompt(String info) {
        label.setText(info);
    }

    /**
     * 设置整体任务的进度值
     * 
     * @param value
     *            --进度值
     */
    public void setProgressValue(int value) {
        progressBar.setValue(value);
    }

    /**
     * 设置任务的最大任务长度
     * 
     * @param taskLength
     */
    public void setProgressLength(int taskLength) {
        progressBar.setMaximum(taskLength);
    }

    /**
     * 获取当前窗口是否已经启动任务
     * 
     * @return --true:已经启动 false:未启动
     */
    public boolean isLaunched() {
        return isStart;
    }

    /**
     * 添加任务
     * 
     * @param task
     *            --需要执行的任务
     */
    public void addTask(Task task) {
        if (task != null && !isStart)
            tasks.add(task);
    }

    /**
     * 设置新的任务集合，将原来的任务集合替换掉
     * 
     * @param list
     *            --新的任务列表
     */
    public void setTasks(List list) {
        if (!isStart && list != null) {
            tasks.clear();
            tasks = new Vector(list);
        }
    }

    /**
     * 删除指定的任务
     * 
     * @param task
     *            --需要删除的任务
     * @return --如果删除成功，返回true，否则返回false
     */
    public boolean removeTask(Task task) {
        if (!isStart && task.getTaskLength() > 0)
            return tasks.remove(task);
        else
            return false;
    }

    /**
     * 启动任务
     *  
     */
    public void start() {

        if (!isStart) {
            try {
                initTask();
                for (int i = 0; i < tasks.size() && isStart; i++) {
                    Object ob = tasks.get(i);
                    if (ob instanceof Task) {
                        Task task = (Task) ob;
                        setPrompt(task.getDescribe());
                        task.execute();
                        progressBar.setValue(progressBar.getValue()
                                + task.getTaskLength());
                    }
                }
            } finally {
                this.dispose();
                System.exit(0);
            }
        }

    }

    /**
     * 停止任务的执行
     *  
     */
    public void stop() {
        isStart = false;
    }

    /**
     * 任务启动前，做好任务显示的初始化工作
     *  
     */
    private void initTask() {
        isStart = true;
        setProgressValue(0);
        setProgressLength(getTotalLength()); //设置任务总量
    }

    /**
     * 获取所有任务的总长度
     * 
     * @return --总的任务量
     */
    private int getTotalLength() {
        int length = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Object ob = tasks.get(i);
            if (ob instanceof Task) {
                Task task = (Task) ob;
                int len = task.getTaskLength();
                if (len > 0)
                    length += len;
            }
        }
        return length;
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.system.SystemProcess#getDescribe()
     */
    public String getDescribe() {
        return "close system";
    }
}
