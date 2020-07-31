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

/**
 * Extension of the {@link Ordered} interface, expressing a <em>priority</em>
 * ordering: {@code PriorityOrdered} objects are always applied before
 * <em>plain</em> {@link Ordered} objects regardless of their order values.
 *
 * <p>When sorting a set of {@code Ordered} objects, {@code PriorityOrdered}
 * objects and <em>plain</em> {@code Ordered} objects are effectively treated as
 * two separate subsets, with the set of {@code PriorityOrdered} objects preceding
 * the set of <em>plain</em> {@code Ordered} objects and with relative
 * ordering applied within those subsets.
 *
 * <p>This is primarily a special-purpose interface, used within the framework
 * itself for objects where it is particularly important to recognize
 * <em>prioritized</em> objects first, potentially without even obtaining the
 * remaining objects. A typical example: prioritized post-processors in a Spring
 * {@link org.springframework.context.ApplicationContext}.
 *
 * <p>Note: {@code PriorityOrdered} post-processor beans are initialized in
 * a special phase, ahead of other post-processor beans. This subtly
 * affects their autowiring behavior: they will only be autowired against
 * beans which do not require eager initialization for type matching.
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 2.5
 * @see org.springframework.beans.factory.config.PropertyOverrideConfigurer
 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
 */
/**
 * 扩展{@link Ordered}接口，表示优先排序
 * {@code PriorityOrdered}对象总是在普通{@link Ordered}对象前面应用，而不考虑它们的order值
 * 在对一组{@code Ordered}对象排序时，{@code PriorityOrdered}对象和普通{@code Ordered}对象被有效地视为两个单独的子集
 * 在每个子集中进行相对排序
 * 
 * 这是一个特殊用途的接口，在框架内使用对于识别特别重要的优先对象
 * 一个典型的例子是{@link org.springframework.context.ApplicationContext}中的后处理器的优先级
 * 注意：{@code PriorityOrdered}后处理器bean在一个特殊阶段被初始化，领先于其他后处理器。 
 * 很微妙的影响了他们的自动注入autowiring行为
 * 他们只会被不需要对类型匹配进行急切初始化的bean自动注入。
 * 
 */
public interface PriorityOrdered extends Ordered {
}
