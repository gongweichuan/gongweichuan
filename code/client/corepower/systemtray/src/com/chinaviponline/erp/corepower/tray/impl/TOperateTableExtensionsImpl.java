/**
 * TODO 增加版权信息
 */
package com.chinaviponline.erp.corepower.tray.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.chinaviponline.erp.corepower.tray.i.IOperateTableExtensions;

/**
 * <p>文件名称：TOperateTableExtensionsImpl.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2011-11-8</p>
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
public class TOperateTableExtensionsImpl extends SqlMapClientDaoSupport implements IOperateTableExtensions
{
    private final Logger log=Logger.getLogger(this.getClass());
    /**
     * 是否存在表
     */
    private String existTableExtensions;
    
   /**
    * 创建表
    */
    private String createTableExtensions;
    
    /**
     *是否有数据 
     */
    private String existExtensionPoint;
    
    /**
     * 删除数据
     */
    private String deleteExtensions;
    
    /**
     * 插入数据
     */
    private String insertIntoExtensions;
    
    /**
     * 查找数据
     */
    private String selectExtensions;
    
    /**
     * 更新数据
     */
    private String updateExtensions;

    /**
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.tray.i.IOperateTableExtensions#countElemntExtensionPoint(java.util.Map)
     */
    public int countElemntExtensionPoint(Map map)
    {
        if(map==null || map.size()<1)
        {
            log.error("map is null or empty");
            return -1;
        }
        
        Integer count=Integer.getInteger("0");        
        Object tmpO=(Integer)this.getSqlMapClientTemplate().queryForObject(existExtensionPoint, map,count);
        log.debug("tmpO:"+tmpO);
        log.debug("count:"+count);
        
        return count.intValue();
    }

    /**
     * 功能描述：创建表
     * @see com.chinaviponline.erp.corepower.tray.i.IOperateTableExtensions#createTableExtensions()
     */
    public boolean createTableExtensions()
    {
//        try
//        {
//            Connection conn=this.getSqlMapClientTemplate().getDataSource().getConnection();
//            log.debug("Connection is "+conn);
            
            Integer countStar=(Integer)this.getSqlMapClientTemplate().queryForObject(existTableExtensions);//表是否存在
            
            if(countStar.intValue()==0)
            {
                log.debug("createTableExtensions countStar:"+countStar);
                int updateCount=this.getSqlMapClientTemplate().update(createTableExtensions);//创建表
                log.debug("createTableExtensions updateCount:"+updateCount);
            }
            else
            {
                log.debug("Table Exist:"+countStar);
            }
//        }
//        catch (SQLException e)
//        {
//           log.error("createTableExtensions:"+e);
//            return false;
//        }
        
        return true;
    }

    /**
     * 功能描述：删除数据
     * @see com.chinaviponline.erp.corepower.tray.i.IOperateTableExtensions#deleteFromTableExtensions(java.util.Map)
     */
    public int deleteFromTableExtensions(Map map)
    {
        if(map==null || map.size()<1)
        {
            log.error("map is null or empty");
            return -1;
        }
        Integer count=this.getSqlMapClientTemplate().delete(deleteExtensions, map);
        log.debug("count:"+count);
        
        return count.intValue();
    }

    /**
     * 功能描述：表是否存在
     * @see com.chinaviponline.erp.corepower.tray.i.IOperateTableExtensions#existTableExtensions()
     */
    public boolean existTableExtensions()
    {
//        Integer countExist=Integer.getInteger("0");
        Integer countStar=(Integer)this.getSqlMapClientTemplate().queryForObject(existTableExtensions);//表是否存在
        log.debug("countStar:"+countStar);
        
        if(countStar.intValue()==0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * 功能描述：插入数据
     * @see com.chinaviponline.erp.corepower.tray.i.IOperateTableExtensions#insertIntoTableExtensions(java.util.Map)
     */
    public int insertIntoTableExtensions(Map map)
    {
        if(map==null || map.size()<1)
        {
            log.error("map is null or empty");
            return -1;
        }
        Integer insertCout=(Integer)this.getSqlMapClientTemplate().insert(insertIntoExtensions, map);
        log.debug("insertCout"+insertCout);
        return insertCout.intValue();
    }

    /**
     * 功能描述：更新数据
     * @see com.chinaviponline.erp.corepower.tray.i.IOperateTableExtensions#updateSetTableExtensions(java.util.Map)
     */
    public int updateSetTableExtensions(Map map)
    {
        if(map==null || map.size()<1)
        {
            log.error("map is null or empty");
            return -1;
        }
        Integer updateCount=(Integer)this.getSqlMapClientTemplate().update(updateExtensions, map);
        log.debug("updateCount:"+updateCount);
        return updateCount.intValue();
    }

    /**
     * 
     * 功能描述：查询数据
     * @see com.chinaviponline.erp.corepower.tray.i.IOperateTableExtensions#selectElementExtensionPoint(java.util.Map)
     */
    public List selectElementExtensionPoint(Map map)
    {
        if(map==null || map.size()<1)
        {
            log.error("map is null or empty");
            return new ArrayList();//不返回NULL、返回空数组
        }
        
        List selectElements=this.getSqlMapClientTemplate().queryForList(selectExtensions, map); 
        log.debug("selectElements:"+selectElements);
        
        return selectElements;
    }

}
