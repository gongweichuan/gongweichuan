/*
 * Created on 2007-2-8
 */
package com.coolsql.view.sqleditor.pop;

/**
 * @author liu_xlin
 *弹出窗口选择想要的值后，所触发的动作监听
 */
public interface SelectValue  {

    /**
     * 选中想要的值后，所触发的动作
     * @return  --选中的对象值
     */
    public Object selected();
}
