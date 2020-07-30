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

package org.springframework.core.annotation;

import java.lang.annotation.Annotation;

/**
 * Callback interface that can be used to filter specific annotation types.
 *
 * <p>Note that the {@link MergedAnnotations} model (which this interface has been
 * designed for) always ignores lang annotations according to the {@link #PLAIN}
 * filter (for efficiency reasons). Any additional filters and even custom filter
 * implementations apply within this boundary and may only narrow further from here.
 *
 * @author Phillip Webb
 * @author Juergen Hoeller
 * @since 5.2
 * @see MergedAnnotations
 */
/**
 * 注解过滤器，用于筛选注解类型，函数式接口
 * 注意：{@link MergedAnnotations}模型总是会忽略java.lang包下的注解，由于效率原因
 * 任何额外的过滤器，甚至自定义过滤器，只会从这个范围进一步缩小
 */
@FunctionalInterface
public interface AnnotationFilter {

	/**
	 * {@link AnnotationFilter} that matches annotations in the
	 * {@code java.lang} and {@code org.springframework.lang} packages
	 * and their subpackages.
	 * <p>This is the default filter in the {@link MergedAnnotations} model.
	 */
  /** PLAIN过滤器，匹配java.lang和org.springframework.lang与它们的子包
   * 这是{@link MergedAnnotations}中的默认过滤器
   */
	AnnotationFilter PLAIN = packages("java.lang", "org.springframework.lang");

	/**
	 * {@link AnnotationFilter} that matches annotations in the
	 * {@code java} and {@code javax} packages and their subpackages.
	 */
  /** 匹配java、javax和它们的子包 */
	AnnotationFilter JAVA = packages("java", "javax");

	/**
	 * {@link AnnotationFilter} that always matches and can be used when no
	 * relevant annotation types are expected to be present at all.
	 */
  /** 匹配所有 */
	AnnotationFilter ALL = new AnnotationFilter() {
		@Override
		public boolean matches(Annotation annotation) {
			return true;
		}
		@Override
		public boolean matches(Class<?> type) {
			return true;
		}
		@Override
		public boolean matches(String typeName) {
			return true;
		}
		@Override
		public String toString() {
			return "All annotations filtered";
		}
	};

	/**
	 * {@link AnnotationFilter} that never matches and can be used when no
	 * filtering is needed (allowing for any annotation types to be present).
	 * @deprecated as of 5.2.6 since the {@link MergedAnnotations} model
	 * always ignores lang annotations according to the {@link #PLAIN} filter
	 * (for efficiency reasons)
	 * @see #PLAIN
	 */
  /** 不匹配任何注解 */
	@Deprecated
	AnnotationFilter NONE = new AnnotationFilter() {
		@Override
		public boolean matches(Annotation annotation) {
			return false;
		}
		@Override
		public boolean matches(Class<?> type) {
			return false;
		}
		@Override
		public boolean matches(String typeName) {
			return false;
		}
		@Override
		public String toString() {
			return "No annotation filtering";
		}
	};


	/**
	 * Test if the given annotation matches the filter.
	 * @param annotation the annotation to test
	 * @return {@code true} if the annotation matches
	 */
  /** 注解是否与过滤器匹配 */
	default boolean matches(Annotation annotation) {
		return matches(annotation.annotationType());
	}

	/**
	 * Test if the given type matches the filter.
	 * @param type the annotation type to test
	 * @return {@code true} if the annotation matches
	 */
  /** 给定类型是否与过滤器匹配 */
	default boolean matches(Class<?> type) {
		return matches(type.getName());
	}

	/**
	 * Test if the given type name matches the filter.
	 * @param typeName the fully qualified class name of the annotation type to test
	 * @return {@code true} if the annotation matches
	 */
  /** 指定类型是否与过滤器匹配 */
	boolean matches(String typeName);


	/**
	 * Create a new {@link AnnotationFilter} that matches annotations in the
	 * specified packages.
	 * @param packages the annotation packages that should match
	 * @return a new {@link AnnotationFilter} instance
	 */
  /** 创建一个注解过滤器，匹配指定包名 */
	static AnnotationFilter packages(String... packages) {
		return new PackagesAnnotationFilter(packages);
	}

}
