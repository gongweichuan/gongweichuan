/*
 * 创建日期 2006-12-28
 */
package com.coolsql.system;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin
 *登陆前的欢迎页面
 */
public class SplashWindow extends JWindow {

	private static final long serialVersionUID = 1L;

	private JProgressBar progressBar=null;
    
    /**
     * 用于保存是否启动任务的标志。防止任务启动后，继续添加任务
     */
    private boolean isStart = false;
    /**
     * 主题图标
     */
    private Icon themeIcon=null;
    /**
     * 保存所有需要执行的任务
     */
    private Vector tasks = null;
    public SplashWindow()
    {
        this(null);
    }
    public SplashWindow(Frame frame)
    {
        this(frame,null,null);
    }
    public SplashWindow(Frame frame,List tasks,Icon themeIcon) {
        super(frame);
        if (tasks != null)
            this.tasks = new Vector(tasks);
        else
            this.tasks = new Vector();
        this.themeIcon=themeIcon;
        initContent();

        pack();
        GUIUtil.centerFrameToFrame(null,this);
    }
    private void initContent()
    {
        JPanel content=(JPanel)getContentPane();
        content.setLayout(new BorderLayout());
        
        /**
         * 将主题图片添加在窗口的顶部
         */
        if(themeIcon==null)
            themeIcon=PublicResource.getIcon("system.splash.icon");
        JLabel themeLabel=new JLabel(themeIcon);
        content.add(themeLabel,BorderLayout.NORTH);
        
        /**
         * 添加版本信息
         */
        VersionLabel version=new VersionLabel();
        content.add(version,BorderLayout.CENTER);
        
        /**
         * 添加进度条
         */
        progressBar=new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        content.add(progressBar,BorderLayout.SOUTH);
        
        content.setBackground(new Color(216,233,236));
    }
    /**
     * 设置进度条的进度提示信息
     * @param txt
     */
    public void setProgressPrompt(String txt)
    {
        progressBar.setString(txt);
    }
    /**
     * 设置进度条的进度值
     * @param value
     */
    public void setProgressValue(int value)
    {
        progressBar.setValue(value);   
    }
    public Vector getAllTask() {
        return tasks;
    }
    public void setTasks(Vector tasks) {
        if(isStart)
            return;
        this.tasks = tasks;
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
     * 启动任务
     *  
     */
    public void start() {

        if (!isStart) {
            initTask();
            for (int i = 0; i < tasks.size() && isStart; i++) {
                Object ob = tasks.get(i);
                if (ob instanceof Task) {
                    Task task = (Task) ob;
                    setProgressPrompt(task.getDescribe());
                    task.execute();
                    progressBar.setValue(progressBar.getValue()
                            + task.getTaskLength());
                }
            }
        }

    }
    /**
     * 注销该窗口
     */
    public void dispose()
    {
        removeAll();
        if(tasks!=null)
            tasks.clear();
        tasks=null;
        themeIcon=null;
        super.dispose();
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
}
