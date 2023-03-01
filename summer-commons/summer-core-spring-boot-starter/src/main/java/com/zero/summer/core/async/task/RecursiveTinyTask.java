package com.zero.summer.core.async.task;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;

/**
 * 自定义并行任务拆分实现 基于#{@link RecursiveTask} 重写#{@link RecursiveTask#complete(Object)}方法
 * 将一个任务按组拆分成多个任务去执行,将由ForkJoinPool执行。
 *
 * @author Zero.
 * @date 2022/4/19 20:25
 */
@AllArgsConstructor
@NoArgsConstructor
public class RecursiveTinyTask<T, R> extends RecursiveTask<Collection<R>> {

    /**
     * 任务参数列表,拆分的依据为此列表。
     */
    private List<T> data;

    /**
     * 参数列表起始位置指针
     * 默认为0,随着任务拆分而移动指针
     */
    private int start;

    /**
     * 参数列表末尾位置指针
     * 默认为 参数列表长度,随着任务拆分而移动指针
     */
    private int end;

    /**
     * 分组数量
     */
    private int limit;

    /**
     * 具体的业务执行函数
     */
    private Function<T, R> process;


    /**
     * 任务分组拆分、合并实现
     */
    @Override
    protected Collection<R> compute() {
        //判断该任务是否需要拆分
        if ((end - start) <= limit) {
            //Do 最终的任务执行实现
            Collection<R> result = new ArrayList<>();
            for (int i = start; i < end; i++) {
                result.add(process.apply(data.get(i)));
            }
            return result;
        }else {
            //拆分任务
            int mid = (start + end) / 2;
            RecursiveTinyTask<T, R> left = new RecursiveTinyTask<>(data, start, mid, limit, process);
            RecursiveTinyTask<T, R> right = new RecursiveTinyTask<>(data, mid, end, limit, process);
            //递归执行
            left.fork();
            right.fork();
            //等待任务结束后,合并结果
            Collection<R> rightResult = right.join();
            Collection<R> leftResult = left.join();
            rightResult.addAll(leftResult);
            return rightResult;
        }
    }

    /**
     * 任务构建
     * @param data      参数列表
     * @param start     分组头指针
     * @param end       分组尾指针
     * @param limit     分组数量
     * @param process   执行函数
     * @return          结果集
     */
    public static <T,R> RecursiveTinyTask<T,R> of (List<T> data, int start, int end, int limit, Function<T,R> process){
        return new RecursiveTinyTask<>(data,start,end,limit,process);
    }
    public RecursiveTinyTask<T,R>  setData(List<T> data) {
        this.data = data;
        return this;
    }
    public RecursiveTinyTask<T,R>  setStart(int start) {
        this.start = start;
        return this;
    }
    public RecursiveTinyTask<T,R>  setEnd(int end) {
        this.end = end;
        return this;
    }
    public RecursiveTinyTask<T,R>  setLimit(int limit) {
        this.limit = limit;
        return this;
    }
    public RecursiveTinyTask<T,R>  setProcess(Function<T, R> process) {
        this.process = process;
        return this;
    }
}
