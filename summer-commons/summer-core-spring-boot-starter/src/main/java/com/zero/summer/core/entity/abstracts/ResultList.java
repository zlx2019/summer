package com.zero.summer.core.entity.abstracts;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zero.summer.core.constant.ResultConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 序列数据 统一响应对象
 *
 * @author Zero.
 * @date 2022/1/23 11:52 上午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResultList<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应体列表
     */
    private List<T> list;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应码  0:正常  1:异常
     */
    private Integer code;
    /**
     * 数据总量
     */
    private long total;
    /**
     * 页容量
     */
    private long pageSize;
    /**
     * 总页数
     */
    private long pages;
    /**
     * 当前页
     */
    private long curPage;
    /**
     * 当前页数据数量
     */
    private int curPageSize;

    /**
     * 是否可以下一页
     */
    private boolean nextPage = Boolean.TRUE;
    /**
     * 是否可以上一页
     */
    private boolean upPage = Boolean.TRUE;

    /**
     * 假分页
     * @param list    总数据集
     * @param curPage 页码
     * @param pageSize   页容量
     */
    public ResultList(List<T> list, int curPage, int pageSize) {
        List<T> dataList = list.stream().skip((long) pageSize * (curPage - 1)).limit(pageSize).collect(Collectors.toList());
        this.setTotal(list.size());
        this.setPages((list.size() - 1) / pageSize + 1);
        this.setCurPage(curPage);
        this.setPageSize(pageSize);
        this.setList(dataList);
        this.setCurPageSize(dataList.size());
        this.setNextPage(this.getCurPage() != this.getPages());
        this.setUpPage(this.getCurPage() != 1);
    }

    /**
     * 将{@link IPage} 封装为{@link ResultList}
     * @param page  MP分页结果实体
     * @return      自定义分页结果实体
     */
    public static <T> ResultList<T> of(IPage<T> page) {
        return new ResultList<T>()
                .setList(page.getRecords())
                .setTotal(page.getTotal())
                .setPages(page.getPages())
                .setCurPage(page.getCurrent())
                .setPageSize(page.getSize())
                .setCurPageSize(page.getRecords().size())
                .setCode(ResultConst.SUCCESS_CODE)
                .setMessage(ResultConst.SUCCESS)
                .setNextPage(page.getCurrent() != page.getPages())
                .setUpPage(page.getCurrent() != 1);
    }

    public static <T,V> ResultList<V> of(IPage<T> page, List<V> voList) {
        return new ResultList<V>()
                .setList(voList)
                .setTotal(page.getTotal())
                .setPages(page.getPages())
                .setCurPage(page.getCurrent())
                .setPageSize(page.getSize())
                .setCurPageSize(page.getRecords().size())
                .setCode(ResultConst.SUCCESS_CODE)
                .setMessage(ResultConst.SUCCESS)
                .setNextPage(page.getCurrent() != page.getPages())
                .setUpPage(page.getCurrent() != 1);
    }
}
