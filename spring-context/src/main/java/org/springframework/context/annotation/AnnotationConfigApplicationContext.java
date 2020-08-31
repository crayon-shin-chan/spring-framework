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

package org.springframework.context.annotation;

import java.util.Arrays;
import java.util.function.Supplier;

import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.metrics.StartupStep;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Standalone application context, accepting <em>component classes</em> as input &mdash;
 * in particular {@link Configuration @Configuration}-annotated classes, but also plain
 * {@link org.springframework.stereotype.Component @Component} types and JSR-330 compliant
 * classes using {@code javax.inject} annotations.
 *
 * <p>Allows for registering classes one by one using {@link #register(Class...)}
 * as well as for classpath scanning using {@link #scan(String...)}.
 *
 * <p>In case of multiple {@code @Configuration} classes, {@link Bean @Bean} methods
 * defined in later classes will override those defined in earlier classes. This can
 * be leveraged to deliberately override certain bean definitions via an extra
 * {@code @Configuration} class.
 *
 * <p>See {@link Configuration @Configuration}'s javadoc for usage examples.
 *
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 3.0
 * @see #register
 * @see #scan
 * @see AnnotatedBeanDefinitionReader
 * @see ClassPathBeanDefinitionScanner
 * @see org.springframework.context.support.GenericXmlApplicationContext
 */

/**
 * 独立的应用程序上下文，接受组件类作为输入
 * 特别是{@link Configuration}注释的类，但也可以是普通的{@link org.springframework.stereotype.Component}类型和使用{@code javax.inject}批注的符合JSR-330的类。
 * 允许使用{@link #register(Class ...)}一一注册类，以及使用{@link #scan(String ...)}进行类路径扫描。
 * 在有多个{@code @Configuration}类的情况下，在以后的类中定义的{@link Bean @Bean}方法将覆盖在先前的类中定义的方法。
 * 可以通过一个额外的{@code @Configuration}类来故意重写某些bean的定义。
 */
public class AnnotationConfigApplicationContext extends GenericApplicationContext implements AnnotationConfigRegistry {

	private final AnnotatedBeanDefinitionReader reader;

	private final ClassPathBeanDefinitionScanner scanner;


	/**
	 * Create a new AnnotationConfigApplicationContext that needs to be populated
	 * through {@link #register} calls and then manually {@linkplain #refresh refreshed}.
	 */
	/**
	 * 创建一个新的AnnotationConfigApplicationContext，
	 * 它需要通过{@link #register}调用进行填充，然后手动{@linkplain #refresh refreshed}。
	 * 也就是创建后还没有启动
	 */
	public AnnotationConfigApplicationContext() {
		StartupStep createAnnotatedBeanDefReader = this.getApplicationStartup().start("spring.context.annotated-bean-reader.create");
		this.reader = new AnnotatedBeanDefinitionReader(this);
		createAnnotatedBeanDefReader.end();
		this.scanner = new ClassPathBeanDefinitionScanner(this);
	}

	/**
	 * Create a new AnnotationConfigApplicationContext with the given DefaultListableBeanFactory.
	 * @param beanFactory the DefaultListableBeanFactory instance to use for this context
	 */
	/**
	 * 使用给定的DefaultListableBeanFactory创建一个新的AnnotationConfigApplicationContext。
	 * @param beanFactory 用于此上下文的DefaultListableBeanFactory实例
	 */
	public AnnotationConfigApplicationContext(DefaultListableBeanFactory beanFactory) {
		super(beanFactory);
		this.reader = new AnnotatedBeanDefinitionReader(this);
		this.scanner = new ClassPathBeanDefinitionScanner(this);
	}

	/**
	 * Create a new AnnotationConfigApplicationContext, deriving bean definitions
	 * from the given component classes and automatically refreshing the context.
	 * @param componentClasses one or more component classes &mdash; for example,
	 * {@link Configuration @Configuration} classes
	 */
	/**
	 * 创建一个新的AnnotationConfigApplicationContext，从给定的组件类派生bean定义，并自动刷新上下文。
	 * @param componentClasses 一个或多个组件类-例如，* {@link Configuration}类
	 * 也就是创建完毕后，容器已经启动
	 */
	public AnnotationConfigApplicationContext(Class<?>... componentClasses) {
		this();
		register(componentClasses);
		refresh();
	}

	/**
	 * Create a new AnnotationConfigApplicationContext, scanning for components
	 * in the given packages, registering bean definitions for those components,
	 * and automatically refreshing the context.
	 * @param basePackages the packages to scan for component classes
	 */
	/**
	 * 创建一个新的AnnotationConfigApplicationContext，在给定的包中扫描组件，为这些组件注册bean定义，然后自动刷新上下文。
	 * @param basePackages 软件包以扫描组件类
	 */
	public AnnotationConfigApplicationContext(String... basePackages) {
		this();
		scan(basePackages);
		refresh();
	}


	/**
	 * Propagate the given custom {@code Environment} to the underlying
	 * {@link AnnotatedBeanDefinitionReader} and {@link ClassPathBeanDefinitionScanner}.
	 */
	/**
	 * 将给定的自定义{@code Environment}传播到基础的* {@link AnnotatedBeanDefinitionReader}和{@link ClassPathBeanDefinitionScanner}。
	 * @param environment
	 */
	@Override
	public void setEnvironment(ConfigurableEnvironment environment) {
		super.setEnvironment(environment);
		this.reader.setEnvironment(environment);
		this.scanner.setEnvironment(environment);
	}

	/**
	 * Provide a custom {@link BeanNameGenerator} for use with {@link AnnotatedBeanDefinitionReader}
	 * and/or {@link ClassPathBeanDefinitionScanner}, if any.
	 * <p>Default is {@link AnnotationBeanNameGenerator}.
	 * <p>Any call to this method must occur prior to calls to {@link #register(Class...)}
	 * and/or {@link #scan(String...)}.
	 * @see AnnotatedBeanDefinitionReader#setBeanNameGenerator
	 * @see ClassPathBeanDefinitionScanner#setBeanNameGenerator
	 * @see AnnotationBeanNameGenerator
	 * @see FullyQualifiedAnnotationBeanNameGenerator
	 */
	/**
	 * 提供一个自定义的{@link BeanNameGenerator}，
	 * 以便与{@link AnnotatedBeanDefinitionReader}和{@link ClassPathBeanDefinitionScanner}一起使用。
	 * 默认值为{@link AnnotationBeanNameGenerator}。对此方法的任何调用必须在调用{@link #register（Class ...）}和{@link #scan（String ...）}之前进行。
	 * @see AnnotatedBeanDefinitionReader＃setBeanNameGenerator
	 * @see ClassPathBeanDefinitionScanner＃setBeanNameGenerator
	 * @see AnnotationBeanNameGenerator
	 * @see FullyQualifiedAnnotationBeanNameGenerator
	 */
	public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
		this.reader.setBeanNameGenerator(beanNameGenerator);
		this.scanner.setBeanNameGenerator(beanNameGenerator);
		/** {@link BeanNameGenerator}设置为Bean */
		getBeanFactory().registerSingleton(AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR, beanNameGenerator);
	}

	/**
	 * Set the {@link ScopeMetadataResolver} to use for registered component classes.
	 * <p>The default is an {@link AnnotationScopeMetadataResolver}.
	 * <p>Any call to this method must occur prior to calls to {@link #register(Class...)}
	 * and/or {@link #scan(String...)}.
	 */
	/**
	 * 设置{@link ScopeMetadataResolver}以用于已注册的组件类。
	 * 默认值为{@link AnnotationScopeMetadataResolver}。
	 * 对该方法的任何调用都必须在调用{@link #register（Class ...）}和{@link #scan（String ...）}之前进行。
	 */
	public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver) {
		this.reader.setScopeMetadataResolver(scopeMetadataResolver);
		this.scanner.setScopeMetadataResolver(scopeMetadataResolver);
	}


	//---------------------------------------------------------------------
	// Implementation of AnnotationConfigRegistry
	//---------------------------------------------------------------------

	/**
	 * Register one or more component classes to be processed.
	 * <p>Note that {@link #refresh()} must be called in order for the context
	 * to fully process the new classes.
	 * @param componentClasses one or more component classes &mdash; for example,
	 * {@link Configuration @Configuration} classes
	 * @see #scan(String...)
	 * @see #refresh()
	 */
	/**
	 * 注册一个或多个要处理的组件类
	 * 请注意，必须调用{@link #refresh（）}才能使上下文完全处理新类。
	 * @param componentClasses 一个或多个组件类-例如，{@link Configuration}类
	 * @see #scan（String ...）
	 * @see #refresh（）
	 */
	@Override
	public void register(Class<?>... componentClasses) {
		Assert.notEmpty(componentClasses, "At least one component class must be specified");
		/* 记录启动步骤 */
		StartupStep registerComponentClass = this.getApplicationStartup().start("spring.context.component-classes.register").tag("classes", () -> Arrays.toString(componentClasses));
		/* 注册到读取器 */
		this.reader.register(componentClasses);
		registerComponentClass.end();
	}

	/**
	 * Perform a scan within the specified base packages.
	 * <p>Note that {@link #refresh()} must be called in order for the context
	 * to fully process the new classes.
	 * @param basePackages the packages to scan for component classes
	 * @see #register(Class...)
	 * @see #refresh()
	 */
	/**
	 * 在指定的基本程序包中执行扫描。
	 * 请注意，必须调用{@link #refresh（）}才能使上下文*完全处理新类。
	 * @param basePackages 软件包以扫描组件类
	 * @see #register（Class ...）
	 * @see #refresh（）
	 */
	@Override
	public void scan(String... basePackages) {
		Assert.notEmpty(basePackages, "At least one base package must be specified");
		StartupStep scanPackages = this.getApplicationStartup().start("spring.context.base-packages.scan")
				.tag("packages", () -> Arrays.toString(basePackages));
		/* 包注册到扫描器 */
		this.scanner.scan(basePackages);
		scanPackages.end();
	}


	//---------------------------------------------------------------------
	// Adapt superclass registerBean calls to AnnotatedBeanDefinitionReader
	//---------------------------------------------------------------------

	/* 直接注册Bean */
	@Override
	public <T> void registerBean(@Nullable String beanName, Class<T> beanClass,
			@Nullable Supplier<T> supplier, BeanDefinitionCustomizer... customizers) {

		this.reader.registerBean(beanClass, beanName, supplier, customizers);
	}

}
