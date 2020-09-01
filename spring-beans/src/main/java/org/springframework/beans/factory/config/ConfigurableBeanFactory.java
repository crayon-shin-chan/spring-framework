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

package org.springframework.beans.factory.config;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;

/**
 * Configuration interface to be implemented by most bean factories. Provides
 * facilities to configure a bean factory, in addition to the bean factory
 * client methods in the {@link org.springframework.beans.factory.BeanFactory}
 * interface.
 *
 * <p>This bean factory interface is not meant to be used in normal application
 * code: Stick to {@link org.springframework.beans.factory.BeanFactory} or
 * {@link org.springframework.beans.factory.ListableBeanFactory} for typical
 * needs. This extended interface is just meant to allow for framework-internal
 * plug'n'play and for special access to bean factory configuration methods.
 *
 * @author Juergen Hoeller
 * @since 03.11.2003
 * @see org.springframework.beans.factory.BeanFactory
 * @see org.springframework.beans.factory.ListableBeanFactory
 * @see ConfigurableListableBeanFactory
 */

/**
 * 大多数bean工厂都将实现配置接口。
 * 除了{@link org.springframework.beans.factory.BeanFactory} 接口中的bean factory客户端方法之外，还提供了用于配置bean factory的工具。
 * 此bean工厂接口不适合在常规应用程序中使用代码：坚持使用{@link org.springframework.beans.factory.BeanFactory}
 * 或{@link org.springframework.beans.factory.ListableBeanFactory }用于典型的需求。
 * 此扩展接口仅用于允许框架内部即插即用，并允许对bean工厂配置方法的特殊访问。
 * @see org.springframework.beans.factory.BeanFactory
 * @see org.springframework.beans.factory.ListableBeanFactory
 * @see ConfigurableListableBeanFactory
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

	/**
	 * Scope identifier for the standard singleton scope: {@value}.
	 * <p>Custom scopes can be added via {@code registerScope}.
	 * @see #registerScope
	 */
	/**
	 * 标准单例作用域的作用域标识符：{@value}。 =可以通过{@code registerScope}添加自定义范围。
	 * @see #registerScope * /
	 */
	String SCOPE_SINGLETON = "singleton";

	/**
	 * Scope identifier for the standard prototype scope: {@value}.
	 * <p>Custom scopes can be added via {@code registerScope}.
	 * @see #registerScope
	 */
	/**
	 * 标准原型范围的范围标识符：{@value}。可以通过{@code registerScope}添加自定义范围。
	 * @see #registerScope
	 */
	String SCOPE_PROTOTYPE = "prototype";


	/**
	 * Set the parent of this bean factory.
	 * <p>Note that the parent cannot be changed: It should only be set outside
	 * a constructor if it isn't available at the time of factory instantiation.
	 * @param parentBeanFactory the parent BeanFactory
	 * @throws IllegalStateException if this factory is already associated with
	 * a parent BeanFactory
	 * @see #getParentBeanFactory()
	 */
	/**
	 * 设置此bean工厂的父级。
	 * 请注意，父级不能更改：如果在工厂实例化时不可用，则只能在构造函数外部设置。
	 * @param parentBeanFactory 父BeanFactory
	 * @throws IllegalStateException 如果此工厂已经与父BeanFactory关联
	 * @see #getParentBeanFactory（）
	 */
	void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException;

	/**
	 * Set the class loader to use for loading bean classes.
	 * Default is the thread context class loader.
	 * <p>Note that this class loader will only apply to bean definitions
	 * that do not carry a resolved bean class yet. This is the case as of
	 * Spring 2.0 by default: Bean definitions only carry bean class names,
	 * to be resolved once the factory processes the bean definition.
	 * @param beanClassLoader the class loader to use,
	 * or {@code null} to suggest the default class loader
	 */
	/**
	 * 设置类加载器以用于加载bean类。
	 * 默认为线程上下文类加载器。
	 * 请注意，此类加载器仅适用于尚不包含已解析的bean类的bean定义。
	 * 在默认情况下，从Spring 2.0开始就是这种情况：Bean定义仅带有Bean类名，在工厂处理Bean定义后即可解决。
	 * @param beanClassLoader 要使用的类加载器，或{@code null}建议默认的类加载器
	 */
	void setBeanClassLoader(@Nullable ClassLoader beanClassLoader);

	/**
	 * Return this factory's class loader for loading bean classes
	 * (only {@code null} if even the system ClassLoader isn't accessible).
	 * @see org.springframework.util.ClassUtils#forName(String, ClassLoader)
	 */
	/**
	 * 返回此工厂的类加载器以加载Bean类（即使无法访问系统ClassLoader，也只能{{code null}）。
	 * @see org.springframework.util.ClassUtils＃forName（String，ClassLoader）
	 */
	@Nullable
	ClassLoader getBeanClassLoader();

	/**
	 * Specify a temporary ClassLoader to use for type matching purposes.
	 * Default is none, simply using the standard bean ClassLoader.
	 * <p>A temporary ClassLoader is usually just specified if
	 * <i>load-time weaving</i> is involved, to make sure that actual bean
	 * classes are loaded as lazily as possible. The temporary loader is
	 * then removed once the BeanFactory completes its bootstrap phase.
	 * @since 2.5
	 */
	/**
	 * 指定一个临时的ClassLoader以用于类型匹配。
	 * 默认为无，只需使用标准bean ClassLoader。
	 * 如果涉及到加载时编织，通常仅指定一个临时的ClassLoader，以确保尽可能延迟地加载实际的bean类。
	 * 一旦BeanFactory完成其引导阶段，便将临时加载器删除
	 * @param tempClassLoader
	 */
	void setTempClassLoader(@Nullable ClassLoader tempClassLoader);

	/**
	 * Return the temporary ClassLoader to use for type matching purposes,
	 * if any.
	 * @since 2.5
	 */
	/**
	 * 返回临时ClassLoader以用于类型匹配，如果有，则返回。
	 * @return
	 */
	@Nullable
	ClassLoader getTempClassLoader();

	/**
	 * Set whether to cache bean metadata such as given bean definitions
	 * (in merged fashion) and resolved bean classes. Default is on.
	 * <p>Turn this flag off to enable hot-refreshing of bean definition objects
	 * and in particular bean classes. If this flag is off, any creation of a bean
	 * instance will re-query the bean class loader for newly resolved classes.
	 */
	/**
	 * 设置是否缓存bean元数据，例如给定的bean定义（以合并方式）和已解析的bean类。
	 * 默认为开。 关闭此标志以启用Bean定义对象（特别是Bean类）的热刷新。
	 * 如果关闭此标志，则任何bean实例的创建都将重新查询bean类加载器以获取新解析的类。
	 * @param cacheBeanMetadata
	 */
	void setCacheBeanMetadata(boolean cacheBeanMetadata);

	/**
	 * Return whether to cache bean metadata such as given bean definitions
	 * (in merged fashion) and resolved bean classes.
	 */
	/**
	 * 返回是否缓存Bean元数据，例如给定的Bean定义（以合并方式）和已解析的Bean类。
	 * @return
	 */
	boolean isCacheBeanMetadata();

	/**
	 * Specify the resolution strategy for expressions in bean definition values.
	 * <p>There is no expression support active in a BeanFactory by default.
	 * An ApplicationContext will typically set a standard expression strategy
	 * here, supporting "#{...}" expressions in a Unified EL compatible style.
	 * @since 3.0
	 */
	/**
	 * 为bean定义值中的表达式指定解析策略。
	 * 默认情况下，BeanFactory中不支持任何表达式支持。
	 * ApplicationContext通常会在此处设置标准的表达式策略，以统一EL兼容样式支持“＃{...}”表达式
	 * @param resolver
	 */
	void setBeanExpressionResolver(@Nullable BeanExpressionResolver resolver);

	/**
	 * Return the resolution strategy for expressions in bean definition values.
	 * @since 3.0
	 */
	/**
	 * 返回bean定义值中表达式的解析策略。
	 * @return
	 */
	@Nullable
	BeanExpressionResolver getBeanExpressionResolver();

	/**
	 * Specify a Spring 3.0 ConversionService to use for converting
	 * property values, as an alternative to JavaBeans PropertyEditors.
	 * @since 3.0
	 */
	/**
	 * 指定一个Spring 3.0 ConversionService以用于转换属性值，以替代JavaBeans PropertyEditors。
	 * @param conversionService
	 */
	void setConversionService(@Nullable ConversionService conversionService);

	/**
	 * Return the associated ConversionService, if any.
	 * @since 3.0
	 */
	@Nullable
	ConversionService getConversionService();

	/**
	 * Add a PropertyEditorRegistrar to be applied to all bean creation processes.
	 * <p>Such a registrar creates new PropertyEditor instances and registers them
	 * on the given registry, fresh for each bean creation attempt. This avoids
	 * the need for synchronization on custom editors; hence, it is generally
	 * preferable to use this method instead of {@link #registerCustomEditor}.
	 * @param registrar the PropertyEditorRegistrar to register
	 */
	/**
	 * 添加一个PropertyEditorRegistrar以应用于所有bean创建过程。
	 * 这样的注册商会创建新的PropertyEditor实例，并在给定的注册表中注册它们，对于每次创建bean的尝试都是新鲜的。
	 * 这避免了在自定义编辑器上进行同步的需要；因此，通常最好使用此方法代替{@link #registerCustomEditor}。
	 * @param registrar PropertyEditorRegistrar进行注册
	 * @param registrar
	 */
	void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);

	/**
	 * Register the given custom property editor for all properties of the
	 * given type. To be invoked during factory configuration.
	 * <p>Note that this method will register a shared custom editor instance;
	 * access to that instance will be synchronized for thread-safety. It is
	 * generally preferable to use {@link #addPropertyEditorRegistrar} instead
	 * of this method, to avoid for the need for synchronization on custom editors.
	 * @param requiredType type of the property
	 * @param propertyEditorClass the {@link PropertyEditor} class to register
	 */
	/**
	 * 为给定类型的所有属性注册给定的定制属性编辑器。在出厂配置期间调用。
	 * 请注意，此方法将注册一个共享的自定义编辑器实例；对该实例的访问将被同步以确保线程安全。
	 * 通常最好使用{@link #addPropertyEditorRegistrar}代替此方法，以避免在自定义编辑器上进行同步。
	 * @param requiredType 属性的类型
	 * @param propertyEditorClass {@link PropertyEditor}类进行注册
	 */
	void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass);

	/**
	 * Initialize the given PropertyEditorRegistry with the custom editors
	 * that have been registered with this BeanFactory.
	 * @param registry the PropertyEditorRegistry to initialize
	 */
	/**
	 * 使用已在此BeanFactory中注册的自定义编辑器初始化给定的PropertyEditorRegistry。
	 * @param registry PropertyEditorRegistry进行初始化
	 */
	void copyRegisteredEditorsTo(PropertyEditorRegistry registry);

	/**
	 * Set a custom type converter that this BeanFactory should use for converting
	 * bean property values, constructor argument values, etc.
	 * <p>This will override the default PropertyEditor mechanism and hence make
	 * any custom editors or custom editor registrars irrelevant.
	 * @since 2.5
	 * @see #addPropertyEditorRegistrar
	 * @see #registerCustomEditor
	 */
	/**
	 * 设置此BeanFactory用于转换bean属性值，构造函数参数值等的自定义类型转换器。
	 * 这将覆盖默认的PropertyEditor机制，从而使任何自定义编辑器或自定义编辑器注册器都无关紧要。
	 * @see #addPropertyEditorRegistrar
	 * @see #registerCustomEditor
	 */
	void setTypeConverter(TypeConverter typeConverter);

	/**
	 * Obtain a type converter as used by this BeanFactory. This may be a fresh
	 * instance for each call, since TypeConverters are usually <i>not</i> thread-safe.
	 * <p>If the default PropertyEditor mechanism is active, the returned
	 * TypeConverter will be aware of all custom editors that have been registered.
	 * @since 2.5
	 */
	/**
	 * 获取此BeanFactory使用的类型转换器。
	 * 对于每个调用，这可能是一个新的实例，因为TypeConverters通常不是不是线程安全的。
	 * 如果默认的PropertyEditor机制处于活动状态，则返回的TypeConverter将知道已注册的所有自定义编辑器。
	 * @return
	 */
	TypeConverter getTypeConverter();

	/**
	 * Add a String resolver for embedded values such as annotation attributes.
	 * @param valueResolver the String resolver to apply to embedded values
	 * @since 3.0
	 */
	/**
	 * 为嵌入值（例如注释属性）添加字符串解析器。
	 * @param valueResolver 应用于嵌入值的字符串解析器
	 */
	void addEmbeddedValueResolver(StringValueResolver valueResolver);

	/**
	 * Determine whether an embedded value resolver has been registered with this
	 * bean factory, to be applied through {@link #resolveEmbeddedValue(String)}.
	 * @since 4.3
	 */
	/**
	 * 确定是否已通过{@link #resolveEmbeddedValue（String）}应用此bean工厂注册了嵌入式值解析器。
	 * @return
	 */
	boolean hasEmbeddedValueResolver();

	/**
	 * Resolve the given embedded value, e.g. an annotation attribute.
	 * @param value the value to resolve
	 * @return the resolved value (may be the original value as-is)
	 * @since 3.0
	 */
	/**
	 * 解决给定的嵌入值，例如注释属性。
	 * @param value 要解析的值
	 * @return 解析后的值（可能是原始值）
	 */
	@Nullable
	String resolveEmbeddedValue(String value);

	/**
	 * Add a new BeanPostProcessor that will get applied to beans created
	 * by this factory. To be invoked during factory configuration.
	 * <p>Note: Post-processors submitted here will be applied in the order of
	 * registration; any ordering semantics expressed through implementing the
	 * {@link org.springframework.core.Ordered} interface will be ignored. Note
	 * that autodetected post-processors (e.g. as beans in an ApplicationContext)
	 * will always be applied after programmatically registered ones.
	 * @param beanPostProcessor the post-processor to register
	 */
	/**
	 * 添加一个新的BeanPostProcessor，它将应用于由该工厂创建的bean。在出厂配置期间调用。
	 * 注意：此处提交的后处理器将按照注册的顺序应用；通过实现* {@link org.springframework.core.Ordered}接口表示的任何排序语义都将被忽略。
	 * 请注意自动检测到的后处理器（例如，作为ApplicationContext中的bean）将始终以编程方式注册后的处理器。
	 * @param beanPostProcessor 要注册的后处理器
	 */
	void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

	/**
	 * Return the current number of registered BeanPostProcessors, if any.
	 */
	/**
	 * 返回当前已注册的BeanPostProcessor的数量（如果有）。
	 * @return
	 */
	int getBeanPostProcessorCount();

	/**
	 * Register the given scope, backed by the given Scope implementation.
	 * @param scopeName the scope identifier
	 * @param scope the backing Scope implementation
	 */
	/**
	 * 注册由给定范围实现支持的给定范围。
	 * @param scopeName 范围标识符
	 * @param scope 支持Scope的实现
	 */
	void registerScope(String scopeName, Scope scope);

	/**
	 * Return the names of all currently registered scopes.
	 * <p>This will only return the names of explicitly registered scopes.
	 * Built-in scopes such as "singleton" and "prototype" won't be exposed.
	 * @return the array of scope names, or an empty array if none
	 * @see #registerScope
	 */
	/**
	 * 返回所有当前注册范围的名称。
	 * 这将仅返回显式注册的作用域的名称。
	 * 内置作用域（例如“singleton”和“prototype”）不会公开。
	 * @return 作用域名称的数组，如果没有则返回空数组
	 * @see #registerScope
	 */
	String[] getRegisteredScopeNames();

	/**
	 * Return the Scope implementation for the given scope name, if any.
	 * <p>This will only return explicitly registered scopes.
	 * Built-in scopes such as "singleton" and "prototype" won't be exposed.
	 * @param scopeName the name of the scope
	 * @return the registered Scope implementation, or {@code null} if none
	 * @see #registerScope
	 */
	/**
	 * 返回给定作用域名称的作用域实现（如果有）。
	 * 这将仅返回显式注册的范围。内置作用域（例如“singleton”和“prototype”）不会公开。
	 * @param scopeName 作用域的名称
	 * @return 注册的作用域实现，如果没有则返回{@code null}
	 * @see #registerScope
	 */
	@Nullable
	Scope getRegisteredScope(String scopeName);

	/**
	 * Set the {@code ApplicationStartup} for this bean factory.
	 * <p>This allows the application context to record metrics during application startup.
	 * @param applicationStartup the new application startup
	 * @since 5.3
	 */
	/**
	 * 为此bean工厂设置{@code ApplicationStartup}。
	 * 这允许应用程序上下文在应用程序启动期间记录指标。
	 * @param applicationStartup 新的应用程序启动
	 */
	void setApplicationStartup(ApplicationStartup applicationStartup);

	/**
	 * Return the {@code ApplicationStartup} for this bean factory.
	 * @since 5.3
	 */
	ApplicationStartup getApplicationStartup();

	/**
	 * Provides a security access control context relevant to this factory.
	 * @return the applicable AccessControlContext (never {@code null})
	 * @since 3.0
	 */
	/**
	 * 提供与此工厂有关的安全访问控制上下文。
	 * @return 适用的AccessControlContext（从不更改{@code null}）
	 */
	AccessControlContext getAccessControlContext();

	/**
	 * Copy all relevant configuration from the given other factory.
	 * <p>Should include all standard configuration settings as well as
	 * BeanPostProcessors, Scopes, and factory-specific internal settings.
	 * Should not include any metadata of actual bean definitions,
	 * such as BeanDefinition objects and bean name aliases.
	 * @param otherFactory the other BeanFactory to copy from
	 */
	/**
	 * 从给定的其他工厂复制所有相关配置。
	 * 应包括所有标准配置设置以及BeanPostProcessor，范围和工厂特定的内部设置。
	 * 不应包含任何实际bean定义的元数据，例如BeanDefinition对象和bean名称别名。
	 * @param otherFactory 要从中复制的另一个BeanFactory
	 */
	void copyConfigurationFrom(ConfigurableBeanFactory otherFactory);

	/**
	 * Given a bean name, create an alias. We typically use this method to
	 * support names that are illegal within XML ids (used for bean names).
	 * <p>Typically invoked during factory configuration, but can also be
	 * used for runtime registration of aliases. Therefore, a factory
	 * implementation should synchronize alias access.
	 * @param beanName the canonical name of the target bean
	 * @param alias the alias to be registered for the bean
	 * @throws BeanDefinitionStoreException if the alias is already in use
	 */
	/**
	 * 给定一个Bean名称，创建一个别名。
	 * 我们通常使用此方法来支持XML ID（用于bean名称）中非法的名称。
	 * 通常在工厂配置期间调用，但也可以用于别名的运行时注册。
	 * 因此，factory实现应同步别名访问。
	 * @param beanName 是目标bean的规范名称
	 * @param alias 是要为该bean注册的别名
	 * @throws BeanDefinitionStoreException（如果别名已被使用）
	 */
	void registerAlias(String beanName, String alias) throws BeanDefinitionStoreException;

	/**
	 * Resolve all alias target names and aliases registered in this
	 * factory, applying the given StringValueResolver to them.
	 * <p>The value resolver may for example resolve placeholders
	 * in target bean names and even in alias names.
	 * @param valueResolver the StringValueResolver to apply
	 * @since 2.5
	 */
	/**
	 * 解析所有别名目标名称和在此工厂中注册的别名，将给定的StringValueResolver应用于它们。
	 * 例如，值解析器可以解析目标bean名称甚至别名中的占位符
	 * @param valueResolver 要应用的StringValueResolver
	 */
	void resolveAliases(StringValueResolver valueResolver);

	/**
	 * Return a merged BeanDefinition for the given bean name,
	 * merging a child bean definition with its parent if necessary.
	 * Considers bean definitions in ancestor factories as well.
	 * @param beanName the name of the bean to retrieve the merged definition for
	 * @return a (potentially merged) BeanDefinition for the given bean
	 * @throws NoSuchBeanDefinitionException if there is no bean definition with the given name
	 * @since 2.5
	 */
	/**
	 * 返回给定bean名称的合并BeanDefinition，如有必要，将子bean定义与其父级合并。
	 * 还要考虑祖先工厂中的bean定义。
	 * @param beanName 要检索给定bean的合并定义的bean的名称
	 * @return 为给定bean返回一个（可能合并的）BeanDefinition
	 * @throws NoSuchBeanDefinitionException 如果没有给定名称的bean定义，则抛出NoSuchBeanDefinitionException
	 */
	BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	/**
	 * Determine whether the bean with the given name is a FactoryBean.
	 * @param name the name of the bean to check
	 * @return whether the bean is a FactoryBean
	 * ({@code false} means the bean exists but is not a FactoryBean)
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.5
	 */
	/**
	 * 确定具有给定名称的bean是否为FactoryBean。
	 * @param name 要检查的bean的名称
	 * @return bean是否为FactoryBean （{@code false}表示该bean存在，但不是FactoryBean）如果没有给定名称的bean，则抛出NoSuchBeanDefinitionException
	 * @throws NoSuchBeanDefinitionException
	 */
	boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Explicitly control the current in-creation status of the specified bean.
	 * For container-internal use only.
	 * @param beanName the name of the bean
	 * @param inCreation whether the bean is currently in creation
	 * @since 3.1
	 */
	/**
	 * 明确控制指定bean的当前增量状态。
	 * 仅供内部容器使用。
	 * @param beanName Bean的名称
	 * @param inCreation 是否当前正在创建该bean
	 */
	void setCurrentlyInCreation(String beanName, boolean inCreation);

	/**
	 * Determine whether the specified bean is currently in creation.
	 * @param beanName the name of the bean
	 * @return whether the bean is currently in creation
	 * @since 2.5
	 */
	/**
	 * 确定指定的bean当前是否在创建中。
	 * @param beanName bean的名称
	 * @return 当前是否正在创建bean
	 */
	boolean isCurrentlyInCreation(String beanName);

	/**
	 * Register a dependent bean for the given bean,
	 * to be destroyed before the given bean is destroyed.
	 * @param beanName the name of the bean
	 * @param dependentBeanName the name of the dependent bean
	 * @since 2.5
	 */
	/**
	 * 在给定的bean被销毁之前，为给定的bean注册一个要被销毁的依赖bean。
	 * @param beanName bean的名称
	 * @param dependentBeanName 从属bean的名称
	 */
	void registerDependentBean(String beanName, String dependentBeanName);

	/**
	 * Return the names of all beans which depend on the specified bean, if any.
	 * @param beanName the name of the bean
	 * @return the array of dependent bean names, or an empty array if none
	 * @since 2.5
	 */
	/**
	 * 返回依赖于指定bean的所有bean的名称（如果有）。
	 * @param beanName bean的名称
	 * @return 相关bean名称的数组，如果没有则返回一个空数组
	 */
	String[] getDependentBeans(String beanName);

	/**
	 * Return the names of all beans that the specified bean depends on, if any.
	 * @param beanName the name of the bean
	 * @return the array of names of beans which the bean depends on,
	 * or an empty array if none
	 * @since 2.5
	 */
	/**
	 * 回指定bean依赖的所有bean的名称（如果有）。
	 * @param beanName bean的名称
	 * @return bean所依赖的bean的名称数组，或者返回空数组（如果没有）
	 */
	String[] getDependenciesForBean(String beanName);

	/**
	 * Destroy the given bean instance (usually a prototype instance
	 * obtained from this factory) according to its bean definition.
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 * @param beanName the name of the bean definition
	 * @param beanInstance the bean instance to destroy
	 */
	/**
	 * 根据其bean定义销毁给定的bean实例（通常是从该工厂获得的原型实例）。
	 * 在破坏过程中出现的任何异常都应该被捕获并记录下来，而不是传播到此方法的调用者。
	 * @param beanName bean定义的名称
	 * @param beanInstance 要销毁的bean实例
	 */
	void destroyBean(String beanName, Object beanInstance);

	/**
	 * Destroy the specified scoped bean in the current target scope, if any.
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 * @param beanName the name of the scoped bean
	 */
	/**
	 * 销毁当前目标范围中的指定范围的bean（如果有）。
	 * 在破坏过程中出现的任何异常都应该被捕获并记录下来，而不是传播到此方法的调用者。
	 * @param beanName 作用域bean的名称
	 */
	void destroyScopedBean(String beanName);

	/**
	 * Destroy all singleton beans in this factory, including inner beans that have
	 * been registered as disposable. To be called on shutdown of a factory.
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 */
	/**
	 * 销毁该工厂中的所有单例bean，包括已注册为一次性的内部bean。在工厂关闭时被调用。
	 * 在破坏过程中出现的任何异常都应该被捕获并记录下来，而不是传播到此方法的调用者。
	 */
	void destroySingletons();

}
