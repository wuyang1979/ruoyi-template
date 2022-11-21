package com.ruoyi.common.utils.beanCompare;

import com.ruoyi.common.annotation.FieldAlias;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author yuanjie
 * @date 2022/10/11
 */
public class BeanCompareUtils {
    private static final String INCLUDE = "INCLUDE";
    private static final String EXCLUDE = "EXCLUDE";
    private static final String FILTER_TYPE = "FILTER_TYPE";
    private static final String FILTER_ARRAY = "FILTER_ARRAY";

    // 存放过滤类型及过滤字段数组
    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    /**
     * bean比较
     *
     * @param oldBean
     * @param newBean
     * @return
     */
    public static BeanDiff compare(Object oldBean, Object newBean) {
        BeanDiff beanDiff = new BeanDiff();

        Class oldClass = oldBean.getClass();
        Class newClass = newBean.getClass();

        if (oldClass.equals(newClass)) {
            List<Field> fieldList = new ArrayList<>();
            fieldList = getCompareFieldList(fieldList, newClass);

            Map<String, Object> map = threadLocal.get();

            boolean needInclude = false;
            boolean needExclude = false;
            boolean hasArray = false;
            String[] fieldArray = null;

            if (map != null) {
                fieldArray = (String[]) map.get(FILTER_ARRAY);
                String type = (String) map.get(FILTER_TYPE);

                if (fieldArray != null && fieldArray.length > 0) {
                    // 数组排序
                    Arrays.sort(fieldArray);
                    hasArray = true;

                    if (INCLUDE.equals(type)) {
                        needInclude = true;
                    } else if (EXCLUDE.equals(type)) {
                        needExclude = true;
                    }
                }
            }

            for (int i = 0; i < fieldList.size(); i++) {
                Field field = fieldList.get(i);
                field.setAccessible(true);
                FieldAlias alias = field.getAnnotation(FieldAlias.class);

                try {
                    Object oldValue = field.get(oldBean);
                    Object newValue = field.get(newBean);

                    if (hasArray) {
                        // 二分法查找该字段是否被排除或包含
                        int idx = Arrays.binarySearch(fieldArray, field.getName());

                        // 该字段被指定排除或没有指定包含
                        if ((needExclude && idx > -1) || (needInclude && idx < 0)) {
                            continue;
                        }
                    }

                    if (nullableNotEquals(oldValue, newValue)) {
                        FieldDiff fieldDiff = new FieldDiff(field.getName(), alias.value(), oldValue, newValue);

                        // 打印
                        System.out.println(fieldDiff.toString());

                        beanDiff.addFieldDiff(fieldDiff);
                    }

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return beanDiff;
    }

    /**
     * bean比较
     *
     * @param oldBean
     * @param newBean
     * @param includeFieldArray 需要包含的字段
     * @return
     */
    public static BeanDiff compareInclude(Object oldBean, Object newBean, String[] includeFieldArray) {
        Map<String, Object> map = new HashMap<>();
        map.put(FILTER_TYPE, INCLUDE);
        map.put(FILTER_ARRAY, includeFieldArray);
        threadLocal.set(map);

        return compare(oldBean, newBean);
    }

    /**
     * bean比较
     *
     * @param oldBean
     * @param newBean
     * @param excludeFieldArray 需要排除的字段
     * @return
     */
    public static BeanDiff compareExclude(Object oldBean, Object newBean, String[] excludeFieldArray) {
        Map<String, Object> map = new HashMap<>();
        map.put(FILTER_TYPE, EXCLUDE);
        map.put(FILTER_ARRAY, excludeFieldArray);
        threadLocal.set(map);

        return compare(oldBean, newBean);
    }


    /**
     * 获取需要比较的字段list
     *
     * @param fieldList
     * @param clazz
     * @return
     */
    private static List<Field> getCompareFieldList(List<Field> fieldList, Class clazz) {
        Field[] fieldArray = clazz.getDeclaredFields();

        List<Field> list = Arrays.asList(fieldArray);

        for (int i = 0; i < list.size(); i++) {
            Field field = list.get(i);
            FieldAlias alias = field.getAnnotation(FieldAlias.class);
            if (alias != null) {
                fieldList.add(field);
            }
        }

        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            getCompareFieldList(fieldList, superClass);
        }
        return fieldList;
    }


    /**
     * 比较值是否不相等
     *
     * @param oldValue
     * @param newValue
     * @return
     */
    private static boolean nullableNotEquals(Object oldValue, Object newValue) {

        if (oldValue == null && newValue == null) {
            return false;
        }

        if (oldValue != null && oldValue.equals(newValue)) {
            return false;
        }

        if (("".equals(oldValue) && newValue == null) || ("".equals(newValue) && oldValue == null)) {
            return false;
        }

        return true;
    }
}
