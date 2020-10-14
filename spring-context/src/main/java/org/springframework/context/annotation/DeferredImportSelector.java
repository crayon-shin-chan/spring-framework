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

package org.springframework.context.annotation;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

/**
 * A variation of {@link ImportSelector} that runs after all {@code @Configuration} beans
 * have been processed. This type of selector can be particularly useful when the selected
 * imports are {@code @Conditional}.
 *
 * <p>Implementations can also extend the {@link org.springframework.core.Ordered}
 * interface or use the {@link org.springframework.core.annotation.Order} annotation to
 * indicate a precedence against other {@link DeferredImportSelector DeferredImportSelectors}.
 *
 * <p>Implementations may also provide an {@link #getImportGroup() import group} which
 * can provide additional sorting and filtering logic across different selectors.
 *
 * @author Phillip Webb
 * @author Stephane Nicoll
 * @since 4.0
 */

/**
 * {@link ImportSelector}的一种变体，在所有{@code @Configuration}bean被处理之后运行。
 * 当所选的导入为{@code @Conditional}时，这种选择器特别有用。
 * 实现还可以扩展{@link org.springframework.core.Ordered}接口或使用{@link org.springframework.core.annotation.Order}注释来表示优先于
 * 其他{@link DeferredImportSelector}。
 * 实现还可以提供一个{@link #getImportGroup（）}，它可以为不同的选择器提供附加的排序和过滤逻辑。
 */
public interface DeferredImportSelector extends ImportSelector {

	/**
	 * Return a specific import group.
	 * <p>The default implementations return {@code null} for no grouping required.
	 * @return the import group class, or {@code null} if none
	 * @since 5.0
	 */
	/**
	 * 返回特定的导入组。
	 * 默认实现返回{@code null}，无需分组。
	 * @return 导入组类；如果没有，则返回{@code null}
	 */
	@Nullable
	default Class<? extends Group> getImportGroup() {
		return null;
	}


	/**
	 * Interface used to group results from different import selectors.
	 * @since 5.0
	 */
	/**
	 * 用于对来自不同导入选择器的结果进行分组的接口。
	 */
	interface Group {

		/**
		 * Process the {@link AnnotationMetadata} of the importing @{@link Configuration}
		 * class using the specified {@link DeferredImportSelector}.
		 */
		/**
		 * 使用指定的{@link DeferredImportSelector}处理导入的{@link Configuration}类的{@link AnnotationMetadata}。
		 * @param metadata
		 * @param selector
		 */
		void process(AnnotationMetadata metadata, DeferredImportSelector selector);

		/**
		 * Return the {@link Entry entries} of which class(es) should be imported
		 * for this group.
		 */
		/**
		 * 返回此组应导入的类的{@link Entry entry}。
		 * @return
		 */
		Iterable<Entry> selectImports();


		/**
		 * An entry that holds the {@link AnnotationMetadata} of the importing
		 * {@link Configuration} class and the class name to import.
		 */
		/**
		 * 一个条目，其中包含导入{@link Configuration}类的{@link AnnotationMetadata}和要导入的类名。
		 */
		class Entry {

			private final AnnotationMetadata metadata;

			private final String importClassName;

			public Entry(AnnotationMetadata metadata, String importClassName) {
				this.metadata = metadata;
				this.importClassName = importClassName;
			}

			/**
			 * Return the {@link AnnotationMetadata} of the importing
			 * {@link Configuration} class.
			 */
			public AnnotationMetadata getMetadata() {
				return this.metadata;
			}

			/**
			 * Return the fully qualified name of the class to import.
			 */
			public String getImportClassName() {
				return this.importClassName;
			}

			@Override
			public boolean equals(@Nullable Object other) {
				if (this == other) {
					return true;
				}
				if (other == null || getClass() != other.getClass()) {
					return false;
				}
				Entry entry = (Entry) other;
				return (this.metadata.equals(entry.metadata) && this.importClassName.equals(entry.importClassName));
			}

			@Override
			public int hashCode() {
				return (this.metadata.hashCode() * 31 + this.importClassName.hashCode());
			}

			@Override
			public String toString() {
				return this.importClassName;
			}
		}
	}

}
