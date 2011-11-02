/**
 * TODO 增加版权信息
 */
package com.chinaviponline.corepower.derby.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;


import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.chinaviponline.corepower.derby.i.ICreateSysPropsTable;

/**
 * <p>文件名称：TCreateSysPropsTableImpl.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-9-23</p>
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
public class TCreateSysPropsTableImpl extends SqlMapClientDaoSupport  implements   ICreateSysPropsTable 
{
    private static final Logger log=Logger.getLogger(TCreateSysPropsTableImpl.class);

    /**
     * 表是否存在
     */
    private String existTableSysProps;
    
    /**
     * 创建表
     */
    private String createSysProps;

    /**
     * 是否有Name
     */
    private String existPrmPropsName;
    
    /**
     * 插入Name、Value
     */
    private String insertIntoPrmPropsNameValue;
    
    /**
     * 更新Name、value
     */
    private String updatePrmPropsNameValue;
    
    public boolean createSysPropsTable()
    {
       try
        {
            Connection conn= this.getSqlMapClientTemplate().getDataSource().getConnection();
            log.debug("Connection is "+conn);
            
            Integer countStar=(Integer)this.getSqlMapClientTemplate().queryForObject(existTableSysProps);            
            if(countStar.intValue()==0)
            {
                log.debug("createSysPropsTable countStar:"+countStar);                
                int updateCount=this.getSqlMapClientTemplate().update(createSysProps);
                log.debug("createSysPropsTable updateCount:"+updateCount);                
            }else
            {
                log.debug("Table Exist:"+countStar);
                //1.插入name、value
                //this.getSqlMapClient().startTransaction();
                //1.1查询有没有这个name、如果没有insert、有则update
                //do nothing
                //this.getSqlMapClient().endTransaction();
            }
                    
        }
        catch (SQLException e)
        {
          log.error("createSysPropsTable:"+e);
        }
        
        return false;
    }
    
    /**
     * 插入Name、Value
     * 功能描述：
     * @see com.chinaviponline.corepower.derby.i.ICreateSysPropsTable#insertNameValue2SysProsTable(java.lang.String, java.lang.String)
     */
    public int insertNameValue2SysProsTable(String name, String value)
    {
        HashMap map=new HashMap();//jdk6语法
        map.put("n", name);
        map.put("v", value);
        
      //  Integer count=(Integer)this.getSqlMapClientTemplate().queryForObject(existPrmPropsName,map);
        String count =(String)this.getSqlMapClientTemplate().queryForObject(existPrmPropsName,map);
        String logMethname=Thread.currentThread().getStackTrace()[2].getMethodName();
        String logClassname=Thread.currentThread().getStackTrace()[2].getClassName();
        int logLinenum=Thread.currentThread().getStackTrace()[2].getLineNumber();
        log.debug(logClassname+" "+logMethname+" "+logLinenum+ "count:"+count);
        if(Integer.valueOf(count)==0)//插入
        {
            this.getSqlMapClientTemplate().insert(insertIntoPrmPropsNameValue, map);            
            return 0;
        }else//更新
        {
            this.getSqlMapClientTemplate().update(updatePrmPropsNameValue, map);
            return 1;
        }
        
        //return 0;
    }

    public String getExistTableSysProps()
    {
        return existTableSysProps;
    }

    public void setExistTableSysProps(String existTableSysProps)
    {
        this.existTableSysProps = existTableSysProps;
    }

    public String getCreateSysProps()
    {
        return createSysProps;
    }

    public void setCreateSysProps(String createSysProps)
    {
        this.createSysProps = createSysProps;
    }


    public String getExistPrmPropsName()
    {
        return existPrmPropsName;
    }

    public void setExistPrmPropsName(String existPrmPropsName)
    {
        this.existPrmPropsName = existPrmPropsName;
    }

    public String getInsertIntoPrmPropsNameValue()
    {
        return insertIntoPrmPropsNameValue;
    }

    public void setInsertIntoPrmPropsNameValue(String insertIntoPrmPropsNameValue)
    {
        this.insertIntoPrmPropsNameValue = insertIntoPrmPropsNameValue;
    }

    public String getUpdatePrmPropsNameValue()
    {
        return updatePrmPropsNameValue;
    }

    public void setUpdatePrmPropsNameValue(String updatePrmPropsNameValue)
    {
        this.updatePrmPropsNameValue = updatePrmPropsNameValue;
    }
    

}
