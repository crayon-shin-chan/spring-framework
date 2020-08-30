/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context;

import java.io.Closeable;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.lang.Nullable;

/**
 * SPI interface to be implemented by most if not all application contexts.
 * Provides facilities to configure an application context in addition
 * to the application context client methods in the
 * {@link org.springframework.context.ApplicationContext} interface.
 *
 * <p>Configuration and lifecycle methods are encapsulated here to avoid
 * making them obvious to ApplicationContext client code. The present
 * methods should only be used by startup and shutdown code.
 *
 * @author Juergen Hoeller
 * @author Chris Beams
 * @author Sam Brannen
 * @since 03.11.2003
 */

/**
 * SPI接口将由大多数（如果不是全部）Application Context实现。
 * 除了在{@link org.springframework.context.ApplicationContext}接口中的应用程序上下文客户端方法之外，还提供了配置应用程序上下文的功能。
 * 配置和生命周期方法封装在此处，以避免使它们对于ApplicationContext客户端代码很明显。
 * 当前方法仅应由启动和关闭代码使用。
 */
public interface ConfigurableApplicationContext extends ApplicationContext, Lifecycle, Closeable {

	/**
	 * Any number of these characters are considered delimiters between
	 * multiple context config paths in a single String value.
	 * @see org.springframework.context.support.AbstractXmlApplicationContext#setConfigLocation
	 * @see org.springframework.web.context.ContextLoader#CONFIG_LOCATION_PARAM
	 * @see org.springframework.web.servlet.FrameworkServlet#setContextConfigLocation
	 */
	/**
	 * 配置文件分隔符
	 * 任何数量的这些字符都被视为单个String值中多个上下文配置路径之间的分隔符。
	 */
	String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

	/**
	 * Name of the ConversionService bean in the factory.
	 * If none is supplied, default conversion rules apply.
	 * @since 3.0
	 * @see org.springframework.core.convert.ConversionService
	 */
	/**
	 * {@link org.springframework.core.convert.ConversionService}Bean名称
	 */
	String CONVERSION_SERVICE_BEAN_NAME = "conversionService";

	/**
	 * Name of the LoadTimeWeaver bean in the factory. If such a bean is supplied,
	 * the context will use a temporary ClassLoader for type matching, in order
	 * to allow the LoadTimeWeaver to process all actual bean classes.
	 * @since 2.5
	 * @see org.springframework.instrument.classloading.LoadTimeWeaver
	 */
	/**
	 * {@link LoadTimeWeaver}bean名称
	 */
	String LOAD_TIME_WEAVER_BEAN_NAME = "loadTimeWeaver";

	/**
	 * Name of the {@link Environment} bean in the factory.
	 * @since 3.1
	 */
	/**
	 * {@link Environment}bean名称
	 */
	String ENVIRONMENT_BEAN_NAME = "environment";

	/**
	 * Name of the System properties bean in the factory.
	 * @see java.lang.System#getProperties()
	 */
	String SYSTEM_PROPERTIES_BEAN_NAME = "systemProperties";

	/**
	 * Name of the System environment bean in the factory.
	 * @see java.lang.System#getenv()
	 */
	/**
	 * {@link System#getenv()}bean名称
	 */
	String SYSTEM_ENVIRONMENT_BEAN_NAME = "systemEnvironment";

	/**
	 * Name of the {@link ApplicationStartup} bean in the factory.
	 * @since 5.3
	 */
	/**
	 * {@link ApplicationStartup}bean名称
	 */
	String APPLICATION_STARTUP_BEAN_NAME = "applicationStartup";

	/**
	 * {@link Thread#getName() Name} of the {@linkplain #registerShutdownHook()
	 * shutdown hook} thread: {@value}.
	 * @since 5.2
	 * @see #registerShutdownHook()
	 */
	/**
	 * {@link #registerShutdownHook()}线程名称
	 */
	String SHUTDOWN_HOOK_THREAD_NAME = "SpringContextShutdownHook";


	/**
	 * Set the unique id of this application context.
	 * @since 3.0
	 */
	/**
	 * 设置唯一ID
	 * @param id
	 */
	void setId(String id);

	/**
	 * Set the parent of this application context.
	 * <p>Note that the parent shouldn't be changed: It should only be set outside
	 * a constructor if it isn't available when an object of this class is created,
	 * for example in case of WebApplicationContext setup.
	 * @param parent the parent context
	 * @see org.springframework.web.context.ConfigurableWebApplicationContext
	 */
	/**
	 * 设置此应用程序上下文的父级。
	 * 请注意，不应更改父级：如果在创建此类的对象时不可用，则仅应在构造函数外部设置
	 * 例如在WebApplicationContext设置的情况下。
	 * @param parent 父上下文
	 * @see org.springframework.web.context.ConfigurableWebApplicationContext
	 */
	void setParent(@Nullable ApplicationContext parent);

	/**
	 * Set the {@code Environment} for this application context.
	 * @param environment the new environment
	 * @since 3.1
	 */
	/**
	 * 设置{@link Environment}对象
	 * @param environment
	 */
	void setEnvironment(ConfigurableEnvironment environment);

	/**
	 * Return the {@code Environment} for this application context in configurable
	 * form, allowing for further customization.
	 * @since 3.1
	 */
	@Override
	ConfigurableEnvironment getEnvironment();

	/**
	 * Set the {@link ApplicationStartup} for this application context.
	 * <p>This allows the application context to record metrics
	 * during startup.
	 * @param applicationStartup the new context event factory
	 * @since 5.3
	 */
	/**
	 * 为此应用程序上下文设置{@link ApplicationStartup}。
	 * 这允许应用程序上下文在启动期间记录度量
	 * @param applicationStartup 启动新的上下文事件工厂
	 */
	void setApplicationStartup(ApplicationStartup applicationStartup);

	/**
	 * Return the {@link ApplicationStartup} for this application context.
	 * @since 5.3
	 */
	ApplicationStartup getApplicationStartup();

	/**
	 * Add a new BeanFactoryPostProcessor that will get applied to the internal
	 * bean factory of this application context on refresh, before any of the
	 * bean definitions get evaluated. To be invoked during context configuration.
	 * @param postProcessor the factory processor to register
	 */
	/**
	 * 添加一个新的BeanFactoryPostProcessor，
	 * 在刷新任何bean定义之前，将在刷新时将其应用于此应用程序上下文的内部bean工厂。在上下文配置期间调用。
	 * @param postProcessor 要注册的工厂处理器
	 */
	void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

	/**
	 * Add a new ApplicationListener that will be notified on context events
	 * such as context refresh and context shutdown.
	 * <p>Note that any ApplicationListener registered here will be applied
	 * on refresh if the context is not active yet, or on the fly with the
	 * current event multicaster in case of a context that is already active.
	 * @param listener the ApplicationListener to register
	 * @see org.springframework.context.event.ContextRefreshedEvent
	 * @see org.springframework.context.event.ContextClosedEvent
	 */
	/**
	 * 添加一个新的ApplicationListener，它将在上下文事件时收到通知，例如上下文刷新和上下文关闭。
	 * 请注意，如果上下文尚未处于活动状态，则将在刷新时应用此处注册的所有ApplicationListener；
	 * 如果上下文已经处于活动状态，则将使用当前事件多播程序即时应用。
	 * @param侦听器，用于注册的ApplicationListener
	 * @see org.springframework.context.event.ContextRefreshedEvent
	 * @see org.springframework.context.event.ContextClosedEvent
	 */
	void addApplicationListener(ApplicationListener<?> listener);

	/**
	 * Specify the ClassLoader to load class path resources and bean classes with.
	 * <p>This context class loader will be passed to the internal bean factory.
	 * @since 5.2.7
	 * @see org.springframework.core.io.DefaultResourceLoader#DefaultResourceLoader(ClassLoader)
	 * @see org.springframework.beans.factory.config.ConfigurableBeanFactory#setBeanClassLoader
	 */
	/**
	 * 指定ClassLoader以加载类路径资源和Bean类。 * <p>此上下文类加载器将传递给内部bean工厂。 *
	 * @param classLoader
	 */
	void setClassLoader(ClassLoader classLoader);

	/**
	 * Register the given protocol resolver with this application context,
	 * allowing for additional resource protocols to be handled.
	 * <p>Any such resolver will be invoked ahead of this context's standard
	 * resolution rules. It may therefore also override any default rules.
	 * @since 4.3
	 */
	/**
	 * 在此应用程序上下文中注册给定的协议解析器，允许处理其他资源协议。
	 * 任何此类解析程序都将在此上下文的标准解析规则之前调用。因此，它也可以覆盖任何默认规则。
	 * @param resolver：协议解析器
	 */
	void addProtocolResolver(ProtocolResolver resolver);

	/**
	 * Load or refresh the persistent representation of the configuration, which
	 * might be from Java-based configuration, an XML file, a properties file, a
	 * relational database schema, or some other format.
	 * <p>As this is a startup method, it should destroy already created singletons
	 * if it fails, to avoid dangling resources. In other words, after invocation
	 * of this method, either all or no singletons at all should be instantiated.
	 * @throws BeansException if the bean factory could not be initialized
	 * @throws IllegalStateException if already initialized and multiple refresh
	 * attempts are not supported
	 */
	/**
	 * 加载或刷新配置的持久性表示形式，它可能来自基于Java的配置，XML文件，属性文件，关系数据库模式或其他格式。
	 * 由于这是一种启动方法，因此，如果失败，则应销毁已创建的单例，以避免资源悬空。
	 * 换句话说，在调用此方法之后，应实例化所有单例或根本不实例化。
	 * @throws BeansException 如果无法初始化bean工厂
	 * @throws IllegalStateException 如果已经初始化并且多次刷新.不支持尝试
	 */
	void refresh() throws BeansException, IllegalStateException;

	/**
	 * Register a shutdown hook with the JVM runtime, closing this context
	 * on JVM shutdown unless it has already been closed at that time.
	 * <p>This method can be called multiple times. Only one shutdown hook
	 * (at max) will be registered for each context instance.
	 * <p>As of Spring Framework 5.2, the {@linkplain Thread#getName() name} of
	 * the shutdown hook thread should be {@link #SHUTDOWN_HOOK_THREAD_NAME}.
	 * @see java.lang.Runtime#addShutdownHook
	 * @see #close()
	 */
	/**
	 * 在JVM运行时中注册一个关闭钩子，除非JVM关闭，否则在JVM关闭时关闭此上下文。
	 * 此方法可以多次调用。每个上下文实例将只注册一个关闭钩*（最大）。
	 */
	void registerShutdownHook();

	/**
	 * Close this application context, releasing all resources and locks that the
	 * implementation might hold. This includes destroying all cached singleton beans.
	 * <p>Note: Does <i>not</i> invoke {@code close} on a parent context;
	 * parent contexts have their own, independent lifecycle.
	 * <p>This method can be called multiple times without side effects: Subsequent
	 * {@code close} calls on an already closed context will be ignored.
	 */
	/**
	 * 关闭此应用程序上下文，释放实现可能持有的所有资源和锁。
	 * 这包括销毁所有缓存的单例bean。
	 * 注意：不是否在父上下文上调用{@code close}；
	 * 父上下文具有自己的独立生命周期。
	 * 可以多次调用此方法，而不会产生副作用：在已关闭的上下文中，随后的{@code close}调用将被忽略。
	 */
	@Override
	void close();

	/**
	 * Determine whether this application context is active, that is,
	 * whether it has been refreshed at least once and has not been closed yet.
	 * @return whether the context is still active
	 * @see #refresh()
	 * @see #close()
	 * @see #getBeanFactory()
	 */
	/**
	 * 确定此应用程序上下文是否处于活动状态，即是否已至少刷新一次并且尚未关闭。
	 * @return 上下文是否仍处于活动状态
	 * @see #refresh（）
	 * @see #close（）
	 * @see #getBeanFactory（）
	 */
	boolean isActive();

	/**
	 * Return the internal bean factory of this application context.
	 * Can be used to access specific functionality of the underlying factory.
	 * <p>Note: Do not use this to post-process the bean factory; singletons
	 * will already have been instantiated before. Use a BeanFactoryPostProcessor
	 * to intercept the BeanFactory setup process before beans get touched.
	 * <p>Generally, this internal factory will only be accessible while the context
	 * is active, that is, in-between {@link #refresh()} and {@link #close()}.
	 * The {@link #isActive()} flag can be used to check whether the context
	 * is in an appropriate state.
	 * @return the underlying bean factory
	 * @throws IllegalStateException if the context does not hold an internal
	 * bean factory (usually if {@link #refresh()} hasn't been called yet or
	 * if {@link #close()} has already been called)
	 * @see #isActive()
	 * @see #refresh()
	 * @see #close()
	 * @see #addBeanFactoryPostProcessor
	 */
	/**
	 * 回此应用程序上下文的内部bean工厂。 可用于访问基础工厂的特定功能。
	 * 注意：不要使用它来对bean工厂进行后处理；单例之前已经实例化。使用BeanFactoryPostProcessor来拦截Bean之前的BeanFactory设置过程。
	 * 通常，只有在上下文处于活动状态时，即在{@link #refresh（）}和{@link #close（）}之间，才能访问此内部工厂。
	 * {@link #isActive（）}标志可用于检查上下文是否处于适当的状态。
	 * @return 基础bean工厂
	 * @throws IllegalStateException 如果上下文不包含内部bean工厂（通常是如果尚未调用{@link #refresh（）}或
	 * 	如果{@link #close（）}，则抛出IllegalStateException）已经被调用）
	 * 	@see #isActive（）
	 * 	@see #refresh（）* @see #close（）* @see #addBeanFactoryPostProcessor
	 */
	ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

}
