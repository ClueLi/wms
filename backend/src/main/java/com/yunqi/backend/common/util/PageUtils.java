package com.yunqi.backend.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.base.PageRequest;
import com.yunqi.backend.common.result.PageResult;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.SystemError;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页查询所使用到的数据
 * @author liyunqi
 */
public class PageUtils {

    /**
     * 获取Page,分页查询
     * @return
     * @param <T>
     */
    public static <T> Page<T> getPage() {
        PageRequest pageRequest = getPageRequest();
        return new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
    }

    /**
     * 获取pageRequest
     * @return
     */
    private static PageRequest getPageRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String pageNum = request.getParameter("pageNum");
            String pageSize = request.getParameter("pageSize");
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPageSize(Integer.parseInt(pageSize));
            pageRequest.setPageNum(Integer.parseInt(pageNum));
            return pageRequest;
        } catch (Exception e) {
            throw new BizException(SystemError.PARSE_PAGE_ERROR);
        }
    }

    /**
     *  封装分页查询结果
     * @param page
     * @return
     */
    public static PageResult convertPageResult(Page page) {
        PageResult pageResult = new PageResult();
        pageResult.setPageNum((int) page.getCurrent());
        pageResult.setPageSize((int) page.getSize());
        pageResult.setTotal((int) page.getTotal());
        pageResult.setRows(page.getRecords());
        return pageResult;
    }

    /**
     * 根据page信息设置分页查询结果
     * @return
     */
    public static void handlePageList(List list, Page page) {
        int beginIndex = (int) ((page.getCurrent() - 1) * page.getSize());
        if (beginIndex > list.size()) {
            beginIndex = list.size();
        }
        int endIndex = (int)page.getCurrent()*(int)page.getSize();
        if (endIndex > list.size()) {
            endIndex = list.size();
        }
        page.setRecords(list.subList(beginIndex, endIndex));
        page.setTotal(list.size());
    }

}
