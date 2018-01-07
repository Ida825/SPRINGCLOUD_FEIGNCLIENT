package cn.et;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//告诉客户端要调哪个微服务
@FeignClient("SENDMAIL")
public interface ISendMail {

	@GetMapping("/user/{userId}")
	public Map getUser(@PathVariable("userId") String userId);
	
	@PostMapping("/send")
	public String send(@RequestBody Map<String,Object> map);
	
}
