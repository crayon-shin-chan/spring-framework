/*
 * Copyright 2002-2017 the original author or authors.
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

import java.util.Iterator;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.lang.Nullable;

/**
 * Configuration interface to be implemented by most listable bean factories.
 * In addition to {@link ConfigurableBeanFactory}, it provides facilities to
 * analyze and modify bean definitions, and to pre-instantiate singletons.
 *
 * <p>This subinterface of {@link org.springframework.beans.factory.BeanFactory}
 * is not meant to be used in normal application code: Stick to
 * {@link org.springframework.beans.factory.BeanFactory} or
 * {@link org.springframework.beans.factory.ListableBeanFactory} for typical
 * use cases. This interface is just meant to allow for framework-internal
 * plug'n'play even when needing access to bean factory configuration methods.
 *
 * @author Juergen Hoeller
 * @since 03.11.2003
 * @see org.springframework.context.support.AbstractApplicationContext#getBeanFactory()
 */

/**
 * 大多数可列出的bean工厂都将实现配置接口。
 * 除了{@link ConfigurableBeanFactory}外，它还提供了用于分析和修改bean定义以及预先实例化单例的工具。
 * {@link org.springframework.beans.factory.BeanFactory}的此子接口不打算在常规应用程序代码中使用：
 * 坚持使用{@link org.springframework.beans.factory.BeanFactory}或典型情况下的{@link org.springframework.beans.factory.ListableBeanFactory}。
 * 即使需要访问bean工厂配置方法，该接口也仅允许框架内部即插即用。
 * @see org.springframework.context.support.AbstractApplicationContext＃getBeanFactory（）
 */
public interface ConfigurableListableBeanFactory
		extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

	/**
	 * Ignore the given dependency type for autowiring:
	 * for example, String. Default is none.
	 * @param type the dependency type to ignore
	 */
	/**
	 * 忽略给定的依赖类型进行自动装配：例如，字符串。默认为无。
	 * @param type 忽略的依赖类型
	 */
	void ignoreDependencyType(Class<?> type);

	/**
	 * Ignore the given dependency interface for autowiring.
	 * <p>This will typically be used by application contexts to register
	 * dependencies that are resolved in other ways, like BeanFactory through
	 * BeanFactoryAware or ApplicationContext through ApplicationContextAware.
	 * <p>By default, only the BeanFactoryAware interface is ignored.
	 * For further types to ignore, invoke this method for each type.
	 * @param ifc the dependency interface to ignore
	 * @see org.springframework.beans.factory.BeanFactoryAware
	 * @see org.springframework.context.ApplicationContextAware
	 */
	/**
	 * 忽略给定的依赖接口进行自动装配。
	 * 通常由应用程序上下文用来注册以其他方式解析的依赖项，例如通过BeanFactoryAware通过BeanFactory或通过ApplicationContextAware通过ApplicationContext进行解析。
	 * 默认情况下，仅BeanFactoryAware接口被忽略。要忽略其他类型，请为每种类型调用此方法。
	 * @param ifc 忽略的依赖关系接口
	 * @see org.springframework.beans.factory.BeanFactoryAware
	 * @see org.springframework.context.ApplicationContextAware
	 */
	void ignoreDependencyInterface(Class<?> ifc);

	/**
	 * Register a special dependency type with corresponding autowired value.
	 * <p>This is intended for factory/context references that are supposed
	 * to be autowirable but are not defined as beans in the factory:
	 * e.g. a dependency of type ApplicationContext resolved to the
	 * ApplicationContext instance that the bean is living in.
	 * <p>Note: There are no such default types registered in a plain BeanFactory,
	 * not even for the BeanFactory interface itself.
	 * @param dependencyType the dependency type to register. This will typically
	 * be a base interface such as BeanFactory, with extensions of it resolved
	 * as well if declared as an autowiring dependency (e.g. ListableBeanFactory),
	 * as long as the given value actually implements the extended interface.
	 * @param autowiredValue the corresponding autowired value. This may also be an
	 * implementation of the {@link org.springframework.beans.factory.ObjectFactory}
	 * interface, which allows for lazy resolution of the actual target value.
	 */
	/**
	 * 用相应的自动装配值注册一个特殊的依赖类型。
	 * 这是用于工厂/上下文引用的，这些引用应该是可自动编写的，但在工厂中未定义为bean：
	 * 解析为bean所在的ApplicationContext实例的ApplicationContext类型的依赖项。
	 * 注意：在普通BeanFactory中没有注册这样的默认类型，甚至对于BeanFactory接口本身也没有。
	 * @param dependencyType 要注册的依赖类型。只要给定值实际上实现了扩展接口，它通常将是一个基本接口，例如BeanFactory，并且扩展名也将被解析为如果声明为自动装配依赖项（例如ListableBeanFactory），则是。
	 * @param autowiredValue 对应的自动装配值。这也可以是{@link org.springframework.beans.factory.ObjectFactory}接口的一个实现，该接口允许延迟解析实际目标值。
	 */
	void registerResolvableDependency(Class<?> dependencyType, @Nullable Object autowiredValue);

	/**
	 * Determine whether the specified bean qualifies as an autowire candidate,
	 * to be injected into other beans which declare a dependency of matching type.
	 * <p>This method checks ancestor factories as well.
	 * @param beanName the name of the bean to check
	 * @param descriptor the descriptor of the dependency to resolve
	 * @return whether the bean should be considered as autowire candidate
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 */
	/**
	 * 确定指定的bean是否符合自动装配候选条件，可以注入到声明匹配类型依赖项的其他bean中。
	 * 此方法也检查祖先工厂。
	 * @param beanName 要检查的bean的名称
	 * @param descriptor 要解析的依赖项的描述符
	 * @return 是否应将bean视为自动装配候选
	 * @throws NoSuchBeanDefinitionException 如果没有给定名称的bean
	 */
	boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor)
			throws NoSuchBeanDefinitionException;

	/**
	 * Return the registered BeanDefinition for the specified bean, allowing access
	 * to its property values and constructor argument value (which can be
	 * modified during bean factory post-processing).
	 * <p>A returned BeanDefinition object should not be a copy but the original
	 * definition object as registered in the factory. This means that it should
	 * be castable to a more specific implementation type, if necessary.
	 * <p><b>NOTE:</b> This method does <i>not</i> consider ancestor factories.
	 * It is only meant for accessing local bean definitions of this factory.
	 * @param beanName the name of the bean
	 * @return the registered BeanDefinition
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * defined in this factory
	 */
	/**
	 * 返回指定Bean的注册BeanDefinition，从而允许对其属性值和构造函数参数值进行访问（可以在Bean工厂后处理期间进行修改）。
	 * 返回的BeanDefinition对象不应是副本，而应是工厂中注册的原始定义对象。
	 * 这意味着，如有必要，应该可以强制转换为更具体的实现类型。
	 * 注意：此方法考虑祖先工厂。 仅用于访问该工厂的本地bean定义。
	 * @param beanName bean的名称
	 * @return 注册的BeanDefinition
	 * @throws NoSuchBeanDefinitionException 如果工厂中没有给定名称的bean
	 */
	BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	/**
	 * Return a unified view over all bean names managed by this factory.
	 * <p>Includes bean definition names as well as names of manually registered
	 * singleton instances, with bean definition names consistently coming first,
	 * analogous to how type/annotation specific retrieval of bean names works.
	 * @return the composite iterator for the bean names view
	 * @since 4.1.2
	 * @see #containsBeanDefinition
	 * @see #registerSingleton
	 * @see #getBeanNamesForType
	 * @see #getBeanNamesForAnnotation
	 */
	/**
	 * 返回对此工厂管理的所有bean名称的统一视图。
	 * 包括Bean定义名称以及手动注册的单例实例的名称，并且始终以Bean定义名称排在第一位，类似于特定于类型/注释的Bean名称检索的工作方式。
	 * @return bean名称视图的复合迭代器
	 * @see #containsBeanDefinition
	 * @see #registerSingleton
	 * @see #getBeanNamesForType
	 * @see #getBeanNamesForAnnotation
	 */
	Iterator<String> getBeanNamesIterator();

	/**
	 * Clear the merged bean definition cache, removing entries for beans
	 * which are not considered eligible for full metadata caching yet.
	 * <p>Typically triggered after changes to the original bean definitions,
	 * e.g. after applying a {@link BeanFactoryPostProcessor}. Note that metadata
	 * for beans which have already been created at this point will be kept around.
	 * @since 4.2
	 * @see #getBeanDefinition
	 * @see #getMergedBeanDefinition
	 */
	/**
	 * 清除合并的bean定义缓存，删除bean的条目，这些条目尚不适合进行完整的元数据缓存。
	 * 通常在更改原始bean定义后触发，例如在应用{@link BeanFactoryPostProcessor}之后。
	 * 请注意，此时将保留已创建的bean的元数据。
	 * @see #getBeanDefinition
	 * @see #getMergedBeanDefinition
	 */
	void clearMetadataCache();

	/**
	 * Freeze all bean definitions, signalling that the registered bean definitions
	 * will not be modified or post-processed any further.
	 * <p>This allows the factory to aggressively cache bean definition metadata.
	 */
	/**
	 * 冻结所有bean定义，表示已注册的bean定义不会再被修改或后处理。
	 * 这允许工厂积极地缓存bean定义元数据。
	 */
	void freezeConfiguration();

	/**
	 * Return whether this factory's bean definitions are frozen,
	 * i.e. are not supposed to be modified or post-processed any further.
	 * @return {@code true} if the factory's configuration is considered frozen
	 */
	/**
	 * 返回此工厂的Bean定义是否被冻结，即不应被修改或进一步处理。
	 * @return {@code true}（如果认为出厂配置已冻结）
	 */
	boolean isConfigurationFrozen();

	/**
	 * Ensure that all non-lazy-init singletons are instantiated, also considering
	 * {@link org.springframework.beans.factory.FactoryBean FactoryBeans}.
	 * Typically invoked at the end of factory setup, if desired.
	 * @throws BeansException if one of the singleton beans could not be created.
	 * Note: This may have left the factory with some beans already initialized!
	 * Call {@link #destroySingletons()} for full cleanup in this case.
	 * @see #destroySingletons()
	 */
	/**
	 * 确保所有非延迟初始化单例都实例化，同时考虑{@link org.springframework.beans.factory.FactoryBean}。
	 * 如果需要，通常在出厂设置结束时调用。
	 * @throws BeansException 如果无法创建一个单例bean。
	 * 注意：这可能已经离开工厂，并且已经初始化了一些bean！
	 * 在这种情况下，请致电{@link #destroySingletons（）}进行全面清理。
	 * @see #destroySingletons（）
	 */
	void preInstantiateSingletons() throws BeansException;

}
