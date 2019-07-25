//package net.tifoha.utils.reflection;
//
//import org.springframework.core.ResolvableType;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.core.io.support.ResourcePatternResolver;
//import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
//import org.springframework.core.type.classreading.MetadataReader;
//import org.springframework.core.type.classreading.MetadataReaderFactory;
//import org.springframework.util.ClassUtils;
//import org.springframework.util.SystemPropertyUtils;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.lang.reflect.TypeVariable;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ReflectionUtils {
//    public static Class<?> determineType(Field field, Class<?> type) {
//        return (Class<?>) getType(type, field).type;
//    }
//
//    private static TypeInfo getType(Class<?> clazz, Field field) {
//        TypeInfo type = new TypeInfo(null, null);
//        if (field.getGenericType() instanceof TypeVariable<?>) {
//            TypeVariable<?> genericTyp = (TypeVariable<?>) field.getGenericType();
//            Class<?> superClazz = clazz.getSuperclass();
//
//            if (clazz.getGenericSuperclass() instanceof ParameterizedType) {
//                ParameterizedType paramType = (ParameterizedType) clazz.getGenericSuperclass();
//                TypeVariable<?>[] superTypeParameters = superClazz.getTypeParameters();
//                if (!Object.class.equals(paramType)) {
//                    if (field.getDeclaringClass().equals(superClazz)) {
//                        // this is the root class an starting point for this search
//                        type.name = genericTyp;
//                        type.type = null;
//                    } else {
//                        type = getType(superClazz, field);
//                    }
//                }
//                if (type.type == null || type.type instanceof TypeVariable<?>) {
//                    // lookup if type is not found or type needs a lookup in current concrete class
//                    for (int j = 0; j < superClazz.getTypeParameters().length; ++j) {
//                        TypeVariable<?> superTypeParam = superTypeParameters[j];
//                        if (type.name.equals(superTypeParam)) {
//                            type.type = paramType.getActualTypeArguments()[j];
//                            Type[] typeParameters = clazz.getTypeParameters();
//                            if (typeParameters.length > 0) {
//                                for (Type typeParam : typeParameters) {
//                                    TypeVariable<?> objectOfComparison = superTypeParam;
//                                    if (type.type instanceof TypeVariable<?>) {
//                                        objectOfComparison = (TypeVariable<?>) type.type;
//                                    }
//                                    if (objectOfComparison.getName().equals(((TypeVariable<?>) typeParam).getName())) {
//                                        type.name = typeParam;
//                                        break;
//                                    }
//                                }
//                            }
//                            break;
//                        }
//                    }
//                }
//            }
//        } else {
//            type.type = field.getGenericType();
//        }
//
//        return type;
//    }
//
//    public static List<Class> findClasses(String basePackage) throws IOException, ClassNotFoundException {
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
//
//        List<Class> candidates = new ArrayList<Class>();
//        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
//                resolveBasePackage(basePackage) + "/" + "**/*.class";
//        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
//        for (Resource resource : resources) {
//            if (resource.isReadable()) {
//                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
//                if (isCandidate(metadataReader)) {
//                    candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
//                }
//            }
//        }
//        return candidates;
//    }
//
//    private static String resolveBasePackage(String basePackage) {
//        return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
//    }
//
//    private static boolean isCandidate(MetadataReader metadataReader) throws ClassNotFoundException {
////        try {
////            Class c = Class.forName(metadataReader.getClassMetadata().getClassName());
////            if (c.getAnnotation(XmlRootElement.class) != null) {
////                return true;
////            }
////        } catch (Throwable e) {
////        }
////        return false;
//        return true;
//    }
//
//    private static class TypeInfo {
//        private Type type;
//        private Type name;
//
//        public TypeInfo(Type type, Type name) {
//            this.type = type;
//            this.name = name;
//        }
//
//        public Type getType() {
//            return type;
//        }
//
//        public void setType(Type type) {
//            this.type = type;
//        }
//
//        public Type getName() {
//            return name;
//        }
//
//        public void setName(Type name) {
//            this.name = name;
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <E, S> Class<E> getEntityClass(Class<S> serviceClass, Class<? super S> superClass) {
//        return (Class<E>) ResolvableType
//                .forClass(serviceClass)
//                .as(superClass)
//                .getGeneric(0)
//                .getRawClass();
//    }
//}