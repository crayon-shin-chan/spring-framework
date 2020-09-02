/*
 * Copyright 2002-2019 the original author or authors.
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

import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.lang.Nullable;

/**
 * Extension of the {@link org.springframework.beans.factory.BeanFactory}
 * interface to be implemented by bean factories that are capable of
 * autowiring, provided that they want to expose this functionality for
 * existing bean instances.
 *
 * <p>This subinterface of BeanFactory is not meant to be used in normal
 * application code: stick to {@link org.springframework.beans.factory.BeanFactory}
 * or {@link org.springframework.beans.factory.ListableBeanFactory} for
 * typical use cases.
 *
 * <p>Integration code for other frameworks can leverage this interface to
 * wire and populate existing bean instances that Spring does not control
 * the lifecycle of. This is particularly useful for WebWork Actions and
 * Tapestry Page objects, for example.
 *
 * <p>Note that this interface is not implemented by
 * {@link org.springframework.context.ApplicationContext} facades,
 * as it is hardly ever used by application code. That said, it is available
 * from an application context too, accessible through ApplicationContext's
 * {@link org.springframework.context.ApplicationContext#getAutowireCapableBeanFactory()}
 * method.
 *
 * <p>You may also implement the {@link org.springframework.beans.factory.BeanFactoryAware}
 * interface, which exposes the internal BeanFactory even when running in an
 * ApplicationContext, to get access to an AutowireCapableBeanFactory:
 * simply cast the passed-in BeanFactory to AutowireCapableBeanFactory.
 *
 * @author Juergen Hoeller
 * @since 04.12.2003
 * @see org.springframework.beans.factory.BeanFactoryAware
 * @see org.springframework.beans.factory.config.ConfigurableListableBeanFactory
 * @see org.springframework.context.ApplicationContext#getAutowireCapableBeanFactory()
 */

/**
 * {@link org.springframework.beans.factory.BeanFactory}接口的扩展将由能够自动装配的bean工厂实现，只要它们想为现有bean实例公开此功能。
 * 此BeanFactory的子接口不适合在常规应用程序代码中使用：
 * 坚持使用{@link org.springframework.beans.factory.BeanFactory}或{@link org.springframework.beans.factory.ListableBeanFactory }适用于典型用例。
 * 其他框架的集成代码可以利用此接口来连接并填充Spring无法控制其生命周期的现有bean实例。
 * 例如，这对于WebWork操作和Tapestry Page对象特别有用。
 * 请注意，此接口不是由{@link org.springframework.context.ApplicationContext}门面实现的，因为应用程序代码几乎从未使用过此接口。
 * 也就是说，它也可以从应用程序上下文中获得，也可以通过ApplicationContext的{@link org.springframework.context.ApplicationContext＃getAutowireCapableBeanFactory（）}方法来访问。
 * 您还可以实现{@link org.springframework.beans.factory.BeanFactoryAware}接口，该接口甚至在ApplicationContext中运行时也公开内部BeanFactory，以访问AutowireCapableBeanFactory：
 * 将BeanFactory传递给AutowireCapableBeanFactory。
 * @see org.springframework.beans.factory.BeanFactoryAware
 * @see org.springframework.beans.factory.config.ConfigurableListableBeanFactory
 * @see org.springframework.context.ApplicationContext＃getAutowireCapableBeanFactory （）
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

	/**
	 * Constant that indicates no externally defined autowiring. Note that
	 * BeanFactoryAware etc and annotation-driven injection will still be applied.
	 * @see #createBean
	 * @see #autowire
	 * @see #autowireBeanProperties
	 */
	/**
	 * 指示没有外部定义的自动装配的常数。请注意，BeanFactoryAware等和注释驱动的注入将仍然适用。
	 * @see #createBean
	 * @see #autowire
	 * @see #autowireBean属性
	 */
	int AUTOWIRE_NO = 0;

	/**
	 * Constant that indicates autowiring bean properties by name
	 * (applying to all bean property setters).
	 * @see #createBean
	 * @see #autowire
	 * @see #autowireBeanProperties
	 */
	/**
	 * 用名称指示自动装配bean属性的常数（适用于所有bean属性设置器）。
	 * @see #createBean
	 * @see #autowire
	 * @see #autowireBean属性
	 */
	int AUTOWIRE_BY_NAME = 1;

	/**
	 * Constant that indicates autowiring bean properties by type
	 * (applying to all bean property setters).
	 * @see #createBean
	 * @see #autowire
	 * @see #autowireBeanProperties
	 */
	/**
	 * 指示按类型自动装配bean属性的常量（适用于所有bean属性设置器）。
	 * @see #createBean
	 * @see #autowire
	 * @see #autowireBean属性
	 */
	int AUTOWIRE_BY_TYPE = 2;

	/**
	 * Constant that indicates autowiring the greediest constructor that
	 * can be satisfied (involves resolving the appropriate constructor).
	 * @see #createBean
	 * @see #autowire
	 */
	/**
	 * 示自动装配可以满足的最贪婪的构造函数的常数（涉及解析适当的构造函数）。
	 * @see #createBean
	 * @see #autowire
	 */
	int AUTOWIRE_CONSTRUCTOR = 3;

	/**
	 * Constant that indicates determining an appropriate autowire strategy
	 * through introspection of the bean class.
	 * @see #createBean
	 * @see #autowire
	 * @deprecated as of Spring 3.0: If you are using mixed autowiring strategies,
	 * prefer annotation-based autowiring for clearer demarcation of autowiring needs.
	 */
	/**
	 * 指示通过内省bean类确定适当的自动装配策略的常数。
	 * @see #createBean
	 * @see #autowire
	 * @从Spring 3.0开始不推荐使用：如果您使用混合自动装配策略，则更希望使用基于注释的自动装配来更清楚地划分自动装配需求。
	 */
	@Deprecated
	int AUTOWIRE_AUTODETECT = 4;

	/**
	 * Suffix for the "original instance" convention when initializing an existing
	 * bean instance: to be appended to the fully-qualified bean class name,
	 * e.g. "com.mypackage.MyClass.ORIGINAL", in order to enforce the given instance
	 * to be returned, i.e. no proxies etc.
	 * @since 5.1
	 * @see #initializeBean(Object, String)
	 * @see #applyBeanPostProcessorsBeforeInitialization(Object, String)
	 * @see #applyBeanPostProcessorsAfterInitialization(Object, String)
	 */
	/**
	 * 初始化现有的bean实例时，“原始实例”约定的后缀：将附加到完全限定的bean类名，例如“com.mypackage.MyClass.ORIGINAL”，以强制执行给定实例的返回即没有代理等。
	 * @see #initializeBean（Object，String）
	 * @see #applyBeanPostProcessorsBeforeInitialization（Object，String ）
	 * @see #applyBeanPostProcessorsAfterInitialization（Object，String）
	 */
	String ORIGINAL_INSTANCE_SUFFIX = ".ORIGINAL";


	//-------------------------------------------------------------------------
	// Typical methods for creating and populating external bean instances
	//-------------------------------------------------------------------------

	/**
	 * Fully create a new bean instance of the given class.
	 * <p>Performs full initialization of the bean, including all applicable
	 * {@link BeanPostProcessor BeanPostProcessors}.
	 * <p>Note: This is intended for creating a fresh instance, populating annotated
	 * fields and methods as well as applying all standard bean initialization callbacks.
	 * It does <i>not</i> imply traditional by-name or by-type autowiring of properties;
	 * use {@link #createBean(Class, int, boolean)} for those purposes.
	 * @param beanClass the class of the bean to create
	 * @return the new bean instance
	 * @throws BeansException if instantiation or wiring failed
	 */
	/**
	 * 完全创建给定类的新bean实例。
	 * 执行Bean的完全初始化，包括所有适用的{@link BeanPostProcessor BeanPostProcessors}。
	 * 注意：这旨在创建一个新实例，填充带注释的字段和方法，以及应用所有标准的bean初始化回调。
	 * 它并不暗示传统的按名称或按类型自动装配属性；为此目的使用{@link #createBean（Class，int，boolean）}。
	 * @param beanClass 要创建的bean的类
	 * @return 新的bean实例
	 * @throws BeansException 如果实例化或连接失败，则抛出BeansException
	 */
	<T> T createBean(Class<T> beanClass) throws BeansException;

	/**
	 * Populate the given bean instance through applying after-instantiation callbacks
	 * and bean property post-processing (e.g. for annotation-driven injection).
	 * <p>Note: This is essentially intended for (re-)populating annotated fields and
	 * methods, either for new instances or for deserialized instances. It does
	 * <i>not</i> imply traditional by-name or by-type autowiring of properties;
	 * use {@link #autowireBeanProperties} for those purposes.
	 * @param existingBean the existing bean instance
	 * @throws BeansException if wiring failed
	 */
	/**
	 * 通过应用实例化后的回调和bean属性的后处理（例如用于注释驱动的注入）来填充给定的bean实例。
	 * 注意：这本质上是用于（重新）填充带注释的字段和方法，用于新实例或反序列化实例。不是暗含传统的按名称或按类型自动装配属性；
	 * 为此目的使用{@link #autowireBeanProperties}。
	 * @param existingBean 现有的bean实例
	 * @throws BeansException 如果接线失败，则抛出BeansException
	 */
	void autowireBean(Object existingBean) throws BeansException;

	/**
	 * Configure the given raw bean: autowiring bean properties, applying
	 * bean property values, applying factory callbacks such as {@code setBeanName}
	 * and {@code setBeanFactory}, and also applying all bean post processors
	 * (including ones which might wrap the given raw bean).
	 * <p>This is effectively a superset of what {@link #initializeBean} provides,
	 * fully applying the configuration specified by the corresponding bean definition.
	 * <b>Note: This method requires a bean definition for the given name!</b>
	 * @param existingBean the existing bean instance
	 * @param beanName the name of the bean, to be passed to it if necessary
	 * (a bean definition of that name has to be available)
	 * @return the bean instance to use, either the original or a wrapped one
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
	 * if there is no bean definition with the given name
	 * @throws BeansException if the initialization failed
	 * @see #initializeBean
	 */
	/**
	 * 配置给定的原始bean：
	 * 自动装配bean属性，应用bean属性值，应用工厂回调，例如{@code setBeanName}和{@code setBeanFactory}，
	 * 还应用所有bean后处理器（包括可能包装给定给定值的处理器）生豆）。
	 * 这实际上是{@link #initializeBean}提供的超集，完全应用了由相应bean定义指定的配置。
	 * 注意：此方法需要给定名称的bean定义！
	 * @param existingBean 现有的bean实例
	 * @param beanName bean的名称，必要时将传递给它必须提供该名称的定义）
	 * @return 要使用的原始或包装的bean实例
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException 如果没有给定名称的bean定义
	 * @throws BeansException 如果初始化失败，则抛出BeansException
	 * @see #initializeBean
	 */
	Object configureBean(Object existingBean, String beanName) throws BeansException;


	//-------------------------------------------------------------------------
	// Specialized methods for fine-grained control over the bean lifecycle
	//-------------------------------------------------------------------------

	/**
	 * Fully create a new bean instance of the given class with the specified
	 * autowire strategy. All constants defined in this interface are supported here.
	 * <p>Performs full initialization of the bean, including all applicable
	 * {@link BeanPostProcessor BeanPostProcessors}. This is effectively a superset
	 * of what {@link #autowire} provides, adding {@link #initializeBean} behavior.
	 * @param beanClass the class of the bean to create
	 * @param autowireMode by name or type, using the constants in this interface
	 * @param dependencyCheck whether to perform a dependency check for objects
	 * (not applicable to autowiring a constructor, thus ignored there)
	 * @return the new bean instance
	 * @throws BeansException if instantiation or wiring failed
	 * @see #AUTOWIRE_NO
	 * @see #AUTOWIRE_BY_NAME
	 * @see #AUTOWIRE_BY_TYPE
	 * @see #AUTOWIRE_CONSTRUCTOR
	 */
	/**
	 * 使用指定的自动装配策略完全创建给定类的新bean实例。
	 * 这里支持此接口中定义的所有常量。
	 * 执行Bean的完全初始化，包括所有适用的{@link BeanPostProcessor BeanPostProcessors}。
	 * 这实际上是{@link #autowire}提供的功能的超集，添加了{@link #initializeBean}行为。
	 * @param beanClass 要创建的bean的类
	 * @param autowireMode 使用此接口中的常量按名称或类型
	 * @param dependencyCheck 是否对对象执行依赖检查（不适用于自动装配构造函数，因此在此处被忽略）
	 * @return 新的bean实例*如果实例化或连接失败，则
	 * @throws BeansException
	 * @see #AUTOWIRE_NO
	 * @see #AUTOWIRE_BY_NAME
	 * @see #AUTOWIRE_BY_TYPE
	 * @see #AUTOWIRE_CONSTRUCTOR
	 */
	Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;

	/**
	 * Instantiate a new bean instance of the given class with the specified autowire
	 * strategy. All constants defined in this interface are supported here.
	 * Can also be invoked with {@code AUTOWIRE_NO} in order to just apply
	 * before-instantiation callbacks (e.g. for annotation-driven injection).
	 * <p>Does <i>not</i> apply standard {@link BeanPostProcessor BeanPostProcessors}
	 * callbacks or perform any further initialization of the bean. This interface
	 * offers distinct, fine-grained operations for those purposes, for example
	 * {@link #initializeBean}. However, {@link InstantiationAwareBeanPostProcessor}
	 * callbacks are applied, if applicable to the construction of the instance.
	 * @param beanClass the class of the bean to instantiate
	 * @param autowireMode by name or type, using the constants in this interface
	 * @param dependencyCheck whether to perform a dependency check for object
	 * references in the bean instance (not applicable to autowiring a constructor,
	 * thus ignored there)
	 * @return the new bean instance
	 * @throws BeansException if instantiation or wiring failed
	 * @see #AUTOWIRE_NO
	 * @see #AUTOWIRE_BY_NAME
	 * @see #AUTOWIRE_BY_TYPE
	 * @see #AUTOWIRE_CONSTRUCTOR
	 * @see #AUTOWIRE_AUTODETECT
	 * @see #initializeBean
	 * @see #applyBeanPostProcessorsBeforeInitialization
	 * @see #applyBeanPostProcessorsAfterInitialization
	 */
	/**
	 * 使用指定的autowire策略实例化给定类的新bean实例。
	 * 这里支持此接口中定义的所有常量。
	 * 也可以通过{@code AUTOWIRE_NO}进行调用，以便仅应用实例化之前的回调（例如，用于注释驱动的注入）。
	 * 不不应用标准的{@link BeanPostProcessor BeanPostProcessors}回调或对bean进行任何进一步的初始化。
	 * 该接口为此目的提供了不同的细粒度操作，例如{@link #initializeBean}。
	 * 但是，如果适用于实例的构造，则将应用{@link InstantiationAwareBeanPostProcessor}回调。
	 * @param beanClass 要实例化的bean的类
	 * @param autowireMode 使用此接口中的常量按名称或类型
	 * @param dependencyCheck 是否对bean实例中的对象执行依赖检查不适用于自动装配a构造函数，因此在那里被忽略）
	 * @return 新的bean实例
	 * @throws BeansException 如果实例化或连接失败，则抛出BeansException
	 * @see #AUTOWIRE_NO
	 * @see #AUTOWIRE_BY_NAME
	 * @see #AUTOWIRE_BY_TYPE
	 * @see #AUTOWIRE_CONSTRUCTOR
	 * @see #AUTOWIRE_AUTODETECT
	 * @see #initializeBean
	 * @see #applyBeanPostProcessorsBeforeInitialization
	 * @see #applyBeanPostProcessorsAfterInitialization
	 */
	Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;

	/**
	 * Autowire the bean properties of the given bean instance by name or type.
	 * Can also be invoked with {@code AUTOWIRE_NO} in order to just apply
	 * after-instantiation callbacks (e.g. for annotation-driven injection).
	 * <p>Does <i>not</i> apply standard {@link BeanPostProcessor BeanPostProcessors}
	 * callbacks or perform any further initialization of the bean. This interface
	 * offers distinct, fine-grained operations for those purposes, for example
	 * {@link #initializeBean}. However, {@link InstantiationAwareBeanPostProcessor}
	 * callbacks are applied, if applicable to the configuration of the instance.
	 * @param existingBean the existing bean instance
	 * @param autowireMode by name or type, using the constants in this interface
	 * @param dependencyCheck whether to perform a dependency check for object
	 * references in the bean instance
	 * @throws BeansException if wiring failed
	 * @see #AUTOWIRE_BY_NAME
	 * @see #AUTOWIRE_BY_TYPE
	 * @see #AUTOWIRE_NO
	 */
	/**
	 * 按名称或类型自动装配给定bean实例的bean属性。
	 * 也可以使用{@code AUTOWIRE_NO}进行调用，以便仅应用实例化后的回调（例如，用于注释驱动的注入）。
	 * 不不应用标准的{@link BeanPostProcessor BeanPostProcessors}回调或对bean进行任何进一步的初始化。
	 * 该接口为此目的提供了不同的细粒度操作，例如{@link #initializeBean}。
	 * 但是，如果适用于实例的配置，则将应用{@link InstantiationAwareBeanPostProcessor}回调。
	 * @param existingBean 现有的bean实例
	 * @param autowireMode 通过名称或类型，使用此接口中的常量
	 * @param dependencyCheck 是否对对象执行依赖检查bean实例中的引用
	 * @throws BeansException 如果连接失败则抛出BeansException
	 * @see #AUTOWIRE_BY_NAME
	 * @see #AUTOWIRE_BY_TYPE
	 * @see #AUTOWIRE_NO
	 */
	void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)
			throws BeansException;

	/**
	 * Apply the property values of the bean definition with the given name to
	 * the given bean instance. The bean definition can either define a fully
	 * self-contained bean, reusing its property values, or just property values
	 * meant to be used for existing bean instances.
	 * <p>This method does <i>not</i> autowire bean properties; it just applies
	 * explicitly defined property values. Use the {@link #autowireBeanProperties}
	 * method to autowire an existing bean instance.
	 * <b>Note: This method requires a bean definition for the given name!</b>
	 * <p>Does <i>not</i> apply standard {@link BeanPostProcessor BeanPostProcessors}
	 * callbacks or perform any further initialization of the bean. This interface
	 * offers distinct, fine-grained operations for those purposes, for example
	 * {@link #initializeBean}. However, {@link InstantiationAwareBeanPostProcessor}
	 * callbacks are applied, if applicable to the configuration of the instance.
	 * @param existingBean the existing bean instance
	 * @param beanName the name of the bean definition in the bean factory
	 * (a bean definition of that name has to be available)
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
	 * if there is no bean definition with the given name
	 * @throws BeansException if applying the property values failed
	 * @see #autowireBeanProperties
	 */
	/**
	 * 将具有给定名称的bean定义的属性值应用于给定的bean实例。
	 * bean定义可以定义一个完全自包含的bean，重用其属性值，或者仅定义要用于现有bean实例的属性值。
	 * 此方法不会不自动装配bean属性；它仅适用显式定义的属性值。
	 * 使用{@link #autowireBeanProperties}方法自动连接现有的bean实例。
	 * 注意：此方法需要给定名称的Bean定义！不应用标准的{@link BeanPostProcessor BeanPostProcessors}回调或对它进行任何进一步的初始化豆。
	 * 该接口为此目的提供了不同的细粒度操作，例如{@link #initializeBean}。
	 * 但是，如果适用于实例的配置，则将应用{@link InstantiationAwareBeanPostProcessor}回调。
	 * @param existingBean 现有的bean实例
	 * @param beanName bean工厂中bean定义的名称（必须提供该名称的bean定义）
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException 如果存在没有给定名称的bean定义如果应用属性值失败，则
	 * @throws BeansException
	 * @see #autowireBeanProperties
	 */
	void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException;

	/**
	 * Initialize the given raw bean, applying factory callbacks
	 * such as {@code setBeanName} and {@code setBeanFactory},
	 * also applying all bean post processors (including ones which
	 * might wrap the given raw bean).
	 * <p>Note that no bean definition of the given name has to exist
	 * in the bean factory. The passed-in bean name will simply be used
	 * for callbacks but not checked against the registered bean definitions.
	 * @param existingBean the existing bean instance
	 * @param beanName the name of the bean, to be passed to it if necessary
	 * (only passed to {@link BeanPostProcessor BeanPostProcessors};
	 * can follow the {@link #ORIGINAL_INSTANCE_SUFFIX} convention in order to
	 * enforce the given instance to be returned, i.e. no proxies etc)
	 * @return the bean instance to use, either the original or a wrapped one
	 * @throws BeansException if the initialization failed
	 * @see #ORIGINAL_INSTANCE_SUFFIX
	 */
	/**
	 * 初始化给定的原始bean，应用工厂回调，例如{@code setBeanName}和{@code setBeanFactory}，还应用所有bean后处理器（包括可能包装给定原始bean的处理器）。
	 * 请注意，bean工厂中不必存在给定名称的bean定义。
	 * 传入的Bean名称将仅用于用于回调，但不会根据已注册的Bean定义进行检查。
	 * @param existingBean 现有的bean实例
	 * @param beanName 必要时将传递给它的bean的名称（仅传递给{@link BeanPostProcessor BeanPostProcessors}；可以依次遵循{@link #ORIGINAL_INSTANCE_SUFFIX}约定强制返回给定的实例，即没有代理等）
	 * @return 要使用的bean实例，无论是原始实例还是已包装的实例
	 * @throws BeansException 如果初始化失败，则抛出BeansException
	 * @see #ORIGINAL_INSTANCE_SUFFIX
	 */
	Object initializeBean(Object existingBean, String beanName) throws BeansException;

	/**
	 * Apply {@link BeanPostProcessor BeanPostProcessors} to the given existing bean
	 * instance, invoking their {@code postProcessBeforeInitialization} methods.
	 * The returned bean instance may be a wrapper around the original.
	 * @param existingBean the existing bean instance
	 * @param beanName the name of the bean, to be passed to it if necessary
	 * (only passed to {@link BeanPostProcessor BeanPostProcessors};
	 * can follow the {@link #ORIGINAL_INSTANCE_SUFFIX} convention in order to
	 * enforce the given instance to be returned, i.e. no proxies etc)
	 * @return the bean instance to use, either the original or a wrapped one
	 * @throws BeansException if any post-processing failed
	 * @see BeanPostProcessor#postProcessBeforeInitialization
	 * @see #ORIGINAL_INSTANCE_SUFFIX
	 */
	/**
	 * 将{@link BeanPostProcessor BeanPostProcessors}应用于给定的现有bean实例，调用其{@code postProcessBeforeInitialization}方法。
	 * 返回的bean实例可能是原始包装。
	 * @param existingBean 现有的bean实例
	 * @param beanName Bean的名称，必要时将传递给它（仅传递给{@link BeanPostProcessor BeanPostProcessors};可以按顺序遵循{@link #ORIGINAL_INSTANCE_SUFFIX}约定强制返回给定的实例，即没有代理等）
	 * @return 要使用的bean实例，无论是原始实例还是已包装的实例
	 * @throws BeansException 如果任何后处理失败，则抛出BeansException
	 * @see BeanPostProcessor＃postProcessBeforeInitialization
	 * @see #ORIGINAL_INSTANCE_SUFFIX
	 */
	Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
			throws BeansException;

	/**
	 * Apply {@link BeanPostProcessor BeanPostProcessors} to the given existing bean
	 * instance, invoking their {@code postProcessAfterInitialization} methods.
	 * The returned bean instance may be a wrapper around the original.
	 * @param existingBean the existing bean instance
	 * @param beanName the name of the bean, to be passed to it if necessary
	 * (only passed to {@link BeanPostProcessor BeanPostProcessors};
	 * can follow the {@link #ORIGINAL_INSTANCE_SUFFIX} convention in order to
	 * enforce the given instance to be returned, i.e. no proxies etc)
	 * @return the bean instance to use, either the original or a wrapped one
	 * @throws BeansException if any post-processing failed
	 * @see BeanPostProcessor#postProcessAfterInitialization
	 * @see #ORIGINAL_INSTANCE_SUFFIX
	 */
	/**
	 * 将{@link BeanPostProcessor BeanPostProcessors}应用于给定的现有bean实例，调用其{@code postProcessAfterInitialization}方法。返回的bean实例可能是原始包装。
	 * @param existingBean 现有的bean实例
	 * @param beanName 必要时将传递给它的bean的名称（仅传递给{@link BeanPostProcessor BeanPostProcessors}；可以依次遵循{@link #ORIGINAL_INSTANCE_SUFFIX}约定强制返回给定的实例，即没有代理等）
	 * @return 要使用的bean实例，无论是原始实例还是已包装的实例
	 * @throws BeansException 如果任何后处理失败
	 * @see BeanPostProcessor＃postProcessAfterInitialization
	 * @see #ORIGINAL_INSTANCE_SUFFIX
	 */
	Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
			throws BeansException;

	/**
	 * Destroy the given bean instance (typically coming from {@link #createBean}),
	 * applying the {@link org.springframework.beans.factory.DisposableBean} contract as well as
	 * registered {@link DestructionAwareBeanPostProcessor DestructionAwareBeanPostProcessors}.
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 * @param existingBean the bean instance to destroy
	 */
	/**
	 * 销毁给定的bean实例（通常来自{@link #createBean}），应用{@link org.springframework.beans.factory.DisposableBean}协定以及已注册的{@link DestructionAwareBeanPostProcessor DestructionAwareBeanPostProcessors}。
	 * 在破坏过程中出现的任何异常都应该被捕获并记录下来，而不是传播到此方法的调用者。
	 * @param existingBean 要销毁的bean实例
	 */
	void destroyBean(Object existingBean);


	//-------------------------------------------------------------------------
	// Delegate methods for resolving injection points
	//-------------------------------------------------------------------------

	/**
	 * Resolve the bean instance that uniquely matches the given object type, if any,
	 * including its bean name.
	 * <p>This is effectively a variant of {@link #getBean(Class)} which preserves the
	 * bean name of the matching instance.
	 * @param requiredType type the bean must match; can be an interface or superclass
	 * @return the bean name plus bean instance
	 * @throws NoSuchBeanDefinitionException if no matching bean was found
	 * @throws NoUniqueBeanDefinitionException if more than one matching bean was found
	 * @throws BeansException if the bean could not be created
	 * @since 4.3.3
	 * @see #getBean(Class)
	 */
	/**
	 * 解析与给定对象类型（如果有）唯一匹配的bean实例，包括其bean名称。
	 * 这实际上是{@link #getBean（Class）}的变体，它保留了匹配实例的bean名称。
	 * @param requiredType bean必须匹配的类型；可以是接口或超类
	 * @return bean名称和bean实例如果没有找到匹配的bean，则
	 * @throws NoSuchBeanDefinitionException 如果找到多个匹配的bean，则
	 * @throws NoUniqueBeanDefinitionException 如果无法创建bean，则
	 * @throws BeansException
	 * @see #getBean（Class）
	 */
	<T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType) throws BeansException;

	/**
	 * Resolve a bean instance for the given bean name, providing a dependency descriptor
	 * for exposure to target factory methods.
	 * <p>This is effectively a variant of {@link #getBean(String, Class)} which supports
	 * factory methods with an {@link org.springframework.beans.factory.InjectionPoint}
	 * argument.
	 * @param name the name of the bean to look up
	 * @param descriptor the dependency descriptor for the requesting injection point
	 * @return the corresponding bean instance
	 * @throws NoSuchBeanDefinitionException if there is no bean with the specified name
	 * @throws BeansException if the bean could not be created
	 * @since 5.1.5
	 * @see #getBean(String, Class)
	 */
	/**
	 * 为给定的bean名称解析一个bean实例，提供一个依赖项描述符公开给目标工厂方法。
	 * 这实际上是{@link #getBean（String，Class）}的变体，它支持带有{@link org.springframework.beans.factory.InjectionPoint} 数的工厂方法。
	 * @param name 要查找的bean的名称
	 * @param descriptor 是请求注入点的依赖项描述符
	 * @return 相应的bean实例
	 * @throws NoSuchBeanDefinitionException 如果没有指定名称的bean
	 * @throws BeansException 如果无法创建bean
	 * @see #getBean（String，Class）
	 */
	Object resolveBeanByName(String name, DependencyDescriptor descriptor) throws BeansException;

	/**
	 * Resolve the specified dependency against the beans defined in this factory.
	 * @param descriptor the descriptor for the dependency (field/method/constructor)
	 * @param requestingBeanName the name of the bean which declares the given dependency
	 * @return the resolved object, or {@code null} if none found
	 * @throws NoSuchBeanDefinitionException if no matching bean was found
	 * @throws NoUniqueBeanDefinitionException if more than one matching bean was found
	 * @throws BeansException if dependency resolution failed for any other reason
	 * @since 2.5
	 * @see #resolveDependency(DependencyDescriptor, String, Set, TypeConverter)
	 */
	/**
	 * 解决针对此工厂中定义的Bean的指定依赖关系。
	 * @param descriptor 依赖项（字段/方法/构造函数）的描述符
	 * @param requestingBeanName 声明给定依赖项的bean的名称
	 * @return 已解析的对象，如果找不到则返回{@code null}
	 * @throws NoSuchBeanDefinitionException 如果没有找到匹配的bean
	 * @throws NoUniqueBeanDefinitionException 如果找到多个匹配的bean
	 * @throws BeansException 如果依赖项解析由于任何其他原因而失败
	 * @see #resolveDependency（DependencyDescriptor，String，Set，TypeConverter）
	 */
	@Nullable
	Object resolveDependency(DependencyDescriptor descriptor, @Nullable String requestingBeanName) throws BeansException;

	/**
	 * Resolve the specified dependency against the beans defined in this factory.
	 * @param descriptor the descriptor for the dependency (field/method/constructor)
	 * @param requestingBeanName the name of the bean which declares the given dependency
	 * @param autowiredBeanNames a Set that all names of autowired beans (used for
	 * resolving the given dependency) are supposed to be added to
	 * @param typeConverter the TypeConverter to use for populating arrays and collections
	 * @return the resolved object, or {@code null} if none found
	 * @throws NoSuchBeanDefinitionException if no matching bean was found
	 * @throws NoUniqueBeanDefinitionException if more than one matching bean was found
	 * @throws BeansException if dependency resolution failed for any other reason
	 * @since 2.5
	 * @see DependencyDescriptor
	 */
	/**
	 * 解决针对此工厂中定义的Bean的指定依赖关系。
	 * @param descriptor 依赖项的描述符（字段/方法/构造函数）
	 * @param requestingBeanName 声明给定依赖项的bean的名
	 * @param autowiredBeanNames 一个设置为所有自动装配的bean的名称（用于解决给定的依赖项） ）应该添加到
	 * @param typeConverter TypeConverter中，以用于填充数组和集合
	 * @return 已解析的对象；如果找不到，则为{@code null}如果找不到匹配的bean，则
	 * @throws NoSuchBeanDefinitionException
	 * @throws NoUniqueBeanDefinitionException 如果找到了多个匹配的bean
	 * @throws BeansException 如果依赖项解析由于任何其他原因而失败
	 * @see DependencyDescriptor
	 */
	@Nullable
	Object resolveDependency(DependencyDescriptor descriptor, @Nullable String requestingBeanName,
			@Nullable Set<String> autowiredBeanNames, @Nullable TypeConverter typeConverter) throws BeansException;

}
