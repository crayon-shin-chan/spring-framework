/*
 * Copyright 2002-2014 the original author or authors.
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

import bsh.This;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.lang.Nullable;

import javax.swing.*;

/**
 * Central interface to provide configuration for an application.
 * This is read-only while the application is running, but may be
 * reloaded if the implementation supports this.
 *
 * <p>An ApplicationContext provides:
 * <ul>
 * <li>Bean factory methods for accessing application components.
 * Inherited from {@link org.springframework.beans.factory.ListableBeanFactory}.
 * <li>The ability to load file resources in a generic fashion.
 * Inherited from the {@link org.springframework.core.io.ResourceLoader} interface.
 * <li>The ability to publish events to registered listeners.
 * Inherited from the {@link ApplicationEventPublisher} interface.
 * <li>The ability to resolve messages, supporting internationalization.
 * Inherited from the {@link MessageSource} interface.
 * <li>Inheritance from a parent context. Definitions in a descendant context
 * will always take priority. This means, for example, that a single parent
 * context can be used by an entire web application, while each servlet has
 * its own child context that is independent of that of any other servlet.
 * </ul>
 *
 * <p>In addition to standard {@link org.springframework.beans.factory.BeanFactory}
 * lifecycle capabilities, ApplicationContext implementations detect and invoke
 * {@link ApplicationContextAware} beans as well as {@link ResourceLoaderAware},
 * {@link ApplicationEventPublisherAware} and {@link MessageSourceAware} beans.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @see ConfigurableApplicationContext
 * @see org.springframework.beans.factory.BeanFactory
 * @see org.springframework.core.io.ResourceLoader
 */

/**
 * 中心接口，为应用程序提供配置。在应用程序运行时为只读，但是如果实现支持，则可以重新加载。
 * ApplicationContext提供：
 * 用于访问应用程序组件Bean的工厂方法。继承自{@link org.springframework.beans.factory.ListableBeanFactory}
 * 以通用的方式加载资源文件的能力，继承自{@link org.springframework.core.io.ResourceLoader}接口
 * 将事件发布到注册的侦听器的能力，继承自{@link ApplicationEventPublisher}接口
 * 解决消息的能力，支持国际化，继承自{@link MessageSource}接口
 * 从父上下文继承，在后代上下文中的定义将始终优先。例如：这意味着整个WEB应用程序都可以使用单个父上下文，而每个servlet都具有自己的子上下文，该子上下文独立于任何其他servlet的子上下文
 * 处理标准的{@link org.springframework.beans.factory.BeanFactory}生命周期功能之外，ApplicationContext实现还检测并调用
 * {@link ApplicationContextAware}和{@link ResourceLoaderAware}、{@link ApplicationEventPublisherAware}、{@link MessageSourceAware}
 */
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory,
		MessageSource, ApplicationEventPublisher, ResourcePatternResolver {

	/**
	 * Return the unique id of this application context.
	 * @return the unique id of the context, or {@code null} if none
	 */
	/**
	 * 返回此应用程序上下文的唯一ID。
	 * @return 上下文的唯一ID，如果没有，则返回{@code null}
	 */
	@Nullable
	String getId();

	/**
	 * Return a name for the deployed application that this context belongs to.
	 * @return a name for the deployed application, or the empty String by default
	 */
	/**
	 * 返回此上下文所属的已部署应用程序的名称。
	 * @return 已部署应用程序的名称，或者默认为空字符串
	 */
	String getApplicationName();

	/**
	 * Return a friendly name for this context.
	 * @return a display name for this context (never {@code null})
	 */
	/**
	 * 返回此上下文的友好名称。
	 * @return 此上下文的显示名称（切勿{@code null}）
	 */
	String getDisplayName();

	/**
	 * Return the timestamp when this context was first loaded.
	 * @return the timestamp (ms) when this context was first loaded
	 */
	/**
	 * 返回第一次加载此上下文时的时间戳。
	 * @return 首次加载此上下文时的时间戳（毫秒）
	 */
	long getStartupDate();

	/**
	 * Return the parent context, or {@code null} if there is no parent
	 * and this is the root of the context hierarchy.
	 * @return the parent context, or {@code null} if there is no parent
	 */
	/**
	 * 返回父上下文，如果没有父*，则返回{@code null}，这是上下文层次结构的根。
	 * @return 父级上下文，如果没有父级，则返回{@code null}
	 */
	@Nullable
	ApplicationContext getParent();

	/**
	 * Expose AutowireCapableBeanFactory functionality for this context.
	 * <p> This is not typically used by application code, except for the purpose of
	 * initializing bean instances that live outside of the application context,
	 * applying the Spring bean lifecycle (fully or partly) to them.
	 * <p>Alternatively, the internal BeanFactory exposed by the
	 * {@link ConfigurableApplicationContext} interface offers access to the
	 * {@link AutowireCapableBeanFactory} interface too. The present method mainly
	 * serves as a convenient, specific facility on the ApplicationContext interface.
	 * <p><b>NOTE: As of 4.2, this method will consistently throw IllegalStateException
	 * after the application context has been closed.</b> In current Spring Framework
	 * versions, only refreshable application contexts behave that way; as of 4.2,
	 * all application context implementations will be required to comply.
	 * @return the AutowireCapableBeanFactory for this context
	 * @throws IllegalStateException if the context does not support the
	 * {@link AutowireCapableBeanFactory} interface, or does not hold an
	 * autowire-capable bean factory yet (e.g. if {@code refresh()} has
	 * never been called), or if the context has been closed already
	 * @see ConfigurableApplicationContext#refresh()
	 * @see ConfigurableApplicationContext#getBeanFactory()
	 */
	/**
	 * 针对此上下文公开AutowireCapableBeanFactory功能。
	 * 应用程序代码通常不使用此功能，除非是为了初始化存在于应用程序上下文之外的bean实例，
	 * （全部或部分）应用Spring bean生命周期。
	 * 通过{@link ConfigurableApplicationContext}接口公开的内部BeanFactory也提供对{{link AutowireCapableBeanFactory}接口的访问。
	 * 本方法主要用作ApplicationContext接口上的便捷特定工具。
	 * 注意：从4.2开始，在关闭应用程序上下文之后，此方法将始终抛出IllegalStateException
	 * 在当前的Spring Framework 版本中，只有可刷新的应用程序上下文才具有这种行为；
	 * 从4.2开始，所有应用程序上下文实现都必须遵守。
	 * @return 该上下文的AutowireCapableBeanFactory
	 * 如果上下文不支持{@link AutowireCapableBeanFactory}接口，或者尚不具有支持自动连线功能的bean工厂，则抛出IllegalStateException
	 * （例如，如果{@code refresh（）}具有*从未调用过），或者上下文已经关闭
	 * @see ConfigurableApplicationContext＃refresh（）
	 * @see ConfigurableApplicationContext＃getBeanFactory（）
	 */
	AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;

}
