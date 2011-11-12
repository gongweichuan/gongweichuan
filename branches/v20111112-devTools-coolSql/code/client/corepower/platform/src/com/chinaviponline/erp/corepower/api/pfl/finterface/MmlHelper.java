/**
 * 
 */
package com.chinaviponline.erp.corepower.api.pfl.finterface;

import java.util.ArrayList;

/**
 * <p>文件名称：MmlHelper.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-18</p>
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
public class MmlHelper
{

    private static final String NULL_FLAG = "NULL";
    private static final String NULL_PLACE_HOLDER = "NULLNULL";

    private MmlHelper()
    {
    }

    private static String displace(String str)
    {
        if(str == null)
        {
            return "NULL";            
        }
        
        if(str.indexOf("NULL") == 0)
        {
            return "NULL" + str;            
        }
        else
        {
            return str;            
        }
    }

    private static String undoDisplace(String str)
    {
        if(str.equals("NULL"))
        {
            return null;            
        }
        
        if(str.indexOf("NULLNULL") == 0)
        {
            return str.substring("NULL".length());            
        }
        else
        {
            return str;            
        }
    }

    public static String[] decodeValues(String OrgStr, String token)
    {
        return extractToken(OrgStr, token);
    }

    public static String convergeValues(String array[])
    {
        if(array == null)
        {
            return null;            
        }
        
        int len = array.length;
        
        if(len == 0)
        {
            return "";            
        }
        
        StringBuffer values = new StringBuffer();
        for(int i = 0; i < len; i++)
        {
            values.append(displace(array[i]));
            values.append("|$|");
        }

        return values.toString();
    }

    private static String[] extractToken(String str, String delim)
    {
        if(str == null || delim == null)
        {
            return null;            
        }
        
        ArrayList strList = new ArrayList(10);
        int iStartPos = 0;
        int length = str.length();
        int delimLengh = delim.length();
        
        do
        {
            if(iStartPos >= length)
            {
                break;                
            }
            
            int pos = str.indexOf(delim, iStartPos);
            
            if(pos == -1)
            {
                break;                
            }
            
            strList.add(undoDisplace(str.substring(iStartPos, pos)));
            iStartPos = pos + delimLengh;
        } while(true);
        
        String tokens[] = new String[strList.size()];
        tokens = (String[])strList.toArray(tokens);
        return tokens;
    }

    public static String replace(String orgStr, String orgToken, String targetToken)
    {
        if(orgStr == null)
        {
            return null;            
        }
        
        int orgLength = orgStr.length();
        StringBuffer sb = new StringBuffer(orgLength);
        int orgTokenLength = orgToken.length();
        int pos = 0;
        
        do
        {
            if(pos >= orgLength)
            {
                break;                
            }
            
            int tokenPos = orgStr.indexOf(orgToken, pos);
            if(tokenPos == -1)
            {
                sb.append(orgStr.substring(pos));
                break;
            }
            sb.append(orgStr.substring(pos, tokenPos));
            sb.append(targetToken);
            pos = tokenPos + orgTokenLength;
        } while(true);
        
        return sb.toString();
    }

    
}
