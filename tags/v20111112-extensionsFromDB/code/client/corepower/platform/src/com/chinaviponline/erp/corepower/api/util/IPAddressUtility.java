/**
 * 
 */
package com.chinaviponline.erp.corepower.api.util;

import java.io.PrintStream;
import java.net.*;
import java.util.*;

import com.chinaviponline.erp.corepower.api.ServiceAccess;
import com.chinaviponline.erp.corepower.api.psl.systemsupport.SystemSupportService;

/**
 * <p>文件名称：IPAddressUtility.java</p>
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
public class IPAddressUtility
{


    static final int INADDRSZ4 = 4;
    static final int INADDRSZ6 = 16;
    private static final int INT16SZ6 = 2;
    private static final char LEFT = 91;
    private static final char RIGHT = 93;
    private static final char COLON = 58;
    private static final char DOT = 46;
    private static final int NEGATIVE = -1;
    private static final int NEGATIVE2 = -2;
    private static final int POSITIVE = 1;
    private static final int POSITIVE3 = 3;
    private static final int POSITIVE11 = 11;
    private static final int POSITIVE12 = 12;
    private static final int POSITIVE13 = 13;
    private static final int POSITIVE14 = 14;
    private static final int POSITIVE15 = 15;
    private static final int POSITIVE255 = 255;
    private static final int POSITIVE256 = 256;
    private static final int ZERO = 0;
    private static final int NUM0XFF = 255;
    private static final int NUM0XFFFF = 65535;
    private static final String V4_PROP_NAME = "erp.ip.version.purev4";
    private static final int ADDR_RANGLE_LIMIT = 10000;
    public static final String COREPOWER_BIND_LOCALADDRESS = "com.chinaviponline.erp.corepower.localaddress";
    public static final String COREPOWER_BIND_IP = "corepower.bind.ip";
    public static final String COREPOWER_BIND_NOT_IP = "0.0.0.0";
    private static boolean needBinding = false;
    private static String hostAdd = null;

    public IPAddressUtility()
    {
    }

    public static String getBindIp()
    {
        return hostAdd;
    }

    public static boolean isLocalAddr(String addr)
    {
        if(addr == null)
        {
            return false;            
        }
        
        InetAddress in = null;
        
        try
        {
            in = InetAddress.getByName(addr);
        }
        catch(UnknownHostException e)
        {
            return false;
        }
        
        if(in.isLoopbackAddress())
        {
            return true;            
        }
        
        InetAddress addrs[];
        
        try
        {
            String name = InetAddress.getLocalHost().getHostName();
            addrs = InetAddress.getAllByName(name);
        }
        catch(UnknownHostException e)
        {
            return false;
        }
        
        for(int i = 0; i < addrs.length; i++)
        {
            if (in.equals(addrs[i]))
            {
                return true;                
            }            
        }

        return false;
    }

    public static String getShowName(InetAddress addr)
    {
        if(addr == null)
        {
            return "";            
        }
        
        if(addr instanceof Inet4Address)
        {
            return addr.getHostAddress();            
        }
        else
        {
            return addr.getHostName();            
        }
    }

    public static String getShowName(String addr)
        throws UnknownHostException
    {
        if(addr == null || addr.length() == 0)
        {
            return "";            
        }
        
        InetAddress iAddr = InetAddress.getByName(addr);
        
        if(iAddr instanceof Inet4Address)
        {
            return iAddr.getHostAddress();            
        }
        else
        {
            return iAddr.getHostName();            
        }
    }

    public static boolean isValidIP(String addr)
    {
        if(addr == null || addr.length() == 0)
        {
            return false;            
        }
        
        boolean ipv6Expected = false;
        
        if(addr.charAt(0) == '[')
        {
            if (addr.length() > 2 && addr.charAt(addr.length() - 1) == ']')
            {
                addr = addr.substring(1, addr.length() - 1);
                ipv6Expected = true;
            }
            else
            {
                return false;
            }            
        }
        
        if(Character.digit(addr.charAt(0), 16) != -1 || addr.charAt(0) == ':')
        {
            byte bAddr[] = null;
            bAddr = textToNumericFormat4(addr);
            
            if(bAddr.length == 0)
            {
                bAddr = textToNumericFormat6(addr);                
            }
            else
            if(ipv6Expected)
            {
                return false;                
            }
            
            return bAddr.length != 0;
        } else
        {
            return false;
        }
    }

    public static boolean isValidIP(String addr, String symbol)
    {
        String temp[];
        int maxValue;
        int radix;
        int symbolNum;
        int num;
        int i;
        
        if(addr == null || addr.length() == 0)
        {
            return false;            
        }
        
        if(symbol == null || symbol.length() == 0)
        {
            return isValidIP(addr);            
        }
        
        if(addr.indexOf(symbol) == -1)
        {
            return isValidIP(addr);            
        }
        
        if(addr.compareTo(symbol) == 0)
        {
            return true;            
        }
        
        char v4SegSymbol[] = {
            '.'
        };
        char v6SegSymbol[] = {
            ':'
        };
        
        String ipv4SegSymbol = new String(v4SegSymbol);
        String ipSegSymbol = new String(v6SegSymbol);
        int maxIpSegNum = 0;
        maxValue = 0;
        radix = 10;
        
        if(addr.indexOf(ipSegSymbol) != -1)
        {
            return false;            
        }
        
        if(addr.indexOf(ipv4SegSymbol) != -1)
        {
            if(addr.endsWith(ipv4SegSymbol))
            {
                return false;                
            }
            
            String tempIP = addr.replace('.', ':');
            temp = tempIP.split(ipSegSymbol);
            maxIpSegNum = 4;
            maxValue = 255;
            radix = 10;
        } else
        {
            return false;
        }
        
        symbolNum = 0;
        num = 0;
        if(temp.length > maxIpSegNum)
        {
            return false;            
        }
        i = 0;
//_L3:
    //TODO 需要二次修改
        do{
            if(i >= temp.length) 
            {
                break;
            }
    //        _L1:
            else
            {
                if(temp[i].compareTo(symbol) == 0)// 如果时分割符号 .或者:
                {
                    symbolNum++;
                    if(i > 0 && i < temp.length - 1 || symbolNum > 1) //判断逻辑有问题
                    {
                        return false;                        
                    }
                    
                    continue;
                }
                
                try
                {
                    num = Integer.parseInt(temp[i], radix);
                    if(num < 0 || num > maxValue)
                    {
                        return false;                    
                    }  
//                    continue;
                }
                catch(NumberFormatException e)
                {
                    return false;
                }                
                i++;
            }
    
        }while(true);


        return temp.length != 0;
    }

    public static boolean isValidAddr(String addr)
    {
        
        if(addr == null || addr.length() == 0)
        {
            return false;            
        }
        
        int dotNum = 0;
        int colonNum = 0;
        String src = addr;
        int len = src.length();
        
        for(int i = 0; i < len; i++)
        {
            if(src.charAt(i) == '.')
            {
                dotNum++;                
            }
            
            if(src.charAt(i) == ':')
            {
                colonNum++;                
            }
        }

        if(dotNum == 3 || colonNum > 1)
        {
            return isValidIP(addr);            
        }
        else
        {
            return true;            
        }
    }

    public static boolean isUnicastAddress(String addr)
        throws UnknownHostException
    {
        if(addr == null || addr.length() == 0)
        {
            return false;
        } else
        {
            InetAddress iAddr = InetAddress.getByName(addr);
            return !iAddr.isMulticastAddress();
        }
    }

    public static String getLocalShowAddress()
        throws UnknownHostException
    {
        boolean v4env = true;
        SystemSupportService sys = ServiceAccess.getSystemSupportService();
        v4env = (new Boolean(sys.getERPProperty("erp.ip.version.purev4"))).booleanValue();
        
        if(v4env)
        {
            return InetAddress.getLocalHost().getHostAddress();            
        }
        else
        {
            return InetAddress.getLocalHost().getHostName();            
        }
    }

    public static boolean isInRange(String addr, String fromIP, String toIP)
    {
        
        if(addr == null || fromIP == null || toIP == null || addr.length() == 0)
        {
            return false;            
        }
        
        if(!isValidRange(fromIP, toIP))
        {
            return false;            
        }
        
        int firstCom = compareIP(fromIP, addr);
        int secondCom = compareIP(addr, toIP);
        
        if(firstCom == -2 || secondCom == -2)
        {
            return false;            
        }
        
        return firstCom <= 0 && secondCom <= 0;
    }

    public static boolean isValidRange(String fromIP, String toIP)
    {
        if(fromIP == null || toIP == null || fromIP.length() == 0 || toIP.length() == 0)
        {
            return false;            
        }
        
        int from = checkIP(fromIP);
        int to = checkIP(toIP);
        byte fromArr[] = null;
        byte toArr[] = null;
        
        if(from == -1 || to == -1)
        {
            return false;            
        }
        if(from != to)
        {
            return false;            
        }
        
        
        if(from == 0)
        {
            fromArr = textToNumericFormat4(fromIP);
            toArr = textToNumericFormat4(toIP);
        } else
        {
            fromArr = textToNumericFormat6(fromIP);
            toArr = textToNumericFormat6(toIP);
        }
        
        for(int i = 0; i < fromArr.length; i++)
        {
            int fromInt = 0;
            int toInt = 0;
            
            if(fromArr[i] < 0)
            {
                fromInt = 256 + fromArr[i];                
            }
            else
            {
                fromInt = fromArr[i];                
            }
            
            if(toArr[i] < 0)
            {
                toInt = 256 + toArr[i];                
            }
            else
            {
                toInt = toArr[i];                
            }
            
            if(fromInt > toInt)
            {
                return false;                
            }
            
            if(fromInt < toInt)
            {
                return true;                
            }
        }

        return true;
    }

    public static int compareIP(String firstIP, String secondIP)
    {
        int from = checkIP(firstIP);
        int to = checkIP(secondIP);
        byte fromArr[] = null;
        byte toArr[] = null;
        
        if(from == -1 || to == -1)
        {
            return -2;            
        }
        if(from != to)
        {
            return -2;            
        }
        
        if(from == 0)
        {
            fromArr = textToNumericFormat4(firstIP);
            toArr = textToNumericFormat4(secondIP);
        } else
        {
            fromArr = textToNumericFormat6(firstIP);
            toArr = textToNumericFormat6(secondIP);
        }
        for(int i = 0; i < fromArr.length; i++)
        {
            int fromInt = 0;
            int toInt = 0;
            
            if(fromArr[i] < 0)
            {
                fromInt = 256 + fromArr[i];                
            }
            else
            {
                fromInt = fromArr[i];                
            }
            
            if(toArr[i] < 0)
            {
                toInt = 256 + toArr[i];                
            }
            else
            {
                toInt = toArr[i];                
            }
            
            if(fromInt > toInt)
            {
                return 1;                
            }
            if(fromInt < toInt)
            {
                return -1;                
            }
        }

        return 0;
    }

    public static int checkIP(String addr)
    {
        if(addr == null || addr.length() == 0)
        {
            return -1;            
        }
        
        boolean ipv6Expected = false;
        
        if(addr.charAt(0) == '[')
        {
            if (addr.length() > 2 && addr.charAt(addr.length() - 1) == ']')
            {
                addr = addr.substring(1, addr.length() - 1);
                ipv6Expected = true;
            }
            else
            {
                return -1;
            }            
        }
        
        if(Character.digit(addr.charAt(0), 16) != -1 || addr.charAt(0) == ':')
        {
            byte bAddr[] = null;
            bAddr = textToNumericFormat4(addr);
            if(bAddr.length == 0)
            {
                bAddr = textToNumericFormat6(addr);
                return bAddr.length != 0 ? 1 : -1;
            }
            return !ipv6Expected ? 0 : -1;
        } else
        {
            return -1;
        }
    }

    private static byte[] textToNumericFormat4(String src)
    {
        if(src.length() == 0)
        {
            return new byte[0];            
        }
        
        int octets = 0;
        char ch = '0';
        byte dst[] = new byte[4];
        char srcb[] = src.toCharArray();
        boolean saw_digit = false;
        octets = 0;
        int i = 0;
        int cur = 0;
        
        do
        {
            if(i >= srcb.length)
            {
                break;                
            }
            
            ch = srcb[i++];
            if(Character.isDigit(ch))
            {
                int sum = (dst[cur] & 0xff) * 10 + (Character.digit(ch, 10) & 0xff);
                if(sum > 255)
                {
                    return new byte[0];                    
                }
                
                dst[cur] = (byte)(sum & 0xff);
                if(!saw_digit)
                {
                    if(++octets > 4)
                    {
                        return new byte[0];                        
                    }
                    
                    saw_digit = true;
                }
            } else
            if(ch == '.' && saw_digit)
            {
                if(octets == 4)
                {
                    return new byte[0];                    
                }
                
                cur++;
                dst[cur] = 0;
                saw_digit = false;
            } else
            {
                return new byte[0];
            }
        } while(true);
        
        if(octets < 4)
        {
            return new byte[0];            
        }
        else
        {
            return dst;            
        }
    }

    private static byte[] textToNumericFormat6(String src)
    {
        int colonp;
        boolean saw_xdigit;
        int val;
        byte dst[];
        int j;
label0:
        {
            if(src.length() == 0)
            {
                return new byte[0];                
            }
            
            colonp = 0;
            char ch = '0';
            saw_xdigit = false;
            val = 0;
            char srcb[] = src.toCharArray();
            dst = new byte[16];
            colonp = -1;
            int i = 0;
            j = 0;
            
            if(srcb[i] == ':' && srcb[++i] != ':')
            {
                return new byte[0];                
            }
            
            int curtok = i;
            saw_xdigit = false;
            val = 0;
            
            do
            {
                if(i >= srcb.length)
                {
                    break label0;                    
                }
                
                ch = srcb[i++];
                int chval = Character.digit(ch, 16);
                if(chval != -1)
                {
                    val <<= 4;
                    val |= chval;
                    
                    if(val > 65535)
                    {
                        return new byte[0];                        
                    }
                    
                    saw_xdigit = true;
                    continue;
                }
                
                if(ch != ':')
                {
                    break;                    
                }
                
                curtok = i;
                if(!saw_xdigit)
                {
                    if(colonp != -1)
                    {
                        return new byte[0];                        
                    }
                    
                    colonp = j;
                } else
                {
                    if(i == srcb.length)
                    {
                        return new byte[0];                        
                    }
                    
                    if(j + 2 > 16)
                    {
                        return new byte[0];                        
                    }
                    
                    dst[j++] = (byte)(val >> 8 & 0xff);
                    dst[j++] = (byte)(val & 0xff);
                    saw_xdigit = false;
                    val = 0;
                }
            } while(true);
            if(ch == '.' && j + 4 <= 16)
            {
                byte v4addr[] = textToNumericFormat4(src.substring(curtok));
                
                if(v4addr.length == 0)
                {
                    return new byte[0];                    
                }
                
                for(int k = 0; k < 4; k++)
                {
                    dst[j++] = v4addr[k];                    
                }

                saw_xdigit = false;
            } else
            {
                return new byte[0];
            }
        }
        
        if(saw_xdigit)
        {
            if(j + 2 > 16)
            {
                return new byte[0];                
            }
            
            
            dst[j++] = (byte)(val >> 8 & 0xff);
            dst[j++] = (byte)(val & 0xff);
        }
        
        if(colonp != -1)
        {
            int n = j - colonp;
            
            if(j == 16)
            {
                return new byte[0];                
            }
            
            for(int i = 1; i <= n; i++)
            {
                dst[16 - i] = dst[(colonp + n) - i];
                dst[(colonp + n) - i] = 0;
            }

            j = 16;
        }
        
        if(j != 16)
        {
            return new byte[0];            
        }
        
        byte newdst[] = convertFromIPv4MappedAddress(dst);
        
        if(newdst.length != 0)
        {
            return newdst;            
        }
        else
        {
            return dst;            
        }
    }

    private static byte[] convertFromIPv4MappedAddress(byte addr[])
    {
        if(isIPv4MappedAddress(addr))
        {
            byte newAddr[] = new byte[4];
            System.arraycopy(addr, 12, newAddr, 0, 4);
            return newAddr;
        } else
        {
            return new byte[0];
        }
    }

    private static boolean isIPv4MappedAddress(byte addr[])
    {
        if(addr.length < 16)
        {
            return false;            
        }
        
        return addr[0] == 0 && addr[1] == 0 && addr[2] == 0 && addr[3] == 0 && addr[4] == 0 && addr[5] == 0 && addr[6] == 0 && addr[7] == 0 && addr[8] == 0 && addr[9] == 0 && addr[10] == -1 && addr[11] == -1;
    }

    public static Vector getAllIP(String fromIP, String toIP, int limit)
    {
        if(!isValidRange(fromIP, toIP))
        {
            return new Vector(0);            
        }
        
        Vector rntArr = new Vector(0);
        
        if(limit > 10000)
        {
            return rntArr;            
        }
        
        int from = checkIP(fromIP);
        byte fromArr[] = null;
        byte toArr[] = null;
        int fromIntArr[] = null;
        int toIntArr[] = null;
        
        if(from == 0)
        {
            fromArr = textToNumericFormat4(fromIP);
            toArr = textToNumericFormat4(toIP);
        } else
        {
            fromArr = textToNumericFormat6(fromIP);
            toArr = textToNumericFormat6(toIP);
        }
        
        fromIntArr = convertToIntArr(fromArr);
        toIntArr = convertToIntArr(toArr);
        int handleIntArr[] = null;
        rntArr.add(intArrToIP(fromIntArr));
        
        if(compareIntArr(fromIntArr, toIntArr))
        {
            return rntArr;            
        }
        
        
        for(int i = 0; i < limit + 10; i++)
        {
            if(i > limit)
            {
                return new Vector(0);                
            }
            
            handleIntArr = increaseIP(fromIntArr);
            rntArr.add(intArrToIP(handleIntArr));
            
            if(compareIntArr(handleIntArr, toIntArr))
            {
                return rntArr;                
            }
            
            fromIntArr = handleIntArr;
        }

        return new Vector(0);
    }

    private static boolean compareIntArr(int arr1[], int arr2[])
    {
        if(arr1.length != arr2.length)
        {
            return false;            
        }
        
        for(int i = 0; i < arr1.length; i++)
        {
            if (arr1[i] != arr2[i])
            {
                return false;                
            }            
        }

        return true;
    }

    private static int[] convertToIntArr(byte arr[])
    {
        int intArr[] = new int[arr.length];
        
        for(int i = 0; i < arr.length; i++)
        {
            if (arr[i] < 0)
            {
                intArr[i] = 256 + arr[i];                
            }
            else
            {
                intArr[i] = arr[i];                
            }            
        }

        return intArr;
    }

    private static int[] increaseIP(int arr[])
    {
        int rntArr[] = new int[arr.length];
        
        for(int i = 0; i < arr.length; i++)
        {
            rntArr[i] = arr[i];            
        }

        for(int i = rntArr.length - 1; i > -1; i--)
        {
            if(rntArr[i] < 255)
            {
                rntArr[i]++;
                return rntArr;
            }
            rntArr[i] = 0;
        }

        return rntArr;
    }

    private static String intArrToIP(int arr[])
    {
        if(arr.length == 4)
        {
            return "" + arr[0] + "." + arr[1] + "." + arr[2] + "." + arr[3];            
        }
        
        if(arr.length == 16)
        {
            String strArr[] = new String[16];            
            for(int i = 0; i < 16; i++)
            {
                strArr[i] = Integer.toHexString(arr[i]);                
            }

            return strArr[0] + strArr[1] + ":" + strArr[2] + strArr[3] + ":" + strArr[4] + strArr[5] + ":" + strArr[6] + strArr[7] + ":" + strArr[8] + strArr[9] + ":" + strArr[10] + strArr[11] + ":" + strArr[12] + strArr[13] + ":" + strArr[14] + strArr[15];
        } else
        {
            return "";
        }
    }

    public static String getSiteLocalAddress()
        throws UnknownHostException
    {
        String siteLocalAddress = InetAddress.getLocalHost().getHostAddress();
        String ipToBind = System.getProperty("com.chinaviponline.erp.corepower.localaddress");
        if(ipToBind != null && !ipToBind.equals(""))
        {
            siteLocalAddress = InetAddress.getLocalHost().getHostAddress();
        } else
        {
            boolean v4env = true;
            String ipVersion = System.getProperty("erp.ip.version.purev4");
            v4env = (new Boolean(ipVersion)).booleanValue();
            Enumeration enums = null;
            try
            {
                enums = NetworkInterface.getNetworkInterfaces();
label0:
                do
                {
                    if(!enums.hasMoreElements())
                    {
                        break;                        
                    }
                    
                    NetworkInterface ni = (NetworkInterface)enums.nextElement();
                    Enumeration ias = ni.getInetAddresses();
                    InetAddress ia;
label1:
                    do
                    {
                        do
                        {
                            do
                            {
                                if(!ias.hasMoreElements())
                                {
                                    continue label0;                                    
                                }
                                
                                ia = (InetAddress)ias.nextElement();
                            } while(!ia.isSiteLocalAddress());
                            
                            boolean isIPv4 = isIPv4(ia.getHostAddress());
                            
                            if(!isIPv4)
                            {
                                continue label1;                                
                            }
                        } while(!v4env);
                        
                        siteLocalAddress = InetAddress.getLocalHost().getHostAddress();
                        continue label0;
                    } while(v4env);
                    
                    String siteLocalAddresswithAreaID = ia.getHostAddress();
                    int index = siteLocalAddresswithAreaID.indexOf("%");
                    
                    if(index != -1)
                    {
                        siteLocalAddress = siteLocalAddresswithAreaID
                                .substring(0, index);                        
                    }
                    else
                    {
                        siteLocalAddress = siteLocalAddresswithAreaID;                        
                    }
                    
                } while(true);
            }
            catch(SocketException e)
            {
                e.printStackTrace();
            }
        }
        return siteLocalAddress;
    }

    private static boolean isIPv4(String aIp)
    {
        StringTokenizer st = new StringTokenizer(aIp, ".");
        int i = st.countTokens();
        return i == 4;
    }

    static 
    {
        String localAdd = System.getProperty("com.chinaviponline.erp.corepower.localaddress");
        String bindipAdd = System.getProperty("corepower.bind.ip");
        if(localAdd != null && bindipAdd == null)
        {
            needBinding = true;
            hostAdd = localAdd;
        } else
        if(localAdd != null && bindipAdd != null)
        {
            if(!bindipAdd.equals("0.0.0.0"))
            {
                if(!bindipAdd.equalsIgnoreCase(localAdd))
                {
                    System.err
                            .println("WARN:com.chinaviponline.erp.corepower.localaddress:"
                                    + localAdd
                                    + " is not same as "
                                    + "corepower.bind.ip"
                                    + ":"
                                    + bindipAdd
                                    + ".IT SHOULD BE AN ERROR! Continue with the latter");                    
                }
                
                needBinding = true;
                hostAdd = bindipAdd;
            } else
            {
                needBinding = false;
            }
        } else
        if(localAdd == null && bindipAdd != null)
        {
            System.err.println(" ERROR:Bad settings.For setting the bind ip and return ip of the InetAddress.getLocalhost,set system property com.chinaviponline.erp.corepower.localaddressFor just setting the return ip of the InetAddress.getLocalhost,,set system property com.chinaviponline.erp.corepower.localaddressand set the system propertyCOREPOWER.bind.ip to be0.0.0.0");
            needBinding = false;
        } else
        {
            needBinding = false;
        }
    }

    
}
