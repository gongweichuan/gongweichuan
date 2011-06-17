/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

/**
 * <p>文件名称：OperationCommandSelection.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-17</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：    版本号：    修改人：    修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
 * @email  gongweichuan(AT)gmail.com
 */
public class OperationCommandSelection
{
    private String operationArray[];
    private int commandArray[];

    public OperationCommandSelection()
    {
        operationArray = new String[0];
        commandArray = new int[0];
    }

    public void setOperation(String operation[])
    {
        operationArray = operation;
    }

    public String[] getOperation()
    {
        return operationArray;
    }

    public void setCommand(int command[])
    {
        commandArray = command;
    }

    public int[] getCommand()
    {
        return commandArray;
    }

}
