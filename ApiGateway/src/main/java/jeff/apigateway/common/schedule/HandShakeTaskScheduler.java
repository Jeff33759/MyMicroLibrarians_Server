package jeff.apigateway.common.schedule;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.exception.RestTemplateErrorResException;
import jeff.apigateway.service.HeartBeatService;
import jeff.apigateway.service.PingService;
import jeff.apigateway.serviceimpl.HeartBeatServiceImpl;
import jeff.apigateway.serviceimpl.PingServiceImpl;

/**
 * 統一控管交握相關的定期任務，
 * 將任務的定期執行相關設定從各Service中分離開來。</p>
 * 
 * @author Jeff Huang
 * */
@Component
public class HandShakeTaskScheduler{
	
	@Autowired
	private SystemParam.Book bookParam;

	@Autowired
	private SystemParam.Book2 book2Param;
	
	@Autowired
	private SystemParam.AuthN authNParam;
	
	@Autowired
	private SystemParam.AuthZ authZParam;

	private final HeartBeatService heartBeatService;
	
	private final PingService pingService;

	@Autowired
	public HandShakeTaskScheduler(HeartBeatServiceImpl heartBeatServiceImpl,
			PingServiceImpl pingServiceImpl) {
		this.heartBeatService = heartBeatServiceImpl;
		this.pingService = pingServiceImpl;
	}

	
	@Scheduled(timeUnit = TimeUnit.SECONDS, initialDelay=10, fixedRate = 10)
	public void taskOfMonitorAuthNServerHeartBeat() {
		if(authNParam.IN_SERVICE) {
			heartBeatService.monitorAuthNServerHeartBeat();
		}else {
			try {
				pingService.pingAuthNServer();
			}catch(ResourceAccessException | RestTemplateErrorResException e){
//				ignore
			}
		}
	}

	@Scheduled(timeUnit = TimeUnit.SECONDS, initialDelay=11, fixedRate = 10)
	public void taskOfMonitorAuthZServerHeartBeat() {
		if(authZParam.IN_SERVICE) {
			heartBeatService.monitorAuthZServerHeartBeat();
		}else {
			try {
				pingService.pingAuthZServer();
			}catch(ResourceAccessException | RestTemplateErrorResException e){
//				ignore
			}
		}
	}
	
	@Scheduled(timeUnit = TimeUnit.SECONDS, initialDelay=12, fixedRate = 10)
	public void taskOfMonitorBookServerHeartBeat() {
		if(bookParam.IN_SERVICE) {
			heartBeatService.monitorBookServerHeartBeat();
		}else {
			try {
				pingService.pingBookServer();
			}catch(ResourceAccessException | RestTemplateErrorResException e){
//				ignore
			}
		}
	}

	@Scheduled(timeUnit = TimeUnit.SECONDS, initialDelay=13, fixedRate = 10)
	public void taskOfMonitorBookServer2HeartBeat() {
		if(book2Param.IN_SERVICE) {
			heartBeatService.monitorBookServer2HeartBeat();
		}else {
			try {
				pingService.pingBookServer2();
			}catch(ResourceAccessException | RestTemplateErrorResException e){
//				ignore
			}
		}
	}

}
