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

package org.springframework.core;

/**
 * Common interface for managing aliases. Serves as a super-interface for
 * {@link org.springframework.beans.factory.support.BeanDefinitionRegistry}.
 *
 * @author Juergen Hoeller
 * @since 2.5.2
 */
/**
 * 用于管理别名的通用接口。
 * 用作* {@link org.springframework.beans.factory.support.BeanDefinitionRegistry}的超级接口。
 */
public interface AliasRegistry {

	/**
	 * Given a name, register an alias for it.
	 * @param name the canonical name
	 * @param alias the alias to be registered
	 * @throws IllegalStateException if the alias is already in use
	 * and may not be overridden
	 */
	/**
	 * 给定名称，为其注册一个别名。
	 * @param name 规范名称
	 * @param alias 要注册的别名
	 * @throws IllegalStateException（如果别名已在使用中*可能不会被覆盖）
	 */
	void registerAlias(String name, String alias);

	/**
	 * Remove the specified alias from this registry.
	 * @param alias the alias to remove
	 * @throws IllegalStateException if no such alias was found
	 */
	/**
	 * 从此注册表中删除指定的别名。
	 * @param alias 要删除的别名
	 * @throws IllegalStateException 如果找不到这样的别名
	 */
	void removeAlias(String alias);

	/**
	 * Determine whether the given name is defined as an alias
	 * (as opposed to the name of an actually registered component).
	 * @param name the name to check
	 * @return whether the given name is an alias
	 */
	/**
	 * 确定给定名称是否定义为别名（与实际注册的组件的名称相反）。
	 * @param name 要检查的名称
	 * @return 给定名称是否为别名
	 */
	boolean isAlias(String name);

	/**
	 * Return the aliases for the given name, if defined.
	 * @param name the name to check for aliases
	 * @return the aliases, or an empty array if none
	 */
	/**
	 * 返回给定名称的别名（如果已定义）。
	 * @param name 要检查别名的名称
	 * @return 别名，如果没有则返回一个空数组
	 */
	String[] getAliases(String name);

}
