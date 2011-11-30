/*
 * Created on 2007-4-26
 */
package com.coolsql.pub.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.coolsql.pub.display.FindProcessConfig;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.display.StatusBar;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 此类型对应一个对话框，用于相应的组件的信息查找处理，
 */
public class FindFrame extends BaseDialog {

    private static List historylist = Collections
            .synchronizedList(new ArrayList());

    /**
     * 当前的查找窗口，该查找窗口同时只能实例化一个。当存在已经实例化的查找窗口，将会将存在的窗口关闭，重新打开新的窗口。
     */
    private static FindFrame currentFrame = null;

    /**
     * 该查找窗口中对应的参数选择，参数选择将直接映射到该对象中
     */
    private FindProcessConfig findConfig;

    /**
     * 查找过程处理对象
     */
    private FindProcess findProcess;

    /**
     * GUI组件部分
     */
    private RecordEditCombBox keyword;

    private JCheckBox caseArg; //大小写选项

    private JCheckBox matchMode; //匹配模式，是否全词匹配

    private JCheckBox isCircle; //是否回绕搜索

    private JRadioButton forward; //向前搜索

    private JRadioButton back; //向后搜索

    private StatusBar status;//状态栏，用于对查找结果的描述
    /**
     * 被查找的组件对象
     */
    private Component target = null;

    /**
     * 获取查找窗口对象实例
     * @param window --父窗口
     * @param title  --标题
     * @param target --被查找的组件
     * @param findProcess  --查找处理过程
     * @return
     */
    public static FindFrame getFindFrameInstance(Window window, String title,
            Component target, FindProcess findProcess) {
        if (currentFrame != null) {

            currentFrame.closeFrame();
        }
        currentFrame=null;
        if (window instanceof Frame)
            currentFrame = new FindFrame((Frame) window, title, target,
                    findProcess);
        else if (window instanceof Dialog)
            currentFrame = new FindFrame((Dialog) window, title, target,
                    findProcess);
        else
            throw new IllegalArgumentException(
                    "parent Window must be frame or dialog,error type:"
                            + window.getClass().getName());
        return currentFrame;
    }

    private FindFrame(Frame frame, String title, Component target,
            FindProcess findProcess) {
        super(frame, title);
        this.target = target;
        initGUI(findProcess);
    }

    private FindFrame(Dialog dialog, String title, Component target,
            FindProcess findProcess) {
        super(dialog, title);
        this.target = target;
        initGUI(findProcess);
    }

    /**
     * 初始化图形布局
     *  
     */
    protected void initGUI(FindProcess findProcess) {
        JPanel main = (JPanel) getContentPane();
        main.setLayout(new BorderLayout());
        
        JPanel inputPane=new JPanel();
        inputPane.setLayout(new BorderLayout());
        
        inputPane.add(createInputPane(), BorderLayout.NORTH); //添加输入面板

        inputPane.add(createArgumentPane(), BorderLayout.CENTER);//参数选择面板

        inputPane.add(createButtonPane(), BorderLayout.SOUTH);

        main.add(inputPane,BorderLayout.CENTER);
        
        status=new StatusBar();
        status.setToolTipText(PublicResource.getUtilString("findinfo.frame.status.tip"));
        main.add(status,BorderLayout.SOUTH);  //添加状态栏
        
        findConfig = new FindProcessConfig();
        this.findProcess = findProcess;

        pack();
        setResizable(false);
        initSelect();
    }

    /**
     * 初始化界面中控件选中与否
     *  
     */
    protected void initSelect() {
        forward.setSelected(true);

        /**
         * 装载上一次保存的关键字列表
         */
        for (int i = 0; i < historylist.size(); i++) {
            keyword.addItem(historylist.get(i));
        }
    }

    /**
     * 创建关键字输入面板
     * 
     * @return
     */
    private JPanel createInputPane() {
        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = GUIUtil.getDefaultBagConstraints();

        pane.add(new JLabel(PublicResource
                .getUtilString("findinfo.frame.keyword.label")), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx++;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        keyword = new RecordEditCombBox();
        pane.add(keyword, gbc);

        return pane;
    }

    /**
     * 创建参数选项面板
     * 
     * @return
     */
    private JPanel createArgumentPane() {
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(1, 2));

        //选项面板
        JPanel argPane = new JPanel();
        argPane.setLayout(new GridLayout(3, 1));
        caseArg = new JCheckBox(PublicResource
                .getUtilString("findinfo.frame.case.label"));
        argPane.add(caseArg);
        matchMode = new JCheckBox(PublicResource
                .getUtilString("findinfo.frame.matchmode.label"));
        argPane.add(matchMode);
        isCircle = new JCheckBox(PublicResource
                .getUtilString("findinfo.frame.iscircle.label"));
        argPane.add(isCircle);

        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        border = BorderFactory.createTitledBorder(border, PublicResource
                .getUtilString("findinfo.frame.argselect.label"));
        argPane.setBorder(border);

        //方向面板
        JPanel directionPane = new JPanel();
        directionPane.setLayout(new GridLayout(3, 1));
        forward = new JRadioButton(PublicResource
                .getUtilString("findinfo.frame.forward.label"));
        back = new JRadioButton(PublicResource
                .getUtilString("findinfo.frame.back.label"));
        ButtonGroup group = new ButtonGroup();
        group.add(forward);
        group.add(back);
        directionPane.add(forward);
        directionPane.add(back);
        directionPane.add(new JPanel());

        border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        border = BorderFactory.createTitledBorder(border, PublicResource
                .getUtilString("findinfo.frame.direction.label"));
        directionPane.setBorder(border);

        pane.add(directionPane);
        pane.add(argPane);

        return pane;
    }

    /**
     * 创建按钮面板
     * 
     * @return
     */
    private JPanel createButtonPane() {
        JPanel pane = new JPanel();
        pane.setLayout(new FlowLayout());
        RenderButton findBtn = new RenderButton(PublicResource
                .getUtilString("findinfo.frame.findbtn.label"));
        findBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                status.setText("");
                updateConfig();
                if (findConfig.getKeyWord().equals("")) {
                    LogProxy.message(FindFrame.this, PublicResource
                            .getUtilString("findinfo.frame.nokeyword"),
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    if (!findProcess.find(findConfig, target)) {
                        status.setText(PublicResource.getUtilString("findinfo.noresult"));
                        LogProxy.message(FindFrame.this, PublicResource
                                .getUtilString("findinfo.frame.nofind"),
                                JOptionPane.WARNING_MESSAGE);
                    } else
                    {
                        status.setText(findProcess.resultInfo());
                        keyword.record();
                    }
                } catch (Exception e1) {
                    status.setText(PublicResource.getUtilString("findinfo.noresult"));
                    LogProxy.errorReport(FindFrame.this, e1);
                }
            }

        });
        RenderButton quitBtn = new RenderButton(PublicResource
                .getUtilString("findinfo.frame.quitbtn.label"));
        quitBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeFrame();
            }

        });
        pane.add(findBtn);
        pane.add(quitBtn);
        return pane;
    }

    /**
     * 关闭窗口
     *  
     */
    public void closeFrame() {
        //保存查找关键字列表
        historylist.clear();
        for(int i=0;i<keyword.getItemCount();i++)
        {
            historylist.add(keyword.getItemAt(i));         
        }
        
        removeAll();
        dispose();
    }

    /**
     * 更新配置选项及参数，便于针对界面所选择的要求进行查找
     *  
     */
    public void updateConfig() {
    	Object selectedObject=keyword.getSelectedItem();
        findConfig.setKeyWord(selectedObject==null?"":selectedObject.toString());
        //方向参数
        if (forward.isSelected()) {
            findConfig.setForward(FindProcessConfig.FORWARD);
        } else if (back.isSelected())
            findConfig.setForward(FindProcessConfig.BACKFORWARD);

        //case
        findConfig
                .setCaseMatch(caseArg.isSelected() ? FindProcessConfig.SENSITIVECASE
                        : FindProcessConfig.IGNORECASE);

        //matchmode
        findConfig
                .setMatchMode(matchMode.isSelected() ? FindProcessConfig.MATCH_FULL
                        : FindProcessConfig.MATCH_PART);

        //isCircle
        findConfig.setCircle(isCircle.isSelected());
    }
}
