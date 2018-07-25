package android_serialport_api;

import com.vigorchip.puliblib.utils.Logutil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/15.
 */

public class Commands {
    /**
     * 得到处理后的数据 传入功能码和参数
     */
    public static char[] getRealCode(ArrayList<Character> codes) {
        if (codes == null || codes.size() == 0) {
            return null;
        }
        for (int i = 0; i < codes.size(); i++) {
            char code = codes.get(i);
            if (code == 0xFF) {
                codes.set(i, (char) 0xFD);
                codes.add(i + 1, (char) 0x02);
            } else if (code == 0xFE) {
                codes.set(i, (char) 0xFD);
                codes.add(i + 1, (char) 0x01);
            } else if (code == 0xFD) {
                codes.set(i, (char) 0xFD);
                codes.add(i + 1, (char) 0x00);
            }
        }
        byte[] code_bytes = new byte[codes.size()];
        for (int i = 0; i < codes.size(); i++) {
            char code_int = codes.get(i);
            code_bytes[i] = (byte) code_int;
        }
        int checkCode = calCrcBytable(code_bytes, code_bytes.length);
        int hei_checkcode = checkCode / 256;
        int low_checkcode = checkCode % 256;
        if (low_checkcode == 0xFF) {
            codes.add((char) 0xFD);
            codes.add((char) 0x02);
        } else if (low_checkcode == 0xFE) {
            codes.add((char) 0xFD);
            codes.add((char) 0x01);
        } else if (low_checkcode == 0xFD) {
            codes.add((char) 0xFD);
            codes.add((char) 0x00);
        } else {
            codes.add((char) low_checkcode);
        }
        if (hei_checkcode == 0xFF) {
            codes.add((char) 0xFD);
            codes.add((char) 0x02);
        } else if (hei_checkcode == 0xFE) {
            codes.add((char) 0xFD);
            codes.add((char) 0x01);
        } else if (hei_checkcode == 0xFD) {
            codes.add((char) 0xFD);
            codes.add((char) 0x00);
        } else {
            codes.add((char) hei_checkcode);
        }
        codes.add(0, (char) 0xFE);
        codes.add((char) 0xFF);
        char[] result_codes = new char[codes.size()];
        for (int i = 0; i < result_codes.length; i++) {
            result_codes[i] = codes.get(i);
        }
        return result_codes;
    }

    /**
     * 根据返回数据判断检验码是否正确
     *
     * @param backCodes
     * @return
     */
    public static boolean isCheckOk(byte[] backCodes, int size) {
        if (backCodes == null && backCodes.length == 0) {
            return false;
        }
        if (backCodes.length < 5) {
            return false;
        }
        ArrayList<Byte> newBytes = new ArrayList<Byte>();
        for (int i = 0; i < backCodes.length; i++) {
            if (backCodes[i] == 0xFD && backCodes[i + 1] == 0x00) {
                newBytes.add((byte) 0xFD);
                i++;
            } else if (backCodes[i] == 0xFD && backCodes[i + 1] == 0x01) {
                newBytes.add((byte) 0xFE);
                i++;
            } else if (backCodes[i] == 0xFD && backCodes[i + 1] == 0x02) {
                newBytes.add((byte) 0xFF);
                i++;
            } else {
                newBytes.add(backCodes[i]);
            }
        }
        ArrayList<Byte> bytes = new ArrayList<Byte>();
        for (int i = 0; i < newBytes.size(); i++) {
            bytes.add(newBytes.get(i));
            if (newBytes.get(i) == 0xFF) {
                break;
            }
        }
        byte[] new_bytes = new byte[bytes.size() - 2];
        for (int i = 0; i < new_bytes.length; i++) {
            new_bytes[i] = bytes.get(i + 1);
        }//掐头去尾
        char hei_checkcode = (char) (new_bytes[new_bytes.length - 1] & 0xFF);
        char low_checkcode = (char) (new_bytes[new_bytes.length - 2] & 0xFF);
        int checkcode = hei_checkcode * 256 + low_checkcode;
        byte[] checkcode_bytes = new byte[new_bytes.length - 2];
        for (int i = 0; i < new_bytes.length - 2; i++) {
            checkcode_bytes[i] = new_bytes[i];
        }
        if (calCrcBytable(checkcode_bytes, checkcode_bytes.length) == checkcode) {
            Logutil.e("校验成功");
            return true;
        }
        return false;
    }


    /**
     * 配合校验数组
     */
    public static char[] crcNibbleTb1 = new char[]{0x0000, 0x1081, 0x2102,
            0x3183, 0x4204, 0x5285, 0x6306, 0x7387, 0x8408, 0x9489, 0xa50a,
            0xb58b, 0xc60c, 0xd68d, 0xe70e, 0xf78f};

    /**
     * 得到校验码
     *
     * @param pSrcBuf
     * @param len
     * @return
     */
    public static char calCrcBytable1(byte[] pSrcBuf, int len) {
        byte crcLower = 0;
        byte i = 0;
        char crcReg;
        crcReg = 0xFFFF;
        i = 0;

        while (len-- != 0) {
            crcLower = (byte) (crcReg & 0x0F);
            crcReg >>= 4;
            crcReg ^= crcNibbleTb1[crcLower ^ (pSrcBuf[i] & 0x0F)];
            crcLower = (byte) (crcReg & 0x0F);
            crcReg >>= 4;
            crcReg ^= crcNibbleTb1[crcLower ^ (pSrcBuf[i] >> 4)];
            i++;
        }
        return crcReg;

    }

    public static char calCrcBytable(byte[] pSrcBuf, int len) {
        char crcReg;
        byte i = 0, j = 0;
        crcReg = 0xFFFF;
        while (len-- != 0) {
            byte temp;
            temp = pSrcBuf[j];
            for (i = 0x01; i != 0; i = (byte) (i << 1)) {
                if ((crcReg & 0x0001) != 0) {
                    crcReg >>= 1;
                    crcReg ^= 0x8408;
                } else {
                    crcReg >>= 1;
                }
                if ((temp & i) != 0) {
                    crcReg ^= 0x8408;
                }
            }
            j++;
        }
        return crcReg;
    }

    public static byte[] charsToBytes(char[] chars) {
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        return bytes;
    }

    public static void LogChar(String tag, char[] chars) {
        StringBuffer sb = new StringBuffer();
        if (chars != null) {
            for (int i = 0; i < chars.length; i++) {
                sb.append(":" + Integer.toHexString(chars[i]));
            }
        }
        Logutil.e(tag, sb.toString());
    }
}
