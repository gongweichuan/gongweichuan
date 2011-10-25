/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;


import java.io.*;
import java.util.*;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * <p>文件名称：DeployExcludeXmlParser.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-14</p>
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
public class DeployExcludeXmlParser
{

    /**
     * XML文件名称
     */
    String xmlFileName;
    
    /**
     * 旧文件集合
     */
    List oldFileSetPathList;
    
    /**
     * 旧文件路径集合
     */
    List oldFileSetParPathList;
    
    /**
     * 文档
     */
    Document doc1;
    
    /**
     * 基础目录
     */
    String baseDir;
    
    /**
     * 临时目录
     */
    String deployTempDirPath;
    
    /**
     * 部署目录
     */
    String deployDirPath;
    
    /**
     * 输出文件名车
     */
    String outFileName;

    /**
     * 构造函数
     * @param aXmlFileName
     * @throws ErpDeploymentException
     */
    public DeployExcludeXmlParser(String aXmlFileName)
        throws ErpDeploymentException
    {
        xmlFileName = null;
        oldFileSetPathList = null;
        oldFileSetParPathList = null;
        doc1 = null;
        baseDir = null;
        deployTempDirPath = PropertiesService.getInstance().getPath("erp.path", "home") + File.separator + "temp" + File.separator;
        deployDirPath = PropertiesService.getInstance().getPath("erp.path", "home") + File.separator + "deploy" + File.separator;
        outFileName = null;
        xmlFileName = aXmlFileName;
        oldFileSetPathList = Arrays.asList(DeploymentExcludeInit.getSingleton().getFSPaths());
        oldFileSetParPathList = DeploymentExcludeInit.getSingleton().getFsParIdLilst();
        File f = new File(xmlFileName);
        outFileName = deployTempDirPath + f.getName().replaceAll("partialdeploy-", "forPartialAnt-");
    }

    /**
     * 构造函数
     * @param aXmlFileName
     * @param aDeployDirPath
     * @param aDeployTempDirPath
     * @throws UmsDeploymentException
     */
    public DeployExcludeXmlParser(String aXmlFileName, String aDeployDirPath, String aDeployTempDirPath)
        throws ErpDeploymentException
    {
        xmlFileName = null;
        oldFileSetPathList = null;
        oldFileSetParPathList = null;
        doc1 = null;
        baseDir = null;
        deployTempDirPath = PropertiesService.getInstance().getPath("erp.path", "home") + File.separator + "temp" + File.separator;
        deployDirPath = PropertiesService.getInstance().getPath("erp.path", "home") + File.separator + "deploy" + File.separator;
        outFileName = null;
        xmlFileName = aXmlFileName;
        oldFileSetPathList = Arrays.asList(DeploymentExcludeInit.getSingleton().getFSPaths());
        oldFileSetParPathList = DeploymentExcludeInit.getSingleton().getFsParIdLilst();
        File f = new File(xmlFileName);
        deployDirPath = aDeployDirPath + File.separator;
        deployTempDirPath = aDeployTempDirPath + File.separator;
        outFileName = deployTempDirPath + f.getName().replaceAll("partialdeploy-", "forPartialAnt-");
    }

    /**
     * 
     * <p>功能描述：读取并插入</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @throws ErpDeploymentException
     */
    public void readAndInsert()
        throws ErpDeploymentException
    {
        try
        {
            SAXBuilder builder = new SAXBuilder(false);
            File f = new File(xmlFileName);
            doc1 = builder.build(f);
            Element project_e = doc1.getRootElement();
            baseDir = project_e.getAttributeValue("basedir");
            Element target_e = project_e.getChild("target");
            Element DeployedPar_e = target_e.getChild("DeployedPar");
            List filesets_e = DeployedPar_e.getChildren("fileset");
            List newFileSetPathList = new LinkedList();
            List newFileSetParPathList = new LinkedList();
            Element newFileset_e;
            
            for(Iterator it = filesets_e.iterator(); it.hasNext(); newFileSetParPathList.add(newFileset_e))
            {
                Element fileset_e = (Element)it.next();
                String dir = fileset_e.getAttributeValue("dir");
                File fdir = new File(deployDirPath + File.separator + baseDir + File.separator + dir);
                dir = fdir.getCanonicalPath();
                newFileset_e = new Element("fileset");
                newFileset_e.setAttribute("dir", dir);
                List children = fileset_e.getChildren();
                List newChildren = new LinkedList();
                Element e;
                for(Iterator it0 = children.iterator(); it0.hasNext(); newChildren.add(e))
                {
                    e = (Element)it0.next();                    
                }

                fileset_e.removeChildren();
                newFileset_e.setChildren(newChildren);
                String path = fdir.getCanonicalPath();
                newFileSetPathList.add(path);
            }

            int insertIndex = -1;
            int oldFileSetPathIndex = -1;
            for(Iterator it = oldFileSetPathList.iterator(); it.hasNext();)
            {
                oldFileSetPathIndex++;
                String path = ((File)it.next()).getAbsolutePath();
                int currIndex = newFileSetPathList.indexOf(path);
                if(currIndex != -1)
                {
                    insertIndex = insertIndex + currIndex + 1;
//                    Element newFileset_e = (Element)newFileSetParPathList.get(insertIndex);
                    
                    newFileset_e = (Element)newFileSetParPathList.get(insertIndex);
                    List oldParPathList = (List)oldFileSetParPathList.get(oldFileSetPathIndex);
                    Element e;
                    for(Iterator it1 = oldParPathList.iterator(); it1.hasNext(); newFileset_e.addContent(e))
                    {
                        e = new Element("include");
                        e.setAttribute("name", (String)it1.next());
                    }

                    int tempSize = newFileSetPathList.size();
                    newFileSetPathList = newFileSetPathList.subList(currIndex + 1, tempSize);
                } else
                {
                    insertIndex++;
//                    Element newFileset_e = new Element("fileset");
                    newFileset_e = new Element("fileset");
                    newFileset_e.setAttribute("dir", path);
                    List oldParPathList = (List)oldFileSetParPathList.get(oldFileSetPathIndex);
                    Element e;
                    for(Iterator it1 = oldParPathList.iterator(); it1.hasNext(); newFileset_e.addContent(e))
                    {
                        e = new Element("include");
                        e.setAttribute("name", (String)it1.next());
                    }

                    newFileSetParPathList.add(insertIndex, newFileset_e);
                }
            }

            DeployedPar_e.setChildren(newFileSetParPathList);
        }
        catch(IOException e)
        {
            throw new ErpDeploymentException("getCanonicalPath error", e);
        }
        catch(JDOMException e1)
        {
            throw new ErpDeploymentException("build " + xmlFileName + "error!", e1);
        }
    }

    /**
     * 
     * <p>功能描述：保存</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @throws UmsDeploymentException
     */
    public void save()
        throws ErpDeploymentException
    {
        FileWriter fw = null;
        try
        {
            fw = new FileWriter(outFileName);
            XMLOutputter outputter = new XMLOutputter();
            outputter.setTrimAllWhite(true);
            outputter.setIndent("    ");
            outputter.setNewlines(true);
            outputter.setEncoding("GB2312");
            outputter.output(doc1, fw);
        }
        catch(Exception ignore)
        {
            throw new ErpDeploymentException("save " + outFileName + " error!", ignore);
        }
        finally
        {
            try
            {
                if(fw != null)
                {
                    fw.close();                    
                }
            }
            catch(IOException ignore)
            {
                
            }
        }
    }

    /**
     * 
     * <p>功能描述：解析XML</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-14</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @return
     * @throws ErpDeploymentException
     */
    public String parserDeployXml()
        throws ErpDeploymentException
    {
        readAndInsert();
        save();
        return outFileName;
    }
}
