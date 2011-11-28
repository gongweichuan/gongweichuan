/*
 * Created on 2007-3-7
 */
package com.coolsql.modifydatabase.insert;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import com.coolsql.pub.component.BaseDialog;
import com.coolsql.pub.component.RenderButton;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.display.Inputer;
import com.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin 在为其他组件赋值之前，利用该对话框输入需要的值。
 */
public class InputDialog extends BaseDialog {

	private static final long serialVersionUID = 1L;

	private JTextArea inputer;//输入文本框

    private JCheckBox selectnull; //输入null值

//    private JRadioButton inputNew; //手工输入

    /**
     * 需要传递数据的接口
     */
    private Inputer input = null;

    public InputDialog(JFrame frame, Inputer input) {
        super(frame, true);
        this.input = input;
        createGUI();
    }

    public InputDialog(JDialog frame, Inputer input) {
        super(frame, true);
        this.input = input;
        createGUI();
    }

    protected void createGUI() {
        setTitle(PublicResource.getUtilString("inputdialog.title"));
        JPanel main = (JPanel) getContentPane();
        main.setLayout(new BorderLayout());

        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
//        ButtonGroup group = new ButtonGroup();
        selectnull = new JCheckBox(PublicResource
                .getUtilString("inputdialog.radiobutton.setnull"));
//        inputNew = new JRadioButton();
        SetAsNullListener listener = new SetAsNullListener();
        selectnull.addItemListener(listener);
//        inputNew.addItemListener(listener);
//        group.add(selectnull);
//        group.add(inputNew);

        gbc.anchor = GridBagConstraints.EAST;
        pane.add(selectnull, gbc);        
//        gbc.gridx++;
        
//        pane.add(new JLabel(PublicResource
//                .getUtilString("inputdialog.radiobutton.setnull")));
//        gbc.gridx--;
        gbc.gridy++;

//        pane.add(inputNew, gbc);
//        gbc.gridx++;
//        pane.add(new JLabel(PublicResource
//                .getUtilString("inputdialog.radiobutton.input")),gbc);
//        gbc.gridx++;
//        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty=1;
        inputer = new JTextArea();
        GUIUtil.installDefaultTextPopMenu(inputer);
        gbc.fill = GridBagConstraints.BOTH;
        pane.add(new JScrollPane(inputer), gbc);

//        inputNew.setSelected(true);
        pane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        main.add(pane, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout());
        RenderButton okButton = new RenderButton(PublicResource
                .getUtilString("inputdialog.okbutton"));
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                finishInput();

                closeDialog();
            }

        });
        RenderButton quitButton = new RenderButton(PublicResource
                .getUtilString("inputdialog.quitbutton"));
        quitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }

        });
        buttonPane.add(okButton);
        buttonPane.add(quitButton);

        main.add(buttonPane, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeDialog();
            }
        });
        setSize(400,330);
        toCenter();
    }
    public void setValue(Object t)
    {
    	if(t==null)
    	{
    		selectnull.setSelected(true);
    	}else
    	{
    		selectnull.setSelected(false);
    		inputer.setText(t.toString());
    	}
    	
    }
    /**
     * 完成输入有,调用该方法进行后续的数据传递处理
     *  
     */
    private void finishInput() {
        if (selectnull.isSelected()) //如果选择了null
        {
            input.setData(null);
        } else {
            String value = inputer.getText();
            input.setData(value);
        }
        input=null;
    }

    /**
     * 关闭窗口时所作的处理
     *  
     */
    private void closeDialog() {
        removeAll();
        dispose();
    }

    /**
     * Do something when the value of cell is set as NULL.
     * @author liu_xlin
     */
    private class SetAsNullListener implements ItemListener {

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
         */
        public void itemStateChanged(ItemEvent e) {
        	if(e.getStateChange() == ItemEvent.SELECTED)
        	{
        		inputer.setEnabled(false);
        	}else
        	{
        		inputer.setEnabled(true);
        		inputer.selectAll();
                inputer.requestFocusInWindow();
        	}
        	
//            if (e.getStateChange() == ItemEvent.SELECTED) { //如果指定项被选中以后,才作相应的调整
//                if (e.getSource() == selectnull) //设为null
//                {
//                    GUIUtil.setComponentEnabled(false, inputer);
//                } else {
//                    GUIUtil.setComponentEnabled(true, inputer);
//                    inputer.selectAll();
//                    inputer.requestFocus();
//                }
//            }

        }

    }
}
