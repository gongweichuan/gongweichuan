/**
 * 
 */
package com.chinaviponline.erp.corepower.api.util;

import java.io.IOException;
import java.util.*;

import com.chinaviponline.erp.corepower.api.ServiceAccess;

import org.jdom.*;
import org.jdom.input.SAXBuilder;

/**
 * <p>文件名称：ErrorCodeI18n.java</p>
 * <p>文件描述：国际化错误码</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-2</p>
 * <p>修改记录1：</p>
 * <pre>
 *  修改日期：
 *  版本号：
 *  修改人：
 *  修改内容：
 * </pre>
 * <p>修改记录2：</p>
 *
 * @version 1.0
 * @author 龚为川
 * @email  gongweichuan(AT)gmail.com
 */
public class ErrorCodeI18n
{
    public static final int DB_LINK_ERROR = 1;

    public static final int DB_SQL_ERROR = 2;

    public static final int DB_CLOSE_ERROR = 3;

    public static final int DB_REPEATED_KEY_ERROR = 11;

    public static final int DB_COLUMN_NOT_NULL_ERROR = 12;

    public static final int DB_VALUE_TOO_LONG_ERROR = 13;

    public static final int DB_DATA_OVERFLOW_ERROR = 14;

    public static final int DB_COLUMN_NAME_ERROR = 15;

    public static final int DB_OBJECT_NOT_EXIST_ERROR = 16;

    public static final int DB_USER_OR_PWD_ERROR = 17;

    public static final int DB_NETWORK_ERROR = 18;

    public static final int DB_USER_OR_PWD_EMPTY_ERROR = 19;

    public static final int DB_PWD_EMPTY_ERROR = 20;

    public static final int DB_LOG_LOW_ERROR = 21;

    public static final int DB_LOG_FULL_ERROR = 22;

    public static final int DB_SQLQUERY_LONG_ERROR = 23;

    public static final int SERVER_SYSTEM_ERROR = 100;

    public static final int SERVER_CLIENT_LINK_ERROR = 200;

    public static final int SERVER_CLIENT_NETWORK_ERROR = 201;

    public static final int OPERATION_NO_RIGHT_ERROR = 300;

    public static final int OBJECT_NO_RIGHT_ERROR = 301;

    public static final int OBJECT_LOCKED_ERROR = 302;

    public static final int FILE_OPEN_ERROR = 400;

    public static final int FILE_SAVE_ERROR = 401;

    public static final int FILE_DELETE_ERROR = 402;

    public static final int SESSION_TIMEOUT_ERROR = 500;

    public static final int SESSION_DELETED_ERROR = 501;

    public static final int APP_NOT_FOUND_ERROR = 600;

    public static final int UNKNOWN_ERROR_CODE = 0x98967f;

    public static final int ERROR_LEVEL = 0;

    public static final int WARN_LEVEL = 2;

    public static final int INFO_LEVEL = 1;

    private static Map instanceMap = Collections.synchronizedMap(new HashMap());

    //    private static DebugPrn dMsg;

    private Map errorItemMap;

    static
    {
        //        dMsg = new DebugPrn((com.zte.ums.uep.api.util.ErrorCodeI18n.class)
        //                .getName());
        new ErrorCodeI18n();
    }

    public static ErrorCodeI18n getInstance()
    {
        return getInstance(Locale.getDefault());
    }

    public static ErrorCodeI18n getInstance(Locale locale)
    {
        ErrorCodeI18n instance = (ErrorCodeI18n) instanceMap.get(locale
                .toString());
        if (instance == null)
        {
            ErrorCodeI18n theInstance = new ErrorCodeI18n(locale);
            instance = theInstance;
        }
        return instance;
    }

    private void printCaller(int errorCode)
    {
        Throwable th = new Throwable("Can not find the error code '"
                + errorCode + "' definition in -errorcode.xml");
        //        dMsg.warn(th.toString());
        StackTraceElement trace[] = th.getStackTrace();
        for (int i = 0; i < trace.length; i++)
        {
            //          dMsg.warn("\tat " + trace[i].toString());  
        }

    }

    public ErrorItem getErrorItem(int errorCode)
    {
        Integer key = new Integer(0 + errorCode);
        ErrorItem item = (ErrorItem) errorItemMap.get(key);
        if (item == null)
        {
            printCaller(errorCode);
            item = (ErrorItem) errorItemMap.get(new Integer(0x98967f));
        }
        return item;
    }

    public ErrorItem getErrorItem2(int errorCode)
    {
        Integer key = new Integer(0 + errorCode);
        ErrorItem item = (ErrorItem) errorItemMap.get(key);
        return item;
    }

    public ErrorItem getErrorItem(int catetory, int errorCode)
    {
        Integer key = new Integer(compositeID(catetory, errorCode));
        ErrorItem item = (ErrorItem) errorItemMap.get(key);
        if (item == null)
        {
            printCaller(errorCode);
            item = (ErrorItem) errorItemMap.get(new Integer(0x98967f));
        }
        return item;
    }

    private ErrorCodeI18n()
    {
        this(Locale.getDefault());
    }

    private ErrorCodeI18n(Locale locale)
    {
        errorItemMap = null;
        java.io.File errorCodeFiles[] = ServiceAccess.getSystemSupportService()
                .getFiles("-errorcode.xml");
        Map itemMaps[] = new HashMap[errorCodeFiles.length];
        int allSize = 0;
        for (int i = 0; i < errorCodeFiles.length; i++)
        {
            SAXBuilder builder = new SAXBuilder();
            try
            {
                Document document = builder.build(errorCodeFiles[i]);
                Element rootElement = document.getRootElement();
                Element localeElement = I18n.getLocaleElement(rootElement,
                        locale);
                if (localeElement == null)
                    throw new IllegalArgumentException(
                            "The errorcode file '"
                                    + errorCodeFiles[i]
                                    + "' is not match locale file because of its 'language-country' is invalid");
                itemMaps[i] = new HashMap(localeElement.getChildren().size());
                allSize += localeElement.getChildren().size();
                ErrorItem item;
                for (Iterator it = localeElement.getChildren().iterator(); it
                        .hasNext(); itemMaps[i].put(new Integer(item
                        .getCompositeID()), item))
                {
                    Element pairElement = (Element) it.next();
                    String categoryText = I18n.getAttributeValue(pairElement,
                            "category");
                    String codeText = I18n.getAttributeValue(pairElement,
                            "code");
                    String levelText = I18n.getAttributeValue(pairElement,
                            "level");
                    String labelText = I18n.getAttributeValue(pairElement,
                            "label");
                    String detailText = I18n.getAttributeValue(pairElement,
                            "detail");
                    if (codeText == null || codeText.length() == 0)
                    {
                        if (labelText != null && labelText.length() > 0)
                        {
                            //                            dMsg.warn("the code is not set of the label '"
                            //                                    + labelText
                            //                                    + "' in the errorcode xml file '"
                            //                                    + errorCodeFiles[i] + "'");                          
                        }

                        else
                        {
                            //                            dMsg
                            //                            .warn("the code is not set  in the errorcode xml file '"
                            //                                    + errorCodeFiles[i] + "'");    
                        }

                    }
                    else
                    {
                        if (levelText == null || levelText.length() == 0)
                        {
                            //                            dMsg.warn("the level is not set of the code '"
                            //                                    + codeText
                            //                                    + "' in the errorcode xml file '"
                            //                                    + errorCodeFiles[i] + "'");
                        }

                        if (labelText == null || labelText.length() == 0)
                        {

                            //                          dMsg.warn("the label is not set of the code '"
                            //                                  + codeText
                            //                                  + "' in the errorcode xml file '"
                            //                                  + errorCodeFiles[i] + "'");   
                        }
                    }
                    item = null;
                    if (categoryText == null)
                        item = new ErrorItem(codeText, levelText, labelText,
                                detailText);
                    else
                        item = new ErrorItem(categoryText, codeText, levelText,
                                labelText, detailText);
                }

            }
            catch (JDOMException e)
            {
                throw new IllegalStateException("The errorcode xml file '"
                        + errorCodeFiles[i] + "' is not valid");
            }
            catch (IOException e)
            {
                throw new IllegalStateException(
                        "IOException occured when reading '"
                                + errorCodeFiles[i] + "'");
            }
        }

        errorItemMap = new HashMap(allSize);
        for (int i = 0; i < itemMaps.length; i++)
            errorItemMap.putAll(itemMaps[i]);

        StringBuffer keyBuffer = (new StringBuffer(locale.getLanguage()))
                .append('_').append(locale.getCountry());
        instanceMap.put(keyBuffer.toString(), this);
    }

    private static int compositeID(int category, int errorCode)
    {
        int pex = 0;
        for (int value = 1; value < category;)
        {
            value *= 10;
            pex++;
        }

        pex = pex >= 7 ? pex : 7;
        int compositeID = (category << pex) + errorCode;
        return compositeID;
    }

  public  class ErrorItem
    {

        public int getCategory()
        {
            return theCategory;
        }

        public int getErrorCode()
        {
            return theErrorCode;
        }

        public int getCompositeID()
        {
            //            return ErrorCodeI18n.access(theCategory, theErrorCode);

            //TODO 获取CompositeID
            return 0;
        }

        public int getLevel()
        {
            return level;
        }

        public String getLabel()
        {
            return label;
        }

        public String getDetail()
        {
            return detail;
        }

        private int theCategory;

        private int theErrorCode;

        private int level;

        private String label;

        private final String detail;

        ErrorItem(String codeText, String levelText, String labelText,
                String detailText)
        {
            theCategory = 0;
            theErrorCode = -1;
            level = 1;
            label = "";
            theErrorCode = Integer.parseInt(codeText);
            if (levelText.equals("ERROR"))
                level = 0;
            else if (levelText.equals("WARN"))
                level = 2;
            else if (levelText.equals("INFO"))
                level = 1;
            else
                throw new IllegalStateException(
                        "The level attribute must be one of 'Error', 'Warn' and 'Info', but it is '"
                                + levelText + "'");
            label = labelText;
            detail = detailText;
        }

        ErrorItem(String categoryText, String codeText, String levelText,
                String labelText, String detailText)
        {
            this(codeText, levelText, labelText, detailText);
            theCategory = Integer.parseInt(categoryText);
        }
    }

}
