package com.jjm.chameleon.support.proxy.jpa;

import com.jjm.chameleon.support.proxy.VendorProxyAdapter;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.proxy.HibernateProxy;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;

public class HibernateVendorProxyAdapter implements VendorProxyAdapter {

    private Object value;
    private Class<?> clazz;

    public HibernateVendorProxyAdapter(Object object, Field field) {
        if (object != null) {
            if (object instanceof HibernateProxy) {
                HibernateProxy proxy = (HibernateProxy) object;
                value = proxy.getHibernateLazyInitializer().getImplementation();
                clazz = field.getType();
            } else if (object instanceof PersistentSet) {
                PersistentSet proxy = (PersistentSet) object;
                Object[] array = proxy.toArray();
                if (array.length > 0) {
                    Set<Object> set = new HashSet<>();
                    for (Object arrayObject : array) {
                        set.add(arrayObject);
                    }
                    value = set;
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    clazz = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                }
            }
        }
    }
    @Override
    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
