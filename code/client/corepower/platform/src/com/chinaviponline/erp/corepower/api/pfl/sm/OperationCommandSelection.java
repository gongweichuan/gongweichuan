/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.sm;

/**
 * <p>�ļ����ƣ�OperationCommandSelection.java</p>
 * <p>�ļ�������</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��    ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2008-6-17</p>
 * <p>�޸ļ�¼1��</p>
 * <pre>
 *  �޸����ڣ�    �汾�ţ�    �޸��ˣ�    �޸����ݣ�
 * </pre>
 * <p>�޸ļ�¼2��</p>
 *
 * @version 1.0
 * @author ��Ϊ��
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
