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

package org.springframework.core.annotation;

import java.lang.annotation.Annotation;

import org.springframework.lang.Nullable;

/**
 * Callback interface used to process annotations.
 *
 * @param <C> the context type
 * @param <R> the result type
 * @author Phillip Webb
 * @since 5.2
 * @see AnnotationsScanner
 * @see TypeMappedAnnotations
 */
/** 处理注解的回调接口
 * C：上下文类型
 * R：结果类型
 */
@FunctionalInterface
interface AnnotationsProcessor<C, R> {

	/**
	 * Called when an aggregate is about to be processed. This method may return
	 * a {@code non-null} result to short-circuit any further processing.
	 * @param context the context information relevant to the processor
	 * @param aggregateIndex the aggregate index about to be processed
	 * @return a {@code non-null} result if no further processing is required
	 */
  /** 当聚合即将被处理时调用，这个方法可以返回一个非null结果来短路任何未来的处理 */
	@Nullable
	default R doWithAggregate(C context, int aggregateIndex) {
		return null;
	}

	/**
	 * Called when an array of annotations can be processed. This method may
	 * return a {@code non-null} result to short-circuit any further processing.
	 * @param context the context information relevant to the processor
	 * @param aggregateIndex the aggregate index of the provided annotations
	 * @param source the original source of the annotations, if known
	 * @param annotations the annotations to process (this array may contain
	 * {@code null} elements)
	 * @return a {@code non-null} result if no further processing is required
	 */
  /**
   * 当一个注解数组可以被处理时调用，方法可以返回非空结果以短路任何进一步的处理
   */
	@Nullable
	R doWithAnnotations(C context, int aggregateIndex, @Nullable Object source, Annotation[] annotations);

	/**
	 * Get the final result to be returned. By default this method returns
	 * the last process result.
	 * @param result the last early exit result, or {@code null} if none
	 * @return the final result to be returned to the caller
	 */
  /** 得到最终返回结果。默认情况下，此方法返回最后一个过程结果 */
	@Nullable
	default R finish(@Nullable R result) {
		return result;
	}

}
