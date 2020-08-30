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

package org.springframework.core.env;

/**
 * Interface indicating a component that contains and exposes an {@link Environment} reference.
 *
 * <p>All Spring application contexts are EnvironmentCapable, and the interface is used primarily
 * for performing {@code instanceof} checks in framework methods that accept BeanFactory
 * instances that may or may not actually be ApplicationContext instances in order to interact
 * with the environment if indeed it is available.
 *
 * <p>As mentioned, {@link org.springframework.context.ApplicationContext ApplicationContext}
 * extends EnvironmentCapable, and thus exposes a {@link #getEnvironment()} method; however,
 * {@link org.springframework.context.ConfigurableApplicationContext ConfigurableApplicationContext}
 * redefines {@link org.springframework.context.ConfigurableApplicationContext#getEnvironment
 * getEnvironment()} and narrows the signature to return a {@link ConfigurableEnvironment}.
 * The effect is that an Environment object is 'read-only' until it is being accessed from
 * a ConfigurableApplicationContext, at which point it too may be configured.
 *
 * @author Chris Beams
 * @since 3.1
 * @see Environment
 * @see ConfigurableEnvironment
 * @see org.springframework.context.ConfigurableApplicationContext#getEnvironment()
 */

/**
 * 指示包含并公开{@link Environment}引用的组件的接口。
 * 所有Spring应用程序上下文都是具有环境能力的，并且该接口主要用于在接受BeanFactory的框架方法中执行{@code instanceof}检查
 * 与之交互或实际上可能不是ApplicationContext实例的实例环境，如果确实可用。
 * 如上所述，{@ link org.springframework.context.ApplicationContext ApplicationContext}扩展了EnvironmentCapable
 * 因此公开了一个{@link #getEnvironment()}方法
 * 但是{@link org.springframework.context.ConfigurableApplicationContext}重新定义了{@link org.springframework.context.ConfigurableApplicationContext＃getEnvironment()}并缩小了签名范围，以返回{@link ConfigurableEnvironment}。
 * 结果是环境对象是“只读的”，直到从ConfigurableApplicationContext访问该对象为止，此时也可以对其进行配置。
 * @see ConfigurableEnvironment
 * @see org.springframework.context
 */
public interface EnvironmentCapable {

	/**
	 * Return the {@link Environment} associated with this component.
	 */
	/**
	 * 返回此组件关联的{@link Environment}
	 * @return
	 */
	Environment getEnvironment();

}
