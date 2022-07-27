package jeff.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jeff.authentication.common.constants.InnerApiConst;
import jeff.authentication.common.param.SystemParam;
import jeff.authentication.service.HandshakeService;
import jeff.authentication.serviceimpl.HandshakeServiceImpl;

/**
 * 與Gateway交握的API。
 * 
 * @author Jeff Huang
 * */
@RestController
@RequestMapping(produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
public class HandshakeController {
	
	private final HandshakeService hsService;
	
	@Autowired
	private SystemParam param;
	
	public HandshakeController(HandshakeServiceImpl hsServiceImpl) {
		this.hsService = hsServiceImpl;
	}

	/**
	 * 當收到gateway的ping，代表gateway判斷此服務端還沒註冊，
	 * 那就初始化此服務端(以免雙方資訊不對等)，然後再跑一次註冊流程。
	 * */
	@GetMapping(path = InnerApiConst.Receive.Path.PING)
	public ResponseEntity<?> pinged() {
		param.initSystemParam();
		hsService.registerWithTheGateway();
		return ResponseEntity.ok(1);
	}

}
