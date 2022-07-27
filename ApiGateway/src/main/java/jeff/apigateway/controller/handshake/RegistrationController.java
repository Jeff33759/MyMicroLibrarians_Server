package jeff.apigateway.controller.handshake;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import jeff.apigateway.common.constants.InnerApiConst;
import jeff.apigateway.model.dto.send.RegistrationDataForAuthN;
import jeff.apigateway.model.dto.send.RegistrationDataForAuthZ;
import jeff.apigateway.service.RegistrationService;
import jeff.apigateway.serviceimpl.RegistrationServiceImpl;

/**
 * 其他微服務啟動時，向本gateway註冊用的控制器。</p>
 * 該回傳key的回傳key給各服務端，
 * 不用回傳東西的，就簡單回傳個1，減少溝通成本。
 * */
@RestController
@RequestMapping(produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Hidden
public class RegistrationController {
	
	private final RegistrationService registrationService;
	
	@Autowired
	public RegistrationController(RegistrationServiceImpl registrationServiceImpl) {
		this.registrationService = registrationServiceImpl;
	}
	
	
	/**
	 * BookService註冊。
	 * */
	@GetMapping(path = InnerApiConst.Receive.Path.REGISTER_BOOK)
	public ResponseEntity<?> registerBookService(){
		registrationService.registerBookServer();
		return ResponseEntity.ok(1);
	}
	
	/**
	 * BookService2註冊。
	 * */
	@GetMapping(path = InnerApiConst.Receive.Path.REGISTER_BOOK2)
	public ResponseEntity<?> registerBookService2(){
		registrationService.registerBookServer2();
		return ResponseEntity.ok(1);
	}
	

	/**
	 * AuthenticationService註冊。</p>
	 * */
	@GetMapping(path = InnerApiConst.Receive.Path.REGISTER_AUTHN)
	public ResponseEntity<?> registerAuthNService(){
		RegistrationDataForAuthN res = 
				registrationService.registerAuthNServer();
		return ResponseEntity.ok(res);
	}
	
	/**
	 * AuthorizationService註冊。
	 * */
	@GetMapping(path = InnerApiConst.Receive.Path.REGISTER_AUTHZ)
	public ResponseEntity<RegistrationDataForAuthZ> registerAuthZService(){
		RegistrationDataForAuthZ res = 
				registrationService.registerAuthZServer();
		return ResponseEntity.ok(res);
	}
	
	
}
