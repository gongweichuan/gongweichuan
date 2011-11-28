/*
 * 创建日期 2006-10-20
 */
package com.coolsql.view.resultset;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.coolsql.pub.component.TextEditor;
import com.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin
 *如果sql为更新或者删除类型,那么其执行后的结果展示在该面板类之上
 */
public class UpdateResultPane extends JPanel {
    
   private TextEditor sql=null;  //sql语句
   private TextEditor costTime=null;  //sql执行所花费时间
   private TextEditor updateCount=null;  //更新数据的总数
   public UpdateResultPane(String sql,long time,int count)
   {
       super();
       guiInit();
       setData(sql,time,count);
   }
   /**
    * 面板界面的初始化
    *
    */
   protected void guiInit()
   {
       this.setLayout(new GridBagLayout());
       GridBagConstraints gbc = new GridBagConstraints();
       
       gbc.gridx = 0;
       gbc.gridy = 0;
       gbc.fill = GridBagConstraints.HORIZONTAL;
       gbc.weightx = 0;
       gbc.weighty = 0.1;
       gbc.insets = new Insets(2, 2, 2, 2);
       
       //sql
       gbc.anchor = GridBagConstraints.EAST;
       gbc.gridwidth=1;
       this.add(new JLabel(PublicResource.getString("resultView.datapane.updatepane.sql"),SwingConstants.RIGHT),gbc);
       gbc.gridwidth = GridBagConstraints.REMAINDER;
       gbc.gridx++;
       gbc.anchor = GridBagConstraints.WEST;
       gbc.weightx = 1;
       if(sql==null)
           sql=new TextEditor();       
       sql.setEditable(false);
       this.add(sql, gbc);
       gbc.gridy++;
       gbc.gridx--;
       
       //花费时间
       gbc.anchor = GridBagConstraints.EAST;
       gbc.gridwidth=1;
       this.add(new JLabel(PublicResource.getString("resultView.datapane.updatepane.costtime"),SwingConstants.RIGHT),gbc);
       gbc.gridwidth = GridBagConstraints.REMAINDER;
       gbc.gridx++;
       gbc.anchor = GridBagConstraints.WEST;
       gbc.weightx = 1;
       if(costTime==null)
           costTime=new TextEditor();   
       costTime.setEditable(false);
       this.add(costTime, gbc);
       gbc.gridy++;
       gbc.gridx--;
       
       //sql
       gbc.anchor = GridBagConstraints.EAST;
       gbc.gridwidth=1;
       this.add(new JLabel(PublicResource.getString("resultView.datapane.updatepane.updatecount"),SwingConstants.RIGHT),gbc);
       gbc.gridwidth = GridBagConstraints.REMAINDER;
       gbc.gridx++;
       gbc.anchor = GridBagConstraints.WEST;
       gbc.weightx = 1;
       if(updateCount==null)
           updateCount=new TextEditor();     
       updateCount.setEditable(false);
       this.add(updateCount, gbc);
       gbc.gridy++;
       gbc.gridx--;
   }
   /**
    * 更新面板的数据信息
    * @param sql
    * @param time
    * @param count
    */
   public void setData(String sql,long time,int count)
   {
       if(this.sql!=null)
           this.sql.setText(sql);
       if(costTime!=null)
           costTime.setText(time+PublicResource.getSQLString("sql.execute.time.unit"));
       if(updateCount!=null)
           updateCount.setText(String.valueOf(count));
   }
}
