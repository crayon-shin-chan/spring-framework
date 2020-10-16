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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCreationNotAllowedException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.core.SimpleAliasRegistry;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Generic registry for shared bean instances, implementing the
 * {@link org.springframework.beans.factory.config.SingletonBeanRegistry}.
 * Allows for registering singleton instances that should be shared
 * for all callers of the registry, to be obtained via bean name.
 *
 * <p>Also supports registration of
 * {@link org.springframework.beans.factory.DisposableBean} instances,
 * (which might or might not correspond to registered singletons),
 * to be destroyed on shutdown of the registry. Dependencies between
 * beans can be registered to enforce an appropriate shutdown order.
 *
 * <p>This class mainly serves as base class for
 * {@link org.springframework.beans.factory.BeanFactory} implementations,
 * factoring out the common management of singleton bean instances. Note that
 * the {@link org.springframework.beans.factory.config.ConfigurableBeanFactory}
 * interface extends the {@link SingletonBeanRegistry} interface.
 *
 * <p>Note that this class assumes neither a bean definition concept
 * nor a specific creation process for bean instances, in contrast to
 * {@link AbstractBeanFactory} and {@link DefaultListableBeanFactory}
 * (which inherit from it). Can alternatively also be used as a nested
 * helper to delegate to.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see #registerSingleton
 * @see #registerDisposableBean
 * @see org.springframework.beans.factory.DisposableBean
 * @see org.springframework.beans.factory.config.ConfigurableBeanFactory
 */
/**
 * 共享bean实例的通用注册表，实现{@link org.springframework.beans.factory.config.SingletonBeanRegistry}。
 * 允许注册应该由bean名称获得的所有注册表调用者共享的单例实例。
 * 还支持{@link org.springframework.beans.factory.DisposableBean}实例（可能对应或可能不对应已注册的单例）的注册，在注册表关闭时销毁。
 * 可以注册bean之间的依赖关系以强制执行适当的关闭命令。 此类主要用作{@link org.springframework.beans.factory.BeanFactory}实现的基类，排除了单例bean实例的常见管理。
 * 请注意，{@link org.springframework.beans.factory.config.ConfigurableBeanFactory}接口扩展了{@link SingletonBeanRegistry}接口。
 * 请注意，与{@link AbstractBeanFactory}和{@link DefaultListableBeanFactory}（从其继承的）相比，该类既不假设bean定义概念也不为bean实例指定创建过程。
 * 也可以用作委托的嵌套助手。
 * @see #registerSingleton
 * @see #registerDisposableBean
 * @see org.springframework.beans.factory.DisposableBean
 * @see org.springframework.beans.factory.config.ConfigurableBeanFactory
 */
public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {

	/** Maximum number of suppressed exceptions to preserve. */
	/**
	 * 保留的最大抑制异常数。
	 */
	private static final int SUPPRESSED_EXCEPTIONS_LIMIT = 100;


	/** Cache of singleton objects: bean name to bean instance. */
	/**
	 * 单例对象的高速缓存：bean名称到bean实例。
	 */
	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

	/** Cache of singleton factories: bean name to ObjectFactory. */
	/**
	 * 单例工厂的缓存：Bean名称为ObjectFactory。
	 */
	private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

	/** Cache of early singleton objects: bean name to bean instance. */
	/**
	 * 早期单例对象的高速缓存：Bean名称到Bean实例。
	 */
	private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

	/** Set of registered singletons, containing the bean names in registration order. */
	/**
	 * 已注册的单例集，按注册顺序包含Bean名称。
	 */
	private final Set<String> registeredSingletons = new LinkedHashSet<>(256);

	/** Names of beans that are currently in creation. */
	/**
	 * 当前正在创建的bean的名称
	 */
	private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

	/** Names of beans currently excluded from in creation checks. */
	/**
	 * 当前从创建检查中排除的bean名称。
	 */
	private final Set<String> inCreationCheckExclusions = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

	/** Collection of suppressed Exceptions, available for associating related causes. */
	/**
	 * 抑制的异常的集合，可用于关联相关原因。
	 */
	@Nullable
	private Set<Exception> suppressedExceptions;

	/** Flag that indicates whether we're currently within destroySingletons. */
	/**
	 * 指示我们当前是否在destroySingletons中的标志。
	 */
	private boolean singletonsCurrentlyInDestruction = false;

	/** Disposable bean instances: bean name to disposable instance. */
	/**
	 * {@link DisposableBean}一次性bean实例：一次性实例的bean名称。
	 */
	private final Map<String, Object> disposableBeans = new LinkedHashMap<>();

	/** Map between containing bean names: bean name to Set of bean names that the bean contains. */
	/**
	 * 在包含的Bean名称之间映射：Bean名称到Bean包含的Bean名称集。
	 */
	private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap<>(16);

	/** Map between dependent bean names: bean name to Set of dependent bean names. */
	/**
	 * 在相关的Bean名称之间映射：Bean名称到一组依赖它的Bean名称。
	 */
	private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);

	/** Map between depending bean names: bean name to Set of bean names for the bean's dependencies. */
	/**
	 * 在相关的Bean名称之间映射：Bean名称到Bean依赖项的Bean名称集。
	 */
	private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);


	/**
	 * 注册单例bean
	 * @param beanName Bean的名称
	 * @param singletonObject 现有的单例对象
	 * @throws IllegalStateException
	 */
	@Override
	public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException {
		Assert.notNull(beanName, "Bean name must not be null");
		Assert.notNull(singletonObject, "Singleton object must not be null");
		synchronized (this.singletonObjects) {
			Object oldObject = this.singletonObjects.get(beanName);
			/* 不能覆盖已有bean名称 */
			if (oldObject != null) {
				throw new IllegalStateException("Could not register object [" + singletonObject +
						"] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
			}
			addSingleton(beanName, singletonObject);
		}
	}

	/**
	 * Add the given singleton object to the singleton cache of this factory.
	 * <p>To be called for eager registration of singletons.
	 * @param beanName the name of the bean
	 * @param singletonObject the singleton object
	 */
	/**
	 * 将给定的单例对象添加到该工厂的单例缓存中。
	 * 被要求进行单身者的急切注册。
	 * @param beanName bean的名称
	 * @param singletonObject 单例对象
	 */
	protected void addSingleton(String beanName, Object singletonObject) {
		synchronized (this.singletonObjects) {
			this.singletonObjects.put(beanName, singletonObject);//添加到单例缓存
			this.singletonFactories.remove(beanName);//移除对象工厂缓存
			this.earlySingletonObjects.remove(beanName);//移除早期单例缓存
			this.registeredSingletons.add(beanName);//单例bean名称缓存
		}
	}

	/**
	 * Add the given singleton factory for building the specified singleton
	 * if necessary.
	 * <p>To be called for eager registration of singletons, e.g. to be able to
	 * resolve circular references.
	 * @param beanName the name of the bean
	 * @param singletonFactory the factory for the singleton object
	 */
	/**
	 * 如有必要，添加给定的单例工厂以构建指定的单例。
	 * 被要求进行单身者的急切注册，例如能够解析循环引用。
	 * @param beanName bean的名称
	 * @param singletonFactory 单例对象的工厂
	 */
	protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
		Assert.notNull(singletonFactory, "Singleton factory must not be null");
		synchronized (this.singletonObjects) {
			/* 单例bean缓存中不存在时才进行操作 */
			if (!this.singletonObjects.containsKey(beanName)) {
				this.singletonFactories.put(beanName, singletonFactory);//添加到对象工厂缓存
				this.earlySingletonObjects.remove(beanName);//移除早期单例缓存
				this.registeredSingletons.add(beanName);//添加到单例bean名称缓存
			}
		}
	}

	@Override
	@Nullable
	public Object getSingleton(String beanName) {
		return getSingleton(beanName, true);
	}

	/**
	 * Return the (raw) singleton object registered under the given name.
	 * <p>Checks already instantiated singletons and also allows for an early
	 * reference to a currently created singleton (resolving a circular reference).
	 * @param beanName the name of the bean to look for
	 * @param allowEarlyReference whether early references should be created or not
	 * @return the registered singleton object, or {@code null} if none found
	 */
	/**
	 * 返回以给定名称注册的（原始）单例对象。
	 * 检查已经实例化的单例，并且还允许对当前创建的单例的早期引用（解析循环引用）。
	 * @param beanName 要查找的bean的名称
	 * @param allowEarlyReference 是否应创建早期引用
	 * @return 注册的单例对象，如果找不到则返回{@code null}
	 */
	@Nullable
	protected Object getSingleton(String beanName, boolean allowEarlyReference) {
		Object singletonObject = this.singletonObjects.get(beanName);
		/* 单例对象不存在，且此单例在创建中 */
		if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
			synchronized (this.singletonObjects) {
				/* 获取早期单例对象 */
				singletonObject = this.earlySingletonObjects.get(beanName);
				/* 为空，且允许早期引用 */
				if (singletonObject == null && allowEarlyReference) {
					/* 从工厂加载 */
					ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
					if (singletonFactory != null) {
						/* 获取工厂对象 */
						singletonObject = singletonFactory.getObject();
						this.earlySingletonObjects.put(beanName, singletonObject);//添加到早期单例缓存
						this.singletonFactories.remove(beanName);//从单例缓存中移除
					}
				}
			}
		}
		return singletonObject;
	}

	/**
	 * Return the (raw) singleton object registered under the given name,
	 * creating and registering a new one if none registered yet.
	 * @param beanName the name of the bean
	 * @param singletonFactory the ObjectFactory to lazily create the singleton
	 * with, if necessary
	 * @return the registered singleton object
	 */
	/**
	 * 返回以给定名称注册的（原始）单例对象，如果尚未注册，则创建并注册一个新对象。
	 * @param beanName Bean的名称
	 * @param singletonFactory 要懒惰地创建单例的ObjectFactory如有必要，使用
	 * @return 注册的单例对象
	 */
	public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
		Assert.notNull(beanName, "Bean name must not be null");
		/** bean单例缓存 */
		synchronized (this.singletonObjects) {
			Object singletonObject = this.singletonObjects.get(beanName);
			/* 单例缓存不存在 */
			if (singletonObject == null) {
				/* 单例在销毁中 */
				if (this.singletonsCurrentlyInDestruction) {
					throw new BeanCreationNotAllowedException(beanName, "Singleton bean creation not allowed while singletons of this factory are in destruction " + "(Do not request a bean from a BeanFactory in a destroy method implementation!)");
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Creating shared instance of singleton bean '" + beanName + "'");
				}
				/* 检查bean名称，是否正在创建 */
				beforeSingletonCreation(beanName);
				/* 是否新的单例 */
				boolean newSingleton = false;
				/* 记录被压制异常 */
				boolean recordSuppressedExceptions = (this.suppressedExceptions == null);
				if (recordSuppressedExceptions) {
					this.suppressedExceptions = new LinkedHashSet<>();
				}
				try {
					/* 从单例工厂获取bean */
					singletonObject = singletonFactory.getObject();
					newSingleton = true;
				}
				catch (IllegalStateException ex) {
					/* 如果出现了异常，但是在此期间单例被创建了，则继续处理 */
					/* 在此期间，单例对象是否隐式出现-> 如果是，请继续处理它，因为异常指示该状态。 */
					singletonObject = this.singletonObjects.get(beanName);
					if (singletonObject == null) {
						throw ex;
					}
				}
				catch (BeanCreationException ex) {
					if (recordSuppressedExceptions) {
						for (Exception suppressedException : this.suppressedExceptions) {
							ex.addRelatedCause(suppressedException);
						}
					}
					throw ex;
				}
				finally {
					if (recordSuppressedExceptions) {
						this.suppressedExceptions = null;
					}
					/* 单例移出正在创建集合 */
					afterSingletonCreation(beanName);
				}
				/* 新的单例bean，添加缓存 */
				if (newSingleton) {
					addSingleton(beanName, singletonObject);
				}
			}
			return singletonObject;
		}
	}

	/**
	 * Register an exception that happened to get suppressed during the creation of a
	 * singleton bean instance, e.g. a temporary circular reference resolution problem.
	 * <p>The default implementation preserves any given exception in this registry's
	 * collection of suppressed exceptions, up to a limit of 100 exceptions, adding
	 * them as related causes to an eventual top-level {@link BeanCreationException}.
	 * @param ex the Exception to register
	 * @see BeanCreationException#getRelatedCauses()
	 */
	protected void onSuppressedException(Exception ex) {
		synchronized (this.singletonObjects) {
			if (this.suppressedExceptions != null && this.suppressedExceptions.size() < SUPPRESSED_EXCEPTIONS_LIMIT) {
				this.suppressedExceptions.add(ex);
			}
		}
	}

	/**
	 * Remove the bean with the given name from the singleton cache of this factory,
	 * to be able to clean up eager registration of a singleton if creation failed.
	 * @param beanName the name of the bean
	 * @see #getSingletonMutex()
	 */
	protected void removeSingleton(String beanName) {
		synchronized (this.singletonObjects) {
			this.singletonObjects.remove(beanName);
			this.singletonFactories.remove(beanName);
			this.earlySingletonObjects.remove(beanName);
			this.registeredSingletons.remove(beanName);
		}
	}

	@Override
	public boolean containsSingleton(String beanName) {
		return this.singletonObjects.containsKey(beanName);
	}

	@Override
	public String[] getSingletonNames() {
		synchronized (this.singletonObjects) {
			return StringUtils.toStringArray(this.registeredSingletons);
		}
	}

	@Override
	public int getSingletonCount() {
		synchronized (this.singletonObjects) {
			return this.registeredSingletons.size();
		}
	}


	public void setCurrentlyInCreation(String beanName, boolean inCreation) {
		Assert.notNull(beanName, "Bean name must not be null");
		if (!inCreation) {
			this.inCreationCheckExclusions.add(beanName);
		}
		else {
			this.inCreationCheckExclusions.remove(beanName);
		}
	}

	public boolean isCurrentlyInCreation(String beanName) {
		Assert.notNull(beanName, "Bean name must not be null");
		return (!this.inCreationCheckExclusions.contains(beanName) && isActuallyInCreation(beanName));
	}

	protected boolean isActuallyInCreation(String beanName) {
		return isSingletonCurrentlyInCreation(beanName);
	}

	/**
	 * Return whether the specified singleton bean is currently in creation
	 * (within the entire factory).
	 * @param beanName the name of the bean
	 */
	public boolean isSingletonCurrentlyInCreation(String beanName) {
		return this.singletonsCurrentlyInCreation.contains(beanName);
	}

	/**
	 * Callback before singleton creation.
	 * <p>The default implementation register the singleton as currently in creation.
	 * @param beanName the name of the singleton about to be created
	 * @see #isSingletonCurrentlyInCreation
	 */
	/**
	 * 创建单例之前的回调。
	 * 默认实现将单例注册为当前正在创建中。
	 * @param beanName 即将创建的单例的名称
	 * @see #isSingletonCurrentlyInCreation
	 */
	protected void beforeSingletonCreation(String beanName) {
		if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.add(beanName)) {
			throw new BeanCurrentlyInCreationException(beanName);
		}
	}

	/**
	 * Callback after singleton creation.
	 * <p>The default implementation marks the singleton as not in creation anymore.
	 * @param beanName the name of the singleton that has been created
	 * @see #isSingletonCurrentlyInCreation
	 */
	protected void afterSingletonCreation(String beanName) {
		if (!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.remove(beanName)) {
			throw new IllegalStateException("Singleton '" + beanName + "' isn't currently in creation");
		}
	}


	/**
	 * Add the given bean to the list of disposable beans in this registry.
	 * <p>Disposable beans usually correspond to registered singletons,
	 * matching the bean name but potentially being a different instance
	 * (for example, a DisposableBean adapter for a singleton that does not
	 * naturally implement Spring's DisposableBean interface).
	 * @param beanName the name of the bean
	 * @param bean the bean instance
	 */
	public void registerDisposableBean(String beanName, DisposableBean bean) {
		synchronized (this.disposableBeans) {
			this.disposableBeans.put(beanName, bean);
		}
	}

	/**
	 * Register a containment relationship between two beans,
	 * e.g. between an inner bean and its containing outer bean.
	 * <p>Also registers the containing bean as dependent on the contained bean
	 * in terms of destruction order.
	 * @param containedBeanName the name of the contained (inner) bean
	 * @param containingBeanName the name of the containing (outer) bean
	 * @see #registerDependentBean
	 */
	public void registerContainedBean(String containedBeanName, String containingBeanName) {
		synchronized (this.containedBeanMap) {
			Set<String> containedBeans =
					this.containedBeanMap.computeIfAbsent(containingBeanName, k -> new LinkedHashSet<>(8));
			if (!containedBeans.add(containedBeanName)) {
				return;
			}
		}
		registerDependentBean(containedBeanName, containingBeanName);
	}

	/**
	 * Register a dependent bean for the given bean,
	 * to be destroyed before the given bean is destroyed.
	 * @param beanName the name of the bean
	 * @param dependentBeanName the name of the dependent bean
	 */
	/**
	 * 在给定的bean被销毁之前，为给定的bean注册一个要被销毁的依赖bean。
	 * @param beanName bean的名称
	 * @param dependentBeanName 从属bean的名称，即依赖于上面bean
	 */
	public void registerDependentBean(String beanName, String dependentBeanName) {
		String canonicalName = canonicalName(beanName);

		synchronized (this.dependentBeanMap) {
			Set<String> dependentBeans = this.dependentBeanMap.computeIfAbsent(canonicalName, k -> new LinkedHashSet<>(8));
			if (!dependentBeans.add(dependentBeanName)) {
				return;
			}
		}

		synchronized (this.dependenciesForBeanMap) {
			Set<String> dependenciesForBean = this.dependenciesForBeanMap.computeIfAbsent(dependentBeanName, k -> new LinkedHashSet<>(8));
			dependenciesForBean.add(canonicalName);
		}
	}

	/**
	 * Determine whether the specified dependent bean has been registered as
	 * dependent on the given bean or on any of its transitive dependencies.
	 * @param beanName the name of the bean to check
	 * @param dependentBeanName the name of the dependent bean
	 * @since 4.0
	 */
	/**
	 * 确定指定的依赖项是否已注册为依赖于给定bean或其任何传递依赖项。
	 * @param beanName 要检查的bean的名称
	 * @param dependentBeanName 依赖bean的名称
	 */
	protected boolean isDependent(String beanName, String dependentBeanName) {
		synchronized (this.dependentBeanMap) {
			return isDependent(beanName, dependentBeanName, null);
		}
	}

	private boolean isDependent(String beanName, String dependentBeanName, @Nullable Set<String> alreadySeen) {
		/* 已经看过，返回false */
		if (alreadySeen != null && alreadySeen.contains(beanName)) {
			return false;
		}
		/* 获取规范名称，以及其依赖的bean名称 */
		String canonicalName = canonicalName(beanName);
		Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
		if (dependentBeans == null) {
			return false;
		}
		/* 包含返回true */
		if (dependentBeans.contains(dependentBeanName)) {
			return true;
		}
		/* 递归查找依赖，即查找依赖的依赖 */
		for (String transitiveDependency : dependentBeans) {
			if (alreadySeen == null) {
				alreadySeen = new HashSet<>();
			}
			alreadySeen.add(beanName);
			if (isDependent(transitiveDependency, dependentBeanName, alreadySeen)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determine whether a dependent bean has been registered for the given name.
	 * @param beanName the name of the bean to check
	 */
	protected boolean hasDependentBean(String beanName) {
		return this.dependentBeanMap.containsKey(beanName);
	}

	/**
	 * Return the names of all beans which depend on the specified bean, if any.
	 * @param beanName the name of the bean
	 * @return the array of dependent bean names, or an empty array if none
	 */
	public String[] getDependentBeans(String beanName) {
		Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
		if (dependentBeans == null) {
			return new String[0];
		}
		synchronized (this.dependentBeanMap) {
			return StringUtils.toStringArray(dependentBeans);
		}
	}

	/**
	 * Return the names of all beans that the specified bean depends on, if any.
	 * @param beanName the name of the bean
	 * @return the array of names of beans which the bean depends on,
	 * or an empty array if none
	 */
	public String[] getDependenciesForBean(String beanName) {
		Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
		if (dependenciesForBean == null) {
			return new String[0];
		}
		synchronized (this.dependenciesForBeanMap) {
			return StringUtils.toStringArray(dependenciesForBean);
		}
	}

	public void destroySingletons() {
		if (logger.isTraceEnabled()) {
			logger.trace("Destroying singletons in " + this);
		}
		synchronized (this.singletonObjects) {
			this.singletonsCurrentlyInDestruction = true;
		}

		String[] disposableBeanNames;
		synchronized (this.disposableBeans) {
			disposableBeanNames = StringUtils.toStringArray(this.disposableBeans.keySet());
		}
		for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
			destroySingleton(disposableBeanNames[i]);
		}

		this.containedBeanMap.clear();
		this.dependentBeanMap.clear();
		this.dependenciesForBeanMap.clear();

		clearSingletonCache();
	}

	/**
	 * Clear all cached singleton instances in this registry.
	 * @since 4.3.15
	 */
	protected void clearSingletonCache() {
		synchronized (this.singletonObjects) {
			this.singletonObjects.clear();
			this.singletonFactories.clear();
			this.earlySingletonObjects.clear();
			this.registeredSingletons.clear();
			this.singletonsCurrentlyInDestruction = false;
		}
	}

	/**
	 * Destroy the given bean. Delegates to {@code destroyBean}
	 * if a corresponding disposable bean instance is found.
	 * @param beanName the name of the bean
	 * @see #destroyBean
	 */
	public void destroySingleton(String beanName) {
		// Remove a registered singleton of the given name, if any.
		removeSingleton(beanName);

		// Destroy the corresponding DisposableBean instance.
		DisposableBean disposableBean;
		synchronized (this.disposableBeans) {
			disposableBean = (DisposableBean) this.disposableBeans.remove(beanName);
		}
		destroyBean(beanName, disposableBean);
	}

	/**
	 * Destroy the given bean. Must destroy beans that depend on the given
	 * bean before the bean itself. Should not throw any exceptions.
	 * @param beanName the name of the bean
	 * @param bean the bean instance to destroy
	 */
	protected void destroyBean(String beanName, @Nullable DisposableBean bean) {
		// Trigger destruction of dependent beans first...
		Set<String> dependencies;
		synchronized (this.dependentBeanMap) {
			// Within full synchronization in order to guarantee a disconnected Set
			dependencies = this.dependentBeanMap.remove(beanName);
		}
		if (dependencies != null) {
			if (logger.isTraceEnabled()) {
				logger.trace("Retrieved dependent beans for bean '" + beanName + "': " + dependencies);
			}
			for (String dependentBeanName : dependencies) {
				destroySingleton(dependentBeanName);
			}
		}

		// Actually destroy the bean now...
		if (bean != null) {
			try {
				bean.destroy();
			}
			catch (Throwable ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Destruction of bean with name '" + beanName + "' threw an exception", ex);
				}
			}
		}

		// Trigger destruction of contained beans...
		Set<String> containedBeans;
		synchronized (this.containedBeanMap) {
			// Within full synchronization in order to guarantee a disconnected Set
			containedBeans = this.containedBeanMap.remove(beanName);
		}
		if (containedBeans != null) {
			for (String containedBeanName : containedBeans) {
				destroySingleton(containedBeanName);
			}
		}

		// Remove destroyed bean from other beans' dependencies.
		synchronized (this.dependentBeanMap) {
			for (Iterator<Map.Entry<String, Set<String>>> it = this.dependentBeanMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, Set<String>> entry = it.next();
				Set<String> dependenciesToClean = entry.getValue();
				dependenciesToClean.remove(beanName);
				if (dependenciesToClean.isEmpty()) {
					it.remove();
				}
			}
		}

		// Remove destroyed bean's prepared dependency information.
		this.dependenciesForBeanMap.remove(beanName);
	}

	/**
	 * Exposes the singleton mutex to subclasses and external collaborators.
	 * <p>Subclasses should synchronize on the given Object if they perform
	 * any sort of extended singleton creation phase. In particular, subclasses
	 * should <i>not</i> have their own mutexes involved in singleton creation,
	 * to avoid the potential for deadlocks in lazy-init situations.
	 */
	@Override
	public final Object getSingletonMutex() {
		return this.singletonObjects;
	}

}
