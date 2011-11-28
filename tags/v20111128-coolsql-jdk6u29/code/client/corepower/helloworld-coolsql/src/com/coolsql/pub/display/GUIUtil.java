/*
 * 创建日期 2006-10-18
 */
package com.coolsql.pub.display;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.MutableComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;

import com.coolsql.bookmarkBean.Bookmark;
import com.coolsql.bookmarkBean.BookmarkManage;
import com.coolsql.main.frame.MainFrame;
import com.coolsql.pub.component.MainFrameStatusBar;
import com.coolsql.pub.component.TextComponentPopMenu;
import com.coolsql.pub.parse.PublicResource;
import com.coolsql.system.PropertyManage;
import com.coolsql.view.View;
import com.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin
 * 
 */
public class GUIUtil {

	public static final int DEFAULT_VIEWWIDTH=200;
	
	private static JFrame mainFrame = null; // cache main frame
	/**
	 * 当前的外观
	 */
	private static String currentLookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";

	public static File selectFolder(Container con,String initDir,boolean isOpen)
	{
		JFileChooser fc = new JFileChooser(initDir);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setMultiSelectionEnabled(false); // 只允许选择一个文件
		if(con==null)
			con=getMainFrame();
		
		int select = isOpen?fc.showOpenDialog(con):fc.showSaveDialog(con);
		if (select == JFileChooser.APPROVE_OPTION) { // 如果选择了文件
			return fc.getSelectedFile();
		}else
			return null;
	}
	/**
	 * 选择单一文件，所选范围为所有文件
	 * 
	 * @param con
	 *            父容器对象
	 * @return
	 */
	public static File selectFileNoFilter(Container con) {
		return selectFileNoFilter(con, PropertyManage.getSystemProperty()
				.getSelectFile_exportData(),false);
	}

	/**
	 * 选择单一文件，所选范围为所有文件
	 * 
	 * @param con
	 * @param filter
	 *            过滤器
	 * @return
	 */
	public static File selectFileByFilter(Container con, FileFilter filter) {
		File file[]= selectFileByFilter(con, filter,new FileFilter[]{filter}, PropertyManage
				.getSystemProperty().getSelectFile_exportData(),false,false,true);
		if(file==null||file.length==0)
			return null;
		return file[0];
	}
	public static File selectFileByFilter(Container con, FileFilter filter,boolean isPromptOnExist) {
		File file[]= selectFileByFilter(con, filter,new FileFilter[]{filter}, PropertyManage
				.getSystemProperty().getSelectFile_exportData(),false,false,isPromptOnExist);
		if(file==null||file.length==0)
			return null;
		return file[0];
	}
	public static File selectFileNoFilter(Container con, String currentDir)
	{
		return selectFileNoFilter(con, currentDir,false);
	}
	public static File FileSelectFileNoFilter(Container con,String currentDir,boolean isPromptOnExist)
	{
		//single select,save dialog
		File file[]= selectFileNoFilter(con,currentDir,false,false,isPromptOnExist);
		if(file==null||file.length==0)
			return null;
		return file[0];
	}
	public static File selectFileNoFilter(Container con, String currentDir,boolean isOpen) {
		File[] file=selectFileNoFilter(con,currentDir,false,isOpen,true);
		if(file==null||file.length==0)
			return null;
		return file[0];
	}
	public static File[] selectFileNoFilter(Container con, String currentDir,
			boolean isMultiSelectable, boolean isOpen, boolean isPromptOnExist) {
		JFileChooser fc = new JFileChooser(currentDir);
		fc.setMultiSelectionEnabled(isMultiSelectable); // 只允许选择一个文件
		if(con==null)
			con=getMainFrame();
		
		int select = isOpen?fc.showOpenDialog(con):fc.showSaveDialog(con);
		if (select == JFileChooser.APPROVE_OPTION) { // 如果选择了文件
			File[] tmp;
			if(isMultiSelectable)
				tmp = fc.getSelectedFiles(); // 获取选中的文件
			else
				tmp=new File[]{fc.getSelectedFile()};
			if (tmp != null&&tmp.length>0) {
				// currentPath = tmp.getParent(); //更新当前所选文件的目录
				if (!isOpen&&isPromptOnExist&&tmp[0].exists()) {
					int result = JOptionPane.showConfirmDialog(con,
							PublicResource.getString("fileselect.exist"),
							"confirm!", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.NO_OPTION)
						return selectFileNoFilter(con, tmp[0].getParent(),isMultiSelectable,isOpen,isPromptOnExist);
				}
			}
			return tmp;
		} else
			return null;
	}
	public static File selectFileByFilter(Container con, FileFilter filter,
			String currentDir) {
		File file[]= selectFileByFilter(con,filter,filter!=null?new FileFilter[]{filter}:null,currentDir,false,true,false);
		if(file==null||file.length==0)
			return null;
		return file[0];
	}
	public static File selectFileByFilter(Container con, FileFilter filter,
			String currentDir,boolean isPromptOnExist) {
		File file[]= selectFileByFilter(con,filter,filter!=null?new FileFilter[]{filter}:null,currentDir,false,true,isPromptOnExist);
		if(file==null||file.length==0)
			return null;
		return file[0];
	}
	public static File[] selectFileByFilter(Container con, FileFilter initialFilter,FileFilter filters[],
			String currentDir,boolean isMutiSelectable,boolean isOpen,boolean isPromptOnExist) {
		JFileChooser fc = new JFileChooser(currentDir);

		if(filters!=null)
		{
			for(int i=0;i<filters.length;i++)
			{
				if(filters[i]==null)
					continue;
				fc.addChoosableFileFilter(filters[i]);
			}
		}
		if(initialFilter!=null)
			fc.setFileFilter(initialFilter);
		fc.setMultiSelectionEnabled(isMutiSelectable);
		int select = isOpen?fc.showOpenDialog(con):fc.showSaveDialog(con);
		if (select == JFileChooser.APPROVE_OPTION) { // 如果选择了文件
			File[] tmp =null;
			if(isMutiSelectable)
				tmp=fc.getSelectedFiles(); // 获取选中的文件
			else
			{
				tmp=new File[]{fc.getSelectedFile()};
			}
			if (tmp != null&&tmp.length==1) {//do only  when one file is selected .
				// currentPath = tmp.getParent(); //更新当前所选文件的目录
				if (isPromptOnExist&&!isOpen&&tmp[0].exists()) {
					int result = JOptionPane.showConfirmDialog(con,
							PublicResource.getString("fileselect.exist"),
							"confirm!", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.NO_OPTION)
						return selectFileByFilter(con,initialFilter ,filters,currentDir,isMutiSelectable,isOpen,isPromptOnExist);
				}
			}
			return tmp;
		} else
			return null;
	}
	/**
	 * 调用操作系统本地文件选择对话框来选择文件
	 */
	public static File selectFileNoFilterWindow(JFrame con) {
		return selectFileNoFilterWindow(con, null);
	}

	/**
	 * 调用操作系统本地文件选择对话框来选择文件
	 */
	public static File selectFileNoFilterWindow(JFrame con,
			FilenameFilter filter) {
		if(con==null)
			con=getMainFrame();
		FileDialog fd = new FileDialog(con, "select a file", FileDialog.LOAD);
		if (filter != null)
			fd.setFilenameFilter(filter);
		fd.setVisible(true);
		if (fd.getDirectory() == null || fd.getFile() == null)
			return null;
		else {
			File file = new File(fd.getDirectory(), fd.getFile());
			return file;
		}
	}

	/**
	 * 获取表控件的列标识
	 * 
	 * @param table
	 *            --表控件
	 * @return --列标识
	 */
	public static Vector getColumnIdenfiers(JTable table) {
		TableColumnModel columnModel = table.getColumnModel();
		if (columnModel == null)
			return null;
		int count = columnModel.getColumnCount();
		Vector ob = new Vector(count);
		for (int i = 0; i < count; i++)
			ob.add(columnModel.getColumn(i).getHeaderValue());
		return ob;
	}

	/**
	 * Get main frame object instance.
	 */
	public static JFrame getMainFrame() {
		if (mainFrame == null)
			mainFrame = new MainFrame();
		return mainFrame;
	}
	public static void updateSystemStatusBarForSQLEditor(String info)
	{
		if(info==null)
			return;
		BorderLayout layout=(BorderLayout)mainFrame.getContentPane().getLayout();
		Component com=layout.getLayoutComponent(BorderLayout.SOUTH);
		if(com instanceof MainFrameStatusBar)
		{
			((MainFrameStatusBar)com).setEditorInfo(info);
		}
	}
	/**
	 * Add a status bar to the main frame
	 */
	public static void showStatusBarOfMainFrame()
	{
		if(mainFrame==null)
			return;
		BorderLayout layout=(BorderLayout)mainFrame.getContentPane().getLayout();
		Component com=layout.getLayoutComponent(BorderLayout.SOUTH);
		if(com instanceof MainFrameStatusBar)
			return;
		
		JPanel pane=(JPanel)mainFrame.getContentPane();
		MainFrameStatusBar statusBar=new MainFrameStatusBar();
		pane.add(statusBar,BorderLayout.SOUTH);
		pane.validate();
	}
	/**
	 * Hide system status bar.
	 */
	public static void hideStatusBarOfMainFrame()
	{
		if(mainFrame==null)
			return;
//		JPanel pane=(JPanel)mainFrame.getContentPane();
//		Component[] components=pane.getComponents();
//		
//		for(Component com:components)
//		{
//			if(com instanceof MainFrameStatusBar)
//			{
//				((MainFrameStatusBar)com).dispose();
//				pane.remove(com);
//			}
//		}
//		pane.validate();
		BorderLayout layout=(BorderLayout)mainFrame.getContentPane().getLayout();
		Component com=layout.getLayoutComponent(BorderLayout.SOUTH);
		if(com instanceof MainFrameStatusBar)
		{
			((MainFrameStatusBar)com).dispose();
			mainFrame.getContentPane().remove(com);
		}
		mainFrame.getContentPane().validate();
	}
	/**
	 * 获取给定菜单项的顶层组件（JPopupMenu），如果该菜单不是弹出菜单，返回null
	 * 
	 * @param item
	 *            --指定的菜单项
	 * @return
	 */
	public static JPopupMenu getTopMenu(JMenuItem item) {
		JPopupMenu popMenu = (JPopupMenu) getUpParent(item, JPopupMenu.class,
				false);
		while (popMenu.getInvoker() instanceof JMenu) {
			popMenu = (JPopupMenu) getUpParent((JComponent) popMenu
					.getInvoker(), JPopupMenu.class, false);
		}
		return popMenu;
	}

	/**
	 * 获取给定组件的顶层组件(parent)，如果给定组件不存在指定类型的父类组件，返回null
	 * 
	 * @param com
	 *            --给定的子组件
	 * @param parent
	 *            --父组件的类型
	 * @return --可视的父类容器
	 */
	public static Container getUpParent(Container com, Class parent) {
		return getUpParent(com, parent, true);
	}

	/**
	 * 获取给定组件的顶层组件(parent)，如果参数：isMustVisible为true，必须返回可视的父容器窗口；如果给定组件不存在指定类型的父类组件，返回null
	 * 
	 * @param com
	 *            --给定的子组件
	 * @param parent
	 *            --父组件的类型
	 * @param isMustVisible
	 *            --是否必须获取可视的父容器
	 * @return --可视的父类容器
	 */
	public static Container getUpParent(Container com, Class parent,
			boolean isMustVisible) {
		if (com == null || parent == null)
			return null;
		if (parent == com.getClass())
			return com;
		Container con = com.getParent();
		for (; con != null && !parent.isAssignableFrom(con.getClass()); con = con
				.getParent());
		if (con != null && !con.isVisible() && isMustVisible) // 如果必须获取可视的组件容器，将递归调用自身方法
		{
			con = getUpParent(con, parent, isMustVisible);
		}
		return con;
	}

	/**
	 * 设置组件的可用性
	 * 
	 * @param isEnable
	 * @param btn
	 */
	public static void setComponentEnabled(boolean isEnable, JComponent btn) {
		if (btn.isEnabled() != isEnable)
			btn.setEnabled(isEnable);
	}

	/**
	 * 创建目录结构，如果目录不存在，则建立之。如果给定的文件是目录，将建立完整目录；如果为文件，根据标志判断是否创建文件
	 * 
	 * @param fileName
	 *            --文件名
	 * @param isDirectory
	 *            --fileName所对应的文件是否为目录
	 * @param isCreate
	 *            --true：如果是文件，创建 。 false：如果是文件，只建立文件的上层目录
	 * @throws IOException
	 *             --如果创建文件出错，抛出此异常
	 */
	public static void createDir(String fileName, boolean isDirectory,
			boolean isCreate) throws IOException {
		File file = new File(fileName);
		if (file.exists())
			return;
		else {
			File parentFile = file.getParentFile();
			if (parentFile != null && !parentFile.exists())
				createDir(parentFile.getAbsolutePath(), true, true);
			else if (parentFile != null && !parentFile.isDirectory()) {
				throw new IOException("目录名称与其他文件重名:"
						+ parentFile.getAbsolutePath());
			}

			if (isDirectory) // 如果上级目录存在，并且当前文件为目录，创建当前目录
				file.mkdir();
			else if (isCreate) {
				file.createNewFile();
			}
		}
	}

	/**
	 * 以一个容器作为参照，使另一个容器居于其中间. 如果父窗口为null，以屏幕作为参照
	 * 
	 * @param owner
	 *            参照容器
	 * @param frame
	 *            被调整容器
	 */
	public static void centerFrameToFrame(Container owner, Container frame) {
		if (owner == null) {
			Dimension rect = Toolkit.getDefaultToolkit().getScreenSize();
			frame.setBounds((int) ((rect.getWidth() - frame.getWidth()) / 2),
					(int) ((rect.getHeight() - frame.getHeight()) / 2), frame
							.getWidth(), frame.getHeight());

			return;
		}
		Rectangle rect = owner.getBounds();
		frame
				.setBounds((int) (rect.getX() + (rect.getWidth() - frame
						.getWidth()) / 2), (int) (rect.getY() + (rect
						.getHeight() - frame.getHeight()) / 2), frame
						.getWidth(), frame.getHeight());
	}
	public static void centerToOwnerWindow(Window frame)
	{
		if(frame==null)
			return;
		Container owner=frame.getOwner();
		if(owner==null)
			return;
		centerFrameToFrame(owner,frame);
	}
	/**
	 * 渲染界面外观主题
	 * 
	 * @param theme
	 *            --新的主题类
	 * @param com
	 *            --被渲染的组件
	 */
	public static void renderTheme(MetalTheme theme, Component com) {

		UIManager.put("swing.boldMetal", Boolean.FALSE);
		if (MetalLookAndFeel.class.isAssignableFrom(UIManager.getLookAndFeel()
				.getClass())) // 如果当前外观为MetalLookAndFeel，才能设置主题。
			MetalLookAndFeel.setCurrentTheme(theme);
		try {
			UIManager.setLookAndFeel(currentLookAndFeel);

			if (com != null)
				SwingUtilities.updateComponentTreeUI(com);
		} catch (Exception e) {
			LogProxy.internalError(e);
			JOptionPane.showMessageDialog(com == null ? getMainFrame() : com,
					"更新主题出错，错误原因为：" + e.getMessage());
		}

	}

	/**
	 * 定义当前的外观
	 * 
	 * @param currentLookAndFeel
	 *            --外观名称
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws UnsupportedLookAndFeelException
	 */
	public static void setCurrentLookAndFeel(String currentLookAndFeel)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		if (currentLookAndFeel == null)
			return;
		GUIUtil.currentLookAndFeel = currentLookAndFeel;
		UIManager.setLookAndFeel(currentLookAndFeel);
		JFrame frame = getMainFrame();
		if (frame != null)
			SwingUtilities.updateComponentTreeUI(frame);
	}

	/**
	 * 获取给定表控件，指定列的最佳宽度
	 * 
	 * @param table
	 *            --给定的表控件
	 * @param columnIndex
	 *            --被指定的列
	 * @return --最佳宽度
	 */
	public static int[] getPreferredColumnWidth(JTable table, int[] columnIndex) {
		if (columnIndex == null || columnIndex.length < 0 || table == null)
			return null;

		/**
		 * 初始化各列的渲染类
		 */
		TableCellRenderer[] render = new TableCellRenderer[columnIndex.length];
		for (int i = 0; i < columnIndex.length; i++) {
			render[i] = table.getColumnModel().getColumn(columnIndex[i])
					.getCellRenderer();
			if (render[i] == null) {
				render[i] = table.getDefaultRenderer(table
						.getColumnClass(columnIndex[i]));
			}
		}

		TableCellRenderer headerRender = table.getTableHeader()
				.getDefaultRenderer();
		int[] width = new int[columnIndex.length];
		for (int i = 0; i < width.length; i++) {
			width[i] = (int) headerRender.getTableCellRendererComponent(
					table,
					table.getColumnModel().getColumn(columnIndex[i])
							.getHeaderValue(), false, false, 0, columnIndex[i])
					.getPreferredSize().getWidth();
			// 首先获取给定列名的长度
			width[i] += 6; // 增加复选框的宽度
		}

		FontMetrics fm = null;
		int additionWidth = 0;
		/**
		 * 循环遍历给定列的所有数据，获取最大显示长度
		 */
		for (int i = 0; i < table.getRowCount(); i++) {
			for (int j = 0; j < columnIndex.length; j++) {
				Object tmpValue = table.getValueAt(i, columnIndex[j]);
				if (tmpValue == null)
					continue;

				Component com = render[j].getTableCellRendererComponent(table,
						tmpValue, false, false, i, columnIndex[j]);

				if (fm == null) {
					fm = com.getFontMetrics(com.getFont());
					additionWidth = fm.stringWidth("xx");
				}

				int tmpWidth = (int) com.getPreferredSize().getWidth()
						+ additionWidth;
				if (tmpWidth > width[j])
					width[j] = tmpWidth;
			}
		}
		return width;
	}

	/**
	 * 将现有的书签信息家在到给定的下拉框选择控件中
	 * 
	 * @param box
	 *            --装载书签信息的下拉框选择控件
	 */
	public static void loadBookmarksToComboBox(JComboBox box) {
		MutableComboBoxModel model = (MutableComboBoxModel) box.getModel();
		Set set = BookmarkManage.getInstance().getAliases();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			model.addElement(it.next());
		}

		// 将数签选择设置成缺省书签
		Bookmark bookmark = BookmarkManage.getInstance().getDefaultBookmark();
		model
				.setSelectedItem(bookmark == null ? null : bookmark
						.getAliasName());
	}

	/**
	 * 将给定的快捷键加载到指定的组件中，可以设置全局和局部两种快捷方式
	 * 
	 * @param componnet
	 *            --需要添加快捷键的组件
	 * @param key
	 *            --快捷键
	 * @param action
	 *            --快捷键执行的处理
	 * @param isGlobal
	 *            --是否是全局方式
	 */
	public static void bindShortKey(JComponent componnet, String key,
			Action action, boolean isGlobal) {
		KeyStroke stroke = KeyStroke.getKeyStroke(key);
		if (isGlobal)
			componnet.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
					stroke, key);
		else
			componnet.getInputMap().put(stroke, key);
		componnet.getActionMap().put(key, action);
	}
	/**
	 * 在其父组件拥有焦点时，触发相应的处理事件
	 * 
	 * @param componnet
	 *            --接受快捷键的组件
	 * @param key
	 *            --快捷键
	 * @param action
	 *            --快捷键执行的处理
	 */
	public static void bindShortKeyInAncestor(JComponent componnet, String key,
			Action action) {
		KeyStroke stroke = KeyStroke.getKeyStroke(key);
		componnet.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
				.put(stroke, key);
		componnet.getActionMap().put(key, action);
	}
	/**
	 * 按照屏幕窗口的尺寸，设置给定窗口对象的大小
	 * 
	 * @param con
	 *            --需要设置大小的窗口对象
	 * @param widthRate
	 *            --需要设置的宽度与屏幕宽度的比值
	 * @param heightRate
	 *            --需要设置的高度与屏幕高度的比值
	 */
	public static void setFrameSizeToScreen(Container con, float widthRate,
			float heightRate) {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // 屏幕像素宽度
		con.setSize((int) (d.getWidth() * widthRate),
				(int) (d.getHeight() * heightRate));
	}
	/**
	 * 获取缺省的网袋布局限制对象。
	 * 
	 * @return
	 */
	public static GridBagConstraints getDefaultBagConstraints() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;

		return gbc;
	}
	/**
	 * 处理容器对象con在隐藏和显示时，其上层的SplitPane的隐藏和显示。
	 * 
	 * @param con
	 *            --被处理的容器对象
	 * @param isHidden
	 *            容器对象con是否被隐藏
	 */
	public static void controlSplit(Container con, boolean isHidden) {
		JSplitPane split = getSplitContainer(con);
		if (split == null)
			return;
		Component left = split.getLeftComponent();
		Component right = split.getRightComponent();
		if (isHidden) {
			if ((left == null || !left.isVisible())
					&& (right == null || !right.isVisible())) // 如果split组件都不存在或者隐藏，该组件也隐藏
			{
				split.setVisible(false);
				controlSplit(split, true);
				
			}
		} else {
			if (!split.isVisible()) // 如果split隐藏，为了保证容器con正常显示，必须显示出来
			{
				split.setVisible(true);
				controlSplit(split, false); // ensure that parent container is
											// visible.
			}
//			int currentLocation=split.getDividerLocation();
			if(isMaxSplitToSelf(split))
			{
				if(left!=null&&left.isVisible()&&right!=null&&right.isVisible())
				{
						Integer location=(Integer)split.getClientProperty(View.LASTLOCATION);
						if(location==null)
							location=DEFAULT_VIEWWIDTH;
						if (location >split.getMinimumDividerLocation()
								&&location <split.getMaximumDividerLocation())
							split.setDividerLocation(location);
				}
			}
		}
	}

	/**
	 * 获取给定组件所在的Split容器
	 * 
	 * @return --JSplitPane对象
	 */
	public static JSplitPane getSplitContainer(Container con) {
		Container p;
		for (p = con.getParent(); p != null && !(p instanceof JSplitPane); p = p
				.getParent());
		JSplitPane split = (JSplitPane) p;
		return split;
	}
	/**
	 * 判断Jsplitpane对象是否是最大状态(相对与整个窗口而言)
	 */
	public static boolean isMaxSplitToParent(JSplitPane split) {
		if (split == null)
			return false;
		if (isMaxSplitToSelf(split)) {
			JSplitPane tmp = GUIUtil.getSplitContainer(split);
			if (tmp == null)
				return true;
			if (GUIUtil.isMaxSplitToSelf(tmp)) {
				tmp = GUIUtil.getSplitContainer(tmp);
				if (tmp == null)
					return true;
				else
					return GUIUtil.isMaxSplitToSelf(tmp); // 以递归的形式判断
			} else
				return false;
		} else
			return false;
	}
	/**
	 * 判断Jsplitpane对象是否是最大状态(相对与自身而言)
	 * 
	 * @param split
	 * @return
	 */
	public static boolean isMaxSplitToSelf(JSplitPane split) {
		if (split == null)
			return false;
		Component left = split.getLeftComponent();
		Component right = split.getRightComponent();
		if (!left.isVisible() || !right.isVisible())
			return true;

		int currentLocation = split.getDividerLocation();
		if (currentLocation < split.getMinimumDividerLocation()
				|| currentLocation > split.getMaximumDividerLocation())
			return true;
		else
			return false;
	}

	/**
	 * 判断指定的JSplitPane是否是最大size
	 * 
	 * @param split
	 * @param 如果不是最大返回空间的分布方向属性，否则返回-1
	 */
	public static int isMaxState(JSplitPane split) {
		int type = split.getOrientation();
		if (isMaxSplitToSelf(split))
			return -1;
		else
			return type;
	}
	/**
	 * get the scale size of screen
	 * @param scale  
	 * @return
	 */
	public static Dimension getScaleScreenDimension(double scale)
	{
		if(scale>1)
			scale=1;
		if(scale<0)  //value of minimum scale is 0.1
			scale=.1;
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		return new Dimension((int)(screenSize.width*scale),(int)(screenSize.height*scale));
	}
	public static void processOnSwingEventThread(Runnable todo)
	{
		processOnSwingEventThread(todo, false);
	}

	public static void processOnSwingEventThread(Runnable todo, boolean wait)
	{
		if (todo == null)
		{
			throw new IllegalArgumentException("Runnable == null");
		}

		if (wait)
		{
			if (SwingUtilities.isEventDispatchThread())
			{
				todo.run();
			}
			else
			{
				try
				{
					SwingUtilities.invokeAndWait(todo);
				}
				catch (InvocationTargetException ex)
				{
					throw new RuntimeException(ex);
				}
				catch (InterruptedException ex)
				{
					throw new RuntimeException(ex);
				}
			}
		}
		else
		{
            if (SwingUtilities.isEventDispatchThread()) {
                todo.run();
            } else {
                SwingUtilities.invokeLater(todo);
            }
		}
	}
    /**
     * Returns the focused Window, if the focused Window is in the same context
     * as the calling thread. The focused Window is the Window that is or
     * contains the focus owner.
     *
     * @return the focused Window
     */         
	public static Window findLikelyOwnerWindow() {
		Window result = KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.getFocusedWindow();
		if (result == null) {
			result = getMainFrame();
		}
		
		return result;
	}
	/**
	 * install a popup menu to specified textcomponent
	 * @param textComponent
	 */
	public static void installDefaultTextPopMenu(JTextComponent textComponent)
	{
		if(textComponent!=null)
			new TextComponentPopMenu(textComponent);
	}
	public static boolean getYesNo(String aMessage)
	{
		return getYesNo(findLikelyOwnerWindow(),aMessage);
	}
	public static boolean getYesNo(Component aCaller, String aMessage)
	{
		int result = JOptionPane.showConfirmDialog(aCaller == null ? getMainFrame() : SwingUtilities.getWindowAncestor(aCaller), aMessage, "CoolSQL", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return (result == JOptionPane.YES_OPTION);
	}
}
