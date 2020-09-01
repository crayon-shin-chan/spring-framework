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

package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

/**
 * The root interface for accessing a Spring bean container.
 *
 * <p>This is the basic client view of a bean container;
 * further interfaces such as {@link ListableBeanFactory} and
 * {@link org.springframework.beans.factory.config.ConfigurableBeanFactory}
 * are available for specific purposes.
 *
 * <p>This interface is implemented by objects that hold a number of bean definitions,
 * each uniquely identified by a String name. Depending on the bean definition,
 * the factory will return either an independent instance of a contained object
 * (the Prototype design pattern), or a single shared instance (a superior
 * alternative to the Singleton design pattern, in which the instance is a
 * singleton in the scope of the factory). Which type of instance will be returned
 * depends on the bean factory configuration: the API is the same. Since Spring
 * 2.0, further scopes are available depending on the concrete application
 * context (e.g. "request" and "session" scopes in a web environment).
 *
 * <p>The point of this approach is that the BeanFactory is a central registry
 * of application components, and centralizes configuration of application
 * components (no more do individual objects need to read properties files,
 * for example). See chapters 4 and 11 of "Expert One-on-One J2EE Design and
 * Development" for a discussion of the benefits of this approach.
 *
 * <p>Note that it is generally better to rely on Dependency Injection
 * ("push" configuration) to configure application objects through setters
 * or constructors, rather than use any form of "pull" configuration like a
 * BeanFactory lookup. Spring's Dependency Injection functionality is
 * implemented using this BeanFactory interface and its subinterfaces.
 *
 * <p>Normally a BeanFactory will load bean definitions stored in a configuration
 * source (such as an XML document), and use the {@code org.springframework.beans}
 * package to configure the beans. However, an implementation could simply return
 * Java objects it creates as necessary directly in Java code. There are no
 * constraints on how the definitions could be stored: LDAP, RDBMS, XML,
 * properties file, etc. Implementations are encouraged to support references
 * amongst beans (Dependency Injection).
 *
 * <p>In contrast to the methods in {@link ListableBeanFactory}, all of the
 * operations in this interface will also check parent factories if this is a
 * {@link HierarchicalBeanFactory}. If a bean is not found in this factory instance,
 * the immediate parent factory will be asked. Beans in this factory instance
 * are supposed to override beans of the same name in any parent factory.
 *
 * <p>Bean factory implementations should support the standard bean lifecycle interfaces
 * as far as possible. The full set of initialization methods and their standard order is:
 * <ol>
 * <li>BeanNameAware's {@code setBeanName}
 * <li>BeanClassLoaderAware's {@code setBeanClassLoader}
 * <li>BeanFactoryAware's {@code setBeanFactory}
 * <li>EnvironmentAware's {@code setEnvironment}
 * <li>EmbeddedValueResolverAware's {@code setEmbeddedValueResolver}
 * <li>ResourceLoaderAware's {@code setResourceLoader}
 * (only applicable when running in an application context)
 * <li>ApplicationEventPublisherAware's {@code setApplicationEventPublisher}
 * (only applicable when running in an application context)
 * <li>MessageSourceAware's {@code setMessageSource}
 * (only applicable when running in an application context)
 * <li>ApplicationContextAware's {@code setApplicationContext}
 * (only applicable when running in an application context)
 * <li>ServletContextAware's {@code setServletContext}
 * (only applicable when running in a web application context)
 * <li>{@code postProcessBeforeInitialization} methods of BeanPostProcessors
 * <li>InitializingBean's {@code afterPropertiesSet}
 * <li>a custom init-method definition
 * <li>{@code postProcessAfterInitialization} methods of BeanPostProcessors
 * </ol>
 *
 * <p>On shutdown of a bean factory, the following lifecycle methods apply:
 * <ol>
 * <li>{@code postProcessBeforeDestruction} methods of DestructionAwareBeanPostProcessors
 * <li>DisposableBean's {@code destroy}
 * <li>a custom destroy-method definition
 * </ol>
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 13 April 2001
 * @see BeanNameAware#setBeanName
 * @see BeanClassLoaderAware#setBeanClassLoader
 * @see BeanFactoryAware#setBeanFactory
 * @see org.springframework.context.ResourceLoaderAware#setResourceLoader
 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher
 * @see org.springframework.context.MessageSourceAware#setMessageSource
 * @see org.springframework.context.ApplicationContextAware#setApplicationContext
 * @see org.springframework.web.context.ServletContextAware#setServletContext
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization
 * @see InitializingBean#afterPropertiesSet
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getInitMethodName
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization
 * @see DisposableBean#destroy
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getDestroyMethodName
 */

/**
 * 访问Spring bean容器的根接口。这是bean容器的基本客户端视图；
 * 其他接口，例如{@link ListableBeanFactory}和{@link org.springframework.beans.factory.config.ConfigurableBeanFactory}可用于特定目的。
 * 此接口由包含多个bean定义的对象实现，每个定义都由String名称唯一标识。
 * 根据bean的定义，工厂将返回一个包含对象的独立实例（原型设计模式），或者返回一个共享实例（对于Singleton设计模式的替代实例是）单例在工厂范围内）。
 * 将返回哪种类型的实例取决于bean工厂的配置：API是相同的。
 * 从Spring * 2.0开始，根据具体的应用程序上下文，可以使用更多范围（例如，网络环境中的“request”和“session”范围）。
 * 此方法的重点是BeanFactory是应用程序组件的中央注册表，并集中了应用程序组件的配置（例如，不再需要单个对象读取属性文件）。
 * 有关此方法的好处的讨论，请参见“一对一的J2EE专家设计和开发”的第4章和第11章。
 * 请注意，通常最好依赖于依赖注入（“推送”配置）通过设置器或构造函数配置应用程序对象，而不是使用任何形式的拉配置（例如BeanFactory查找）。
 * 使用此BeanFactory接口及其子接口可以实现Spring的依赖注入功能。
 * 通常，BeanFactory将加载存储在配置源（例如XML文档）中的bean定义，并使用{@code org.springframework.beans}包来配置bean。
 * 但是，实现可以根据需要直接在Java代码中直接返回创建的Java对象。
 * 定义的存储方式没有任何限制：LDAP，RDBMS，XML，属性文件等。鼓励实现在bean之间支持引用（依赖注入）。
 * 与{@link ListableBeanFactory}中的方法相反，如果这是一个{@link HierarchicalBeanFactory}，则此接口中的所有操作还将检查父工厂。
 * 如果在此工厂实例中未找到bean，则将询问直接的父工厂。
 * 该工厂实例中的Bean应该覆盖任何父工厂中同名的Bean。
 * Bean工厂实现应尽可能支持标准Bean生命周期接口。
 * 全套初始化方法及其标准顺序为：
 * BeanNameAware的{@code setBeanName}
 * BeanClassLoaderAware的{@code setBeanClassLoader}
 * BeanFactoryAware的{@code setBeanFactory}
 * EnvironmentAware的{@code setEnvironment}
 * EmbeddedValueResolverAware的{@code setEmbeddedValueResolver}
 * ResourceLoaderAware的{@code setResourceLoader}（仅在应用程序上下文中运行时适用）
 * ApplicationEventPublisherAware的{@code setApplicationEventPublisher}在应用程序上下文中运行时适用）
 * MessageSourceAware的{@code setMessageSource}（仅在应用程序上下文中运行时适用）
 * ApplicationContextAware的{@code setApplicationContext}（仅在应用程序上下文中运行时适用）
 * ServletContextAware的{@code setServletContext}（仅适用于在Web应用程序上下文中运行）
 * {@code postProcessBeforeInitialization} methods of BeanPostProcessors
 * InitializingBean的{@code afterPropertiesSet}
 * 自定义初始化方法定义
 * BeanPostProcessors的{@code postProcessAfterInitialization}方法
 * 在关闭bean工厂时，适用以下生命周期方法：DeolwareAwareBeanPostProcessors的{@code postProcessBeforeDestruction}方法
 * DisposableBean的{@code destroy}
 * 自定义销毁方法定义
 * @see BeanNameAware＃setBeanName
 * @see BeanClassLoaderAware＃setBeanClassLoader
 * @see BeanFactoryAware＃setBeanFactory
 * @see org.springframework.context.ResourceLoaderAware＃setResourceLoader
 * @see org.springframework.context.ApplicationEventPublisherAware＃setApplicationEventPublisher
 * @see org.springframework.context.MessageSourceAware＃setMessageSource
 * @see org.springframework.context.ApplicationContextAware＃setApplicationContext
 * @see org.springframew ork.web.context.ServletContextAware＃setServletContext
 * @see org.springframework.beans.factory.config.BeanPostProcessor＃postProcessBeforeInitialization
 * @see InitializingBean＃afterPropertiesSet
 * @see org.springframework.beans.factory.support.RootBeanDefinition＃getInitMethodName
 * @see org.springframework.beans.factory.config.BeanPostProcessor＃postProcessAfterInitialization
 * @see DisposableBean＃destroy
 * @see org.springframework.beans.factory.support.RootBeanDefinition＃getDestroyMethodName
 */
public interface BeanFactory {

	/**
	 * Used to dereference a {@link FactoryBean} instance and distinguish it from
	 * beans <i>created</i> by the FactoryBean. For example, if the bean named
	 * {@code myJndiObject} is a FactoryBean, getting {@code &myJndiObject}
	 * will return the factory, not the instance returned by the factory.
	 */
	/**
	 * 用于引用{@link FactoryBean}实例，并将其与由FactoryBean创建的bean区别开来。
	 * 例如，如果名为{@code myJndiObject}的bean是FactoryBean，则获取{@code＆myJndiObject}将返回工厂，而不是工厂返回的实例。
	 * 工厂Bean名称前缀
	 */
	String FACTORY_BEAN_PREFIX = "&";


	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>This method allows a Spring BeanFactory to be used as a replacement for the
	 * Singleton or Prototype design pattern. Callers may retain references to
	 * returned objects in the case of Singleton beans.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to retrieve
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no bean with the specified name
	 * @throws BeansException if the bean could not be obtained
	 */
	/**
	 * 返回一个实例，该实例可以是指定bean的共享或独立的。
	 * 此方法允许使用Spring BeanFactory替代Singleton或Prototype设计模式。
	 * 对于Singleton Bean，调用者可以保留对返回对象的引用。
	 * 将别名转换回相应的规范bean名称。
	 * 如果在该工厂实例中找不到该bean，将询问父工厂。
	 * @param name 要检索的bean的名称
	 * @return bean的实例
	 * @throws NoSuchBeanDefinitionException 如果没有指定名称的bean
	 * @throws BeansException 如果无法获得bean
	 */
	Object getBean(String name) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Behaves the same as {@link #getBean(String)}, but provides a measure of type
	 * safety by throwing a BeanNotOfRequiredTypeException if the bean is not of the
	 * required type. This means that ClassCastException can't be thrown on casting
	 * the result correctly, as can happen with {@link #getBean(String)}.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to retrieve
	 * @param requiredType type the bean must match; can be an interface or superclass
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanNotOfRequiredTypeException if the bean is not of the required type
	 * @throws BeansException if the bean could not be created
	 */
	/**
	 * 返回一个实例，该实例可以是指定bean的共享或独立的。
	 * 与{@link #getBean（String）}的行为相同，但是如果Bean不是必需的类型，则抛出BeanNotOfRequiredTypeException来提供类型安全性的度量。
	 * 这意味着无法正确投射结果，例如{@link #getBean（String）}会抛出ClassCastException。
	 * 将别名转换回相应的规范bean名称。
	 * 将询问父工厂，如果在该工厂实例中找不到该bean。
	 * @param name 要检索的bean的名称
	 * @param requiredType 类型bean必须匹配的类型；可以是接口或超类
	 * @return bean的实例
	 * @throws NoSuchBeanDefinitionException 如果没有这样的bean定义
	 * @throws BeanNotOfRequiredTypeException 如果bean不是必需的类型
	 * @throws BeansException 如果不能创建bean
	 */
	<T> T getBean(String name, Class<T> requiredType) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Allows for specifying explicit constructor arguments / factory method arguments,
	 * overriding the specified default arguments (if any) in the bean definition.
	 * @param name the name of the bean to retrieve
	 * @param args arguments to use when creating a bean instance using explicit arguments
	 * (only applied when creating a new instance as opposed to retrieving an existing one)
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanDefinitionStoreException if arguments have been given but
	 * the affected bean isn't a prototype
	 * @throws BeansException if the bean could not be created
	 * @since 2.5
	 */
	/**
	 * 返回一个实例，该实例可以是指定bean的共享或独立的。
	 * 允许指定显式构造函数参数/工厂方法参数，覆盖Bean定义中指定的默认参数（如果有）。
	 * @paramname 要检索的bean的名称
	 * @param args 使用显式参数创建bean实例时要使用的参数（仅在创建新实例而不是检索现有实例时才应用）
	 * @return 的实例bean
	 * @throws NoSuchBeanDefinitionException 如果没有这样的bean定义
	 * @throws BeanDefinitionStoreException 如果给出了参数但受影响的bean不是原型
	 * @throws BeansException 如果无法创建bean
	 */
	Object getBean(String name, Object... args) throws BeansException;

	/**
	 * Return the bean instance that uniquely matches the given object type, if any.
	 * <p>This method goes into {@link ListableBeanFactory} by-type lookup territory
	 * but may also be translated into a conventional by-name lookup based on the name
	 * of the given type. For more extensive retrieval operations across sets of beans,
	 * use {@link ListableBeanFactory} and/or {@link BeanFactoryUtils}.
	 * @param requiredType type the bean must match; can be an interface or superclass
	 * @return an instance of the single bean matching the required type
	 * @throws NoSuchBeanDefinitionException if no bean of the given type was found
	 * @throws NoUniqueBeanDefinitionException if more than one bean of the given type was found
	 * @throws BeansException if the bean could not be created
	 * @since 3.0
	 * @see ListableBeanFactory
	 */
	/**
	 * 返回与给定对象类型唯一匹配的bean实例（如果有）
	 * 此方法进入{@link ListableBeanFactory}按类型查找区域，但也可以根据给定类型的名称转换为常规的按名称查找。
	 * 对于跨bean集的更广泛的检索操作，请使用{@link ListableBeanFactory}和{@link BeanFactoryUtils}。
	 * @param requiredType bean必须匹配的类型；可以是接口或超类
	 * @return 与所需类型匹配的单个bean的实例
	 * @throws NoSuchBeanDefinitionException 如果没有找到给定类型的bean
	 * @throws NoUniqueBeanDefinitionException 如果找到了给定类型的多个bean
	 * @throws BeansException 如果无法创建bean
	 * @see ListableBeanFactory
	 */
	<T> T getBean(Class<T> requiredType) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Allows for specifying explicit constructor arguments / factory method arguments,
	 * overriding the specified default arguments (if any) in the bean definition.
	 * <p>This method goes into {@link ListableBeanFactory} by-type lookup territory
	 * but may also be translated into a conventional by-name lookup based on the name
	 * of the given type. For more extensive retrieval operations across sets of beans,
	 * use {@link ListableBeanFactory} and/or {@link BeanFactoryUtils}.
	 * @param requiredType type the bean must match; can be an interface or superclass
	 * @param args arguments to use when creating a bean instance using explicit arguments
	 * (only applied when creating a new instance as opposed to retrieving an existing one)
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanDefinitionStoreException if arguments have been given but
	 * the affected bean isn't a prototype
	 * @throws BeansException if the bean could not be created
	 * @since 4.1
	 */
	/**
	 * 返回一个实例，该实例可以是指定bean的共享或独立的。
	 * 允许指定显式构造函数参数/工厂方法参数，覆盖Bean定义中指定的默认参数（如果有）。
	 * 此方法进入{@link ListableBeanFactory}按类型查找区域，但也可以根据给定类型的名称转换为常规的按名称查找。
	 * 对于跨bean集的更广泛的检索操作，请使用{@link ListableBeanFactory}和{@link BeanFactoryUtils}。
	 * @param requiredType bean必须匹配的类型；可以是接口或超类
	 * @param args 参数在使用显式参数创建bean实例时使用（仅在创建新实例而不是检索现有实例时才应用）
	 * @return bean的实例
	 * @throws NoSuchBeanDefinitionException 如果没有这样的bean定义
	 * @throws BeanDefinitionStoreException 如果给出了参数，但是受影响的bean不是原型
	 * @throws BeansException 如果无法创建bean
	 */
	<T> T getBean(Class<T> requiredType, Object... args) throws BeansException;

	/**
	 * Return a provider for the specified bean, allowing for lazy on-demand retrieval
	 * of instances, including availability and uniqueness options.
	 * @param requiredType type the bean must match; can be an interface or superclass
	 * @return a corresponding provider handle
	 * @since 5.1
	 * @see #getBeanProvider(ResolvableType)
	 */
	/**
	 * 返回指定bean的提供程序，以允许按需延迟检索实例，包括可用性和唯一性选项。
	 * @param requiredType bean必须匹配的类型；可以是接口或超类
	 * @return 相应的提供程序句柄
	 * @see #getBeanProvider（ResolvableType）
	 */
	<T> ObjectProvider<T> getBeanProvider(Class<T> requiredType);

	/**
	 * Return a provider for the specified bean, allowing for lazy on-demand retrieval
	 * of instances, including availability and uniqueness options.
	 * @param requiredType type the bean must match; can be a generic type declaration.
	 * Note that collection types are not supported here, in contrast to reflective
	 * injection points. For programmatically retrieving a list of beans matching a
	 * specific type, specify the actual bean type as an argument here and subsequently
	 * use {@link ObjectProvider#orderedStream()} or its lazy streaming/iteration options.
	 * @return a corresponding provider handle
	 * @since 5.1
	 * @see ObjectProvider#iterator()
	 * @see ObjectProvider#stream()
	 * @see ObjectProvider#orderedStream()
	 */
	/**
	 * 返回指定bean的提供程序，以允许按需延迟检索实例，包括可用性和唯一性选项。
	 * @param requiredType bean必须匹配的类型；可以是通用类型声明。
	 * 请注意，与反射注入点相比，此处不支持收集类型。
	 * 要以编程方式检索与特定类型匹配的bean列表，请在此处指定实际的bean类型作为参数，然后使用{@link ObjectProvider＃orderedStream（）}或它的延迟流/迭代选项。
	 * @return 相应的提供程序句柄
	 * @see ObjectProvider＃iterator（）
	 * @see ObjectProvider＃stream（）
	 * @see ObjectProvider＃orderedStream（）
	 */
	<T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType);

	/**
	 * Does this bean factory contain a bean definition or externally registered singleton
	 * instance with the given name?
	 * <p>If the given name is an alias, it will be translated back to the corresponding
	 * canonical bean name.
	 * <p>If this factory is hierarchical, will ask any parent factory if the bean cannot
	 * be found in this factory instance.
	 * <p>If a bean definition or singleton instance matching the given name is found,
	 * this method will return {@code true} whether the named bean definition is concrete
	 * or abstract, lazy or eager, in scope or not. Therefore, note that a {@code true}
	 * return value from this method does not necessarily indicate that {@link #getBean}
	 * will be able to obtain an instance for the same name.
	 * @param name the name of the bean to query
	 * @return whether a bean with the given name is present
	 */
	/**
	 * 该bean工厂是否包含具有给定名称的bean定义或外部注册的singleton实例
	 * 如果给定名称是别名，它将被转换回相应的规范bean名称。
	 * 如果该工厂是分层工厂，将询问任何父工厂，如果在该工厂实例中找不到该bean。
	 * 如果找到了与给定名称匹配的bean定义或单例实例，则无论命名的bean定义是具体的还是抽象的，惰性的或渴望的，此方法都将返回{@code true}。
	 * 因此，请注意，此方法的{@code true}返回值不一定表示{@link #getBean}将能够获得具有相同名称的实例。
	 * @param name 要查询的bean的名称
	 * @return 是否存在具有给定名称的bean
	 */
	boolean containsBean(String name);

	/**
	 * Is this bean a shared singleton? That is, will {@link #getBean} always
	 * return the same instance?
	 * <p>Note: This method returning {@code false} does not clearly indicate
	 * independent instances. It indicates non-singleton instances, which may correspond
	 * to a scoped bean as well. Use the {@link #isPrototype} operation to explicitly
	 * check for independent instances.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @return whether this bean corresponds to a singleton instance
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @see #getBean
	 * @see #isPrototype
	 */
	/**
	 * 该bean是共享单身吗,也就是说，{@link #getBean}是否总是返回相同的实例
	 * 注意：此返回{@code false}的方法不能清楚地表明独立实例。它表示非单实例，也可能对应于作用域bean。
	 * 使用{@link #isPrototype}操作来显式检查独立实例。
	 * 将别名转换回相应的规范bean名称。
	 * 将询问父工厂，如果在该工厂实例中找不到该bean。
	 * @param name 要查询的bean的名称
	 * @return 此bean是否对应于一个单例实例
	 * @throws NoSuchBeanDefinitionException 如果没有给定名称的bean，则抛出NoSuchBeanDefinitionException
	 * @see #getBean
	 * @see #isPrototype
	 */
	boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Is this bean a prototype? That is, will {@link #getBean} always return
	 * independent instances?
	 * <p>Note: This method returning {@code false} does not clearly indicate
	 * a singleton object. It indicates non-independent instances, which may correspond
	 * to a scoped bean as well. Use the {@link #isSingleton} operation to explicitly
	 * check for a shared singleton instance.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @return whether this bean will always deliver independent instances
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.0.3
	 * @see #getBean
	 * @see #isSingleton
	 */
	/**
	 * 这个豆是原型吗,也就是说，{@link #getBean}将始终返回独立实例吗
	 * 注意：此返回{@code false}的方法不能清楚地表明一个单例对象。
	 * 它指示非独立实例，也可能对应于作用域bean。
	 * 使用{@link #isSingleton}操作来显式检查共享的单例实例。
	 * 将别名转换回相应的规范bean名称。
	 * 将询问父工厂，如果在该工厂实例中找不到该bean。
	 * @param name 要查询的bean的名称
	 * @return 此bean是否将始终提供独立的实例
	 * @throws NoSuchBeanDefinitionException 如果没有给定名称的bean，则抛出NoSuchBeanDefinitionException
	 * @see #getBean
	 * @see＃ isSingleton
	 */
	boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Check whether the bean with the given name matches the specified type.
	 * More specifically, check whether a {@link #getBean} call for the given name
	 * would return an object that is assignable to the specified target type.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @param typeToMatch the type to match against (as a {@code ResolvableType})
	 * @return {@code true} if the bean type matches,
	 * {@code false} if it doesn't match or cannot be determined yet
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 4.2
	 * @see #getBean
	 * @see #getType
	 */
	/**
	 * 检查具有给定名称的Bean是否与指定的类型匹配。
	 * 更具体地说，检查对给定名称的{@link #getBean}调用是否将返回可分配给指定目标类型的对象。
	 * 将别名转换回相应的规范bean名称。
	 * 将询问父工厂，如果在该工厂实例中找不到该bean。
	 * @param name 要查询的bean的名称
	 * @param typeToMatch 要匹配的类型（作为{@code ResolvableType}）
	 * @return {@code true}如果bean类型匹配，{@code false}如果它不匹配或尚未确定
	 * @throws NoSuchBeanDefinitionException 如果没有给定名称的bean
	 * @see #getBean
	 * @see #getType
	 */
	boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException;

	/**
	 * Check whether the bean with the given name matches the specified type.
	 * More specifically, check whether a {@link #getBean} call for the given name
	 * would return an object that is assignable to the specified target type.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @param typeToMatch the type to match against (as a {@code Class})
	 * @return {@code true} if the bean type matches,
	 * {@code false} if it doesn't match or cannot be determined yet
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.0.1
	 * @see #getBean
	 * @see #getType
	 */
	/**
	 * 检查具有给定名称的Bean是否与指定的类型匹配。更具体地说，检查对给定名称的{@link #getBean}调用是否将返回可分配给指定目标类型的对象。
	 * 将别名转换回相应的规范bean名称。
	 * 将询问父工厂，如果在该工厂实例中找不到该bean。
	 * @param name 要查询的bean的名称
	 * @param typeToMatch 要匹配的类型（作为{@code Class}）
	 * @return {@code true}如果bean类型匹配，{@code false}如果它不匹配或尚未确定
	 * @throws NoSuchBeanDefinitionException 如果没有给定名称的bean
	 * @see #getBean * @see #getType
	 */
	boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException;

	/**
	 * Determine the type of the bean with the given name. More specifically,
	 * determine the type of object that {@link #getBean} would return for the given name.
	 * <p>For a {@link FactoryBean}, return the type of object that the FactoryBean creates,
	 * as exposed by {@link FactoryBean#getObjectType()}. This may lead to the initialization
	 * of a previously uninitialized {@code FactoryBean} (see {@link #getType(String, boolean)}).
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @return the type of the bean, or {@code null} if not determinable
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 1.1.2
	 * @see #getBean
	 * @see #isTypeMatch
	 */
	/**
	 * 确定具有给定名称的bean的类型。更具体地说，
	 * 确定{@link #getBean}将针对给定名称返回的对象的类型。
	 * 对于{@link FactoryBean}，返回由{@link FactoryBean＃getObjectType（）}公开的FactoryBean创建的对象的类型。
	 * 这可能导致先前未初始化的{@code FactoryBean}的初始化（请参阅{@link #getType（String，boolean）}）。
	 * 将别名转换回相应的规范bean名称。
	 * 将询问父工厂，如果在该工厂实例中找不到该bean。
	 * @param name 要查询的bean的名称
	 * @return bean的类型，如果不确定则返回{@code null}
	 * @throws NoSuchBeanDefinitionException 如果没有给定名称的bean，则抛出NoSuchBeanDefinitionException
	 */
	@Nullable
	Class<?> getType(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Determine the type of the bean with the given name. More specifically,
	 * determine the type of object that {@link #getBean} would return for the given name.
	 * <p>For a {@link FactoryBean}, return the type of object that the FactoryBean creates,
	 * as exposed by {@link FactoryBean#getObjectType()}. Depending on the
	 * {@code allowFactoryBeanInit} flag, this may lead to the initialization of a previously
	 * uninitialized {@code FactoryBean} if no early type information is available.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the name of the bean to query
	 * @param allowFactoryBeanInit whether a {@code FactoryBean} may get initialized
	 * just for the purpose of determining its object type
	 * @return the type of the bean, or {@code null} if not determinable
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 5.2
	 * @see #getBean
	 * @see #isTypeMatch
	 */
	/**
	 * 确定具有给定名称的bean的类型。更具体地说，确定{@link #getBean}将针对给定名称返回的对象的类型。
	 * 对于{@link FactoryBean}，返回由{@link FactoryBean＃getObjectType（）}公开的FactoryBean创建的对象的类型。
	 * 如果没有早期类型信息，则取决于{@code allowFactoryBeanInit}标志，这可能导致先前未初始化的{@code FactoryBean}的初始化。
	 * 将别名转换回相应的规范bean名称。
	 * 将询问父工厂，如果在该工厂实例中找不到该bean。
	 * @param name 为要查询的bean的名称
	 * @param allowFactoryBeanInit 是否可以初始化{@code FactoryBean} 仅用于确定其对象类型
	 * @return bean的类型，或{@code null}如果不是可确定的
	 * @throws NoSuchBeanDefinitionException 如果没有给定名称的bean
	 * @see #getBean
	 * @see #isTypeMatch
	 */
	@Nullable
	Class<?> getType(String name, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException;

	/**
	 * Return the aliases for the given bean name, if any.
	 * <p>All of those aliases point to the same bean when used in a {@link #getBean} call.
	 * <p>If the given name is an alias, the corresponding original bean name
	 * and other aliases (if any) will be returned, with the original bean name
	 * being the first element in the array.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 * @param name the bean name to check for aliases
	 * @return the aliases, or an empty array if none
	 * @see #getBean
	 */
	/**
	 * 返回给定bean名称的别名（如果有）。
	 * 在{@link #getBean}调用中使用时，所有这些别名都指向同一个bean。
	 * 如果给定名称是别名，则将返回相应的原始bean名称和其他别名（如果有），原始Bean名称*是数组中的第一个元素。
	 * 将询问父工厂，如果在该工厂实例中找不到该bean。
	 * @param name 为要检查别名的bean名称命名
	 * @return 别名，如果没有别名则返回一个空数组
	 * @see #getBean
	 */
	String[] getAliases(String name);

}
