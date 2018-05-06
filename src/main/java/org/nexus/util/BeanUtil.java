package org.nexus.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * JavaBean与JDBC间的转化
 * @author ZuoYu
 *
 */
public class BeanUtil {

	private JdbcUtil jdbcUtil;
	
	private BeanUtil() {
		jdbcUtil = JdbcUtil.getJdbcUtil();
	}
	
	/**
	 * 获取对象
	 * @return BeanUtil
	 */
	public static BeanUtil getBeanUtil() {
		return new BeanUtil();
	}
	
	/**
	 * 插入
	 * @param object
	 * @return Integer
	 */
	public Integer insertByBean(String table_name, Object object) {
		StringBuffer stringBuffer = new StringBuffer("insert into " + table_name + "(");
		List<String> objects = new ArrayList<String>();
		Class<?> objectClass = object.getClass();
		Field[] fields = objectClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().endsWith("id")) {
				continue;
			}
			stringBuffer.append(field.getName() + ",");
			field.setAccessible(true);
			try {
				objects.add(field.get(object).toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		String[] args = new String[objects.size()];
		objects.toArray(args);
		String sql = stringBuffer.substring(0, stringBuffer.length() - 1);
		stringBuffer = new StringBuffer(sql);
		stringBuffer.append(") values(");
		for (int i = 0; i < fields.length - 1; i++) {
			stringBuffer.append("?,");
		}
		sql = stringBuffer.substring(0, stringBuffer.length() - 1);
		sql += ");";
		return jdbcUtil.insert(sql, args);
	}
	
	/**
	 * 根据id修改
	 * @param table_name
	 * @param object
	 * @return Boolean
	 */
	public Boolean upData(String table_name, Object object) {
		StringBuffer stringBuffer = new StringBuffer("update " + table_name + " set ");
		List<String> objects = new ArrayList<String>();
		Class<?> objectClass = object.getClass();
		Field[] fields = objectClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().endsWith("id")) {
				continue;
			}
			stringBuffer.append(field.getName() + " = ?,");
			field.setAccessible(true);
			try {
				objects.add(field.get(object).toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		String sql = stringBuffer.substring(0, stringBuffer.length() - 1);
		fields[0].setAccessible(true);
		sql += "where " + fields[0].getName() + "= ?;";
		try {
			objects.add(fields[0].get(object).toString());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		String[] args = new String[objects.size()];
		objects.toArray(args);
		return jdbcUtil.upDateOrDelete(sql, args);
	}
	
	/**
	 * 删除
	 * @param table_name
	 * @param object
	 * @return Boolean
	 */
	public Boolean delete(String table_name, Object object) {
		Class<?> objectClass = object.getClass();
		Field[] fields = objectClass.getDeclaredFields();
		fields[0].setAccessible(true);
		String sql = null;
		try {
			sql = "delete from " + table_name + " where " + fields[0].getName()
					+ "=" + fields[0].get(object).toString();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return jdbcUtil.upDateOrDelete(sql, null);
	}
	
	/**
	 * 查询全部
	 * @param table_name
	 * @return List<Object>
	 */
	public List<Object> query(String table_name, Class<?> oClass) {
		String sql = "select * from " + table_name;
		List<Map<String, Object>> reluts = jdbcUtil.query(sql, null);
		List<Object> objects = new ArrayList<Object>();
		Field[] fields = oClass.getDeclaredFields();
		for (Map<String, Object> map : reluts) {
			try {
				Object object = mapToBean(map, fields, oClass);
				objects.add(object);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return objects;
	}
	
	/**
	 * 通过id查找对应的数据
	 * @param table_name
	 * @param id
	 * @param oClass
	 * @return
	 */
	public Object findById(String table_name, Integer id, Class<?> oClass) {
		String sql = "select * from " + table_name + " where ";
		try {
			Field[] fields = oClass.getDeclaredFields();
			List<Field> fieldss = Arrays.asList(fields);
			for (Field field : fieldss) {
				if (field.getName().endsWith("id")) {
					sql += field.getName() + " = " + id + ";";
				}
			}
			List<Map<String, Object>> results = jdbcUtil.query(sql, null);
			Map<String, Object> result = results.get(0);
			Object object = mapToBean(result, fields, oClass);
			return object;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查找相似的数据
	 * （传入的JavaBean模型仅加入需要查找的条件属性即可，
	 * 不需要的属性设为null）
	 * @param table_name
	 * @param object
	 * @return
	 */
	public List<Object> findSimilar(String table_name, Object object) {
		Class<?> objectClass = object.getClass();
		StringBuffer stringBuffer = new StringBuffer("select * from " + table_name + " where ");
		try {
			Field[] fields = objectClass.getDeclaredFields();
			List<String> list = new ArrayList<String>();
			List<Object> objects = new ArrayList<Object>();
			for (Field field : fields) {
				field.setAccessible(true);
				String paramName = field.getName();
					Object paramValue = field.get(object);
					if (paramValue != null && !paramValue.equals(null)) {
						stringBuffer.append(paramName + "= ? and ");
						list.add(paramValue.toString());
					}
			}
			String sql = stringBuffer.substring(0, stringBuffer.length() - 4);
			String[] args = new String[list.size()];
			list.toArray(args);
			List<Map<String, Object>> maps = jdbcUtil.query(sql, args);
			for (Map<String, Object> map : maps) {
				Object obj = mapToBean(map, fields, objectClass);
				objects.add(obj);
			}
			return objects;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 首写字母转大写
	 * @param string
	 * @return String
	 */
	private String fistLetterToUpperCase(String string) {
		byte[] bs = string.getBytes();
		bs[0] = (byte) Character.toUpperCase(string.charAt(0));
		return new String(bs);
	}
	
	/**
	 * 通过反射机制将map转换为JavaBean
	 * @param map
	 * @param fields
	 * @param oClass
	 * @return Object
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws NumberFormatException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private Object mapToBean(Map<String, Object> map, Field[] fields, Class<?> oClass) 
			throws InstantiationException, IllegalAccessException, NoSuchMethodException,
			SecurityException, NumberFormatException, IllegalArgumentException, 
			InvocationTargetException {
		Object object = oClass.newInstance();
		for (Field field : fields) {
			field.setAccessible(true);
			String name = field.getName();
			if (map.containsKey(name)) {
				Class<?> paramType = field.getType();
				String methodName = fistLetterToUpperCase(name);
				Method method = oClass.getMethod("set" + methodName, paramType);
				if (paramType == Integer.class) {
					method.invoke(object, new Integer(map.get(name).toString()));
				}
				if (paramType == String.class) {
					method.invoke(object, (String)map.get(name));
				}
				if (paramType == Double.class) {
					method.invoke(object, new Double(map.get(name).toString()));
				}
				if (paramType == Float.class) {
					method.invoke(object, new Float(map.get(name).toString()));
				}
				if (paramType == Long.class) {
					method.invoke(object, new Long(map.get(name).toString()));
				}
				if (paramType == Short.class) {
					method.invoke(object, new Short(map.get(name).toString()));
				}
				if (paramType == Byte.class) {
					method.invoke(object, new Byte(map.get(name).toString()));
				}
				if (paramType == Boolean.class) {
					String string = map.get(name).toString();
					if (string.equals("1")) {
						method.invoke(object, new Boolean(true));
					} else if (string.equals("0")) {
						method.invoke(object, new Boolean(false));
					} else {
						method.invoke(object, new Boolean(map.get(name).toString()));
					}
				}
				if (paramType == Date.class) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					ParsePosition pos = new ParsePosition(0);
					Date date = simpleDateFormat.parse(map.get(name).toString(), pos);
					method.invoke(object, date);
				}
			}
		}
		return object;
	}
}
