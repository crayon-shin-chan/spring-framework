/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.instrument.classloading;

import java.lang.instrument.ClassFileTransformer;

/**
 * Defines the contract for adding one or more
 * {@link ClassFileTransformer ClassFileTransformers} to a {@link ClassLoader}.
 *
 * <p>Implementations may operate on the current context {@code ClassLoader}
 * or expose their own instrumentable {@code ClassLoader}.
 *
 * @author Rod Johnson
 * @author Costin Leau
 * @since 2.0
 * @see java.lang.instrument.ClassFileTransformer
 */

/**
 * 加载时织入
 * 定义将一个或多个{@link ClassFileTransformer}添加到{@link ClassLoader}的合同。
 * 实现可以在当前上下文{@code ClassLoader}上操作或公开其自己的可检测{@code ClassLoader}。
 * @see java.lang.instrument.ClassFileTransformer
 */
public interface LoadTimeWeaver {

	/**
	 * Add a {@code ClassFileTransformer} to be applied by this
	 * {@code LoadTimeWeaver}.
	 * @param transformer the {@code ClassFileTransformer} to add
	 */
	/**
	 * 添加一个{@code ClassFileTransformer}，以供此{@code LoadTimeWeaver}应用。
	 * @param transformer {@code ClassFileTransformer}要添加
	 * @param transformer
	 */
	void addTransformer(ClassFileTransformer transformer);

	/**
	 * Return a {@code ClassLoader} that supports instrumentation
	 * through AspectJ-style load-time weaving based on user-defined
	 * {@link ClassFileTransformer ClassFileTransformers}.
	 * <p>May be the current {@code ClassLoader}, or a {@code ClassLoader}
	 * created by this {@link LoadTimeWeaver} instance.
	 * @return the {@code ClassLoader} which will expose
	 * instrumented classes according to the registered transformers
	 */
	/**
	 * 返回一个基于用户定义的{@link ClassFileTransformer}通过AspectJ样式的加载时编织来支持仪表的{@code ClassLoader}。
	 * 可能是当前的{@code ClassLoader}或由此{@link LoadTimeWeaver}实例创建的{@code ClassLoader}。
	 * @return {@code ClassLoader}，它将根据注册的转换器公开检测类
	 */
	ClassLoader getInstrumentableClassLoader();

	/**
	 * Return a throwaway {@code ClassLoader}, enabling classes to be
	 * loaded and inspected without affecting the parent {@code ClassLoader}.
	 * <p>Should <i>not</i> return the same instance of the {@link ClassLoader}
	 * returned from an invocation of {@link #getInstrumentableClassLoader()}.
	 * @return a temporary throwaway {@code ClassLoader}; should return
	 * a new instance for each call, with no existing state
	 */
	/**
	 * 返回一个废弃的{@code ClassLoader}，使类可以被加载和检查，而不会影响父类{@code ClassLoader}。
	 * 应该不返回与{@link #getInstrumentableClassLoader（）}调用相同的{@link ClassLoader}实例。
	 * @return 临时扔掉的{@code ClassLoader}；应该为每个调用返回一个新实例，没有现有状态\
	 */
	ClassLoader getThrowawayClassLoader();

}
