/*
 * Created on 2007-1-15
 */
package com.coolsql.modifydatabase;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.coolsql.pub.component.BaseDialog;
import com.coolsql.pub.component.RenderButton;
import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.display.Inputer;
import com.coolsql.pub.exception.UnifyException;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.sql.commonoperator.EntityPropertyOperator;
import com.coolsql.sql.commonoperator.Operatable;
import com.coolsql.sql.commonoperator.OperatorFactory;
import com.coolsql.sql.model.Entity;
import com.coolsql.view.bookmarkview.model.Identifier;
import com.coolsql.view.bookmarkview.model.TableNode;
import com.coolsql.view.bookmarkview.model.ViewNode;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin 实体选择对话框
 */
public class EntitySelectDialog extends BaseDialog {
	private static final long serialVersionUID = 1L;

	private EntitySelectPanel entitySelector = null;

    /**
     * 需要传递数据的接口
     */
    private Inputer inputer = null;

    public EntitySelectDialog(Inputer inputer) {
        this(GUIUtil.getMainFrame(),null, inputer);
    }
    public EntitySelectDialog(Entity entity,Inputer inputer) {
        this(GUIUtil.getMainFrame(),entity, inputer);
    }
    public EntitySelectDialog(Frame frame, Entity entity, Inputer inputer) {
        super(frame, true);
        createGUI(entity,inputer);
    }
    public EntitySelectDialog(Dialog dialog, Entity entity, Inputer inputer) {
        super(dialog, true);
        createGUI(entity,inputer);
    }
    private void createGUI(Entity entity, Inputer inputer)
    {
        setTitle(PublicResource.getSQLString("entityselect.dialog.title"));
        if (inputer == null) //inputer 不能为null
            throw new IllegalArgumentException("no Inputer is transferred");
        this.inputer = inputer;

        JPanel pane = (JPanel) getContentPane();
        pane.setLayout(new BorderLayout());

        if (entity == null)
            entitySelector = new EntitySelectPanel();
        else
            entitySelector = new EntitySelectPanel(entity.getBookmark()
                    .getAliasName(), entity.getCatalog(),entity.getSchema(), entity.getName());
        entitySelector.setBorder(BorderFactory.createEtchedBorder());
        pane.add(entitySelector, BorderLayout.CENTER);

        JPanel btnPane = new JPanel();
        btnPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        RenderButton detailInfo = new RenderButton(PublicResource.getSQLString("entityselect.btn.detail"));
        detailInfo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    detailEntity();
                } catch (UnifyException e1) {
                    LogProxy.errorReport(EntitySelectDialog.this, e1);
                } catch (SQLException e1) {
                    LogProxy.SQLErrorReport(EntitySelectDialog.this, e1);
                }

            }

        });
        RenderButton okBtn = new RenderButton(PublicResource.getSQLString("entityselect.btn.ok"));
        okBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(!confirmSelect())
                	return;
                quitSelect();
            }

        });
        RenderButton quit = new RenderButton(PublicResource.getSQLString("entityselect.btn.quit"));
        quit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                quitSelect();
            }

        });
        
        btnPane.add(okBtn);
        btnPane.add(detailInfo);       
        btnPane.add(quit);

        getRootPane().setDefaultButton(okBtn);
        pane.add(btnPane, BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quitSelect();
            }
        });
        setSize(350,250);
        toCenter();
    }
    /**
     * 点击确定按钮后,进行相应的数据处理 1、校验书签，模式，实体名称的有效性 2、执行数据传递
     * @return true:如果选择的信息合法，false:选择不合法
     */
    private boolean confirmSelect() {
        if (!checkSelectedValidate())
            return false;

        if (inputer != null)
            inputer.setData(getSelectedEntity());
        return true;
    }

    /**
     * 对当前实体选择的有效性进行校验
     * 
     * @return --true：有效 false:无效
     */
    private boolean checkSelectedValidate() {
        if (StringUtil.trim(getSelectedBookmark()).equals("")) {
            JOptionPane.showMessageDialog(this, PublicResource
                    .getSQLString("entityselect.nobookmark"), "warning", 2);
            return false;
        }
        /**
         * 2007-12-8 去除模式是否选择的判断。从模式数据加载的逻辑上看，不会出现选择null的情况。
         */
//        if (StringUtil.trim(getSelectedSchema()).equals("")) {
//            JOptionPane.showMessageDialog(this, PublicResource
//                    .getSQLString("entityselect.noschema"), "warning", 2);
//            return false;
//        }
        if (getSelectedEntity() == null
                || StringUtil.trim(getSelectedEntity().toString()).equals("")) {
            JOptionPane.showMessageDialog(this, PublicResource
                    .getSQLString("entityselect.nobookmark"), "warning", 2);
            return false;
        }
        return true;
    }

    /**
     * 查看实体的详细信息
     * 
     * @throws UnifyException
     * @throws SQLException
     */
    private void detailEntity() throws UnifyException, SQLException {
        if (!checkSelectedValidate())
            return;

        Entity entity = getSelectedEntity();
        String type = entity.getType();
        Identifier id = null;
        if (type == "VIEW") {
            id = new ViewNode(entity.getName(), entity.getBookmark(), entity);
        } else {
            id = new TableNode(entity.getName(), entity.getBookmark(), entity);
        }

        Operatable operator;
        try {
            operator = OperatorFactory
                    .getOperator(EntityPropertyOperator.class);
        } catch (ClassNotFoundException e) {
            LogProxy.errorReport(e);
            return;
        } catch (InstantiationException e) {

            LogProxy.errorReport(new UnifyException("internal error"));
            return;
        } catch (IllegalAccessException e) {
            LogProxy.errorReport(new UnifyException("internal error"));
            return;
        }
        List list=new ArrayList();
        list.add(id);
        list.add(this);
        list.add(new Boolean(true));
        operator.operate(list);
    }

    /**
     * 退出实体选择面板
     *  
     */
    private void quitSelect() {
        entitySelector.removeAll();
        entitySelector = null;
        removeAll();

        this.dispose();
    }

    /**
     * @return
     */
    public String getSelectedBookmark() {
        return entitySelector.getSelectedBookmark();
    }

    /**
     * @return
     */
    public String getSelectedSchema() {
        return entitySelector.getSelectedSchema();
    }

    /**
     * @return
     */
    public Entity getSelectedEntity() {
        return entitySelector.getSelectedEntity();
    }

    /**
     * @param bookmark
     * @param schema
     * @param entity
     */
    public void resetSelected(String bookmark, String catalog,String schema, String entity) {
        entitySelector.resetSelected(bookmark, catalog,schema, entity);
    }
}
