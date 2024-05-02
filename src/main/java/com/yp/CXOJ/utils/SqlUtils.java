package com.yp.CXOJ.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * SQL 工具
 *
 */
public class SqlUtils {

    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField 数据库表中字段名称
     * @return
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        //防止恶意添加一些字符如出现where 1=1这种的sql注入(还有其他情况)
        return !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }
}
