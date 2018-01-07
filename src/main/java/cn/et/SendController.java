package cn.et;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Controller
public class SendController {
	
	@Autowired
	ISendMail sendMail;
	
	/**
	 * 启动多个发布者 端口不一致  程序名相同
	 * 使用必须添加注解 @LoadBalanced  启动负载均衡
	 * 在配置文件中配置NDMAIL.ribbon.NFLoadBalancerRuleClassName=com.netflix.loadbalancer.RandomRule，
	 * 选择负载均衡的方法
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/choosePub")
	//验证负载均衡
	public String choosePub(){
		/*StringBuffer sb = new StringBuffer();
		for(int i=1;i<=10;i++){
			ServiceInstance ss = lbc.choose("SENDMAIL");//根据注册的名字选择一个服务   这里涉及到选择算法
			sb.append(ss.getUri().toString()+"<br/>");
		}
		return sb.toString();*/
		return null;
	}
	
	/**
	 * 调用sendmail的 /user/ 请求
	 * @param id
	 * @return
	 */
	@ResponseBody
	@GetMapping("/invokeUser")
	public String invokeUser(String id){
		Map result = sendMail.getUser(id);
		//template.getForObject("http://SENDMAIL/user/{id}",String.class,id);
		return result.get("name").toString();
	}
	
	
	@GetMapping("/sendClient")
	public String sendClient(String sendTo,String sendSubject,String sendContent){
		//调用sendMail服务
		//String controller = "/send";
		//通过注册中心客户端负载均衡  获取一台主机来调用
		try {
			//controller+="?sendTo="+sendTo+"&sendSubject="+sendSubject+"&sendContent="+sendContent;
			
			//通过在eureka server注册的 应用名称 直接来访问  
			//String result = template.getForObject("http://SENDMAIL"+controller,String.class);
			/*HttpHeaders header = new HttpHeaders(); 
			header.setContentType(MediaType.APPLICATION_JSON);
			header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));*/
			
			
			Map<String,Object> map = new HashMap();
			map.put("sendTo", sendTo);
			map.put("sendSubject", sendSubject);
			map.put("sendContent", sendContent);
			sendMail.send(map);
			//HttpEntity<Map> request = new HttpEntity<Map>(map,header);
			//template.postForObject("http://SENDMAIL/send", request, String.class);
		} catch (RestClientException e) {
			e.printStackTrace();
			return "redirect:/error.html";
		}
		
		return "redirect:/suc.html";
	}
	
}
