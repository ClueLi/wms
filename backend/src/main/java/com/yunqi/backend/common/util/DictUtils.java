package com.yunqi.backend.common.util;

import com.yunqi.backend.common.constant.CacheConstants;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.DictError;
import com.yunqi.backend.model.entity.DictItem;
import com.yunqi.backend.model.entity.DictType;
import com.yunqi.backend.service.DictItemService;
import com.yunqi.backend.service.DictTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jca.context.SpringContextResourceAdapter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liyunqi
 */
public class DictUtils {
    private static final DictTypeService dictTypeService = SpringUtils.getBean(DictTypeService.class);
    private static final DictItemService dictItemService = SpringUtils.getBean(DictItemService.class);
    private static final RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    /**
     * 根据字典的typeCode直接设置字典项的缓存
     * @param typeCode
     */
    public static void setDictItemCache(String typeCode, List<DictItem> itemList) {
        redisCache.setCacheObject(CacheConstants.SYS_DICT_CODE+typeCode, itemList);
    }

    /**
     * 根据字典的typeCode直接获取字典项的缓存
     * @param typeCode
     * @return
     */
    public static List<DictItem> getDictItemCache(String typeCode) {
        return redisCache.getCacheObject(CacheConstants.SYS_DICT_CODE+typeCode);
    }

    /**
     * 刷新缓存
     */
    public static void refreshCache() {
        // 清除所有缓存
        Collection<String> keys = redisCache.keys(CacheConstants.SYS_DICT_CODE + "*");
        redisCache.deleteObject(keys);

        // 设置所有新的缓存
        List<DictType> dictTypeAll = dictTypeService.getDictTypeAll();
        if (dictTypeAll != null && dictTypeAll.size() > 0 ) {
            for (DictType dictType : dictTypeAll) {
                List<DictItem> itemList = dictItemService.getDictItemByCode(dictType.getCode());
                setDictItemCache(dictType.getCode(), itemList);
            }
        }
    }

    /**
     * 刷新缓存
     */
    public static void refreshCache(String code) {
        // 清除所有缓存
        redisCache.deleteObject(CacheConstants.SYS_DICT_CODE+code);

        // 设置所有新的缓存
        List<DictItem> dictItemList = dictItemService.getDictItemByCode(code);
        setDictItemCache(code, dictItemList);
    }

    /**
     * 根据值和编码获取标签
     * @param value
     * @param typeCode
     */
    public static String getLabelByValue(String value, String typeCode) {
        List<DictItem> dictItemList = getDictItemCache(typeCode).stream().filter(dictItem -> dictItem.getValue().equals(value)).collect(Collectors.toList());
        if (dictItemList.size() == 0) {
            throw new BizException(DictError.FIND_ITEM_ERROR);
        }
        return dictItemList.get(0).getLabel();
    }

    /**
     * 根据值和编码获取标签
     * @param label
     * @param typeCode
     */
    public static String getValueByLabel(String label, String typeCode) {
        List<DictItem> dictItemList = getDictItemCache(typeCode).stream().filter(dictItem -> dictItem.getLabel().equals(label)).collect(Collectors.toList());
        if (dictItemList.size() == 0) {
            throw new BizException(DictError.FIND_ITEM_ERROR);
        }
        return dictItemList.get(0).getValue();
    }
}
