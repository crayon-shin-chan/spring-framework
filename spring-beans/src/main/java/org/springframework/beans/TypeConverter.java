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

package org.springframework.beans;

import java.lang.reflect.Field;

import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

/**
 * Interface that defines type conversion methods. Typically (but not necessarily)
 * implemented in conjunction with the {@link PropertyEditorRegistry} interface.
 *
 * <p><b>Note:</b> Since TypeConverter implementations are typically based on
 * {@link java.beans.PropertyEditor PropertyEditors} which aren't thread-safe,
 * TypeConverters themselves are <em>not</em> to be considered as thread-safe either.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see SimpleTypeConverter
 * @see BeanWrapperImpl
 */

/**
 * 定义类型转换方法的接口。
 * 通常（但不是必须）与{@link PropertyEditorRegistry}接口一起实现。
 * 注意：由于TypeConverter实现通常基于{@link java.beans.PropertyEditor}，它们不是线程安全的，因此TypeConverters本身不是被视为线程安全的。
 * @see SimpleTypeConverter
 * @see BeanWrapperImpl * /
 */
public interface TypeConverter {

	/**
	 * Convert the value to the required type (if necessary from a String).
	 * <p>Conversions from String to any type will typically use the {@code setAsText}
	 * method of the PropertyEditor class, or a Spring Converter in a ConversionService.
	 * @param value the value to convert
	 * @param requiredType the type we must convert to
	 * (or {@code null} if not known, for example in case of a collection element)
	 * @return the new value, possibly the result of type conversion
	 * @throws TypeMismatchException if type conversion failed
	 * @see java.beans.PropertyEditor#setAsText(String)
	 * @see java.beans.PropertyEditor#getValue()
	 * @see org.springframework.core.convert.ConversionService
	 * @see org.springframework.core.convert.converter.Converter
	 */
	/**
	 * 将值转换为所需的类型（如果需要，则从字符串）。
	 * 从String到任何类型的转换通常将使用PropertyEditor类的{@code setAsText}方法或ConversionService中的Spring Converter。
	 * @param value 要转换的值
	 * @param requiredType 我们必须转换为的类型（或者，如果未知，则为{@code null}，例如在一个collection元素的情况下）
	 * @return 新值，可能是类型转换
	 * @throws TypeMismatchException 如果类型转换失败，则抛出TypeMismatchException
	 * @see java.beans.PropertyEditor＃setAsText（String）
	 * @see java.beans.PropertyEditor＃getValue（）
	 * @see org.springframework.core.convert.ConversionService
	 * @see org.springframework.core.convert.converter.Converter
	 */
	@Nullable
	<T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType) throws TypeMismatchException;

	/**
	 * Convert the value to the required type (if necessary from a String).
	 * <p>Conversions from String to any type will typically use the {@code setAsText}
	 * method of the PropertyEditor class, or a Spring Converter in a ConversionService.
	 * @param value the value to convert
	 * @param requiredType the type we must convert to
	 * (or {@code null} if not known, for example in case of a collection element)
	 * @param methodParam the method parameter that is the target of the conversion
	 * (for analysis of generic types; may be {@code null})
	 * @return the new value, possibly the result of type conversion
	 * @throws TypeMismatchException if type conversion failed
	 * @see java.beans.PropertyEditor#setAsText(String)
	 * @see java.beans.PropertyEditor#getValue()
	 * @see org.springframework.core.convert.ConversionService
	 * @see org.springframework.core.convert.converter.Converter
	 */
	/**
	 * 将值转换为所需的类型（如果需要，则从字符串）。
	 * 从String到任何类型的转换通常将使用PropertyEditor类的{@code setAsText}方法或ConversionService中的Spring Converter。
	 * @param value 要转换的值
	 * @param requiredType 我们必须转换为的类型（如果未知，例如，如果是集合元素，则为{@code null}）
	 * @param methodParam 作为目标的方法参数转换的时间（用于分析通用类型；可能为{@code null}）
	 * @return 新值，可能是类型转换的结果
	 * @throws TypeMismatchException 如果类型转换失败，则抛出TypeMismatchException
	 * @see java.beans.PropertyEditor＃setAsText （字符串）
	 * @see java.beans.PropertyEditor＃getValue（）
	 * @see org.springframework.core.convert.ConversionService
	 * @see org.springframework.core.convert.converter.Converter
	 */
	@Nullable
	<T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType,
			@Nullable MethodParameter methodParam) throws TypeMismatchException;

	/**
	 * Convert the value to the required type (if necessary from a String).
	 * <p>Conversions from String to any type will typically use the {@code setAsText}
	 * method of the PropertyEditor class, or a Spring Converter in a ConversionService.
	 * @param value the value to convert
	 * @param requiredType the type we must convert to
	 * (or {@code null} if not known, for example in case of a collection element)
	 * @param field the reflective field that is the target of the conversion
	 * (for analysis of generic types; may be {@code null})
	 * @return the new value, possibly the result of type conversion
	 * @throws TypeMismatchException if type conversion failed
	 * @see java.beans.PropertyEditor#setAsText(String)
	 * @see java.beans.PropertyEditor#getValue()
	 * @see org.springframework.core.convert.ConversionService
	 * @see org.springframework.core.convert.converter.Converter
	 */
	/**
	 * 将值转换为所需的类型（如果需要，则从字符串）。
	 * 从String到任何类型的转换通常将使用PropertyEditor类的{@code setAsText}方法或ConversionService中的Spring Converter。
	 * @param value 要转换的值
	 * @param requiredType 我们必须转换为的类型（如果未知，则为{@code null}，例如在收集元素的情况下
	 * @param field 作为目标的反射场的转换（用于分析通用类型；可能为{@code null}）
	 * @return 新值，可能是类型转换的结果*
	 * @throws TypeMismatchException 如果类型转换失败，则抛出TypeMismatchException
	 * @see java.beans.PropertyEditor＃setAsText （字符串）
	 * @see java.beans.PropertyEditor＃getValue（）
	 * @see org.springframework.core.convert.ConversionService
	 * @see org.springframework.core.convert.converter.Converter
	 */
	@Nullable
	<T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable Field field)
			throws TypeMismatchException;

	/**
	 * Convert the value to the required type (if necessary from a String).
	 * <p>Conversions from String to any type will typically use the {@code setAsText}
	 * method of the PropertyEditor class, or a Spring Converter in a ConversionService.
	 * @param value the value to convert
	 * @param requiredType the type we must convert to
	 * (or {@code null} if not known, for example in case of a collection element)
	 * @param typeDescriptor the type descriptor to use (may be {@code null}))
	 * @return the new value, possibly the result of type conversion
	 * @throws TypeMismatchException if type conversion failed
	 * @since 5.1.4
	 * @see java.beans.PropertyEditor#setAsText(String)
	 * @see java.beans.PropertyEditor#getValue()
	 * @see org.springframework.core.convert.ConversionService
	 * @see org.springframework.core.convert.converter.Converter
	 */
	/**
	 * 将值转换为所需的类型（如果需要，则从字符串）。
	 * 从String到任何类型的转换通常将使用PropertyEditor类的{@code setAsText}方法或ConversionService中的Spring Converter。
	 * @param value 要转换的值
	 * @param requiredType 我们必须转换为的类型（或者，如果未知，则为{@code null}，例如在收集元素的情况下）
	 * @param typeDescriptor 或要使用的类型描述符是{@code null}））
	 * @return 新值，可能是类型转换的结果*
	 * @throws TypeMismatchException 如果类型转换失败，则抛出TypeMismatchException
	 * @see java.beans.PropertyEditor＃setAsText（String）
	 * @see java.beans.PropertyEditor＃getValue（）
	 * @see org.springframework.core.convert.ConversionService
	 * @see org.springframework.core.convert.converter.Converter
	 */
	@Nullable
	default <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType,
			@Nullable TypeDescriptor typeDescriptor) throws TypeMismatchException {

		throw new UnsupportedOperationException("TypeDescriptor resolution not supported");
	}

}
