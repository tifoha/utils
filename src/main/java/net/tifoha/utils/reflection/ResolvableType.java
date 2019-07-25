//package net.tifoha.utils.reflection;
//
///*
// * Copyright 2002-2018 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//import org.springframework.core.SerializableTypeWrapper.FieldTypeProvider;
//import org.springframework.core.SerializableTypeWrapper.MethodParameterTypeProvider;
//import org.springframework.core.SerializableTypeWrapper.TypeProvider;
//import org.springframework.util.*;
//
//import java.io.Serializable;
//import java.lang.reflect.*;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.IdentityHashMap;
//import java.util.Map;
//
//@SuppressWarnings("serial")
//public class ResolvableType implements Serializable {
//    /**
//     * Return the underlying Java {@link Class} being managed, if available;
//     * otherwise {@code null}.
//     */
//
//    public Class<?> getRawClass() {
//        if (this.type == this.resolved) {
//            return this.resolved;
//        }
//        Type rawType = this.type;
//        if (rawType instanceof ParameterizedType) {
//            rawType = ((ParameterizedType) rawType).getRawType();
//        }
//        return (rawType instanceof Class ? (Class<?>) rawType : null);
//    }
//
//    /**
//     * Return a {@link ResolvableType} representing the generic parameter for the given
//     * indexes. Indexes are zero based; for example given the type
//     * {@code Map<Integer, List<String>>}, {@code getGeneric(0)} will access the
//     * {@code Integer}. Nested generics can be accessed by specifying multiple indexes;
//     * for example {@code getGeneric(1, 0)} will access the {@code String} from the nested
//     * {@code List}. For convenience, if no indexes are specified the first generic is
//     * returned.
//     * <p>If no generic is available at the specified indexes {@link #NONE} is returned.
//     *
//     * @param indexes the indexes that refer to the generic parameter (may be omitted to
//     *                return the first generic)
//     * @return a {@link ResolvableType} for the specified generic or {@link #NONE}
//     * @see #hasGenerics()
//     * @see #getGenerics()
//     * @see #resolveGeneric(int...)
//     * @see #resolveGenerics()
//     */
//    public ResolvableType getGeneric(int... indexes) {
//        ResolvableType[] generics = getGenerics();
//        if (indexes == null || indexes.length == 0) {
//            return (generics.length == 0 ? NONE : generics[0]);
//        }
//        ResolvableType generic = this;
//        for (int index : indexes) {
//            generics = generic.getGenerics();
//            if (index < 0 || index >= generics.length) {
//                return NONE;
//            }
//            generic = generics[index];
//        }
//        return generic;
//    }
//
////    /**
////     * Return an array of {@link ResolvableType}s representing the generic parameters of
////     * this type. If no generics are available an empty array is returned. If you need to
////     * access a specific generic consider using the {@link #getGeneric(int...)} method as
////     * it allows access to nested generics and protects against
////     * {@code IndexOutOfBoundsExceptions}.
////     * @return an array of {@link ResolvableType}s representing the generic parameters
////     * (never {@code null})
////     * @see #hasGenerics()
////     * @see #getGeneric(int...)
////     * @see #resolveGeneric(int...)
////     * @see #resolveGenerics()
////     */
////    public ResolvableType[] getGenerics() {
////        if (this == NONE) {
////            return EMPTY_TYPES_ARRAY;
////        }
////        ResolvableType[] generics = this.generics;
////        if (generics == null) {
////            if (this.type instanceof Class) {
////                Class<?> typeClass = (Class<?>) this.type;
////                generics = forTypes(SerializableTypeWrapper.forTypeParameters(typeClass), this.variableResolver);
////            }
////            else if (this.type instanceof ParameterizedType) {
////                Type[] actualTypeArguments = ((ParameterizedType) this.type).getActualTypeArguments();
////                generics = new ResolvableType[actualTypeArguments.length];
////                for (int i = 0; i < actualTypeArguments.length; i++) {
////                    generics[i] = forType(actualTypeArguments[i], this.variableResolver);
////                }
////            }
////            else {
////                generics = resolveType().getGenerics();
////            }
////            this.generics = generics;
////        }
////        return generics;
////    }
//
//
//    /**
//     * Return this type as a {@link ResolvableType} of the specified class. Searches
//     * {@link #getSuperType() supertype} and {@link #getInterfaces() interface}
//     * hierarchies to find a match, returning {@link #NONE} if this type does not
//     * implement or extend the specified class.
//     *
//     * @param type the required type (typically narrowed)
//     * @return a {@link ResolvableType} representing this object as the specified
//     * type, or {@link #NONE} if not resolvable as that type
//     * @see #asCollection()
//     * @see #asMap()
//     * @see #getSuperType()
//     * @see #getInterfaces()
//     */
//    public ResolvableType as(Class<?> type) {
//        if (this == NONE) {
//            return NONE;
//        }
//        if (ObjectUtils.nullSafeEquals(resolve(), type)) {
//            return this;
//        }
//        for (ResolvableType interfaceType : getInterfaces()) {
//            ResolvableType interfaceAsType = interfaceType.as(type);
//            if (interfaceAsType != NONE) {
//                return interfaceAsType;
//            }
//        }
//        return getSuperType().as(type);
//    }
//
//
//    /**
//     * {@code ResolvableType} returned when no value is available. {@code NONE} is used
//     * in preference to {@code null} so that multiple method calls can be safely chained.
//     */
//    public static final ResolvableType NONE = new ResolvableType(EmptyType.INSTANCE, null, null, 0);
//
//    private static final ResolvableType[] EMPTY_TYPES_ARRAY = new ResolvableType[0];
//
//    private static final ConcurrentReferenceHashMap<ResolvableType, ResolvableType> cache =
//            new ConcurrentReferenceHashMap<>(256);
//
//
//    /**
//     * The underlying Java type being managed.
//     */
//    private final Type type;
//
//    /**
//     * Optional provider for the type.
//     */
//
//    private final TypeProvider typeProvider;
//
//    /**
//     * The {@code VariableResolver} to use or {@code null} if no resolver is available.
//     */
//
//    private final VariableResolver variableResolver;
//
//    /**
//     * The component type for an array or {@code null} if the type should be deduced.
//     */
//
//    private final ResolvableType componentType;
//
//
//    private final Integer hash;
//
//
//    private Class<?> resolved;
//
//
//    private volatile ResolvableType superType;
//
//
//    private volatile ResolvableType[] interfaces;
//
//
//    private volatile ResolvableType[] generics;
//
//    /**
//     * Private constructor used to create a new {@link ResolvableType} on a {@link Class} basis.
//     * Avoids all {@code instanceof} checks in order to create a straight {@link Class} wrapper.
//     *
//     * @since 4.2
//     */
//    private ResolvableType(Class<?> clazz) {
//        this.resolved = (clazz != null ? clazz : Object.class);
//        this.type = this.resolved;
//        this.typeProvider = null;
//        this.variableResolver = null;
//        this.componentType = null;
//        this.hash = null;
//    }
//
//    /**
//     * Return a {@link ResolvableType} for the specified {@link Class},
//     * using the full generic type information for assignability checks.
//     * For example: {@code ResolvableType.forClass(MyArrayList.class)}.
//     *
//     * @param clazz the class to introspect ({@code null} is semantically
//     *              equivalent to {@code Object.class} for typical use cases here}
//     * @return a {@link ResolvableType} for the specified class
//     * @see #forClass(Class, Class)
//     * @see #forClassWithGenerics(Class, Class...)
//     */
//    public static ResolvableType forClass(Class<?> clazz) {
//        return new ResolvableType(clazz);
//    }
//
//}
