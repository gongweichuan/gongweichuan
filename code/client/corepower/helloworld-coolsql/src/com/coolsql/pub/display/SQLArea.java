/*
 * Created on 2007-1-8
 */
package com.coolsql.pub.display;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.coolsql.action.common.TextCopyAction;
import com.coolsql.action.common.TextSelectAllAction;
import com.coolsql.pub.component.BaseMenuManage;
import com.coolsql.pub.component.BasePopupMenu;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.pub.util.StringUtil;
import com.coolsql.view.sqleditor.SyntaxHighlighter;

/**
 * @author liu_xlin sql显示区域
 */
public class SQLArea extends WrapTextPane {

    /**
     * 高亮显示关键字的线程
     */
//    private SyntaxHighlighter highLight = null;

    /**
     * 文本编辑器的文档对象模型
     */
    protected SQLAreaDocument document = null;

    private SQLAreaPopMenu areaMenu=null;
    
    /**
     * 缺省元素属性设置
     */
    private MutableAttributeSet myAttributeSet;
    public SQLArea() {
        super();
        initComponent();
    }

    public SQLArea(StyledDocument doc) {
        super(doc);
        initComponent();
    }

    private void initComponent() {
        setEditable(false);
        setWrap(true);
        document = new SQLAreaDocument();
        setDocument(document);
//        highLight = new SyntaxHighlighter(document);
//        highLight.setName("highLight(SQLArea) Thread");

        
        myAttributeSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(myAttributeSet, 12);
        setParagraphAttributes(myAttributeSet, true);
        
        areaMenu=new SQLAreaPopMenu();
        
        this.addMouseListener(new PopMenuMouseListener()
                {
        			@Override
            		public void mouseReleased(MouseEvent e)
            		{
            		    if(isPopupTrigger(e))
            		    {
            		        areaMenu.getPopMenu().show(SQLArea.this,e.getX(),e.getY());
            		    }
            		}
                }
        );
    }

    public void setText(String text) {
        super.setText(text);
//        highLight.updateText(text, 0);
    }
    /**
     * 在文档模型最后添加文本内容
     * @param txt  --追加的文本内容
     * @throws BadLocationException
     */
    public void append(String txt) throws BadLocationException
    {
        document.insertString(document.getLength(),txt+"\n",myAttributeSet);
    }
    /**
     * 关闭高亮显示线程 删除容器内的所有组件
     */
    public void dispose() {
        SyntaxHighlighter.stopThread(document);

        removeAll();
    }
    /**
     * Translates an offset into the components text to a 
     * line number.
     *
     * @param offset the offset >= 0
     * @return the line number >= 0
     * @exception BadLocationException thrown if the offset is
     *   less than zero or greater than the document length.
     */
    public int getLineOfOffset(int offset) throws BadLocationException {
        Document doc = getDocument();
        if (offset < 0) {
            throw new BadLocationException("Can't translate offset to line", -1);
        } else if (offset > doc.getLength()) {
            throw new BadLocationException("Can't translate offset to line", doc.getLength()+1);
        } else {
            Element map = getDocument().getDefaultRootElement();
            return map.getElementIndex(offset);
        }
    }
    /**
     * 插入菜单项
     * @param label  --标签
     * @param icon  --图标
     * @param action  --点击菜单项的处理逻辑
     * @return  --返回被插入的菜单项，如果插入不成功，返回null
     */
    public JMenuItem insertMenuItem(String label,Icon icon,ActionListener action)
    {
        return areaMenu.insertMenuItem(label,icon,action);
    }
    public JCheckBoxMenuItem insertCheckBoxMenuItem(String label,Icon icon,ActionListener action)
    {
        return areaMenu.insertCheckBoxMenuItem(label,icon,action);
    }
    /**
     * 添加组合菜单
     * @param label  --组合菜单的标签
     * @return  --被添加的组合菜单
     */
    private JMenu insertMenu(String label)
    {
        return areaMenu.insertMenu(label);
    }
    /**
     * @return Returns the areaMenu.
     */
    public SQLAreaPopMenu getAreaMenuManage() {
        return areaMenu;
    }
    /**
     * 是否进行高亮处理
     * @return  --true：高亮处理 false：不处理
     */
    public boolean isHighlight()
    {
        return document.isHighlight();
    }
    /**
     * 设置是否高亮处理文档对象中的内容
     * @param isHighlight true:高亮处理 false:不作处理
     */
    public void setHighlight(boolean isHighlight)
    {
        document.setHighlight(isHighlight);
    }
    /**
     * 
     * @author liu_xlin
     *sql显示区域的右键菜单管理类
     */
    protected class SQLAreaPopMenu extends BaseMenuManage {

        private JMenuItem copy = null;

        private JMenuItem selectAll=null;
        /**
         * 是否已经添加了其他菜单项
         */
        private boolean isAddElseItem;
        public SQLAreaPopMenu() {
            super(SQLArea.this);
            isAddElseItem=false;
            createPopMenu();
        }

        /**
         * 插入普通菜单项
         * @param label  --标签
         * @param icon  --图标
         * @param action  --点击菜单项的处理逻辑
         * @return  --返回被插入的菜单项，如果插入不成功，返回null
         */
        private JMenuItem insertMenuItem(String label,Icon icon,ActionListener action)
        {
            if(popMenu==null)
                return null;
            
            if(!isAddElseItem)  //如果是第一次添加，添加分隔线
                popMenu.addSeparator();
            JMenuItem item=createMenuItem(label, icon, action);
            popMenu.insert(item,popMenu.getComponentCount());
            return item;
        }
        /**
         * 插入复选菜单项
         * @param label  --标签
         * @param icon  --图标
         * @param action  --点击菜单项的处理逻辑
         * @return  --返回被插入的菜单项，如果插入不成功，返回null
         */
        private JCheckBoxMenuItem insertCheckBoxMenuItem(String label,Icon icon,ActionListener action)
        {
            if(popMenu==null)
                return null;
            
            if(!isAddElseItem)  //如果是第一次添加，添加分隔线
                popMenu.addSeparator();
            JCheckBoxMenuItem item=new JCheckBoxMenuItem(label, icon, false);
            if(action!=null)
                item.addActionListener(action);
            popMenu.insert(item,popMenu.getComponentCount());
            return item;
           
        }
        /**
         * 添加组合菜单
         * @param label  --组合菜单的标签
         * @return  --被添加的组合菜单
         */
        private JMenu insertMenu(String label)
        {
            if(popMenu==null)
                return null;
            
            if(!isAddElseItem)  //如果是第一次添加，添加分隔线
                popMenu.addSeparator();
            
            JMenu m=new JMenu(label);
            popMenu.insert(m,popMenu.getComponentCount());
            return m;
        }
        protected void createPopMenu() {
            if (popMenu == null) {
                popMenu = new BasePopupMenu();

                Action copyAction = new TextCopyAction(SQLArea.this);
                copy = createMenuItem(PublicResource
                        .getString("TextEditor.popmenu.copy"), PublicResource
                        .getIcon("TextMenu.icon.copy"), copyAction);
                popMenu.add(copy);
                
        		Action selectAllAction = new TextSelectAllAction((JTextComponent)this.getComponent());
        		selectAll = this.createMenuItem(PublicResource
        				.getString("TextEditor.popmenu.selectAll"), PublicResource.getIcon("popmenu.icon.blank"),
        				selectAllAction);
        		popMenu.add(selectAll);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.coolsql.pub.display.BaseMenuManage#itemSet()
         */
        public BasePopupMenu itemCheck() {
            if(popMenu==null)
                createPopMenu();
                
			if (SQLArea.this.getSelectedText() == null
					||SQLArea.this.getSelectedText().trim().equals("")) {
			    GUIUtil.setComponentEnabled(false,copy);
			}else
			{
			    GUIUtil.setComponentEnabled(true,copy);
			}
			
			super.menuCheck();
            return popMenu;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.coolsql.pub.display.BaseMenuManage#getPopMenu()
         */
        public BasePopupMenu getPopMenu() {
            return itemCheck();
        }

    }

    protected class SQLAreaDocument extends DefaultStyledDocument{
        private HighlightDocumentListener highlightListener=null;
        public SQLAreaDocument() {
            super();
            String name=StringUtil.getHashNameOfObject(this);
            highlightListener=new HighlightDocumentListener(this);
            addDocumentListener(highlightListener);
        }
        public boolean isHighlight()
        {
            return highlightListener.isHighlight();
        }
        public void setHighlight(boolean isHighlight)
        {
            highlightListener.setHighlight(isHighlight);
        }
//
//        /*
//         * (non-Javadoc)
//         * 
//         * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
//         */
//        public void changedUpdate(DocumentEvent e) {
//
//        }
//
//        /*
//         * (non-Javadoc)
//         * 
//         * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
//         */
//        public void insertUpdate(DocumentEvent e) {
//            try {
//                highLight.updateText(getText(0, getLength()), 0);
//            } catch (BadLocationException e1) {
//                LogProxy.outputErrorLog(e1);
//            }
//        }
//
//        /*
//         * (non-Javadoc)
//         * 
//         * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
//         */
//        public void removeUpdate(DocumentEvent e) {
//            try {
//                highLight.updateText(getText(0, getLength()), 0);
//            } catch (BadLocationException e1) {
//                LogProxy.outputErrorLog(e1);
//            }
//        }
    }

}
