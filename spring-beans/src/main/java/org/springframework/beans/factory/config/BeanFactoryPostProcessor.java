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

import org.springframework.beans.BeansException;

/**
 * Factory hook that allows for custom modification of an application context's
 * bean definitions, adapting the bean property values of the context's underlying
 * bean factory.
 *
 * <p>Useful for custom config files targeted at system administrators that
 * override bean properties configured in the application context. See
 * {@link PropertyResourceConfigurer} and its concrete implementations for
 * out-of-the-box solutions that address such configuration needs.
 *
 * <p>A {@code BeanFactoryPostProcessor} may interact with and modify bean
 * definitions, but never bean instances. Doing so may cause premature bean
 * instantiation, violating the container and causing unintended side-effects.
 * If bean instance interaction is required, consider implementing
 * {@link BeanPostProcessor} instead.
 *
 * <h3>Registration</h3>
 * <p>An {@code ApplicationContext} auto-detects {@code BeanFactoryPostProcessor}
 * beans in its bean definitions and applies them before any other beans get created.
 * A {@code BeanFactoryPostProcessor} may also be registered programmatically
 * with a {@code ConfigurableApplicationContext}.
 *
 * <h3>Ordering</h3>
 * <p>{@code BeanFactoryPostProcessor} beans that are autodetected in an
 * {@code ApplicationContext} will be ordered according to
 * {@link org.springframework.core.PriorityOrdered} and
 * {@link org.springframework.core.Ordered} semantics. In contrast,
 * {@code BeanFactoryPostProcessor} beans that are registered programmatically
 * with a {@code ConfigurableApplicationContext} will be applied in the order of
 * registration; any ordering semantics expressed through implementing the
 * {@code PriorityOrdered} or {@code Ordered} interface will be ignored for
 * programmatically registered post-processors. Furthermore, the
 * {@link org.springframework.core.annotation.Order @Order} annotation is not
 * taken into account for {@code BeanFactoryPostProcessor} beans.
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 06.07.2003
 * @see BeanPostProcessor
 * @see PropertyResourceConfigurer
 */

/**
 * 工厂狗子允许自定义修改应用程序上下文的bean定义，以适应上下文基础bean工厂的bean属性值。
 * 对于面向系统管理员的自定义配置文件很有用，这些文件会覆盖在应用程序上下文中配置的bean属性。
 * {@link PropertyResourceConfigurer}及其具体实现，以了解可以解决此类配置需求的现成解决方案。
 * {@code BeanFactoryPostProcessor}可以与bean定义进行交互并对其进行修改，但不能与bean实例进行交互。这样做可能会导致bean实例化过早，从而违反了容器并造成了意想不到的副作用。
 * 如果需要与bean实例交互，请考虑实施{@link BeanPostProcessor}。
 * 注册{@code ApplicationContext}在其bean定义中自动检测{@code BeanFactoryPostProcessor}bean，并在创建任何其他bean之前应用它们。
 * 也可以通过{@code ConfigurableApplicationContext}以编程方式注册{@code BeanFactoryPostProcessor}。
 * 订购在{@code ApplicationContext}中自动检测到的{@code BeanFactoryPostProcessor} bean
 * 将根据{@link org.springframework.core.PriorityOrdered}和{@link org.springframework.core.Ordered}语义。
 * 相比之下，以编程方式注册了{{code BeanFactoryPostProcessor}的bean将以注册的顺序应用。
 * 对于以编程方式注册的后处理器，将忽略通过实现{@code PriorityOrdered}或{@code Ordered}接口表示的任何排序语义。
 * 此外，对于{@code BeanFactoryPostProcessor} bean，不考虑{@link org.springframework.core.annotation.Order}批注。
 * @作者Juergen Hoeller * @作者Sam Brannen * @自从06.07.2003 * @see BeanPostProcessor * @see PropertyResourceConfigurer
 */
@FunctionalInterface
public interface BeanFactoryPostProcessor {

	/**
	 * Modify the application context's internal bean factory after its standard
	 * initialization. All bean definitions will have been loaded, but no beans
	 * will have been instantiated yet. This allows for overriding or adding
	 * properties even to eager-initializing beans.
	 * @param beanFactory the bean factory used by the application context
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	/**
	 * 在标准初始化之后，修改应用程序上下文的内部bean工厂。
	 * 所有bean定义都将被加载，但是还没有实例化bean。
	 * 这甚至可以覆盖或添加属性，甚至可以用于初始化bean。
	 * @param beanFactory 应用程序上下文使用的bean工厂
	 * @throws BeansException org.springframework.beans.BeansException
	 */
	void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
