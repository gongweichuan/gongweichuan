/**
 * 
 */
package com.coolsql.view.bookmarkview;

import com.coolsql.pub.component.selectabletree.SelectableTreeNode;

/**
 * find suitable nodes for user,the method of interface will match specified node.
 * @author 刘孝林(kenny liu)
 *
 * 2008-3-19 create
 */
public interface ISelectableNodeMatch {

	/**
	 * return a valid data if node is suitable for user, otherwise a null value will be returned. 
	 * @param node --tree node that need be matched
	 * @return
	 */
	public Object match(SelectableTreeNode node);
}
