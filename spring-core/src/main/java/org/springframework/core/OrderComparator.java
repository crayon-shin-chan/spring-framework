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

package org.springframework.core;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

/**
 * {@link Comparator} implementation for {@link Ordered} objects, sorting
 * by order value ascending, respectively by priority descending.
 *
 * <h3>{@link PriorityOrdered} Objects</h3>
 * <p>{@link PriorityOrdered} objects will be sorted with higher priority than
 * <em>plain</em> {@code Ordered} objects.
 *
 * <h3>Same Order Objects</h3>
 * <p>Objects that have the same order value will be sorted with arbitrary
 * ordering with respect to other objects with the same order value.
 *
 * <h3>Non-ordered Objects</h3>
 * <p>Any object that does not provide its own order value is implicitly
 * assigned a value of {@link Ordered#LOWEST_PRECEDENCE}, thus ending up
 * at the end of a sorted collection in arbitrary order with respect to
 * other objects with the same order value.
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 07.04.2003
 * @see Ordered
 * @see PriorityOrdered
 * @see org.springframework.core.annotation.AnnotationAwareOrderComparator
 * @see java.util.List#sort(java.util.Comparator)
 * @see java.util.Arrays#sort(Object[], java.util.Comparator)
 */
/**
 * {@link Ordered}对象的比较器，排序时按照order值升序排列，按照优先级降序排列
 * {@link PriorityOrdered}对象将以高于普通{@link Ordered}对象的优先级排序
 * 具有相同顺序值的对象将被任意排序
 * 任何不提供自己顺序值的对象都是隐式分配了{@link Ordered#LOWEST_PRECEDENCE}的优先级，从而排在排序集合的末尾，按任意顺序排列
 */
public class OrderComparator implements Comparator<Object> {

	/**
   * Shared default instance of {@code OrderComparator}.
   */
  /** 默认比较器实例 */
	public static final OrderComparator INSTANCE = new OrderComparator();


	/**
     * Build an adapted order comparator with the given source provider.
     * @param sourceProvider the order source provider to use
     * @return the adapted comparator
     * @since 4.1
     */
  /** 使用给定的{@link OrderSourceProvider}构建一个排序比较器 */
	public Comparator<Object> withSourceProvider(OrderSourceProvider sourceProvider) {
		return (o1, o2) -> doCompare(o1, o2, sourceProvider);
	}

  /** 默认的比较方法，使用null为OrderSourceProvider */
	@Override
	public int compare(@Nullable Object o1, @Nullable Object o2) {
		return doCompare(o1, o2, null);
	}

  /** 执行比较
   * @param o1：比较对象1
   * @param o2：比较对象2
   * @param sourceProvider：order顺序值提供者
   */
  private int doCompare(@Nullable Object o1, @Nullable Object o2, @Nullable OrderSourceProvider sourceProvider) {
    /** 是否为高优先级对象 */
    boolean p1 = (o1 instanceof PriorityOrdered);
    boolean p2 = (o2 instanceof PriorityOrdered);
    /** o1是，o2不是，则o1小于o2 */
    if (p1 && !p2) {
      return -1;
    }
    /** o1不是，o2是，则o1大于o2 */
		else if (p2 && !p1) {
			return 1;
		}

    /** 都是或者都不是，比较获取的order */
		int i1 = getOrder(o1, sourceProvider);
		int i2 = getOrder(o2, sourceProvider);
		return Integer.compare(i1, i2);
	}

	/**
     * Determine the order value for the given object.
     * <p>The default implementation checks against the given {@link OrderSourceProvider}
     * using {@link #findOrder} and falls back to a regular {@link #getOrder(Object)} call.
     * @param obj the object to check
     * @return the order value, or {@code Ordered.LOWEST_PRECEDENCE} as fallback
     */
  /** 确定给定对象的order值，默认实现检查给定的{@link OrderSourceProvider} */
	private int getOrder(@Nullable Object obj, @Nullable OrderSourceProvider sourceProvider) {
    Integer order = null;
    /** sourceProvider不为null */
    if (obj != null && sourceProvider != null) {
      /** 获取order的源 */
			Object orderSource = sourceProvider.getOrderSource(obj);
			if (orderSource != null) {
        /** 如果是数组，则遍历每个元素查找 */
				if (orderSource.getClass().isArray()) {
          Object[] sources = ObjectUtils.toObjectArray(orderSource);
          /** 遍历所有order源 */
					for (Object source : sources) {
            /** 从order源查找order值，存在即返回 */
						order = findOrder(source);
						if (order != null) {
							break;
						}
					}
				}
				else {
					order = findOrder(orderSource);
				}
			}
		}
		return (order != null ? order : getOrder(obj));
	}

	/**
	 * Determine the order value for the given object.
	 * <p>The default implementation checks against the {@link Ordered} interface
	 * through delegating to {@link #findOrder}. Can be overridden in subclasses.
	 * @param obj the object to check
	 * @return the order value, or {@code Ordered.LOWEST_PRECEDENCE} as fallback
	 */
  /** 确定给定对象的order值，默认实现通过{@link #findOrder}检查{@link Ordered}接口 */
	protected int getOrder(@Nullable Object obj) {
		if (obj != null) {
			Integer order = findOrder(obj);
			if (order != null) {
				return order;
			}
    }
    /** 默认返回最低优先级 */
		return Ordered.LOWEST_PRECEDENCE;
	}

	/**
	 * Find an order value indicated by the given object.
	 * <p>The default implementation checks against the {@link Ordered} interface.
	 * Can be overridden in subclasses.
	 * @param obj the object to check
	 * @return the order value, or {@code null} if none found
	 */
  /** 通过检查{@link Ordered}接口，获取给的对象的order值 */
	@Nullable
	protected Integer findOrder(Object obj) {
		return (obj instanceof Ordered ? ((Ordered) obj).getOrder() : null);
	}

	/**
	 * Determine a priority value for the given object, if any.
	 * <p>The default implementation always returns {@code null}.
	 * Subclasses may override this to give specific kinds of values a
	 * 'priority' characteristic, in addition to their 'order' semantics.
	 * A priority indicates that it may be used for selecting one object over
	 * another, in addition to serving for ordering purposes in a list/array.
	 * @param obj the object to check
	 * @return the priority value, or {@code null} if none
	 * @since 4.1
	 */
  /** 确定给定对象的优先级值，默认返回null */
	@Nullable
	public Integer getPriority(Object obj) {
		return null;
	}


	/**
	 * Sort the given List with a default OrderComparator.
	 * <p>Optimized to skip sorting for lists with size 0 or 1,
	 * in order to avoid unnecessary array extraction.
	 * @param list the List to sort
	 * @see java.util.List#sort(java.util.Comparator)
	 */
  /** 对列表进行排序 */
	public static void sort(List<?> list) {
		if (list.size() > 1) {
			list.sort(INSTANCE);
		}
	}

	/**
	 * Sort the given array with a default OrderComparator.
	 * <p>Optimized to skip sorting for lists with size 0 or 1,
	 * in order to avoid unnecessary array extraction.
	 * @param array the array to sort
	 * @see java.util.Arrays#sort(Object[], java.util.Comparator)
	 */
  /** 对象数组进行排序 */
	public static void sort(Object[] array) {
		if (array.length > 1) {
			Arrays.sort(array, INSTANCE);
		}
	}

	/**
	 * Sort the given array or List with a default OrderComparator,
	 * if necessary. Simply skips sorting when given any other value.
	 * <p>Optimized to skip sorting for lists with size 0 or 1,
	 * in order to avoid unnecessary array extraction.
	 * @param value the array or List to sort
	 * @see java.util.Arrays#sort(Object[], java.util.Comparator)
	 */
  /** 如果是列表或者数组，则排序 */
	public static void sortIfNecessary(Object value) {
		if (value instanceof Object[]) {
			sort((Object[]) value);
		}
		else if (value instanceof List) {
			sort((List<?>) value);
		}
	}


	/**
	 * Strategy interface to provide an order source for a given object.
	 * @since 4.1
	 */
  /** 策略接口，为给定对象提供order源。 */
	@FunctionalInterface
	public interface OrderSourceProvider {

		/**
		 * Return an order source for the specified object, i.e. an object that
		 * should be checked for an order value as a replacement to the given object.
		 * <p>Can also be an array of order source objects.
		 * <p>If the returned object does not indicate any order, the comparator
		 * will fall back to checking the original object.
		 * @param obj the object to find an order source for
		 * @return the order source for that object, or {@code null} if none found
		 */
    /** 返回给定对象的order源，可以返回order源对象的数组 */
		@Nullable
		Object getOrderSource(Object obj);
	}

}
