/*
 * Copyright 2002-2018 the original author or authors.
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
 * Subinterface of {@link BeanPostProcessor} that adds a before-destruction callback.
 *
 * <p>The typical usage will be to invoke custom destruction callbacks on
 * specific bean types, matching corresponding initialization callbacks.
 *
 * @author Juergen Hoeller
 * @since 1.0.1
 */

/**
 * {@link BeanPostProcessor}的子接口，用于添加破坏前的回调。
 * 通常的用法是在特定的bean类型上调用自定义销毁回调，并匹配相应的初始化回调。
 */
public interface DestructionAwareBeanPostProcessor extends BeanPostProcessor {

	/**
	 * Apply this BeanPostProcessor to the given bean instance before its
	 * destruction, e.g. invoking custom destruction callbacks.
	 * <p>Like DisposableBean's {@code destroy} and a custom destroy method, this
	 * callback will only apply to beans which the container fully manages the
	 * lifecycle for. This is usually the case for singletons and scoped beans.
	 * @param bean the bean instance to be destroyed
	 * @param beanName the name of the bean
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#setDestroyMethodName(String)
	 */
	/**
	 * 在销毁它之前，将此BeanPostProcessor应用于给定的bean实例，例如调用自定义销毁回调。
	 * 就像DisposableBean的{@code destroy}和自定义的destroy方法一样，此回调仅适用于容器完全管理生命周期的bean。
	 * 单例和作用域bean通常是这种情况。
	 * @param bean 要销毁的bean实例
	 * @param beanName bean的名称
	 * @throws BeansException 发生错误时抛出org.springframework.beans.BeansException
	 * @see org.springframework.beans.factory.DisposableBean＃destroy（）
	 * @see org.springframework.beans.factory.support.AbstractBeanDefinition＃setDestroyMethodName（String）
	 */
	void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException;

	/**
	 * Determine whether the given bean instance requires destruction by this
	 * post-processor.
	 * <p>The default implementation returns {@code true}. If a pre-5 implementation
	 * of {@code DestructionAwareBeanPostProcessor} does not provide a concrete
	 * implementation of this method, Spring silently assumes {@code true} as well.
	 * @param bean the bean instance to check
	 * @return {@code true} if {@link #postProcessBeforeDestruction} is supposed to
	 * be called for this bean instance eventually, or {@code false} if not needed
	 * @since 4.3
	 */
	/**
	 * 确定给定的bean实例是否需要此后处理器销毁。
	 * 默认实现返回{@code true}。
	 * 如果{@code DestructionAwareBeanPostProcessor}的之前的实现没有提供此方法的具体实现，Spring也会默默地假设{@code true}。
	 * @param bean 要检查的bean实例
	 * @return {@code true}如果应该最终为该bean实例调用{@link #postProcessBeforeDestruction}，或者如果不需要，则返回{@code false}
	 */
	default boolean requiresDestruction(Object bean) {
		return true;
	}

}
