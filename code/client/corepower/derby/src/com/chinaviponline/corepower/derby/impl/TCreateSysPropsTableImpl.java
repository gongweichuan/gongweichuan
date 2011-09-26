/**
 * TODO 增加版权信息
 */
package com.chinaviponline.corepower.derby.impl;

import java.sql.Connection;
import java.sql.SQLException;


import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.chinaviponline.corepower.derby.i.ICreateSysPropsTable;
import com.ibatis.sqlmap.client.SqlMapClient;

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
public class TCreateSysPropsTableImpl  implements   ICreateSysPropsTable //extends SqlMapClientDaoSupport
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
    
    protected SqlMapClient sqlMapClient;
    
    public boolean createSysPropsTable()
    {
       try
        {
//            Connection conn= this.getSqlMapClientTemplate().getDataSource().getConnection();
//            log.debug("Connection is "+conn);
            
/*            String countStar=(String)this.getSqlMapClientTemplate().queryForObject(existTableSysProps);            
            if("0".equalsIgnoreCase(countStar))
            {
                log.debug("createSysPropsTable countStar:"+countStar);
                
                int updateCount=this.getSqlMapClientTemplate().update(createSysProps);
                log.debug("createSysPropsTable updateCount:"+updateCount);
            }else
            {
                log.debug("Table Exist:"+countStar);
            }*/
           this.sqlMapClient.queryForObject("getUserbyUsername", "xxx");
//            log.debug(this.getSqlMapClientTemplate().queryForObject("getUserbyUsername"));
            
        }
        catch (SQLException e)
        {
          log.error("createSysPropsTable:"+e);
        }
        
        return false;
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
    
    public SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }

    public void setSqlMapClient(SqlMapClient sqlMapClient) {
        this.sqlMapClient = sqlMapClient;
    }
}
