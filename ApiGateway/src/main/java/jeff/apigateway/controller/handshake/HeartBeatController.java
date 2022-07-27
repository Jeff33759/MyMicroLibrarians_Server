package jeff.apigateway.controller.handshake;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import jeff.apigateway.common.constants.InnerApiConst;
import jeff.apigateway.service.HeartBeatService;
import jeff.apigateway.serviceimpl.HeartBeatServiceImpl;

/**
 * 接收各微服務傳來的心跳訊號。
 * 因為心跳收發比較頻繁，回應盡量簡單，減少溝通成本。
 * 
 * @author Jeff Huang
 * */
@RestController
@RequestMapping(produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Hidden
public class HeartBeatController {
	
	private final HeartBeatService heartBeatService;
	
	@Autowired
	public HeartBeatController(HeartBeatServiceImpl heartBeatServiceImpl) {
		this.heartBeatService = heartBeatServiceImpl;
	}

	
	/**
	 * BookService心跳訊號。
	 * */
	@GetMapping(path = InnerApiConst.Receive.Path.HEARTBEAT_BOOK)
	public ResponseEntity<?> MonitorBookServerHeartBeat(){
		heartBeatService.bookServerHeartBeat();
		return ResponseEntity.ok(1);
	}
	
	/**
	 * BookService2心跳訊號。
	 * */
	@GetMapping(path = InnerApiConst.Receive.Path.HEARTBEAT_BOOK2)
	public ResponseEntity<?> MonitorBookServer2HeartBeat(){
		heartBeatService.bookServer2HeartBeat();
		return ResponseEntity.ok(1);
	}
	
	/**
	 * AuthenticationService心跳訊號。
	 * */
	@GetMapping(path = InnerApiConst.Receive.Path.HEARTBEAT_AUTHN)
	public ResponseEntity<?> registerAuthNService(){
		heartBeatService.authNServerHeartBeat();
		return ResponseEntity.ok(1);
	}
	
	/**
	 * AuthorizationService心跳訊號。
	 * */
	@GetMapping(path = InnerApiConst.Receive.Path.HEARTBEAT_AUTHZ)
	public ResponseEntity<?> registerAuthZService(){
		heartBeatService.authZServerHeartBeat();
		return ResponseEntity.ok(1);
	}
	
}
