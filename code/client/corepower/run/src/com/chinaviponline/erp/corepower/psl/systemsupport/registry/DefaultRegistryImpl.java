/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport.registry;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.chinaviponline.erp.corepower.psl.systemsupport.PropertiesService;
/**
 * <p>文件名称：DefaultRegistryImpl.java</p>
 * <p>文件描述：</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-6-3</p>
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
public class DefaultRegistryImpl implements ICOREPOWERRegistry
{
    
    // FastFileInputStream
    private static class FastFileInputStream extends InputStream
    {

        /**
         * 文件通道
         */
        private FileChannel fileChannel;
        
        /**
         * 可读取字节
         */
        private MappedByteBuffer readableByteBuffer;

        /**
         * 构造函数
         * @param file
         * @throws IOException
         */
        FastFileInputStream(File file)
        throws IOException
        {
            fileChannel = null;
            readableByteBuffer = null;
            fileChannel = (new RandomAccessFile(file, "r")).getChannel();
            readableByteBuffer = fileChannel.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0L, fileChannel.size());
        }
        
        /**
         * 
         * 功能描述：
         * @see java.io.InputStream#read()
         */
        public int read()
            throws IOException
        {
            return readableByteBuffer.hasRemaining() ? readableByteBuffer.get() : -1;
        }

        /**
         * 
         * 功能描述：
         * @see java.io.InputStream#read(byte[], int, int)
         */
        public int read(byte b[], int off, int len)
            throws IOException
        {
            int remaining = readableByteBuffer.remaining();
            len = len <= remaining ? len : remaining;
            readableByteBuffer.get(b, off, len);
            return len != 0 ? len : -1;
        }

        /**
         * 
         * 功能描述：
         * @see java.io.InputStream#available()
         */
        public int available()
            throws IOException
        {
            return readableByteBuffer.remaining();
        }

        /**
         * 
         * 功能描述：
         * @see java.io.InputStream#close()
         */
        public void close()
            throws IOException
        {
            fileChannel.close();
        }

    }
    

    /**
     * 全路径
     */
    protected static String fileFullPath;
    
    /**
     * 存储图
     */
    protected HashMap map;
    
    /**
     * 是否改变
     */
    private boolean changed;
    
    /**
     * 日志
     */
    private static Logger log;
    
    // 日志
    static 
    {
        fileFullPath = PropertiesService.getInstance().getPath("erp.path", "home") + File.separator + "temp" + File.separator + "COREPOWERRegistry.bin";
        log = Logger.getLogger(DefaultRegistryImpl.class);
    }


    /**
     * 构造函数
     *
     */
    protected DefaultRegistryImpl()
    {
        map = null;
        changed = false;
    }

    /**
     * 
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.psl.systemsupport.registry.ICOREPOWERRegistry#getFromRegistry(java.lang.String)
     */
    public ObjectInputStream getFromRegistry(String registryKey)
    {
        byte bytes[];
        if(map == null || !map.containsKey(registryKey))
        {
//          break MISSING_BLOCK_LABEL_58;    
            return null;
        }

        bytes = (byte[])map.get(registryKey);
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois;
        }
        catch (IOException ex)
        {
          log.info(ex);
        }
        
        return null;
    }

    /**
     * 
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.psl.systemsupport.registry.ICOREPOWERRegistry#setIntoRegistry(java.lang.String, byte[])
     */
    public void setIntoRegistry(String registryKey, byte bytes[])
        throws IOException
    {
        if(map == null)
        {
            map = new HashMap(1000);            
        }
        map.put(registryKey, bytes);
        changed = true;
    }

    /**
     * 
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.psl.systemsupport.registry.ICOREPOWERRegistry#loadRegistry()
     */
    public void loadRegistry()
    {
        File file = new File(fileFullPath);
        if(!file.exists())
        {
            log.debug("register file is not exists");
            return;            
        }
        
        ObjectInputStream ois = null;
        try
        {
            ois = new ObjectInputStream(new FastFileInputStream(file));
            int pairCount = ois.readInt();
            map = new HashMap(pairCount);
            for(int i = 0; i < pairCount; i++)
            {
                String registryKey = ois.readUTF();
                int valueBytesCount = ois.readInt();
                byte value[] = new byte[valueBytesCount];
                ois.readFully(value);
                map.put(registryKey, value);
            }

        }
        catch(Exception ignore)
        {
            log.info("Can not load register cache file, the file maybe collapsed!");
        }
        finally
        {
            if(ois != null)
            {
                try
                {
                    ois.close();
                }
                catch (IOException ignore)
                {
                    
                }                
            }
        }
    }

    /**
     * 
     * 功能描述：
     * @see com.chinaviponline.erp.corepower.psl.systemsupport.registry.ICOREPOWERRegistry#saveRegistry()
     */
    public void saveRegistry()
        throws FileNotFoundException, IOException
    {
        if(!changed)
        {
            log.debug("changed is "+changed);
            return;            
        }
        
        if(map != null)
        {
            File file = new File(fileFullPath);
            ObjectOutputStream oos = null;
            try
            {
                oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeInt(map.size());
                byte value[];
                for(Iterator it = map.entrySet().iterator(); it.hasNext(); oos.write(value))
                {
                    java.util.Map.Entry next = (java.util.Map.Entry)it.next();
                    String key = (String)next.getKey();
                    value = (byte[])next.getValue();
                    oos.writeUTF(key);
                    oos.writeInt(value.length);
                }
                oos.flush();
            }
            finally
            {
                if(oos != null)
                {
                    try
                    {
                        oos.close();
                    }
                    catch (IOException ignore)
                    {

                    }                    
                }
            }
        }
    }

}
