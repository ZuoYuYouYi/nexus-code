import org.junit.Test;
import org.nexus.entity.Nexus;
import org.nexus.util.BeanUtil;

import java.util.List;


public class TestDemo {


	
	@Test
	public void nexus() {
		BeanUtil beanUtil = BeanUtil.getBeanUtil();
		List<Object> nexusList = beanUtil.findSimilar("tb_nexus",new Nexus("爸爸","爸爸"));
		Nexus nexu = (Nexus) nexusList.get(0);
		System.out.println(nexu);
	}
}
