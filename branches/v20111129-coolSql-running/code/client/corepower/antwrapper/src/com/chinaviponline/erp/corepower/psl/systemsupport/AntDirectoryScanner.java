/**
 * 
 */
package com.chinaviponline.erp.corepower.psl.systemsupport;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.FileScanner;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceFactory;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.SelectorScanner;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.apache.tools.ant.util.FileUtils;

/**
 * <p>文件名称：AntDirectoryScanner.java</p>
 * <p>文件描述：扫描目录</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公    司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2008-5-6</p>
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
public class AntDirectoryScanner implements FileScanner, SelectorScanner,
        ResourceFactory
{
    private static final boolean ON_VMS = Os.isFamily("openvms");

    /**
     * @deprecated Field DEFAULTEXCLUDES is deprecated
     */
    protected static final String DEFAULTEXCLUDES[] = 
    {
        // Miscellaneous typical temporary files
        "**/*~", 
        "**/#*#",
        "**/.#*", 
        "**/%*%", 
        "**/._*",
        
        // CVS
        "**/CVS", 
        "**/CVS/**",
       "**/.cvsignore",
       
       // SCCS
       "**/SCCS", 
       "**/SCCS/**", 
       
       // Visual SourceSafe
       "**/vssver.scc",
       
       // Subversion
       "**/.svn",
       "**/.svn/**",
       
       // Mac
       "**/.DS_Store"
       };

    private static Vector defaultExcludes = new Vector();

    protected File basedir;

    protected String includes[];

    protected String excludes[];

    protected FileSelector selectors[];

    protected Vector filesIncluded;

    protected Vector filesNotIncluded;

    protected Vector filesExcluded;

    protected Vector dirsIncluded;

    protected Vector dirsNotIncluded;

    protected Vector dirsExcluded;

    protected Vector filesDeselected;

    protected Vector dirsDeselected;

    protected boolean haveSlowResults;

    protected boolean isCaseSensitive;

    private boolean followSymlinks;

    private static final FileUtils FILEUTILS = FileUtils.newFileUtils();

    protected boolean everythingIncluded;

    private Map fileListMap;

    private Set scannedDirs;

    static
    {
        resetDefaultExcludes();
    }

    public AntDirectoryScanner()
    {
        basedir = null;
        includes = null;
        excludes = null;
        selectors = null;
        filesIncluded = null;
        filesNotIncluded = null;
        filesExcluded = null;
        dirsIncluded = null;
        dirsNotIncluded = null;
        dirsExcluded = null;
        filesDeselected = null;
        dirsDeselected = null;
        haveSlowResults = false;
        isCaseSensitive = true;
        followSymlinks = true;
        everythingIncluded = true;
        fileListMap = new HashMap();
        scannedDirs = new HashSet();
    }

    protected static boolean matchPatternStart(String pattern, String str)
    {
        return SelectorUtils.matchPatternStart(pattern, str);
    }

    protected static boolean matchPatternStart(String pattern, String str,
            boolean isCaseSensitive1)
    {
        return SelectorUtils.matchPatternStart(pattern, str, isCaseSensitive1);
    }

    protected static boolean matchPath(String pattern, String str)
    {
        return SelectorUtils.matchPath(pattern, str);
    }

    protected static boolean matchPath(String pattern, String str,
            boolean isCaseSensitive1)
    {
        return SelectorUtils.matchPath(pattern, str, isCaseSensitive1);
    }

    public static boolean match(String pattern, String str)
    {
        return SelectorUtils.match(pattern, str);
    }

    protected static boolean match(String pattern, String str,
            boolean isCaseSensitive1)
    {
        return SelectorUtils.match(pattern, str, isCaseSensitive1);
    }

    public static String[] getDefaultExcludes()
    {
        return (String[]) defaultExcludes.toArray(new String[defaultExcludes
                .size()]);
    }

    public static boolean addDefaultExclude(String s)
    {
        if (defaultExcludes.indexOf(s) == -1)
        {
            defaultExcludes.add(s);
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean removeDefaultExclude(String s)
    {
        return defaultExcludes.remove(s);
    }

    public static void resetDefaultExcludes()
    {
        defaultExcludes = new Vector();
        for (int i = 0; i < DEFAULTEXCLUDES.length; i++)
            defaultExcludes.add(DEFAULTEXCLUDES[i]);

    }

    public void setBasedir(String aBasedir)
    {
        setBasedir(new File(aBasedir.replace('/', File.separatorChar).replace(
                '\\', File.separatorChar)));
    }

    public void setBasedir(File basedir)
    {
        this.basedir = basedir;
    }

    public File getBasedir()
    {
        return basedir;
    }

    public boolean isCaseSensitive()
    {
        return isCaseSensitive;
    }

    public void setCaseSensitive(boolean isCaseSensitive1)
    {
        isCaseSensitive = isCaseSensitive1;
    }

    public boolean isFollowSymlinks()
    {
        return followSymlinks;
    }

    public void setFollowSymlinks(boolean followSymlinks)
    {
        this.followSymlinks = followSymlinks;
    }

    public void setIncludes(String includes[])
    {
        if (includes == null)
        {
            this.includes = null;
        }
        else
        {
            this.includes = new String[includes.length];
            for (int i = 0; i < includes.length; i++)
            {
                String pattern = includes[i].replace('/', File.separatorChar)
                        .replace('\\', File.separatorChar);
                if (pattern.endsWith(File.separator))
                {
                    pattern = pattern + "**";                    
                }
                
                this.includes[i] = pattern;
            }

        }
    }

    /**
     * 
     * 功能描述：不包含的
     * @see org.apache.tools.ant.FileScanner#setExcludes(java.lang.String[])
     */
    public void setExcludes(String excludes[])
    {
        if (excludes == null)
        {
            this.excludes = null;
        }
        else
        {
            this.excludes = new String[excludes.length];
            for (int i = 0; i < excludes.length; i++)
            {
                String pattern = excludes[i].replace('/', File.separatorChar)
                        .replace('\\', File.separatorChar);
                if (pattern.endsWith(File.separator))
                {
                    pattern = pattern + "**";                    
                }
                this.excludes[i] = pattern;
            }

        }
    }

    public void setSelectors(FileSelector selectors[])
    {
        this.selectors = selectors;
    }

    public boolean isEverythingIncluded()
    {
        return everythingIncluded;
    }

    public void scan() throws BuildException
    {
        if (basedir == null)
        {
            throw new BuildException("No basedir set");            
        }
        
        if (!basedir.exists())
        {
            throw new BuildException("basedir " + basedir + " does not exist");            
        }
        
        if (!basedir.isDirectory())
        {
            throw new BuildException("basedir " + basedir
                    + " is not a directory");            
        }
        
        if (includes == null)
        {
            includes = new String[0];            
        }
        
        if (excludes == null)
        {
            excludes = new String[0];            
        }
        
        filesIncluded = new Vector();
        filesNotIncluded = new Vector();
        filesExcluded = new Vector();
        filesDeselected = new Vector();
        dirsIncluded = new Vector();
        dirsNotIncluded = new Vector();
        dirsExcluded = new Vector();
        dirsDeselected = new Vector();
        
        if (isIncluded(""))
        {
            if (!isExcluded(""))
            {
                if (isSelected("", basedir))
                {
                    dirsIncluded.addElement("");                    
                }
                else
                {
                    dirsDeselected.addElement("");                    
                }
            }
            else
            {
                dirsExcluded.addElement("");
            }
        }
        else
        {
            dirsNotIncluded.addElement("");
        }
        checkIncludePatterns();
        clearCaches();
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    private void checkIncludePatterns()
    {
        Hashtable newroots = new Hashtable();
        for (int icounter = 0; icounter < includes.length; icounter++)
        {
            String newpattern = SelectorUtils
                    .rtrimWildcardTokens(includes[icounter]);
            newroots.put(newpattern, includes[icounter]);
        }

        if (newroots.containsKey(""))
        {
            scandir(basedir, "", true);
        }
        else
        {
            Enumeration enum2 = newroots.keys();
            File canonBase = null;
            try
            {
                canonBase = basedir.getCanonicalFile();
            }
            catch (IOException ex)
            {
                throw new BuildException(ex);
            }
            
            do
            {
                if (!enum2.hasMoreElements())
                {
                    break;                    
                }
                String currentelement = (String) enum2.nextElement();
                String originalpattern = (String) newroots.get(currentelement);
                File myfile = new File(basedir, currentelement);
                
                if (myfile.exists())
                {
                    try
                    {
                        File canonFile = myfile.getCanonicalFile();
                        String path = FILEUTILS.removeLeadingPath(canonBase,
                                canonFile);
                        if (!path.equals(currentelement) || ON_VMS)
                        {
                            myfile = findFile(basedir, currentelement);
                            if (myfile != null)
                            {
                                currentelement = FILEUTILS.removeLeadingPath(
                                        basedir, myfile);                                
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        throw new BuildException(ex);
                    }                    
                }
                    
                if ((myfile == null || !myfile.exists()) && !isCaseSensitive)
                {
                    File f = findFileCaseInsensitive(basedir, currentelement);
                    if (f.exists())
                    {
                        currentelement = FILEUTILS
                                .removeLeadingPath(basedir, f);
                        myfile = f;
                    }
                }
                
                if (myfile != null
                        && myfile.exists()
                        && (followSymlinks || !isSymlink(basedir,
                                currentelement)))
                {
                    if (myfile.isDirectory())
                    {
                        if (isIncluded(currentelement)
                                && currentelement.length() > 0)
                        {
                            accountForIncludedDir(currentelement, myfile, true);
                        }
                        else
                        {
                            if (currentelement.length() > 0
                                    && currentelement.charAt(currentelement
                                            .length() - 1) != File.separatorChar)
                                currentelement = currentelement
                                        + File.separatorChar;
                            scandir(myfile, currentelement, true);
                        }
                    }
                    else if (isCaseSensitive
                            && originalpattern.equals(currentelement))
                    {
                        accountForIncludedFile(currentelement, myfile);
                    }
                    else if (!isCaseSensitive
                            && originalpattern.equalsIgnoreCase(currentelement))
                    {
                        accountForIncludedFile(currentelement, myfile);
                    }                    
                }
            }
            while (true);
        }
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    protected void slowScan()
    {
        if (haveSlowResults)
        {
            return;            
        }
        
        String excl[] = new String[dirsExcluded.size()];
        dirsExcluded.copyInto(excl);
        String notIncl[] = new String[dirsNotIncluded.size()];
        dirsNotIncluded.copyInto(notIncl);
        
        for (int i = 0; i < excl.length; i++)
        {
            if (!couldHoldIncluded(excl[i]))
            {
                scandir(new File(basedir, excl[i]), excl[i] + File.separator,
                        false);
            }            
        }

        for (int i = 0; i < notIncl.length; i++)
        {
            if (!couldHoldIncluded(notIncl[i]))
            {
                scandir(new File(basedir, notIncl[i]), notIncl[i]
                        + File.separator, false);
            }            
        }

        haveSlowResults = true;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param dir
     * @param vpath
     * @param fast
     */
    protected void scandir(File dir, String vpath, boolean fast)
    {
        if (fast && hasBeenScanned(vpath))
        {
            return;            
        }
        
        String newfiles[] = dir.list();
        if (newfiles == null)
        {
            throw new BuildException("IO error scanning directory "
                    + dir.getAbsolutePath());            
        }
        
        if (!followSymlinks)
        {
            Vector noLinks = new Vector();
            for (int i = 0; i < newfiles.length; i++)
            {
                String msg;
                try
                {
                    if (FILEUTILS.isSymbolicLink(dir, newfiles[i]))
                    {
                        String name = vpath + newfiles[i];
                        File file = new File(dir, newfiles[i]);
                        
                        if (file.isDirectory())
                        {
                            dirsExcluded.addElement(name);                            
                        }
                        else
                        {
                            filesExcluded.addElement(name);                            
                        }
                    }
                    else
                    {
                        noLinks.addElement(newfiles[i]);
                    }
                    continue;
                }
                catch (IOException ioe)
                {
                    msg = "IOException caught while checking for links, couldn't get canonical path!";
                }
                
                System.err.println(msg);
                noLinks.addElement(newfiles[i]);
            }

            newfiles = new String[noLinks.size()];
            noLinks.copyInto(newfiles);
        }
        
        for (int i = 0; i < newfiles.length; i++)
        {
            String name = vpath + newfiles[i];
            File file = new File(dir, newfiles[i]);
            if (file.isDirectory())
            {
                if (isIncluded(name))
                {
                    accountForIncludedDir(name, file, fast);
                }
                else
                {
                    everythingIncluded = false;
                    dirsNotIncluded.addElement(name);
                    if (fast && couldHoldIncluded(name))
                        scandir(file, name + File.separator, fast);
                }
                if (!fast)
                {
                    scandir(file, name + File.separator, fast);                    
                }
                continue;
            }
            
            if (!file.isFile())
            {
                continue;                
            }
            
            if (isIncluded(name))
            {
                accountForIncludedFile(name, file);
            }
            else
            {
                everythingIncluded = false;
                filesNotIncluded.addElement(name);
            }
        }

    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param name
     * @param file
     */
    private void accountForIncludedFile(String name, File file)
    {
        if (!filesIncluded.contains(name) && !filesExcluded.contains(name)
                && !filesDeselected.contains(name))
        {
            if (!isExcluded(name))
            {
                if (isSelected(name, file))
                {
                    filesIncluded.addElement(name);
                }
                else
                {
                    everythingIncluded = false;
                    filesDeselected.addElement(name);
                }
            }
            else
            {
                everythingIncluded = false;
                filesExcluded.addElement(name);
            }            
        }
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param name
     * @param file
     * @param fast
     */
    private void accountForIncludedDir(String name, File file, boolean fast)
    {
        if (!dirsIncluded.contains(name) && !dirsExcluded.contains(name)
                && !dirsDeselected.contains(name))
        {
            if (!isExcluded(name))
            {
                if (isSelected(name, file))
                {
                    dirsIncluded.addElement(name);
                    if (fast)
                    {
                        scandir(file, name + File.separator, fast);
                    }
                }
                else
                {
                    everythingIncluded = false;
                    dirsDeselected.addElement(name);
                    if (fast && couldHoldIncluded(name))
                    {
                        scandir(file, name + File.separator, fast);
                    }
                }
            }
            else
            {
                everythingIncluded = false;
                dirsExcluded.addElement(name);
                if (fast && couldHoldIncluded(name))
                {
                    scandir(file, name + File.separator, fast);
                }
            }            
        }
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param name
     * @return
     */
    protected boolean isIncluded(String name)
    {
        for (int i = 0; i < includes.length; i++)
        {
            if (matchPath(includes[i], name, isCaseSensitive))
            {
                return true;
            }            
        }

        return false;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param name
     * @return
     */
    protected boolean couldHoldIncluded(String name)
    {
        for (int i = 0; i < includes.length; i++)
        {
            if (matchPatternStart(includes[i], name, isCaseSensitive)
                    && isMorePowerfulThanExcludes(name, includes[i]))
            {
                return true;
            }            
        }

        return false;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param name
     * @param includepattern
     * @return
     */
    private boolean isMorePowerfulThanExcludes(String name,
            String includepattern)
    {
        String soughtexclude = name + File.separator + "**";
        for (int counter = 0; counter < excludes.length; counter++)
        {
            if (excludes[counter].equals(soughtexclude))
            {
                return false;
            }            
        }

        return true;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param name
     * @return
     */
    protected boolean isExcluded(String name)
    {
        for (int i = 0; i < excludes.length; i++)
        {
            if (matchPath(excludes[i], name, isCaseSensitive))
            {
                return true;
            }            
        }

        return false;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param name
     * @param file
     * @return
     */
    protected boolean isSelected(String name, File file)
    {
        if (selectors != null)
        {
            for (int i = 0; i < selectors.length; i++)
            {
                if (!selectors[i].isSelected(basedir, name, file))
                {
                    return false;
                }                
            }

        }
        return true;
    }

    /**
     * 
     * 功能描述：
     * @see org.apache.tools.ant.FileScanner#getIncludedFiles()
     */
    public String[] getIncludedFiles()
    {
        String files[] = new String[filesIncluded.size()];
        filesIncluded.copyInto(files);
        Arrays.sort(files);
        return files;
    }

    /**
     * 
     * 功能描述：
     * @see org.apache.tools.ant.FileScanner#getNotIncludedFiles()
     */
    public String[] getNotIncludedFiles()
    {
        slowScan();
        String files[] = new String[filesNotIncluded.size()];
        filesNotIncluded.copyInto(files);
        return files;
    }

    /**
     * 
     * 功能描述：
     * @see org.apache.tools.ant.FileScanner#getExcludedFiles()
     */
    public String[] getExcludedFiles()
    {
        slowScan();
        String files[] = new String[filesExcluded.size()];
        filesExcluded.copyInto(files);
        return files;
    }

    /**
     * 
     * 功能描述：
     * @see org.apache.tools.ant.types.selectors.SelectorScanner#getDeselectedFiles()
     */
    public String[] getDeselectedFiles()
    {
        slowScan();
        String files[] = new String[filesDeselected.size()];
        filesDeselected.copyInto(files);
        return files;
    }

    /**
     * 
     * 功能描述：
     * @see org.apache.tools.ant.FileScanner#getIncludedDirectories()
     */
    public String[] getIncludedDirectories()
    {
        String directories[] = new String[dirsIncluded.size()];
        dirsIncluded.copyInto(directories);
        Arrays.sort(directories);
        return directories;
    }

    /**
     * 
     * 功能描述：
     * @see org.apache.tools.ant.FileScanner#getNotIncludedDirectories()
     */
    public String[] getNotIncludedDirectories()
    {
        slowScan();
        String directories[] = new String[dirsNotIncluded.size()];
        dirsNotIncluded.copyInto(directories);
        return directories;
    }

    /**
     * 
     * 功能描述：
     * @see org.apache.tools.ant.FileScanner#getExcludedDirectories()
     */
    public String[] getExcludedDirectories()
    {
        slowScan();
        String directories[] = new String[dirsExcluded.size()];
        dirsExcluded.copyInto(directories);
        return directories;
    }

    /**
     * 
     * 功能描述：
     * @see org.apache.tools.ant.types.selectors.SelectorScanner#getDeselectedDirectories()
     */
    public String[] getDeselectedDirectories()
    {
        slowScan();
        String directories[] = new String[dirsDeselected.size()];
        dirsDeselected.copyInto(directories);
        return directories;
    }

    /**
     * 
     * 功能描述：
     * @see org.apache.tools.ant.FileScanner#addDefaultExcludes()
     */
    public void addDefaultExcludes()
    {
        int excludesLength = excludes != null ? excludes.length : 0;
        String newExcludes[] = new String[excludesLength
                + defaultExcludes.size()];
        
        if (excludesLength > 0)
        {
            System.arraycopy(excludes, 0, newExcludes, 0, excludesLength);            
        }
        
        String defaultExcludesTemp[] = getDefaultExcludes();
        
        for (int i = 0; i < defaultExcludesTemp.length; i++)
        {
            newExcludes[i + excludesLength] = defaultExcludesTemp[i].replace(
                    '/', File.separatorChar).replace('\\', File.separatorChar);            
        }

        excludes = newExcludes;
    }

    /**
     * 
     * 功能描述：
     * @see org.apache.tools.ant.types.ResourceFactory#getResource(java.lang.String)
     */
    public Resource getResource(String name)
    {
        File f = FILEUTILS.resolveFile(basedir, name);
        return new Resource(name, f.exists(), f.lastModified(), f.isDirectory());
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param file
     * @return
     */
    private String[] list(File file)
    {
        String files[] = (String[]) fileListMap.get(file);
        if (files == null)
        {
            files = file.list();
            if (files != null)
                fileListMap.put(file, files);
        }
        return files;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param base
     * @param path
     * @return
     */
    private File findFileCaseInsensitive(File base, String path)
    {
        File f = findFileCaseInsensitive(base, SelectorUtils.tokenizePath(path));
        return f != null ? f : new File(base, path);
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param base
     * @param pathElements
     * @return
     */
    private File findFileCaseInsensitive(File base, Vector pathElements)
    {
        if (pathElements.size() == 0)
        {
            return base;            
        }
        
        if (!base.isDirectory())
        {
            return null;            
        }
        
        String files[] = list(base);
        
        if (files == null)
        {
            throw new BuildException("IO error scanning directory "
                    + base.getAbsolutePath());            
        }
        
        String current = (String) pathElements.remove(0);
        
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].equals(current))
            {
                base = new File(base, files[i]);
                return findFileCaseInsensitive(base, pathElements);
            }            
        }

        
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].equalsIgnoreCase(current))
            {
                base = new File(base, files[i]);
                return findFileCaseInsensitive(base, pathElements);
            }            
        }

        return null;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param base
     * @param path
     * @return
     */
    private File findFile(File base, String path)
    {
        return findFile(base, SelectorUtils.tokenizePath(path));
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param base
     * @param pathElements
     * @return
     */
    private File findFile(File base, Vector pathElements)
    {
        if (pathElements.size() == 0)
        {
            return base;            
        }
        
        if (!base.isDirectory())
        {
            return null;            
        }
        
        String files[] = list(base);
        
        if (files == null)
        {
            throw new BuildException("IO error scanning directory "
                    + base.getAbsolutePath());            
        }
        
        String current = (String) pathElements.remove(0);
        
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].equals(current))
            {
                base = new File(base, files[i]);
                return findFile(base, pathElements);
            }            
        }

        return null;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param base
     * @param path
     * @return
     */
    private boolean isSymlink(File base, String path)
    {
        return isSymlink(base, SelectorUtils.tokenizePath(path));
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param base
     * @param pathElements
     * @return
     */
    private boolean isSymlink(File base, Vector pathElements)
    {
        if (pathElements.size() > 0)
        {
            String current = (String) pathElements.remove(0);
            try
            {
                return FILEUTILS.isSymbolicLink(base, current)
                        || isSymlink(new File(base, current), pathElements);
            }
            catch (IOException ioe)
            {
                String msg = "IOException caught while checking "
                        + "for links, couldn't get canonical path!";
                // will be caught and redirected to Ant's logging system
                System.err.println(msg);
            }
        }
        return false;
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param vpath
     * @return
     */
    private boolean hasBeenScanned(String vpath)
    {
        return !scannedDirs.add(vpath);
    }

    /**
     * 
     * <p>功能描述：</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2008-5-25</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：    修改日期：    修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     */
    private void clearCaches()
    {
        fileListMap.clear();
        scannedDirs.clear();
    }

}
