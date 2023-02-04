package com.summer.core.spring;

import com.summer.core.utils.text.TextUtil;
import com.summer.core.utils.valid.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Spring 扩展工具类
 * 目前支持如下功能:
 * 1. 在非Spring环境下获取Bean实例
 * 2. 动态注入、销毁、读取Bean
 * 3. 统一发布Spring事件
 *
 * @author Zero9501.
 * @date 2023/2/2 7:58 PM
 */
@Slf4j
public class SpringContextUtil implements ApplicationContextAware, BeanFactoryPostProcessor {

    /**
     * Spring的上下文,通过实现ApplicationContextAware接口获取到该上下文。
     * 通过该上下文可以动态获取Bean
     */
    private static ApplicationContext applicationContext;

    /**
     * 一个Bean中被"@PostConstruct"注解的方法,表示实例化完该Bean后马上执行该方法,然后再继续初始化其他的Bean.
     * 在该方法执行时,由于ApplicationContext还未加载完成,导致空指针异常.
     * 因此实现BeanFactoryPostProcessor接口,通过ConfigurableListableBeanFactory实现对bean的操作更为保险.
     */
    private static ConfigurableListableBeanFactory beanFactory;


    /**
     * DefaultListableBeanFactory 实现于BeanDefinitionRegistry具有Bean的注册与销毁等入口
     */
    private static DefaultListableBeanFactory defaultBeanFactory;

    /**
     * 获取ApplicationContext
     * @return org.springframework.context.ApplicationContext
     */
    private static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     * 获取 beanFactory
     * @return org.springframework.beans.factory.ListableBeanFactory
     */
    public static ListableBeanFactory getBeanFactory() {
        return null == beanFactory ? applicationContext : beanFactory;
    }

    /**
     * 初始化 applicationContext
     * @param applicationContext Spring 上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
        initDefaultBeanFactory();
    }

    /**
     * 初始化 beanFactory
     * @param beanFactory Spring Bean工厂
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringContextUtil.beanFactory = beanFactory;
    }

    /**
     * 初始化 defaultBeanFactory
     *
     */
    private static void initDefaultBeanFactory(){
        defaultBeanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext)getApplicationContext()).getBeanFactory();
    }

    /**
     * 根据Class类型获取对应的Bean实例
     *
     * @param clazz Bean Class
     * @param <T>   Bean 原型
     * @return      Bean 实例
     */
    public static <T> T getBean(Class<T> clazz) {
        return getBeanFactory().getBean(clazz);
    }


    /**
     * 根据Class类型获取 Bean实例,不存在Bean实例就预先注入
     *
     * @param clazz Bean Class
     * @param <T>   Bean 原型
     * @return      Bean 实例
     */
    public static <T> T getBeanOfNullInject(Class<T> clazz){
        if (!containsBean(clazz)){
            injectBean(clazz);
        }
        return getBean(clazz);
    }

    /**
     * 根据Name 获取Bean
     *
     * @param beanName Bean 名称
     * @param <T>      Bean 原型
     * @return         Bean 实例
     */
    public static <T> T getBean(String beanName) {
        getBeanFactory().getBeanDefinitionCount();
        return (T) getBeanFactory().getBean(beanName);
    }

    /**
     * 根据BeanType及BeanName 获取Bean
     *
     * @param beanName Bean 名称
     * @param clazz    Bean Class
     * @param <T>      Bean 原型
     * @return         Bean 实例
     */
    public static <T> T getBean(String beanName, Class<T> clazz) {
        return getBeanFactory().getBean(beanName, clazz);
    }

    /**
     * 根据Bean Name 获取Bean的原型
     *
     * @param beanName Bean名称
     * @return         Bean原型
     */
    public static Class<?> getBeanType(String beanName) {
        return getBeanFactory().getType(beanName);
    }


    /**
     * 获取指定类型Class的所有Bean,包括子类
     *
     * @param clazz 获取的Bean原型,Null表示获取所有Bean
     * @return Map<java.lang.String, T> Key-BeanName Value - Bean实例
     */
    public static <T> Map<String, T> getBeanForTypeAndChild(Class<T> clazz) {
        return getBeanFactory().getBeansOfType(clazz);
    }

    /**
     * 根据指定类型Class 获取所有Bean实例名称
     *
     * @param clazz  Bean 原型
     * @return       Bean实例名称数组
     */
    public static <T> String[] getBeanNamesForType(Class<T> clazz) {
        return getBeanFactory().getBeanNamesForType(clazz);
    }

    /**
     * 根据指定的注解类型,获取所有标注了该注解所有Bean的Name和实例
     *
     * @param annotationClazz   条件注解类型
     * @return         Map<BeanName,Bean>
     */
    public static Map<String,Object> getBeanForAnnotation(Class<? extends Annotation> annotationClazz){
        return getBeanFactory().getBeansWithAnnotation(annotationClazz);
    }


    /**
     * 获取Bean实例上的一个注解
     * 根据BeanName和要获取的指定注解类型
     *
     * @param beanName Bean名称
     * @param annotationClazz   注解类型
     * @return      指定的注解
     * @param <A>   注解泛型
     */
    public static <A extends Annotation> A getAnnotationForBeanName(String beanName,Class<A> annotationClazz){
        return getBeanFactory().findAnnotationOnBean(beanName,annotationClazz);
    }

    /**
     * TODO 暂且没搞懂具体有什么作用.
     * @param beanName
     * @param annotationClazz
     * @return
     * @param <A>
     */
    public static <A extends Annotation> Set<A> getAnnotationAllForBeanName(String beanName,Class<A> annotationClazz){
        return getBeanFactory().findAllAnnotationsOnBean(beanName,annotationClazz,true);
    }

    /**
     * 获取工厂中的所有Bean数量
     * @return  Bean数量
     */
    public static int getBeanCount(){
        return getBeanFactory().getBeanDefinitionCount();
    }

    /**
     * 获取工厂中的所有Bean的Name
     * @return 所有Bean的名称
     */
    public static List<String> getAllBeanNames(){
        return List.of(getBeanFactory().getBeanDefinitionNames());
    }

    /**
     * 获取配置文件属性值
     *
     * @param keyName 配置键
     * @return        配置值
     */
    public static String getApplicationConfig(String keyName) {
        return applicationContext.getEnvironment().getProperty(keyName);
    }

    /**
     * IOC是否包含该名称的Bean
     *
     * @param beanName bean名称
     * @return         是/否
     */
    public static boolean containsBean(String beanName) {
        return defaultBeanFactory.containsBean(beanName);
    }

    /**
     * IOC是否包含该原型类的Bean实例
     *
     * @param clazz Bean 原型
     * @return      是/否
     */
    public static boolean containsBean(Class<?> clazz) {
        Map<String, ?> beans = defaultBeanFactory.getBeansOfType(clazz);
        return !MapUtils.isEmpty(beans);
    }

    /**
     * 该Bean Scope是否为 singleton
     *
     * @param beanName Bean 名称
     * @return         是/否
     */
    public static boolean isSingleton(String beanName) {
        if (!containsBean(beanName)) {
            return Boolean.FALSE;
        }
        return getBeanFactory().isSingleton(beanName);
    }

    /**
     * 该Bean Scope 是否为prototype
     *
     * @param beanName Bean名称
     * @return         是/否
     */
    public static boolean isProtoType(String beanName) {
        if (!containsBean(beanName)) {
            return Boolean.FALSE;
        }
        return getBeanFactory().isPrototype(beanName);
    }

    /**
     * 判断Bean的原型是否为指定的class
     *
     * @param beanName Bean 名称
     * @param clazz    Bean Class元类型
     * @return         是/否
     */
    public static boolean isBeanEqualsType(String beanName, Class<?> clazz) {
        if (!containsBean(beanName)) {
            return Boolean.FALSE;
        }
        return getBeanFactory().isTypeMatch(beanName, clazz);
    }

    /**
     * 通过 Class元数据信息 将其实例实例化后注入IOC容器
     * 默认 Bean 名称为全类名 风格为驼峰命名(首字母小写)
     * 如: Class Name: SysUserService --> Bean Name: sysUserService
     *
     * @param clazz Bean 元数据信息
     */
    public static void injectBean(Class<?> clazz) {
        injectBean(null, clazz);
    }


    /**
     * 动态向Spring注入Bean
     * 由org.springframework.beans.factory.BeanFactory 实现，通过工具开放API
     *
     * @param beanName Bean 名称
     * @param clazz    Bean 原型
     */
    public static void injectBean(String beanName, Class<?> clazz) {
        beanName = Optional.ofNullable(beanName).orElse(TextUtil.toLowerCaseFirstOne(clazz.getSimpleName()));
        if (SpringContextUtil.containsBean(beanName)){
            throw new BeanCreationException("BeanName 已存在,无法重复注入!");
        }
        try {
            //构建Bean的Definition 然后注入到IOC
            BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(clazz);
            defaultBeanFactory.registerBeanDefinition(beanName,definition.getBeanDefinition());
        } catch (Exception e) {
            throw new BeanCreationException("动态注入Bean实例失败:" + e.getMessage());
        }
    }

    /**
     * 通过 Class 元数据信息,销毁对应的Bean实例
     *
     * @param clazz 要销毁的Bean原型
     */
    public static void destroyBean(Class<?> clazz) {
        destroyBean(TextUtil.toLowerCaseFirstOne(clazz.getSimpleName()));
    }

    /**
     * 通过 Class 元数据信息,销毁IOC中该类型的所有 Bean实例
     *
     * @param clazz Bean 元数据信息
     */
    public static void destroyAllBeanOfType(Class<?> clazz){
        Stream.of(getBeanNamesForType(clazz)).forEach(SpringContextUtil::destroyBean);
    }

    /**
     * 根据BeanName 销毁Bean
     *
     * @param beanName Bean 名称
     */
    public static void destroyBean(String beanName) {
        try {
            // 移除BeanDefinition
            if (containsBean(beanName)){
                defaultBeanFactory.removeBeanDefinition(beanName);
            }
        } catch (Exception e) {
            log.error("销毁Bean失败!", e);
        }
    }


    /**
     * 通用推送Spring 事件机制 API
     * @param source    事件源
     * @param clazz     事件源对应的Event对象 参考com.movies.common.application.event.ExampleEvent
     * @param <R>       事件源原型
     * @param <T>       事件Event对象原型
     * @apiNote 使用方式 如:SpringContextUtil.eventPublisher(userBo, UserBoEvent.class);
     */
    public static <R,T extends ApplicationEvent> void eventPublisher(R source, Class<T> clazz) {
        T instance = eventInstanceBuild(source,clazz);
        if (null != instance){
            getApplicationContext().publishEvent(instance);
        }
    }

    /**
     * 根据事件源、Event元数据信息,构建Event事件对象
     *
     * @param source    事件源
     * @param clazz     事件源对应的Event对象 参考com.movies.common.application.event.ExampleEvent
     * @param <R>       事件源原型
     * @param <T>       事件Event对象原型
     * @return          要派发的Event事件
     */
    private static <T extends ApplicationEvent, R> T eventInstanceBuild(R source, Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor(Object.class, source.getClass());
            AssertUtil.isNull(constructor,"【事件对象不规范,没有包含对应的构造函数!】");
            T instance = constructor.newInstance(applicationContext, source);
            return instance;
        }catch (Exception e){
            log.error("事件Event对象 构建错误! err:{}",e.getMessage());
            return null;
        }
    }
}
