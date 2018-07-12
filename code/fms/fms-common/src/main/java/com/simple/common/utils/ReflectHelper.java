package com.simple.common.utils;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

/**********************************************************************************
 * Copyright(c)2017 Simple-air.com All rights reserved.
 * @Title: ReflectHelper.java.
 * @Package com.simple.common.utils.
 * @Description: ReflectHelper.
 *
 * @author guowei.
 * @version 1.0.
 * @created 2017/3/1 18:13.
 **********************************************************************************/
public class ReflectHelper {
	private static final Logger logger = Logger.getLogger(ReflectHelper.class);

	public static Field getFieldByFieldName(Object obj, String fieldName) {
		for (Class superClass = obj.getClass(); superClass != Object.class;) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException localNoSuchFieldException) {
				logger.info(localNoSuchFieldException);
				superClass = superClass.getSuperclass();
			}

		}

		return null;
	}

	public static Object getValueByFieldName(Object obj, String fieldName)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field field = getFieldByFieldName(obj, fieldName);
		Object value = null;
		if (field != null) {
			if (field.isAccessible()) {
				value = field.get(obj);
			} else {
				field.setAccessible(true);
				value = field.get(obj);
				field.setAccessible(false);
			}
		}
		return value;
	}

	public static void setValueByFieldName(Object obj, String fieldName,
			Object value) throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field field = obj.getClass().getDeclaredField(fieldName);
		if (field.isAccessible()) {
			field.set(obj, value);
		} else {
			field.setAccessible(true);
			field.set(obj, value);
			field.setAccessible(false);
		}
	}
}
