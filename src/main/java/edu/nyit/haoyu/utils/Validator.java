package edu.nyit.haoyu.utils;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Jerry Fang
 * @Date: 2023/07/08/6:03
 * @Description:
 */
public class Validator {

    public Validator() {
        throw new AssertionError(this.getClass().getName() + "not can be instances for you!");
    }

    public static boolean isNullOrEmpty(Object value) {
        if (null == value) {
            return true;
        } else if (value instanceof CharSequence) {
            return StringUtils.isBlank((CharSequence) value);
        } else {
            return isCollectionsSupportType(value) ? CollectionUtils.sizeIsEmpty(value) : false;
        }
    }

    private static boolean isCollectionsSupportType(Object value) {
        boolean isCollectionOrMap = value instanceof Collection || value instanceof Map;
        boolean isEnumerationOrIterator = value instanceof Enumeration || value instanceof Iterator;
        return isCollectionOrMap || isEnumerationOrIterator || value.getClass().isArray();
    }
}