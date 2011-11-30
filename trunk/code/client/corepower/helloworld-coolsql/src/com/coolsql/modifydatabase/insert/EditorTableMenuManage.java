/*
 * Created on 2007-1-30
 */
package com.coolsql.modifydatabase.insert;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import com.coolsql.pub.display.GUIUtil;
import com.coolsql.pub.display.Inputer;
import com.coolsql.pub.display.MenuCheckable;
import com.coolsql.pub.display.TableMenu;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.menubuild.IconResource;

/**
 * @author liu_xlin 可编辑表控件的弹出菜单管理器
 */
public class EditorTableMenuManage extends TableMenu {

    private JMenuItem paste;//粘贴菜单项

//    private JMenuItem previewSelect;  //预览选中菜单
    private JMenuItem insertSingle; //插入一行数据

    private JMenuItem insertRows; //插入多行数据

    private JMenuItem deleteSelected;//删除所选

//    private JMenuItem preferWidth;  //将选中列设置为最佳宽度
    
    private JMenuItem setValue;  //为选中的单元格赋值
    
    private JMenu shortDragType; //shortdrag type menu
    private JRadioButtonMenuItem shortDrag_copyType;  //set same value for all cells that be drag-selected
    private JRadioButtonMenuItem shortDrag_continusType;  //set continuous value for all cells that be drag-selected
    
    //  粘贴事件处理
    Action pasteAction ;
    Action adjustWidth;  //调整表格的最佳宽度
    /**
     * @param table
     */
    public EditorTableMenuManage(EditorTable table) {
        super(table);
        setExportable(true);
        setPrintable(false); //不提供打印菜单
        pasteAction = new BatchPasteAction();
        adjustWidth=new AbstractAction(){
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                EditorTable table = (EditorTable) getComponent();
                int[] cols=table.getSelectedColumns();
                table.columnsToFitWidth(cols);
            }
        };
        bindKey("alt W",adjustWidth,false);
    }
    protected void createPopMenu() {
        super.createPopMenu();        
        paste = addMenuItem(PublicResource
                .getString("TextEditor.popmenu.paste"), pasteAction,
                PublicResource.getIcon("TextMenu.icon.paste"), false);
        paste.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
        addMenuCheck(new MenuCheckable()
                {

                    public void check(Component table) {
                        if(getComponent() instanceof JTable)
                        {
                            /**
                             * 如果表控件选中了行,置为可用,否则不可用
                             */
                            int selectRows=((JTable)getComponent()).getSelectedRowCount();
                            if(selectRows<1)
                                GUIUtil.setComponentEnabled(false,paste);
                            else
                                GUIUtil.setComponentEnabled(true,paste);
                        }
                    }
            
                }
        );        
        
        //插入行
        JMenu insert = addMenu(PublicResource
                .getSQLString("rowinsert.table.popmenu.insert.label"), true);
        insert.setIcon(IconResource.getBlankIcon());
        //插入单行的事件处理类，在选种位置插入一行，如果没有选种，在表格尾部插入一条行
        ActionListener insertOneAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                EditorTable table = (EditorTable) getComponent();
                int row=table.getSelectedRow();
                if(row<0)
                    row=table.getRowCount();
                EditorTableModel model = (EditorTableModel) table
                .getModel();
                model.insertRow(row,EditorTableModel.getEmptyCell(table.getColumnCount()));
            }

        };
        insertSingle = createMenuItem(
                PublicResource
                        .getSQLString("rowinsert.table.popmenu.insertsingle.label"),
                PublicResource
                        .getSQLIcon("rowinsert.table.popmenu.insertsingle.icon"),
                insertOneAction);
        insert.add(insertSingle);
        ActionListener insertRowsAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {                
                EditorTable table = (EditorTable) getComponent();
                int row=table.getSelectedRow();
                if(row<0)
                    row=table.getRowCount();
                
                int rows=0;
                String promptTxt=PublicResource.getSQLString("rowinsert.table.popmenu.insertmutirows");
                String value="";
                while(true)
                {
                    value=JOptionPane.showInputDialog((Component)GUIUtil.getUpParent(table,Window.class),promptTxt);
                    if(value==null)  //撤销
                        return;
                    try
                    {
                        rows=Integer.parseInt(value);
                        break;
                    }catch(NumberFormatException e1)
                    {
                        promptTxt=PublicResource.getSQLString("rowinsert.table.popmenu.insert.invalidatevalue");
                    }
                }
                EditorTableModel model = (EditorTableModel) table
                .getModel();
                model.insertRows(row,EditorTableModel.getEmptyCell(rows,table.getColumnCount()));
            }

        };
        insertRows = createMenuItem(PublicResource
                .getSQLString("rowinsert.table.popmenu.insertrows.label"),
                PublicResource
                        .getSQLIcon("rowinsert.table.popmenu.insertrows.icon"),
                        insertRowsAction);
        insert.add(insertRows);

        /**
         * 考虑到其他地方需要用到编辑表控件,所以去掉该菜单项
         */
//        //添加"预览选中"菜单
//        previewSelect=addMenuItem(
//                PublicResource
//                .getSQLString("rowinsert.table.popmenu.previewselect"),
//                null,  
//        PublicResource
//                .getSQLIcon("rowinsert.table.popmenu.previewselect.icon"),
//        false);
//        addMenuCheck(new MenuCheckable()
//                {
//
//                    public void check(Component table) {
//                        if(getComponent() instanceof JTable)
//                        {
//                            /**
//                             * 如果表控件选中了行,置为可用,否则不可用
//                             */
//                            int selectRows=((JTable)getComponent()).getSelectedRowCount();
//                            if(selectRows<1)
//                                GUIUtil.setComponentEnabled(false,previewSelect);
//                            else
//                                GUIUtil.setComponentEnabled(true,previewSelect);
//                        }
//                    }
//            
//                }
//        );
        
        //‘删除’菜单项
        ActionListener deleteAction = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                EditorTable table = (EditorTable) getComponent();
                int[] selectRows=table.getSelectedRows();
                if(selectRows==null||selectRows.length<1)
                    return;
                EditorTableModel model = (EditorTableModel) table
                .getModel();
                
                for(int i=selectRows.length-1;i>-1;i--)
                {
                    model.removeRow(selectRows[i]);
                }
            }
            
        };
        deleteSelected = addMenuItem(
                PublicResource
                        .getSQLString("rowinsert.table.popmenu.deleteselected.label"),
                        deleteAction,
                PublicResource
                        .getSQLIcon("rowinsert.table.popmenu.deleteselected.icon"),
                false);
        addMenuCheck(new MenuCheckable()
                {

                    public void check(Component table) {
                        if(getComponent() instanceof JTable)
                        {
                            /**
                             * 如果表控件选中了行,置为可用,否则不可用
                             */
                            int selectRows=((JTable)getComponent()).getSelectedRowCount();
                            if(selectRows<1)
                                GUIUtil.setComponentEnabled(false,deleteSelected);
                            else
                                GUIUtil.setComponentEnabled(true,deleteSelected);
                        }
                    }
            
                }
        );
        
        //最佳宽度设置
//        preferWidth=addMenuItem(
//                PublicResource
//                .getSQLString("rowinsert.table.popmenu.preferwidth"),
//                adjustWidth,
//        PublicResource
//                .getSQLIcon("rowinsert.table.popmenu.preferwidth.icon"),
//        false);
//        preferWidth.setAccelerator(KeyStroke.getKeyStroke("alt W"));
//        addMenuCheck(new MenuCheckable()
//                {
//
//                    public void check(Component table) {
//                        if(getComponent() instanceof JTable)
//                        {
//                            /**
//                             * 如果表控件选中了列,置为可用,否则不可用
//                             */
//                            int selectColss=((JTable)getComponent()).getSelectedColumnCount();
//                            if(selectColss<1)
//                                GUIUtil.setComponentEnabled(false,preferWidth);
//                            else
//                                GUIUtil.setComponentEnabled(true,preferWidth);
//                        }
//                    }
//            
//                }
//        );
        //为单元格赋值菜单项
        Action setValueAction=new AbstractAction()
        {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
                Inputer inputer=new Inputer()
                {

                    public void setData(Object value) {
                       
                        JTable table=(JTable)getComponent();
                        int[] selectRows=table.getSelectedRows();
                        int[] selectColumns=table.getSelectedColumns();
                        for(int i=0;i<selectRows.length;i++)
                        {
                            for(int j=0;j<selectColumns.length;j++)
                            {
                                InsertTableCell cell=(InsertTableCell)table.getValueAt(selectRows[i],selectColumns[j]);
                                if(value==null)
                                {
                                    cell.setNull(true);                                    
                                }else
                                {
                                    cell.setNull(false);
                                    cell.setValue(value);
                                }
                                table.setValueAt(cell,selectRows[i],selectColumns[j]);
                            }
                        }
                    }
                    
                };
                InputDialog dialog=null;
                Window window=(Window)GUIUtil.getUpParent(getComponent(),Window.class);
                if(window instanceof JFrame)
                    dialog=new InputDialog((JFrame)window,inputer);
                else 
                    dialog=new InputDialog((JDialog)window,inputer);
                
                JTable table=(JTable)getComponent();
                if(table.getSelectedColumnCount()==1&&table.getSelectedRowCount()==1)
                {
                	int rowIndex=table.getSelectedRow();
                	int columnIndex=table.getSelectedColumn();
                	InsertTableCell cell=(InsertTableCell)table.getValueAt(rowIndex,columnIndex);
                	dialog.setValue(cell.isNull()?null:cell.toString());
                }
                
                dialog.setVisible(true);
            }
            
        };
        setValue=addMenuItem(
                PublicResource
                .getSQLString("rowinsert.table.popmenu.setvalue"),
                setValueAction,
        PublicResource
                .getSQLIcon("rowinsert.table.popmenu.setvalue.icon"),
        false);
        addMenuCheck(new MenuCheckable()  //如果表控件没有选中任何表格,置位不可用
                {

                    public void check(Component table) {
                        JTable t=(JTable)table;
                        if(t.getSelectedRowCount()<1)
                        {
                            GUIUtil.setComponentEnabled(false,setValue);
                        }else
                            GUIUtil.setComponentEnabled(true,setValue);
                    }
            
                }
        );
        
        shortDragType=addMenu(PublicResource.getSQLString("rowinsert.table.popmenu.shortdragtype.label"), false);
        shortDragType.setIcon(IconResource.getBlankIcon());
        
        shortDrag_copyType=new JRadioButtonMenuItem(PublicResource.getSQLString("rowinsert.table.popmenu.shortdrag.copytype"));
        shortDrag_copyType.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		EditorTable table = (EditorTable) getComponent();
//        		int type=shortDrag_copyType.isSelected()?EditorTable.SHORTDRAG_TYPE_COPY:EditorTable.SHORTDRAG_TYPE_CONTINUOUS;
        		table.setShortDragType(EditorTable.SHORTDRAG_TYPE_COPY);
        	}
        }
        );
        addMenuCheck(new MenuCheckable()
        {

			public void check(Component com) {
				EditorTable table = (EditorTable) getComponent();
				int type=table.getShortDragType();
				shortDrag_copyType.setSelected(type==EditorTable.SHORTDRAG_TYPE_COPY);
			}
        	
        }
        );
        shortDragType.add(shortDrag_copyType);
        shortDrag_continusType=new JRadioButtonMenuItem(PublicResource.getSQLString("rowinsert.table.popmenu.shortdrag.continuoustype"));
        shortDrag_continusType.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		EditorTable table = (EditorTable) getComponent();
//        		int type=shortDrag_continusType.isSelected()?EditorTable.SHORTDRAG_TYPE_CONTINUOUS:EditorTable.SHORTDRAG_TYPE_COPY;
        		table.setShortDragType(EditorTable.SHORTDRAG_TYPE_CONTINUOUS);
        	}
        }
        );
        addMenuCheck(new MenuCheckable()
        {

			public void check(Component com) {
				EditorTable table = (EditorTable) getComponent();
				int type=table.getShortDragType();
				shortDrag_continusType.setSelected(type==EditorTable.SHORTDRAG_TYPE_CONTINUOUS);
			}
        	
        }
        );
        shortDragType.add(shortDrag_continusType);
    }
     protected class BatchPasteAction extends AbstractAction 
    {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
            EditorTable table = (EditorTable) getComponent();

            try {
                table.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                EditorTableModel model = (EditorTableModel) table
                        .getModel();
                model.batchPaste(table.getSelectedRow(), table
                        .getSelectedColumn(),table);
            } finally {
                table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }     
    }
}
