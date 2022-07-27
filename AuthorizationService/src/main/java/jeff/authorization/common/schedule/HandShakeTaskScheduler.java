package jeff.authorization.common.schedule;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import jeff.authorization.common.param.SystemParam;
import jeff.authorization.exception.RestTemplateErrorResException;
import jeff.authorization.service.HandshakeService;
import jeff.authorization.serviceimpl.HandshakeServiceImpl;

/**
 * 統一控管定期任務，將任務的定期執行相關邏輯從各Service中分離開來。
 * */
@Component
public class HandShakeTaskScheduler {
	
	private final HandshakeService hsService;
	
	@Autowired
	private SystemParam param;
	
	@Autowired
	public HandShakeTaskScheduler(HandshakeServiceImpl hsServiceImpl) {
		this.hsService = hsServiceImpl;
	}


	@Scheduled(timeUnit = TimeUnit.SECONDS,initialDelay=5 , fixedRate = 5)
	public void taskOfSendHeartbeatToGateway() {
//		若已經是註冊狀態，就正常發送心跳請求
		if(param.HAS_REGISTERED) {
			try {
				hsService.sendHeartbeatToGateway();
//			連接超時、等待回應超時，都進行一樣的處理
			}catch(ResourceAccessException  rae) {
//				檢查超時容許額度是否用盡
				if(param.HB_TIMEOUT_QUATA == 0) {
					param.initSystemParam();
				}else {
					param.HB_TIMEOUT_QUATA--;
				}
//			收到非狀態碼200的回應，重置系統
			}catch(RestTemplateErrorResException rtere) {
				param.initSystemParam();
			}
		}else {
//			還未註冊，代表程式啟動的註冊流程沒有成功，改為發送註冊請求
			try {
				hsService.registerWithTheGateway();
//			連接超時、等待回應超時、收到非200的回應，都進行一樣的處理
			}catch(ResourceAccessException | RestTemplateErrorResException e) {
				param.initSystemParam();
			}
		}
	}

}
