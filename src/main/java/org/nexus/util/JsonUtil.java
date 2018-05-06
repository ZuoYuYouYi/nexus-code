package org.nexus.util;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * JSON工具包
 * @author ZuoYu
 *
 */
public class JsonUtil {

	private JsonUtil () {
		
	}
	
	/**
	 * 将对象转换为JSON格式并输出至客户端
	 * @param object
	 * @param response
	 */
	public static void jsonHelper(String resultName, Object object, HttpServletResponse response) {
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		JSONObject jsonObject = new JSONObject();
		PrintWriter printWriter = null;
		try {
			printWriter = response.getWriter();
			jsonObject.put(resultName, JSONObject.fromObject(object));
			printWriter.print(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			printWriter.flush();
			printWriter.close();
		}

	}
}
