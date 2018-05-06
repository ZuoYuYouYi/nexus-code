package org.nexus.servlet;


import org.nexus.entity.Nexus;
import org.nexus.util.BeanUtil;
import org.nexus.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 对数据的处理
 * @author ZuoYu
 *
 */
@SuppressWarnings("serial")
public class Index extends HttpServlet {

	private BeanUtil beanUtil;
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		beanUtil = BeanUtil.getBeanUtil();
		String fistAndLast = request.getParameter("string");
		String resultName = request.getParameter("one");
		String string = request.getParameter("data");
		String updata = request.getParameter("updata");
		Nexus data = null;
		
		if (((fistAndLast != "") && (fistAndLast != null)) && ((resultName != "") && (resultName != null))) {
			if (fistAndLast.contains("的") && fistAndLast.length() >= 5) {
				String[] strings = fistAndLast.split("的");
				data = new Nexus(strings[0], strings[1], resultName);
				Integer integer = beanUtil.insertByBean("tb_nexus", data);
				if (integer >= 0) {
					JsonUtil.jsonHelper("data",true, response);
				} else {
					JsonUtil.jsonHelper("data",false, response);
				}
			}
			return;
		}
		if (((updata != "") && (updata != null)) && ((resultName != "") && (resultName != null))) {
			if (updata.contains("的") && updata.length() >= 5) {
				String[] strings = updata.split("的");
				data = new Nexus(strings[0], strings[1]);
				List<Object> nexus = beanUtil.findSimilar("tb_nexus", data);
				Nexus nexus2 = (Nexus) nexus.get(0);
				data = new Nexus(nexus2.getId(), nexus2.getFirstName(), nexus2.getLastName(), resultName);
				Boolean boo = beanUtil.upData("tb_nexus", data);
				if (boo) {
					JsonUtil.jsonHelper("data",true, response);
				} else {
					JsonUtil.jsonHelper("data",false, response);
				}
			}
			return;
		}
		if (string.length() >= 5  && string.contains("的") && !string.startsWith("的") && !string.endsWith("的")) {
			String[] strings = string.split("的");
			data = new Nexus(strings[0], strings[1]);
			List<Object> es = beanUtil.findSimilar("tb_nexus", data);
			if (es.isEmpty()) {
				data = new Nexus();
			} else {
				data = (Nexus) es.get(0);
			}
			JsonUtil.jsonHelper("data",data, response);
		} else {
			return;
		}
	}

}
