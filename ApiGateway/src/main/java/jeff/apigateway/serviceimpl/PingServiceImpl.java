package jeff.apigateway.serviceimpl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jeff.apigateway.common.constants.InnerApiConst;
import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.config.MyComponentConfig;
import jeff.apigateway.service.PingService;

@Service
public class PingServiceImpl implements PingService{
	
	/**
	 * @see {@link MyComponentConfig}
	 * */
	@Resource(name="pingRestTemplateBean")
	private RestTemplate restTemplate;
	
	@Autowired
	private SystemParam.Book bookParam;

	@Autowired
	private SystemParam.Book2 book2Param;

	@Autowired
	private SystemParam.AuthN authNParam;
	
	@Autowired
	private SystemParam.AuthZ authZParam;
	

	@Override
	public void pingBookServer() {
		String url = 
				"http://" + bookParam.IP + ":" + bookParam.PORT
				+ InnerApiConst.Send.Path.BOOK_PING;
		restTemplate.getForObject(url, String.class);
	}
	
	@Override
	public void pingBookServer2() {
		String url = 
				"http://" + book2Param.IP + ":" + book2Param.PORT
				+ InnerApiConst.Send.Path.BOOK2_PING;
		restTemplate.getForObject(url, String.class);
	}

	@Override
	public void pingAuthNServer() {
		String url = 
				"http://" + authNParam.IP + ":" + authNParam.PORT
				+ InnerApiConst.Send.Path.AUTHN_PING;
		restTemplate.getForObject(url, String.class);
	}

	@Override
	public void pingAuthZServer() {
		String url = 
				"http://" + authZParam.IP + ":" + authZParam.PORT
				+ InnerApiConst.Send.Path.AUTHZ_PING;
		restTemplate.getForObject(url, String.class);
	}

}
