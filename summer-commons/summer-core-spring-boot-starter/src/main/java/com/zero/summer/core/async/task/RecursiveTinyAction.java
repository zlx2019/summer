package com.zero.summer.core.async.task;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

/**
 * 自定义并行任务类 (无返回值)实现 @{@link java.util.concurrent.RecursiveAction}
 * 将一个任务按组拆分成多个任务去执行,将由ForkJoinPool执行。
 *
 * @author Zero.
 * @date 2022/4/21 7:06 下午
 */
@AllArgsConstructor
@NoArgsConstructor
public class RecursiveTinyAction<T> extends RecursiveAction {

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
    private Consumer<T> process;

    /**
     * 任务拆分
     */
    @Override
    protected void compute() {
        //判断该任务是否需要拆分
        if ((end - start) <= limit) {
            //Do 最终的任务执行实现
            for (int i = start; i < end; i++) {
                process.accept(data.get(i));
            }
        }else {
            //拆分任务,只需要拆分,无需合并
            int mid = (start + end) / 2;
            RecursiveTinyAction.of(data, start, mid, limit, process).fork();
            RecursiveTinyAction.of(data, mid, end, limit, process).fork();
        }
    }


    public static <T> RecursiveTinyAction<T> of (List<T> data,int start,int end,int limit,Consumer<T> process){
        return new RecursiveTinyAction<>(data,start,end,limit,process);
    }

    public RecursiveTinyAction<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

    public RecursiveTinyAction<T> setStart(int start) {
        this.start = start;
        return this;
    }

    public RecursiveTinyAction<T> setEnd(int end) {
        this.end = end;
        return this;
    }

    public RecursiveTinyAction<T> setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public RecursiveTinyAction<T> setProcess(Consumer<T> process) {
        this.process = process;
        return this;
    }

}
