/*
 * 创建日期 2006-9-15
 */
package com.coolsql.pub.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import com.coolsql.action.common.ExportExcelOfTableAction;
import com.coolsql.action.common.ExportHtmlOfTableAction;
import com.coolsql.action.common.ExportTextOfTableAction;
import com.coolsql.action.common.TableCopyAction;
import com.coolsql.pub.component.BaseMenuManage;
import com.coolsql.pub.component.BasePopupMenu;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.menubuild.IconResource;

/**
 * @author liu_xlin 表控件右键管理。基本功能包含:复制，数据导出，打印
 */
public class TableMenu extends BaseMenuManage {
    public static ActionListener printAction = null;

    /**
     * 复制
     */
    private JMenuItem copy;

    /**
     * 导出
     */
    private JMenu export;

    //导出成文本
    private JMenuItem exportTxt;

    //导出成excel文件
    private JMenuItem exportExcel;

    //导出成网页
    private JMenuItem exportHtml;

    private JMenuItem findInfo; //查找相关信息
    private JMenuItem adjustWidth; //调整列宽
    private JCheckBoxMenuItem isToolTip;//是否进行表格信息的提示
    /**
     * 表格设置
     */
    private JMenu tableLine = null;

    //横向线条的显示
    private JCheckBoxMenuItem horizontalLine = null;

    //竖向线条的显示
    private JCheckBoxMenuItem verticalLine = null;

    /**
     * enable/disable displaying table line number
     */
    private JCheckBoxMenuItem lineNumber;
    /**
     * 打印
     */
    private JMenuItem print;

    //是否显示打印想
    private boolean isPrintable = false;

    //是否现实数据导出项
    private boolean isExport = true;

    /**
     * @param com
     */
    public TableMenu(JTable table) {
        super(table);

    }

    protected void createPopMenu() {
        popMenu = new BasePopupMenu();

        //复制菜单的初始化
        Action copyAction = new TableCopyAction((JTable)getComponent());
        copy = createMenuItem(PublicResource
                .getString("TextEditor.popmenu.copy"), IconResource.ICON_COPY, copyAction);
        copy.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        popMenu.add(copy);

        
        
        boolean isAddSeparator=false; //判断是否添加分隔线
        
        if(getComponent()!=null&&getComponent() instanceof BaseTable)
        {
        	popMenu.addSeparator(); //添加分隔线
        	isAddSeparator=true;
        	
        	BaseTable tmpTable=(BaseTable)getComponent();
        	findInfo=createMenuItem(PublicResource                      //查找菜单
                .getString("table.popup.lable.findInfo"), PublicResource
                .getIcon("table.popup.icon.findInfo"), tmpTable.getDefaultFindAction());
        	popMenu.add(findInfo);
        	findInfo.setAccelerator(KeyStroke.getKeyStroke("alt F"));
        	
        	//调整列宽菜单项
        	adjustWidth=createMenuItem(PublicResource
                    .getString("table.popup.lable.adjustWidth"), PublicResource
                    .getIcon("table.popup.icon.adjustWidth"), tmpTable.getDefaultAdjustWidthAction());
        	popMenu.add(adjustWidth);
        	adjustWidth.setAccelerator(KeyStroke.getKeyStroke("alt W"));
        	
        	if (getComponent() instanceof CommonDataTable) // 如果为CommonDataTable类型的table控件，则添加是否显示提示信息的选项菜单
			{
        		if (((CommonDataTable)getComponent()).isDisplayToolTipSelectMenu()) {  //如果显示“提示菜单项”
					isToolTip = new JCheckBoxMenuItem(PublicResource
							.getString("table.popup.lable.istooltip"));
					isToolTip.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {

							((CommonDataTable) getComponent())
									.setEnableToolTip(isToolTip.getState());
						}

					});
					popMenu.add(isToolTip);
				}
			}
        }
        if (isExportable()) {
        	if(!isAddSeparator)
        		popMenu.addSeparator(); //添加分隔线
            //导出数据菜单
            export = new JMenu(PublicResource.getString("table.popup.export"));
            export.setIcon(IconResource.getBlankIcon());
            popMenu.add(export);

            //导出文本菜单
            exportTxt = createMenuItem(PublicResource
                    .getString("table.popup.exportTxt"), PublicResource
                    .getIcon("table.popup.export.txticon"),
                    new ExportTextOfTableAction((JTable)getComponent()));
            export.add(exportTxt);

            //导出excel菜单
            exportExcel = createMenuItem(PublicResource
                    .getString("table.popup.exportExcel"), PublicResource
                    .getIcon("table.popup.export.excelicon"),
                    new ExportExcelOfTableAction((JTable)getComponent()));
            export.add(exportExcel);

            //导出网页菜单
            exportHtml = createMenuItem(PublicResource
                    .getString("table.popup.exportHtml"), PublicResource
                    .getIcon("table.popup.export.htmlicon"),
                    new ExportHtmlOfTableAction((JTable)getComponent()));
            export.add(exportHtml);
        }

        if (isPrintable()) {
        	if(!isAddSeparator)
        		popMenu.addSeparator(); //添加分隔线
            //打印
            print = createMenuItem(PublicResource
                    .getString("table.popup.print"), PublicResource
                    .getIcon("table.popup.print.icon"), printAction);
            popMenu.add(print);
        }

        if (isTableLineSetting()) {
        	if(!isAddSeparator)
        		popMenu.addSeparator(); //添加分隔线
            //表格线设置
            tableLine = new JMenu(PublicResource
                    .getString("table.popup.tableline"));
            tableLine.setIcon(IconResource.getBlankIcon());
            popMenu.add(tableLine);

            //横向线设置
            horizontalLine = new JCheckBoxMenuItem(PublicResource
                    .getString("table.popup.horizontal"));
            horizontalLine.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    setHorizontalLine(horizontalLine.getState());
                }

            });
            tableLine.add(horizontalLine);

            //竖向线设置
            verticalLine = new JCheckBoxMenuItem(PublicResource
                    .getString("table.popup.vertical"));
            verticalLine.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    setVerticalLine(verticalLine.getState());
                }

            });
            tableLine.add(verticalLine);
        }
        lineNumber=new JCheckBoxMenuItem(PublicResource
                .getString("table.popup.linenumber"));
        lineNumber.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	((BaseTable)getComponent()).setTableRowNumberVisible(lineNumber.getState());
            }

        });
        popMenu.add(lineNumber);
    }
    /**
     * 如果要添加新的菜单项，获取插入的位置
     * @return --可添加菜单项的位置,如果弹出菜单对象没有初始化，返回-1
     * @override 
     */
    public int getAddPostion() {
        int index = -1;
        if (popMenu == null)
            return -1;
        
        if (findInfo != null) {
            index = popMenu.getComponentIndex(findInfo);
            if (index != -1) //如果查找菜单项已经添加在菜单中
                return index - 1;
        }
        //因为查找菜单和调整宽度菜单肯定同时显示/不显示所以，直接判断export菜单
        if (export != null) {
            index = popMenu.getComponentIndex(export);
            if (index != -1) //如果导出菜单项已经添加在菜单中
                return index - 1;
        }
        if (print != null) {
            index = popMenu.getComponentIndex(print);
            if (index != -1) //如果打印菜单项已经添加在菜单中
                return index - 1;
        }
        if (tableLine != null) {
            index = popMenu.getComponentIndex(tableLine);
            if (index != -1) //如果表格设置菜单项已经添加在菜单中
                return index - 1;
        }
        //如果导出菜单和打印菜单都没有被添加，那么插入最后
        return popMenu.getComponentCount();

    }

    private void setHorizontalLine(boolean b) {
        JTable tmpTable = (JTable) getComponent();
        if (tmpTable == null)
            return;
        tmpTable.setShowHorizontalLines(b);
    }

    private void setVerticalLine(boolean b) {
        JTable tmpTable = (JTable) getComponent();
        if (tmpTable == null)
            return;
        tmpTable.setShowVerticalLines(b);
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.pub.display.BaseMenuManage#itemSet()
     */
    public BasePopupMenu itemCheck() {
        int[] selects = ((JTable) getComponent()).getSelectedRows();
        if (selects == null || selects.length == 0) {
            if (copy.isEnabled())
                copy.setEnabled(false);
        } else {
            if (!copy.isEnabled())
                copy.setEnabled(true);
        }
        
        if(isToolTip!=null)
        	isToolTip.setSelected(((CommonDataTable)getComponent()).isToolTip());
        //属性设置部分
        horizontalLine.setSelected(((JTable) getComponent()).getShowHorizontalLines());
        verticalLine.setSelected(((JTable) getComponent()).getShowVerticalLines());

        lineNumber.setSelected(((CommonDataTable) getComponent()).isRowNumberVisible());
        
        menuCheck();
        return popMenu;
    }

    /*
     * （非 Javadoc）
     * 
     * @see com.coolsql.pub.display.BaseMenuManage#getPopMenu()
     */
    public BasePopupMenu getPopMenu() {
        if (popMenu == null)
            createPopMenu();

        return itemCheck();
    }

    /**
     * 设置右键菜单是否包含数据导出项,该设置只在菜单初始化前有效
     */
    public void setExportable(boolean isexport) {
        isExport = isexport;
    }

    /**
     * 是否显示导出数据菜单项
     * 
     * @return
     */
    public boolean isExportable() {
        return isExport;
    }

    /**
     * 设置该菜单是否包含打印按钮
     * 
     * @return
     */
    public boolean isPrintable() {
        return isPrintable;
    }

    /**
     * 返回是否显示表格线设置菜单项,可覆盖该方法
     * 
     * @return
     */
    public boolean isTableLineSetting() {
        return true;
    }

    /**
     * 设置是否显示打印项
     * 
     * @param isPrintable
     */
    public void setPrintable(boolean isPrintable) {
        this.isPrintable = isPrintable;
        if (!isPrintable) //不显示打印项
        {
            if (popMenu != null) {
                popMenu.remove(print);
                print.removeActionListener(printAction);
                print.removeAll();
                print = null;
            }
        } else //显示打印按钮
        {
            if (popMenu != null) {
                if (print == null) {
                    //打印
                    print = createMenuItem(PublicResource
                            .getString("table.popup.print"), PublicResource
                            .getIcon("table.popup.print.icon"), printAction);
                    int index = popMenu.getComponentIndex(export) + 1;
                    popMenu.insert(print, index);
                }
            }
        }
    }
}
