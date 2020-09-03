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

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

/**
 * A BeanDefinition describes a bean instance, which has property values,
 * constructor argument values, and further information supplied by
 * concrete implementations.
 *
 * <p>This is just a minimal interface: The main intention is to allow a
 * {@link BeanFactoryPostProcessor} to introspect and modify property values
 * and other bean metadata.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 19.03.2004
 * @see ConfigurableListableBeanFactory#getBeanDefinition
 * @see org.springframework.beans.factory.support.RootBeanDefinition
 * @see org.springframework.beans.factory.support.ChildBeanDefinition
 */
/**
 * BeanDefinition描述了一个bean实例，该实例具有属性值，构造函数参数值以及具体实现提供的更多信息。
 * 这只是一个最小的接口：主要目的是允许{@link BeanFactoryPostProcessor}内省和修改属性值和其他bean元数据。
 * @see ConfigurableListableBeanFactory＃getBeanDefinition
 * @see org.springframework.beans.factory.support.RootBeanDefinition
 * @see org.springframework.beans.factory.support.ChildBeanDefinition
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

	/**
	 * Scope identifier for the standard singleton scope: {@value}.
	 * <p>Note that extended bean factories might support further scopes.
	 * @see #setScope
	 * @see ConfigurableBeanFactory#SCOPE_SINGLETON
	 */
	/**
	 * 标准单例作用域的作用域标识符：{@value}。
	 * 请注意，扩展的bean工厂可能支持更多范围。
	 * @see #setScope
	 * @see ConfigurableBeanFactory＃SCOPE_SINGLETON
	 */
	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

	/**
	 * Scope identifier for the standard prototype scope: {@value}.
	 * <p>Note that extended bean factories might support further scopes.
	 * @see #setScope
	 * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE
	 */
	/**
	 * 标准原型范围的范围标识符：{@value}。
	 * 请注意，扩展的bean工厂可能支持更多范围。
	 * @see #setScope
	 * @see ConfigurableBeanFactory＃SCOPE_PROTOTYPE
	 */
	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;


	/**
	 * Role hint indicating that a {@code BeanDefinition} is a major part
	 * of the application. Typically corresponds to a user-defined bean.
	 */
	/**
	 * 角色提示，指示{@code BeanDefinition}是应用程序的主要部分。
	 * 通常对应于用户定义的bean。
	 */
	int ROLE_APPLICATION = 0;

	/**
	 * Role hint indicating that a {@code BeanDefinition} is a supporting
	 * part of some larger configuration, typically an outer
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 * {@code SUPPORT} beans are considered important enough to be aware
	 * of when looking more closely at a particular
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition},
	 * but not when looking at the overall configuration of an application.
	 */
	/**
	 * 角色提示，指示{@code BeanDefinition}是某些较大配置的支持部分，通常是外部{@link org.springframework.beans.factory.parsing.ComponentDefinition}。
	 * {@code SUPPORT}bean被认为很重要，足以使您更加仔细地查看特定的
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition}，但在查看Bean的整体配置时却不了解一个应用程序。
	 */
	int ROLE_SUPPORT = 1;

	/**
	 * Role hint indicating that a {@code BeanDefinition} is providing an
	 * entirely background role and has no relevance to the end-user. This hint is
	 * used when registering beans that are completely part of the internal workings
	 * of a {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 */
	/**
	 * 角色提示，表明{@code BeanDefinition}正在提供完全是后台角色，与最终用户无关。
	 * 当注册完全属于{@link org.springframework.beans.factory.parsing.ComponentDefinition}内部工作的bean时，将使用此提示。
	 */
	int ROLE_INFRASTRUCTURE = 2;


	// Modifiable attributes

	/**
	 * Set the name of the parent definition of this bean definition, if any.
	 */
	/**
	 * 设置此bean定义的父定义的名称（如果有）。
	 * @param parentName
	 */
	void setParentName(@Nullable String parentName);

	/**
	 * Return the name of the parent definition of this bean definition, if any.
	 */
	/**
	 * 返回此bean定义的父定义的名称（如果有）。
	 * @return
	 */
	@Nullable
	String getParentName();

	/**
	 * Specify the bean class name of this bean definition.
	 * <p>The class name can be modified during bean factory post-processing,
	 * typically replacing the original class name with a parsed variant of it.
	 * @see #setParentName
	 * @see #setFactoryBeanName
	 * @see #setFactoryMethodName
	 */
	/**
	 * 指定此bean定义的bean类名。
	 * 可以在bean工厂的后期处理期间修改类名，通常用解析后的变体替换原始的类名。
	 * @see #setParentName
	 * @see #setFactoryBeanName
	 * @see #setFactoryMethodName
	 * @param beanClassName
	 */
	void setBeanClassName(@Nullable String beanClassName);

	/**
	 * Return the current bean class name of this bean definition.
	 * <p>Note that this does not have to be the actual class name used at runtime, in
	 * case of a child definition overriding/inheriting the class name from its parent.
	 * Also, this may just be the class that a factory method is called on, or it may
	 * even be empty in case of a factory bean reference that a method is called on.
	 * Hence, do <i>not</i> consider this to be the definitive bean type at runtime but
	 * rather only use it for parsing purposes at the individual bean definition level.
	 * @see #getParentName()
	 * @see #getFactoryBeanName()
	 * @see #getFactoryMethodName()
	 */
	/**
	 * 返回此Bean定义的当前Bean类名称。
	 * 请注意，在子定义从父项继承/继承子项定义的情况下，该名称不必是运行时使用的实际类名。
	 * 另外，这可能只是调用工厂方法的类，或者如果调用了方法的工厂bean引用，则可能为空。
	 * 因此，不在运行时不要将其视为确定的bean类型，而应仅在单个bean定义级别将其用于解析目的。
	 * @see #getParentName（）
	 * @see #getFactoryBeanName（）
	 * @see #getFactoryMethodName（）
	 * @return
	 */
	@Nullable
	String getBeanClassName();

	/**
	 * Override the target scope of this bean, specifying a new scope name.
	 * @see #SCOPE_SINGLETON
	 * @see #SCOPE_PROTOTYPE
	 */
	/**
	 * 覆盖此bean的目标作用域，并指定一个新的作用域名称。
	 * @see #SCOPE_SINGLETON
	 * @see #SCOPE_PROTOTYPE
	 * @param scope
	 */
	void setScope(@Nullable String scope);

	/**
	 * Return the name of the current target scope for this bean,
	 * or {@code null} if not known yet.
	 */
	/**
	 * 返回此bean当前目标作用域的名称，如果尚未知道，则返回*或{@code null}。
	 * @return
	 */
	@Nullable
	String getScope();

	/**
	 * Set whether this bean should be lazily initialized.
	 * <p>If {@code false}, the bean will get instantiated on startup by bean
	 * factories that perform eager initialization of singletons.
	 */
	/**
	 * 设置是否应延迟初始化此bean。
	 * 如果{@code false}，则将在执行启动单例初始化的bean工厂时实例化bean。
	 * @param lazyInit
	 */
	void setLazyInit(boolean lazyInit);

	/**
	 * Return whether this bean should be lazily initialized, i.e. not
	 * eagerly instantiated on startup. Only applicable to a singleton bean.
	 */
	/**
	 * 返回是否应延迟初始化此bean，即不要在启动时急于实例化。仅适用于单例bean。
	 * @return
	 */
	boolean isLazyInit();

	/**
	 * Set the names of the beans that this bean depends on being initialized.
	 * The bean factory will guarantee that these beans get initialized first.
	 */
	/**
	 * 设置该bean依赖于初始化的bean的名称。
	 * bean工厂将保证这些bean首先被初始化。
	 * @param dependsOn
	 */
	void setDependsOn(@Nullable String... dependsOn);

	/**
	 * Return the bean names that this bean depends on.
	 */
	/**
	 * 返回此bean依赖的bean名称。
	 * @return
	 */
	@Nullable
	String[] getDependsOn();

	/**
	 * Set whether this bean is a candidate for getting autowired into some other bean.
	 * <p>Note that this flag is designed to only affect type-based autowiring.
	 * It does not affect explicit references by name, which will get resolved even
	 * if the specified bean is not marked as an autowire candidate. As a consequence,
	 * autowiring by name will nevertheless inject a bean if the name matches.
	 */
	/**
	 * 设置此bean是否适合自动连接到其他bean。
	 * 请注意，此标志旨在仅影响基于类型的自动装配。
	 * 它不会影响名称的显式引用，即使指定的bean未标记为自动装配候选，它也将得到解析。
	 * 结果，如果名称匹配，按名称自动装配仍会注入一个bean。
	 * @param autowireCandidate
	 */
	void setAutowireCandidate(boolean autowireCandidate);

	/**
	 * Return whether this bean is a candidate for getting autowired into some other bean.
	 */
	/**
	 * 返回此bean是否适合自动连接到其他bean。
	 * @return
	 */
	boolean isAutowireCandidate();

	/**
	 * Set whether this bean is a primary autowire candidate.
	 * <p>If this value is {@code true} for exactly one bean among multiple
	 * matching candidates, it will serve as a tie-breaker.
	 */
	/**
	 * 设置此bean是否为自动装配的主要候选对象。
	 * 如果对于多个匹配的候选对象中的一个，该值恰好是{@code true}，它将用作平局。
	 * @param primary
	 */
	void setPrimary(boolean primary);

	/**
	 * Return whether this bean is a primary autowire candidate.
	 */
	/**
	 * 返回此bean是否为自动装配的主要候选对象。
	 * @return
	 */
	boolean isPrimary();

	/**
	 * Specify the factory bean to use, if any.
	 * This the name of the bean to call the specified factory method on.
	 * @see #setFactoryMethodName
	 */
	/**
	 * 指定要使用的工厂bean（如果有）。
	 * 这是调用指定工厂方法的bean的名称。
	 * @see #setFactoryMethodName
	 * @param factoryBeanName
	 */
	void setFactoryBeanName(@Nullable String factoryBeanName);

	/**
	 * 返回工厂bean名称（如果有）。
	 */
	@Nullable
	String getFactoryBeanName();

	/**
	 * Specify a factory method, if any. This method will be invoked with
	 * constructor arguments, or with no arguments if none are specified.
	 * The method will be invoked on the specified factory bean, if any,
	 * or otherwise as a static method on the local bean class.
	 * @see #setFactoryBeanName
	 * @see #setBeanClassName
	 */
	/**
	 * 指定工厂方法（如果有）。将使用构造函数参数调用此方法，如果未指定，则不使用任何参数。
	 * 该方法将在指定的工厂bean（如果有）上被调用，否则将作为本地bean类上的静态方法被调用。
	 * @see #setFactoryBeanName
	 * @see #setBeanClassName
	 * @param factoryMethodName
	 */
	void setFactoryMethodName(@Nullable String factoryMethodName);

	/**
	 * 返回工厂方法（如果有）。
	 */
	@Nullable
	String getFactoryMethodName();

	/**
	 * Return the constructor argument values for this bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the ConstructorArgumentValues object (never {@code null})
	 */
	/**
	 * 返回此bean的构造函数参数值。
	 * 可以在bean工厂后处理期间修改返回的实例。
	 * @return 返回ConstructorArgumentValues对象（切勿{@code null}）
	 */
	ConstructorArgumentValues getConstructorArgumentValues();

	/**
	 * 如果为此bean定义了构造函数参数值，则返回。
	 */
	default boolean hasConstructorArgumentValues() {
		return !getConstructorArgumentValues().isEmpty();
	}

	/**
	 * Return the property values to be applied to a new instance of the bean.
	 * <p>The returned instance can be modified during bean factory post-processing.
	 * @return the MutablePropertyValues object (never {@code null})
	 */
	/**
	 * 返回要应用于新的bean实例的属性值。
	 * 可以在bean工厂后处理期间修改返回的实例。
	 * @return MutablePropertyValues对象（切勿{@code null}）
	 */
	MutablePropertyValues getPropertyValues();

	/**
	 * 返回是否有为此bean定义的属性值。
	 */
	default boolean hasPropertyValues() {
		return !getPropertyValues().isEmpty();
	}

	/**
	 * 设置初始化方法的名称。
	 */
	void setInitMethodName(@Nullable String initMethodName);

	/**
	 * 返回初始化方法的名称。
	 */
	@Nullable
	String getInitMethodName();

	/**
	 * 设置destroy方法的名称。
	 */
	void setDestroyMethodName(@Nullable String destroyMethodName);

	/**
	 * 返回destroy方法的名称。
	 */
	@Nullable
	String getDestroyMethodName();

	/**
	 * Set the role hint for this {@code BeanDefinition}. The role hint
	 * provides the frameworks as well as tools an indication of
	 * the role and importance of a particular {@code BeanDefinition}.
	 * @since 5.1
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 */
	/**
	 * 设置此{@code BeanDefinition}的角色提示。
	 * 角色提示为框架和工具提供了特定{@code BeanDefinition}的角色和重要性的指示。
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 */
	void setRole(int role);

	/**
	 * Get the role hint for this {@code BeanDefinition}. The role hint
	 * provides the frameworks as well as tools an indication of
	 * the role and importance of a particular {@code BeanDefinition}.
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 */
	/**
	 * 获取此{@code BeanDefinition}的角色提示。
	 * 角色提示为框架和工具提供了特定{@code BeanDefinition}的角色和重要性的指示。
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 */
	int getRole();

	/**
	 * 设置此bean定义的可读描述
	 */
	void setDescription(@Nullable String description);

	/**
	 * 返回此bean定义的可读描述。
	 */
	@Nullable
	String getDescription();


	// Read-only attributes

	/**
	 * Return a resolvable type for this bean definition,
	 * based on the bean class or other specific metadata.
	 * <p>This is typically fully resolved on a runtime-merged bean definition
	 * but not necessarily on a configuration-time definition instance.
	 * @return the resolvable type (potentially {@link ResolvableType#NONE})
	 * @since 5.2
	 * @see ConfigurableBeanFactory#getMergedBeanDefinition
	 */
	/**
	 * 根据bean类或其他特定的元数据，返回此bean定义的可解析类型。
	 * 这通常在运行时合并的bean定义中得到完全解决，但不一定在配置时定义实例上得到完全解决。
	 * @return 可解析的类型（可能为{@link ResolvableType＃NONE}）
	 * @see ConfigurableBeanFactory＃getMergedBeanDefinition()
	 */
	ResolvableType getResolvableType();

	/**
	 * Return whether this a <b>Singleton</b>, with a single, shared instance
	 * returned on all calls.
	 * @see #SCOPE_SINGLETON
	 */
	/**
	 * 返回此Singleton是否具有在所有调用中返回的单个共享实例
	 * @see #SCOPE_SINGLETON
	 */
	boolean isSingleton();

	/**
	 * Return whether this a <b>Prototype</b>, with an independent instance
	 * returned for each call.
	 * @since 3.0
	 * @see #SCOPE_PROTOTYPE
	 */
	/**
	 * 返回此是否为Prototype，并为每个调用返回一个独立的实例
	 * @see #SCOPE_PROTOTYPE
	 * @return
	 */
	boolean isPrototype();

	/**
	 * 返回此bean是否为“抽象”，即不打算实例化。
	 */
	boolean isAbstract();

	/**
	 * 返回此bean定义*所来自的资源的描述
	 */
	@Nullable
	String getResourceDescription();

	/**
	 * Return the originating BeanDefinition, or {@code null} if none.
	 * <p>Allows for retrieving the decorated bean definition, if any.
	 * <p>Note that this method returns the immediate originator. Iterate through the
	 * originator chain to find the original BeanDefinition as defined by the user.
	 */
	/**
	 * 返回原始的BeanDefinition，如果没有，则返回{@code null}。
	 * 允许检索修饰的bean定义（如果有）。
	 * 请注意，此方法返回直接发起者。遍历originator链以查找用户定义的原始BeanDefinition。
	 */
	@Nullable
	BeanDefinition getOriginatingBeanDefinition();

}
