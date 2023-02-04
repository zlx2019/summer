package com.zero.summer.db.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * MP 全局统一配置
 * @author Zero.
 * @date 2022/1/19 12:24 下午
 *
 * 1. 导入自动填充的配置类
 * 2. 扫描每个服务下的Mapper接口
 * 3. 配置分页、乐观锁等配置
 */
@Import(ModelAutoFillHandler.class)
@MapperScan({"com.zero.summer.*.mapper"})
public class DefaultMybatisPlusConfig {

    /**
     * 配置MP的各个插件拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        //乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        //防止全表删除或者修改插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }
}
