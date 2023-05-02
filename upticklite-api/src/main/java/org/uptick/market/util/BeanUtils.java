// +---------------------------------------------------------------------------- 
// | UptickLite is the community version of Uptick NFT marketpalce, including    
// | three major components - service, api and web, powered by Uptick Network    
// | and Loopring. It is available to the Uptick and Loopring dev community      
// | through the open source license.                                            
// | Uptick Network is a busienss grade NFT infrastructure for NFT applications  
// | with multichian and interchain support. Loopring is a zkRollup layer2 on top
// | of Ethereum.                                                                
// +---------------------------------------------------------------------------- 
// | Copyright (c) 2022~2099 Uptick Network (https://uptick.network/)            
// | All rights reserved.                                                        
// +---------------------------------------------------------------------------- 
// | Licensed ( https://www.gnu.org/licenses/gpl-3.0.en.html )                   
// +---------------------------------------------------------------------------- 
// | Author: Uptick Network development team tech@uptickproject.com              
// +---------------------------------------------------------------------------- 
package org.uptick.market.util;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("rawtypes")
public class BeanUtils {

    public static Object getPrivateProperty(Object object, String propertyName)
            throws IllegalAccessException, NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(propertyName);
        field.setAccessible(true);
        return field.get(object);
    }

    public static void setPrivateProperty(Object object, String propertyName, Object newValue)
            throws IllegalAccessException, NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(propertyName);
        field.setAccessible(true);
        field.set(object, newValue);
    }

    public static Object invokePrivateMethod(Object object, String methodName, Object[] params)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class[] types = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = params[i].getClass();
        }
        Method method = object.getClass().getDeclaredMethod(methodName, types);
        method.setAccessible(true);
        return method.invoke(object, params);
    }


    public static Object invokePrivateMethod(Object object, String methodName, Object param)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokePrivateMethod(object, methodName, new Object[]{param});
    }

    public static Field getDeclaredField(Object object, String propertyName)
            throws NoSuchFieldException {
        return getDeclaredField(object.getClass(), propertyName);
    }


    public static Field getDeclaredField(Class clazz, String propertyName)
            throws NoSuchFieldException {
        for (Class superClass = clazz;
             superClass != Object.class;
             superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {

            }
        }
        throw new NoSuchFieldException("No such field: " + clazz.getName() + '.' + propertyName);
    }


    public static Object forceGetProperty(Object object, String propertyName)
            throws NoSuchFieldException {

        Field field = getDeclaredField(object, propertyName);

        boolean accessible = field.isAccessible();
        field.setAccessible(true);

        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {

        }
        field.setAccessible(accessible);
        return result;
    }


    public static void forceSetProperty(Object object, String propertyName, Object newValue)
            throws NoSuchFieldException {

        Field field = getDeclaredField(object, propertyName);
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(object, newValue);
        } catch (IllegalAccessException e) {

        }
        field.setAccessible(accessible);
    }


    public static Object getDeclaredProperty(Object object, String propertyName)
            throws IllegalAccessException, NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(propertyName);
        return getDeclaredProperty(object, field);
    }


    public static Object getDeclaredProperty(Object object, Field field)
            throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        Object result = field.get(object);
        field.setAccessible(accessible);
        return result;
    }

    public static void setDeclaredProperty(Object object, String propertyName, Object newValue)
            throws IllegalAccessException, NoSuchFieldException {

        Field field = object.getClass().getDeclaredField(propertyName);
        setDeclaredProperty(object, field, newValue);
    }


    public static void setDeclaredProperty(Object object, Field field, Object newValue)
            throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        field.set(object, newValue);
        field.setAccessible(accessible);
    }


    public static List<Field> getFieldsByType(Object object, Class type) {
        ArrayList<Field> list = new ArrayList<Field>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(type)) {
                list.add(field);
            }
        }
        return list;
    }


    public static void setProperty(Object object, String name, Object value)
            throws IllegalAccessException, NoSuchFieldException {
        String[] properties = getProperties(object);
        for (String p : properties) {
            if (p.equalsIgnoreCase(name)) {
                setDeclaredProperty(object, p, value);
                break;
            }
        }
    }


    public static String[] getProperties(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        String[] properties = new String[fields.length];
        int i = 0;
        for (Field field : fields) {
            properties[i++] = field.getName();
        }
        return properties;
    }


    public static Class getPropertyType(Class type, String name) throws NoSuchFieldException {
        return getDeclaredField(type, name).getType();
    }

}
