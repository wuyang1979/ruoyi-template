package com.ruoyi.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * Created by yj on 2022/10/20 0020 14:50.
 */
public class HypyUtil {
    /**
     * 获取字符串的全拼音(全小写)
     *
     * @param src
     * @return
     */
    public static String getPinYin(String src) {
        String t4 = "";
        if (src != null) {
            char[] t1 = src.toCharArray();
            String[] t2 = new String[t1.length];
            // 设置汉字拼音输出的格式
            HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
            t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            t3.setVCharType(HanyuPinyinVCharType.WITH_V);
            int t0 = t1.length;
            try {
                for (int i = 0; i < t0; i++) {
                    // 判断是否为汉字字符
                    if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                        // 将汉字的几种全拼都存到t2数组中
                        t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                        // 取出该汉字全拼的第一种读音并连接到字符串t4后
                        t4 += t2[0];
                    } else {
                        // 如果不是汉字字符，直接取出字符并连接到字符串t4后
                        t4 += Character.toString(t1[i]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (t4 != null && !"".equals(t4)) {
                t4 = t4.toLowerCase();
            }
        }
        return t4.toLowerCase();
    }

}
