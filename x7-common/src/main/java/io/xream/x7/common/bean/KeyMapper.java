package io.xream.x7.common.bean;

import io.xream.x7.common.util.BeanUtil;
import io.xream.x7.common.util.BeanUtilX;

public interface KeyMapper {

    default String mapping(String key, CriteriaCondition criteria) {


        if (key.contains(SqlScript.DOT)) {

            String[] arr = key.split("\\.");
            String alia = arr[0];
            String property = arr[1];

            String clzName = BeanUtilX.getClzName(alia, criteria);

            Parsed parsed = Parser.get(clzName);
            if (parsed == null)
                throw new RuntimeException("Entity Bean Not Exist: " + BeanUtil.getByFirstUpper(key));

            String value = parsed.getTableName(alia) + SqlScript.DOT + parsed.getMapper(property);

            return value;
        }

        if (criteria instanceof Criteria.ResultMappedCriteria) {
            Parsed parsed = Parser.get(key);
            if (parsed != null) {
                return parsed.getTableName();
            }
        }

        Parsed parsed = criteria.getParsed();
        if (key.equals(BeanUtilX.getByFirstLower(parsed.getClz().getSimpleName())))
            return parsed.getTableName();
        String value = parsed.getMapper(key);
        if (value == null)
            return key;
        return value;

    }
}
