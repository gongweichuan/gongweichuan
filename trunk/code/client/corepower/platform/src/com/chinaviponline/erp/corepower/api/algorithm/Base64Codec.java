package com.chinaviponline.erp.corepower.api.algorithm;

/**
 * <p>文件名称：Base64Codec.java</p>
 * <p>文件描述：Base64的编码解码算法</p>
 * <p>版权所有： 版权所有(C)2007-2017</p>
 * <p>公   司： 与龙共舞独立工作室</p>
 * <p>内容摘要： </p>
 * <p>其他说明： </p>
 * <p>完成日期：2007-10-7</p>
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
public class Base64Codec
{
    /**
     * 编码用到的字符
     */
    protected static byte[] _encode_map = {'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '+', '/'};

    /**
     * 解码
     */
    protected static byte[] _decode_map = new byte[128];

    /**
     * 填充解码数组
     */
    static
    {
        for (int i = 0; i < _encode_map.length; i++)
        {
            _decode_map[_encode_map[i]] = (byte) i;
        }
    }

    /**
     * 单例模式
     *
     */
    private Base64Codec()
    {

    }

    /**
     * 
     * <p>功能描述：编码</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2007-10-7</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：
     *  修改日期：
     *  修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param data 编码前的数据流
     * @return 编码后的数据
     */
    public final static byte[] encode(byte[] data)
    {
        if (data == null)
        {
            return new byte[0];
        }

        byte dest[] = new byte[((data.length + 2) / 3) * 4];
        int i = 0, j = 0;
        int data_len = data.length - 2;

        for (i = 0, j = 0; i < data_len; i += 3)
        {
            dest[j++] = _encode_map[(data[i] >>> 2) & 077];
            dest[j++] = _encode_map[(data[i + 1] >> 4) & 017 | (data[i] << 4)
                    & 077];
            dest[j++] = _encode_map[(data[i + 2] >>> 6) & 003
                    | (data[i + 1] << 2) & 077];
            dest[j++] = _encode_map[data[i + 2] & 077];
        }

        if (i < data.length)
        {
            dest[j++] = _encode_map[(data[i] >>> 2) & 077];

            if (i < data.length - 1)
            {
                dest[j++] = _encode_map[(data[i + 1] >>> 2) & 017
                        | (data[i] << 4) & 077];
                dest[j++] = _encode_map[(data[i + 1] << 2) & 077];
            }
            else
            {
                dest[j++] = _encode_map[(data[i] << 4) & 077];
            }
        }

        // 补足 = 对齐
        for (; j < dest.length; j++)
        {
            dest[j] = (byte) '=';
        }

        return dest;
    }

    /**
     * 
     * <p>功能描述：解码</p>
     * <p>创建人：龚为川</p>
     * <p>创建日期：2007-10-7</p>
     * <p>修改记录1：</p>
     * <pre>
     *  修改人：
     *  修改日期：
     *  修改内容：
     * </pre>
     * <p>修改记录2：</p>
     *
     * @param data 加密的数据
     * @return 解密后的数据
     */
    public final static byte[] decode(byte[] data)
    {
        // 非空判断
        if (data == null)
        {
            return new byte[0];
        }

        int ending = data.length;

        // 不能为0
        if (ending < 1)
        {
            return new byte[0];
        }

        // 把=去掉
        while (data[ending - 1] == '=')
        {
            ending--;
        }

        byte dest[] = new byte[ending - data.length / 4];
        for (int i = 0; i < data.length; i++)
        {
            data[i] = _decode_map[data[i]];
        }

        int i = 0, j = 0;
        int dest_len = dest.length - 2;
        for (i = 0, j = 0; j < dest_len; i += 4, j += 3)
        {
            dest[j] = (byte) (((data[i] << 2) & 255) | ((data[i + 1] >>> 4) & 003));
            dest[j + 1] = (byte) (((data[i + 1] << 4) & 255) | ((data[i + 2] >>> 2) & 017));
            dest[j + 2] = (byte) (((data[i + 2] << 6) & 255) | (data[i + 3] & 077));
        }

        if (j < dest.length)
        {
            dest[j] = (byte) (((data[i] << 2) & 255) | ((data[i + 1] >>> 4) & 003));
        }

        j++;

        if (j < data.length)
        {
            dest[j] = (byte) (((data[i + 1] << 4) & 255) | ((data[i + 2] >>> 2) & 017));
        }

        return dest;
    }
}
