package com.yunqi.backend.controller;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.PageResult;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.DictUtils;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.model.dto.DictItemDTO;
import com.yunqi.backend.model.entity.DictItem;
import com.yunqi.backend.service.DictItemService;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liyunqi
 */
@RestController
@RequestMapping("/system/dict/item")
public class DictItemController {

    @Resource
    DictItemService dictItemService;

    /**
     *  获取字典项分页列表
     * @param dictItem
     * @return
     */
    @PreAuthorize("@sps.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public Result<PageResult> getDictItemPage(DictItemDTO dictItemDTO) {
        Page<DictItem> dictItemPage = dictItemService.getDictItemPage(dictItemDTO);
        return Result.success(PageUtils.convertPageResult(dictItemPage));
    }

    /**
     * 根据字典项id获取字典项
     * @param itemId
     * @return
     */
    @PreAuthorize("@sps.hasPermi('system:dict:query')")
    @GetMapping("/{itemId}")
    public Result getDictItemByItemId(@PathVariable Long itemId) {
        DictItem dictItem = dictItemService.getById(itemId);
        return Result.success(dictItem);
    }

    /**
     * 保存字典项
     * @param dictItemDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('system:dict:add')")
    @PostMapping
    public Result save(@Validated @RequestBody DictItemDTO dictItemDTO) {
        dictItemService.saveDictItem(dictItemDTO);
        DictUtils.refreshCache(dictItemDTO.getTypeCode());
        return Result.success();
    }

    /**
     * 更新字典项
     * @param dictItemDTO
     * @return
     */
    @PreAuthorize("@sps.hasPermi('system:dict:edit')")
    @PutMapping
    public Result updateDictItem(@Validated @RequestBody DictItemDTO dictItemDTO) {
        dictItemService.updateDictItem(dictItemDTO);
        DictUtils.refreshCache(dictItemDTO.getTypeCode());
        return Result.success();
    }

    /**
     * 删除字典项
     * @param dictItemIds
     * @return
     */
    @PreAuthorize("@sps.hasPermi('system:dict:delete')")
    @DeleteMapping("/{dictItemIds}")
    public Result delete(@PathVariable List<Long> dictItemIds) {
        dictItemService.deleteDictItem(dictItemIds);
        DictUtils.refreshCache();
        return Result.success();
    }

    /**
     * 根据字典code，获取字典项
     * @param code
     * @return
     */
    @GetMapping("/code/{code}")
    public Result getDictItemByCode(@PathVariable String code) {
        List<DictItem> dictItemCache = DictUtils.getDictItemCache(code);
        if (dictItemCache != null) {
            return Result.success(dictItemCache);
        }
        List<DictItem> dictItemByCode = dictItemService.getDictItemByCode(code);
        if (dictItemByCode != null && dictItemByCode.size() > 0) {
            DictUtils.setDictItemCache(code, dictItemByCode);
        } else {
            return Result.fail("查找字典项出错");
        }

        return Result.success(dictItemByCode);
    }

}
