/*
 * Copyright 2002-2016 the original author or authors.
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

import java.lang.reflect.Constructor;

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;

/**
 * Extension of the {@link InstantiationAwareBeanPostProcessor} interface,
 * adding a callback for predicting the eventual type of a processed bean.
 *
 * <p><b>NOTE:</b> This interface is a special purpose interface, mainly for
 * internal use within the framework. In general, application-provided
 * post-processors should simply implement the plain {@link BeanPostProcessor}
 * interface or derive from the {@link InstantiationAwareBeanPostProcessorAdapter}
 * class. New methods might be added to this interface even in point releases.
 *
 * @author Juergen Hoeller
 * @since 2.0.3
 * @see InstantiationAwareBeanPostProcessorAdapter
 */

/**
 * {@link InstantiationAwareBeanPostProcessor}接口的扩展，添加了一个回调，用于预测已处理bean的最终类型。
 * 注意：该接口是一个专用接口，主要供框架内部使用。
 * 通常，应用程序提供的后处理器应仅实现简单的{@link BeanPostProcessor}接口或从{@link InstantiationAwareBeanPostProcessorAdapter}类派生。
 * 即使在点发行版中，新方法也可能会添加到此接口。
 * @see InstantiationAwareBeanPostProcessorAdapter
 */
public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {

	/**
	 * Predict the type of the bean to be eventually returned from this
	 * processor's {@link #postProcessBeforeInstantiation} callback.
	 * <p>The default implementation returns {@code null}.
	 * @param beanClass the raw class of the bean
	 * @param beanName the name of the bean
	 * @return the type of the bean, or {@code null} if not predictable
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	/**
	 * 预测最终从此处理器的{@link #postProcessBeforeInstantiation}回调返回的bean的类型。
	 * 默认实现返回{@code null}。
	 * @param beanClass bean的原始类
	 * @param beanName bean的名称
	 * @return bean的类型，或者{@code null}（如果不可预测的话）
	 * @throws org.springframework.beans.BeansException错误
	 */
	@Nullable
	default Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	/**
	 * Determine the candidate constructors to use for the given bean.
	 * <p>The default implementation returns {@code null}.
	 * @param beanClass the raw class of the bean (never {@code null})
	 * @param beanName the name of the bean
	 * @return the candidate constructors, or {@code null} if none specified
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	/**
	 * 确定要用于给定bean的候选构造函数。
	 * 默认实现返回{@code null}。
	 * @param beanClass Bean的原始类（不要{@code null}）
	 * @param beanName Bean的名称
	 * @return 候选构造函数，如果未指定则返回{@code null}
	 * @throws org.springframework。发生错误时的beans.BeansException
	 */
	@Nullable
	default Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName)
			throws BeansException {

		return null;
	}

	/**
	 * Obtain a reference for early access to the specified bean,
	 * typically for the purpose of resolving a circular reference.
	 * <p>This callback gives post-processors a chance to expose a wrapper
	 * early - that is, before the target bean instance is fully initialized.
	 * The exposed object should be equivalent to the what
	 * {@link #postProcessBeforeInitialization} / {@link #postProcessAfterInitialization}
	 * would expose otherwise. Note that the object returned by this method will
	 * be used as bean reference unless the post-processor returns a different
	 * wrapper from said post-process callbacks. In other words: Those post-process
	 * callbacks may either eventually expose the same reference or alternatively
	 * return the raw bean instance from those subsequent callbacks (if the wrapper
	 * for the affected bean has been built for a call to this method already,
	 * it will be exposes as final bean reference by default).
	 * <p>The default implementation returns the given {@code bean} as-is.
	 * @param bean the raw bean instance
	 * @param beanName the name of the bean
	 * @return the object to expose as bean reference
	 * (typically with the passed-in bean instance as default)
	 * @throws org.springframework.beans.BeansException in case of errors
	 */
	/**
	 * 获取用于早期访问指定bean的引用，通常是为了解决循环参考。
	 * 此回调使后处理器有机会及早公开包装器-即在目标Bean实例完全初始化之前。
	 * 公开的对象应等效于{@link #postProcessBeforeInitialization} / {@link #postProcessAfterInitialization}否则将公开的对象。
	 * 注意，除非后处理器返回与所述后处理回调不同的包装器，否则此方法返回的对象将用作Bean引用。
	 * 换句话说：这些后期处理回调可能最终会公开相同的引用，或者从这些后续回调返回原始bean实例（如果受影响的bean的包装器已经为调用此方法而构建，默认情况下，它将作为最终bean引用公开）。
	 * 默认实现按原样返回给定的{@code bean}。
	 * @param bean 原始bean实例
	 * @param beanName bean的名称
	 * @return 要公开为bean引用的对象（通常将传入的bean实例作为默认值）
	 * @throws org.springframework.beans.BeansException如果有错误
	 */
	default Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
