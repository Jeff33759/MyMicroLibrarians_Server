package jeff.apigateway.controller.business;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jeff.apigateway.aop.BusinessControllerExResAspect;
import jeff.apigateway.common.constants.OpenApiConst;
import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.config.MyComponentConfig;
import jeff.apigateway.service.BusinessForwardingService;
import jeff.apigateway.serviceimpl.BusinessForwardingServiceImpl;
import jeff.apigateway.swagger.annotation.MySwaggerForAuthN.MySwaggerForAllAuthNApi;
import jeff.apigateway.swagger.annotation.MySwaggerForAuthN.MySwaggerForLogin;
import jeff.apigateway.swagger.annotation.MySwaggerForAuthN.MySwaggerForRefreshAccessToken;


/**
 * AuthenticationService Server的業務服務API。
 * 
 * @author Jeff Huang
 * @see BusinessControllerExResAspect
 * */
@MySwaggerForAllAuthNApi
@RestController
@RequestMapping(produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {
	
	/**
	 * @see {@link MyComponentConfig}
	 * */
	@Resource(name="businessRestTemplateBean")
	private RestTemplate restTemplate;
	
	@Autowired
	private SystemParam.AuthN authNParam;
	
	private BusinessForwardingService bfService;
	
	@Autowired
	public AuthenticationController(BusinessForwardingServiceImpl bfServiceImpl) {
		this.bfService = bfServiceImpl;
	}

	/**
	 * 使用者登入的API，通過身份驗證後，回傳含有AT、RT與簽名的完整JWTs
	 * */
	@MySwaggerForLogin
	@PostMapping(path = OpenApiConst.Path.AUTHN_LOGIN)
	public ResponseEntity<?> login(HttpServletRequest req) 
			throws Exception {
		String rootUrl = 
				"http://" + authNParam.IP + ":" + authNParam.PORT 
				+ OpenApiConst.Path.AUTHN_LOGIN;
		return bfService.forwardPostReq(req, rootUrl, null);
	}
	
	/**
	 * 刷新Access Token的API。
	 * */
	@MySwaggerForRefreshAccessToken
	@PostMapping(path = OpenApiConst.Path.AUTHN_RF_AT)
	public ResponseEntity<?> refreshAccessToken(HttpServletRequest req) 
			throws Exception {
		String rootUrl = 
				"http://" + authNParam.IP + ":" + authNParam.PORT 
				+ OpenApiConst.Path.AUTHN_RF_AT;
		return bfService.forwardPostReq(req, rootUrl, null);
	}
	

}
