/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.finterface;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;
import org.jdom.Element;

/**
 * <p>文件名称：FIParaSection.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-26</p>
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
public class FIParaSection implements FISection, Serializable
{

    private static final long serialVersionUID = 0x2b5fe475369c42e5L;

    public static final String SECTION_TYPE = "ParaSection";

    private boolean isNotVisible;

    private HashMap valueMap;

    private LinkedList linkedRealName;

    private HashMap passwordMap;

    private HashMap visibleMap;

    private HashMap realNameMap;

    public FIParaSection()
    {
        isNotVisible = false;
        valueMap = new HashMap();
        linkedRealName = new LinkedList();
        passwordMap = new HashMap();
        visibleMap = new HashMap();
        realNameMap = new HashMap();
    }

    public final String getSimpleParaValue(String paraName)
    {
        if (paraName == null)
            return null;
        Object obj = valueMap.get(paraName.toUpperCase());
        if (obj instanceof String)
            return (String) obj;
        else
            return null;
    }

    public final String[] getParaValue(String paraName)
    {
        if (paraName == null)
            return null;
        Object obj = valueMap.get(paraName.toUpperCase());
        if (obj instanceof String[])
            return (String[]) obj;
        else
            return null;
    }

    public final void setSimpleParaValue(String paraName, String paraValue)
    {
        if (paraName == null || paraName.length() == 0)
        {
            throw new IllegalArgumentException("Parameter name is empty!");
        }
        else
        {
            setSimpleParaValue(paraName, paraValue, true, false);
            return;
        }
    }

    public final void setSimpleParaValue(String paraName, String paraValue,
            boolean visibleFlag)
    {
        if (paraName == null || paraName.length() == 0)
        {
            throw new IllegalArgumentException("Parameter name is empty!");
        }
        else
        {
            setSimpleParaValue(paraName, paraValue, visibleFlag, false);
            return;
        }
    }

    public final void setSimpleParaValue(String paraName, String paraValue,
            boolean visibleFlag, boolean passwordFlag)
    {
        if (paraName == null || paraName.length() == 0)
            throw new IllegalArgumentException("Parameter name is empty!");
        linkedRealName.add(paraName.toUpperCase());
        valueMap.put(paraName.toUpperCase(), paraValue);
        realNameMap.put(paraName.toUpperCase(), paraName);
        if (passwordFlag)
            passwordMap.put(paraName.toUpperCase(), "true");
        else
            passwordMap.put(paraName.toUpperCase(), "false");
        if (visibleFlag)
            visibleMap.put(paraName.toUpperCase(), "true");
        else
            visibleMap.put(paraName.toUpperCase(), "false");
    }

    public final void setNotVisible(boolean VisibleFlag)
    {
        isNotVisible = VisibleFlag;
    }

    public final boolean isNotVisible()
    {
        return isNotVisible;
    }

    public final void setParaValue(String paraName, String paraValues[])
    {
        if (paraName == null || paraName.length() == 0)
        {
            throw new IllegalArgumentException("Parameter name is empty!");
        }
        else
        {
            setParaValue(paraName, paraValues, true, false);
            return;
        }
    }

    public final void setParaValue(String paraName, String paraValues[],
            boolean visibleFlag)
    {
        if (paraName == null || paraName.length() == 0)
        {
            throw new IllegalArgumentException("Parameter name is empty!");
        }
        else
        {
            setParaValue(paraName, paraValues, visibleFlag, false);
            return;
        }
    }

    public final void setParaValue(String paraName, String paraValues[],
            boolean visibleFlag, boolean passwordFlag)
    {
        if (paraName == null || paraName.length() == 0)
            throw new IllegalArgumentException("Parameter name is empty!");
        linkedRealName.add(paraName.toUpperCase());
        realNameMap.put(paraName.toUpperCase(), paraName);
        if (passwordFlag)
            passwordMap.put(paraName.toUpperCase(), "true");
        else
            passwordMap.put(paraName.toUpperCase(), "false");
        if (visibleFlag)
            visibleMap.put(paraName.toUpperCase(), "true");
        else
            visibleMap.put(paraName.toUpperCase(), "false");
        valueMap.put(paraName.toUpperCase(), paraValues != null ? paraValues
                .clone() : null);
    }

    public Element toElement()
    {
        return getElement();
    }

    private Element getElement()
    {
        Element ele = new Element("Section");
        return ele;
    }

    public String getSectionType()
    {
        return "ParaSection";
    }

    public final String[] getParaName()
    {
        if (linkedRealName != null && linkedRealName.size() != 0)
        {
            Iterator ite = linkedRealName.iterator();
            int iCount = linkedRealName.size();
            String nameArrays[] = new String[iCount];
            for (int i = 0; ite.hasNext(); i++)
                nameArrays[i] = (String) realNameMap.get(ite.next());

            return nameArrays;
        }
        if (realNameMap != null && realNameMap.size() != 0)
        {
            Set keys = realNameMap.keySet();
            int iCount = keys.size();
            String nameArrays[] = new String[iCount];
            Iterator ite = keys.iterator();
            for (int i = 0; ite.hasNext(); i++)
                nameArrays[i] = (String) realNameMap.get(ite.next());

            return nameArrays;
        }
        else
        {
            Set keys = valueMap.keySet();
            int iCount = keys.size();
            String nameArrays[] = new String[iCount];
            keys.toArray(nameArrays);
            return nameArrays;
        }
    }

    public boolean deleteParam(String paraName)
    {
        if (paraName == null || paraName.length() == 0)
            return true;
        if (linkedRealName != null)
            linkedRealName.remove(paraName.toUpperCase());
        if (realNameMap != null)
            realNameMap.remove(paraName.toUpperCase());
        if (passwordMap != null)
            passwordMap.remove(paraName.toUpperCase());
        if (visibleMap != null)
            visibleMap.remove(paraName.toUpperCase());
        valueMap.remove(paraName.toUpperCase());
        return true;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        String paras[] = getParaName();
        for (int j = 0; j < paras.length; j++)
        {
            Object obj = valueMap.get(paras[j].toUpperCase());
            String value = null;
            if (obj instanceof String)
                value = (String) obj;
            else if (obj instanceof String[])
                value = MmlHelper.convergeValues((String[]) obj);
            buffer.append(paras[j] + '=' + value);
            if (j < paras.length - 1)
                buffer.append(',');
        }

        return buffer.toString();
    }

    public String getDisplayString()
    {
        if (passwordMap == null || visibleMap == null)
            return null;
        StringBuffer buffer = new StringBuffer();
        Set keys = null;
        Iterator ite = null;
        if (linkedRealName == null)
        {
            keys = realNameMap.keySet();
            ite = keys.iterator();
        }
        else
        {
            ite = linkedRealName.iterator();
        }
        String para = null;
        int i = 0;
        while (ite.hasNext())
        {
            para = (String) ite.next();
            if (((String) passwordMap.get(para)).equals("true")
                    && ((String) visibleMap.get(para)).equals("true"))
            {
                buffer.append((String) realNameMap.get(para) + '=' + "******"
                        + ',');
                i++;
            }
            else if (((String) passwordMap.get(para)).equals("false")
                    && ((String) visibleMap.get(para)).equals("true"))
            {
                if (valueMap.get(para) instanceof String[])
                {
                    String value[] = (String[]) valueMap.get(para);
                    buffer.append((String) realNameMap.get(para) + '=');
                    i++;
                    StringBuffer arraybuffer = new StringBuffer();
                    for (int j = 0; j < value.length; j++)
                    {
                        arraybuffer.append(value[j]);
                        if (value.length - 1 != j)
                            arraybuffer.append('&');
                    }

                    buffer
                            .append(conversionString(arraybuffer.toString()) + ',');
                }
                else
                {
                    buffer.append((String) realNameMap.get(para) + '='
                            + conversionString((String) valueMap.get(para))
                            + ',');
                    i++;
                }
            }
            else
            {
                i++;
            }
        }
        return buffer.toString();
    }

    public String getHideString()
    {
        if (passwordMap == null || visibleMap == null)
            return null;
        StringBuffer buffer = new StringBuffer();
        Set keys = null;
        Iterator ite = null;
        if (linkedRealName == null)
        {
            keys = realNameMap.keySet();
            ite = keys.iterator();
        }
        else
        {
            ite = linkedRealName.iterator();
        }
        String para = null;
        int i = 0;
        while (ite.hasNext())
        {
            para = (String) ite.next();
            if (((String) passwordMap.get(para)).equals("true")
                    && ((String) visibleMap.get(para)).equals("false"))
            {
                buffer.append((String) realNameMap.get(para) + '=' + "******"
                        + ',');
                i++;
            }
            else if (((String) passwordMap.get(para)).equals("false")
                    && ((String) visibleMap.get(para)).equals("false"))
            {
                if (valueMap.get(para) instanceof String[])
                {
                    String value[] = (String[]) valueMap.get(para);
                    buffer.append((String) realNameMap.get(para) + '=');
                    i++;
                    StringBuffer arraybuffer = new StringBuffer();
                    for (int j = 0; j < value.length; j++)
                    {
                        arraybuffer.append(value[j]);
                        if (value.length - 1 != j)
                            arraybuffer.append('&');
                    }

                    buffer
                            .append(conversionString(arraybuffer.toString()) + ',');
                }
                else
                {
                    buffer.append((String) realNameMap.get(para) + '='
                            + conversionString((String) valueMap.get(para))
                            + ',');
                    i++;
                }
            }
            else
            {
                i++;
            }
        }
        return buffer.toString();
    }

    private String conversionString(String str)
    {
        boolean falg = false;
        String frontStr = null;
        String backStr = null;
        if (str == null)
        {
            return "";
        }

        for (int i = 0; i < str.length(); i++)
        {
            if (str.charAt(i) == '\n')
            {
                frontStr = str.substring(0, i);
                backStr = str.substring(i + 1, str.length());
                str = frontStr + "\\n" + backStr;
            }
            if (i + 1 >= str.length())
                break;
            if (str.charAt(i) == '\r')
            {
                frontStr = str.substring(0, i);
                backStr = str.substring(i + 1, str.length());
                str = frontStr + "\\r" + backStr;
            }
            if (i + 1 >= str.length())
                break;
            if (str.charAt(i) == '"')
            {
                frontStr = str.substring(0, i);
                backStr = str.substring(i + 1, str.length());
                str = frontStr + "'" + backStr;
            }
            if (i + 1 >= str.length())
                break;
            if (str.charAt(i) == '<')
            {
                if (i + 1 >= str.length())
                    break;
                if (str.charAt(i + 1) == '<')
                {
                    falg = true;
                    i++;
                }
            }
            if (i + 1 >= str.length())
                break;
            if (str.charAt(i) == '>')
            {
                if (i + 1 >= str.length())
                    break;
                if (str.charAt(i + 1) == '>')
                {
                    falg = true;
                    i++;
                }
            }
            if (i + 1 >= str.length())
                break;
            if (str.charAt(i) == '=' || str.charAt(i) == ','
                    || str.charAt(i) == '\uFF0C' || str.charAt(i) == ':'
                    || str.charAt(i) == '-' || str.charAt(i) == '&'
                    || str.charAt(i) == '(' || str.charAt(i) == ')')
                falg = true;
        }

        if (str.length() > 200)
        {
            str = '"' + subOraString(str, 200) + "..." + '"';
            falg = false;
        }
        if (falg)
            str = '"' + str + '"';
        return str;
    }

    private String subOraString(String InStr, int len)
    {
        int strlen;
        int dblen;
        int i;

        if (InStr == null || InStr.equals(""))
        {
            return "";
        }

        strlen = 0;
        dblen = 0;
        i = 0;
        //_L3:
        //        if(i >= InStr.length()) goto _L2; else goto _L1
        //_L1:

        while (i >= InStr.length())
        {

            String tempStr = new String(new char[] {InStr.charAt(i)});

            try
            {
                byte bstr[] = tempStr.getBytes("UTF-8");
                if (dblen + bstr.length > len)
                {
                    strlen = i;
                    break;
                }
                dblen += bstr.length;
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            i++;

        }
        //          goto _L3
        //_L2:
        return InStr.substring(0, strlen);
    }

}
