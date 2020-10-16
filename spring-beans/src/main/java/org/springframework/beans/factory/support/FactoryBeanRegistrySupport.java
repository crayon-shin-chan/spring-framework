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

package org.springframework.beans.factory.support;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.lang.Nullable;

/**
 * Support base class for singleton registries which need to handle
 * {@link org.springframework.beans.factory.FactoryBean} instances,
 * integrated with {@link DefaultSingletonBeanRegistry}'s singleton management.
 *
 * <p>Serves as base class for {@link AbstractBeanFactory}.
 *
 * @author Juergen Hoeller
 * @since 2.5.1
 */

/**
 * 支持单例注册表的基类，该单例注册表需要处理{@link org.springframework.beans.factory.FactoryBean}实例，并与{@link DefaultSingletonBeanRegistry}的单例管理集成在一起。
 * 用作{@link AbstractBeanFactory}的基类。
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

	/** Cache of singleton objects created by FactoryBeans: FactoryBean name to object. */
	/**
	 * 由FactoryBean创建的单例对象的缓存：对象的FactoryBean名称。
	 */
	private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>(16);


	/**
	 * Determine the type for the given FactoryBean.
	 * @param factoryBean the FactoryBean instance to check
	 * @return the FactoryBean's object type,
	 * or {@code null} if the type cannot be determined yet
	 */
	/**
	 * 确定给定FactoryBean的类型。
	 * @param factoryBean 要检查的FactoryBean实例
	 * @return FactoryBean的对象类型，或者如果尚未确定类型，则返回{@code null}
	 */
	@Nullable
	protected Class<?> getTypeForFactoryBean(FactoryBean<?> factoryBean) {
		try {
			if (System.getSecurityManager() != null) {
				return AccessController.doPrivileged(
						(PrivilegedAction<Class<?>>) factoryBean::getObjectType, getAccessControlContext());
			}
			else {
				return factoryBean.getObjectType();
			}
		}
		catch (Throwable ex) {
			// Thrown from the FactoryBean's getObjectType implementation.
			logger.info("FactoryBean threw exception from getObjectType, despite the contract saying " +
					"that it should return null if the type of its object cannot be determined yet", ex);
			return null;
		}
	}

	/**
	 * Obtain an object to expose from the given FactoryBean, if available
	 * in cached form. Quick check for minimal synchronization.
	 * @param beanName the name of the bean
	 * @return the object obtained from the FactoryBean,
	 * or {@code null} if not available
	 */
	/**
	 * 如果有，以缓存形式从给定的FactoryBean获取要暴露的对象。
	 * 快速检查最小同步。
	 * @param beanName bean的名称
	 * @return 从FactoryBean获得的对象，如果没有，则返回*或{@code null}
	 */
	@Nullable
	protected Object getCachedObjectForFactoryBean(String beanName) {
		return this.factoryBeanObjectCache.get(beanName);
	}

	/**
	 * Obtain an object to expose from the given FactoryBean.
	 * @param factory the FactoryBean instance
	 * @param beanName the name of the bean
	 * @param shouldPostProcess whether the bean is subject to post-processing
	 * @return the object obtained from the FactoryBean
	 * @throws BeanCreationException if FactoryBean object creation failed
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	/**
	 * 获取一个对象以从给定的FactoryBean中公开。
	 * @param factory 实例的工厂
	 * @param beanName Bean的名称
	 * @param shouldPostProcess Bean是否要进行后处理，如果bean定义是合成的，则为false
	 * @return 从FactoryBean获得的对象
	 * 如果FactoryBean对象创建失败，则抛出BeanCreationException
	 * @see org.springframework.beans.factory.FactoryBean＃getObject（）
	 */
	protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName, boolean shouldPostProcess) {
		/* 工厂是单例且包含此单例 */
		if (factory.isSingleton() && containsSingleton(beanName)) {
			synchronized (getSingletonMutex()) {
				/* 获取工厂bean结果缓存 */
				Object object = this.factoryBeanObjectCache.get(beanName);
				/* 缓存为空 */
				if (object == null) {
					/* 获取工厂bean结果 */
					object = doGetObjectFromFactoryBean(factory, beanName);
					/** 仅对上面的getObject（）调用期间进行后处理和存储（如果尚未存储）（例如，由于自定义getBean调用触发了循环引用处理）*/
					Object alreadyThere = this.factoryBeanObjectCache.get(beanName);
					/* 如果获取期间以及有了缓存，使用已有缓存 */
					if (alreadyThere != null) {
						object = alreadyThere;
					}
					else {
						/* 非合成bean定义，执行后处理 */
						if (shouldPostProcess) {
							/* 单例当前在创建，则不进行后处理，也不存储，直接返回未处理对象 */
							if (isSingletonCurrentlyInCreation(beanName)) {
								/* 暂时返回未处理的对象，但尚未存储。*/
								return object;
							}
							/* 设置单例正在创建中 */
							beforeSingletonCreation(beanName);
							try {
								/** 后处理{@link FactoryBean}返回的对象 */
								object = postProcessObjectFromFactoryBean(object, beanName);
							}
							catch (Throwable ex) {
								throw new BeanCreationException(beanName, "Post-processing of FactoryBean's singleton object failed", ex);
							}
							finally {
								/* 单例移除正在创建中状态 */
								afterSingletonCreation(beanName);
							}
						}
						/** 缓存{@link FactoryBean}生成单例 */
						if (containsSingleton(beanName)) {
							this.factoryBeanObjectCache.put(beanName, object);
						}
					}
				}
				return object;
			}
		}
		/* 工厂不是单例，或者单例不包含此bean名称 */
		else {
			Object object = doGetObjectFromFactoryBean(factory, beanName);
			/** 执行{@link FactoryBean}获取对象的后处理 */
			if (shouldPostProcess) {
				try {
					object = postProcessObjectFromFactoryBean(object, beanName);
				}
				catch (Throwable ex) {
					throw new BeanCreationException(beanName, "Post-processing of FactoryBean's object failed", ex);
				}
			}
			return object;
		}
	}

	/**
	 * Obtain an object to expose from the given FactoryBean.
	 * @param factory the FactoryBean instance
	 * @param beanName the name of the bean
	 * @return the object obtained from the FactoryBean
	 * @throws BeanCreationException if FactoryBean object creation failed
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	/**
	 * 从{@link FactoryBean}获取结果对象
	 * @param factory
	 * @param beanName
	 * @return
	 * @throws BeanCreationException
	 */
	private Object doGetObjectFromFactoryBean(FactoryBean<?> factory, String beanName) throws BeanCreationException {
		Object object;
		try {
			if (System.getSecurityManager() != null) {
				AccessControlContext acc = getAccessControlContext();
				try {
					object = AccessController.doPrivileged((PrivilegedExceptionAction<Object>) factory::getObject, acc);
				}
				catch (PrivilegedActionException pae) {
					throw pae.getException();
				}
			}
			else {
				object = factory.getObject();
			}
		}
		catch (FactoryBeanNotInitializedException ex) {
			throw new BeanCurrentlyInCreationException(beanName, ex.toString());
		}
		catch (Throwable ex) {
			throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
		}

		if (object == null) {
			if (isSingletonCurrentlyInCreation(beanName)) {
				throw new BeanCurrentlyInCreationException(beanName, "FactoryBean which is currently in creation returned null from getObject");
			}
			object = new NullBean();
		}
		return object;
	}

	/**
	 * Post-process the given object that has been obtained from the FactoryBean.
	 * The resulting object will get exposed for bean references.
	 * <p>The default implementation simply returns the given object as-is.
	 * Subclasses may override this, for example, to apply post-processors.
	 * @param object the object obtained from the FactoryBean.
	 * @param beanName the name of the bean
	 * @return the object to expose
	 * @throws org.springframework.beans.BeansException if any post-processing failed
	 */
	/**
	 * 对从FactoryBean获得的给定对象进行后处理。结果对象将暴露给bean引用。
	 * 默认实现只是按原样返回给定的对象。子类可以覆盖它，例如，以应用后处理器。
	 * @param object 是从FactoryBean获得的对象。
	 * @param beanName bean的名称
	 * @return 对象以公开
	 * @throws org.springframework.beans.BeansException 如果任何后处理失败
	 */
	protected Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException {
		return object;
	}

	/**
	 * Get a FactoryBean for the given bean if possible.
	 * @param beanName the name of the bean
	 * @param beanInstance the corresponding bean instance
	 * @return the bean instance as FactoryBean
	 * @throws BeansException if the given bean cannot be exposed as a FactoryBean
	 */
	protected FactoryBean<?> getFactoryBean(String beanName, Object beanInstance) throws BeansException {
		if (!(beanInstance instanceof FactoryBean)) {
			throw new BeanCreationException(beanName,
					"Bean instance of type [" + beanInstance.getClass() + "] is not a FactoryBean");
		}
		return (FactoryBean<?>) beanInstance;
	}

	/**
	 * Overridden to clear the FactoryBean object cache as well.
	 */
	@Override
	protected void removeSingleton(String beanName) {
		synchronized (getSingletonMutex()) {
			super.removeSingleton(beanName);
			this.factoryBeanObjectCache.remove(beanName);
		}
	}

	/**
	 * Overridden to clear the FactoryBean object cache as well.
	 */
	@Override
	protected void clearSingletonCache() {
		synchronized (getSingletonMutex()) {
			super.clearSingletonCache();
			this.factoryBeanObjectCache.clear();
		}
	}

	/**
	 * Return the security context for this bean factory. If a security manager
	 * is set, interaction with the user code will be executed using the privileged
	 * of the security context returned by this method.
	 * @see AccessController#getContext()
	 */
	protected AccessControlContext getAccessControlContext() {
		return AccessController.getContext();
	}

}
