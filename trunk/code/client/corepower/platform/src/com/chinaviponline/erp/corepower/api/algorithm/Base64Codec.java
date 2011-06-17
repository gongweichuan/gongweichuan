package com.chinaviponline.erp.corepower.api.algorithm;

/**
 * <p>�ļ����ƣ�Base64Codec.java</p>
 * <p>�ļ�������Base64�ı�������㷨</p>
 * <p>��Ȩ���У� ��Ȩ����(C)2007-2017</p>
 * <p>��   ˾�� �����������������</p>
 * <p>����ժҪ�� </p>
 * <p>����˵���� </p>
 * <p>������ڣ�2007-10-7</p>
 * <p>�޸ļ�¼1��</p>
 * <pre>
 *  �޸����ڣ�
 *  �汾�ţ�
 *  �޸��ˣ�
 *  �޸����ݣ�
 * </pre>
 * <p>�޸ļ�¼2��</p>
 *
 * @version 1.0
 * @author ��Ϊ��
 * @email  gongweichuan(AT)gmail.com
 */
public class Base64Codec
{
    /**
     * �����õ����ַ�
     */
    protected static byte[] _encode_map = {'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '+', '/'};

    /**
     * ����
     */
    protected static byte[] _decode_map = new byte[128];

    /**
     * ����������
     */
    static
    {
        for (int i = 0; i < _encode_map.length; i++)
        {
            _decode_map[_encode_map[i]] = (byte) i;
        }
    }

    /**
     * ����ģʽ
     *
     */
    private Base64Codec()
    {

    }

    /**
     * 
     * <p>��������������</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2007-10-7</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�
     *  �޸����ڣ�
     *  �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @param data ����ǰ��������
     * @return ����������
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

        // ���� = ����
        for (; j < dest.length; j++)
        {
            dest[j] = (byte) '=';
        }

        return dest;
    }

    /**
     * 
     * <p>��������������</p>
     * <p>�����ˣ���Ϊ��</p>
     * <p>�������ڣ�2007-10-7</p>
     * <p>�޸ļ�¼1��</p>
     * <pre>
     *  �޸��ˣ�
     *  �޸����ڣ�
     *  �޸����ݣ�
     * </pre>
     * <p>�޸ļ�¼2��</p>
     *
     * @param data ���ܵ�����
     * @return ���ܺ������
     */
    public final static byte[] decode(byte[] data)
    {
        // �ǿ��ж�
        if (data == null)
        {
            return new byte[0];
        }

        int ending = data.length;

        // ����Ϊ0
        if (ending < 1)
        {
            return new byte[0];
        }

        // ��=ȥ��
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
