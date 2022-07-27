package jeff.apigateway.service;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

/**
 * 業務邏輯的轉發服務器。
 * 因為不想把轉發相關邏輯寫在控制器裡，所以新增一個處理
 * 業務轉發的服務器，也方便和其他服務器一起捕捉例外並進行log處理。
 * */
public interface BusinessForwardingService {
	
	/**
	 * get轉發。
	 * 
	 * @param req
	 * @param rootUrl 
	 * @param pathVarMap 路徑參數的LinkedHashMap，K為路徑參數名，V為路徑參數值，沒有就傳null。
	 * */
	public ResponseEntity<?> forwardGetReq(HttpServletRequest req,
			String rootUrl,LinkedHashMap<String,String> pathVarMap) throws Exception;
	
	/**
	 * post轉發。
	 * 
	 * @param req
	 * @param rootUrl 
	 * 	 * @param pathVarMap 路徑參數的LinkedHashMap，K為路徑參數名，V為路徑參數值，沒有就傳null。
	 * */
	public ResponseEntity<?> forwardPostReq(HttpServletRequest req,
			String rootUrl,LinkedHashMap<String,String> pathVarMap) throws Exception;
	
	/**
	 * put轉發。
	 * 
	 * @param req
	 * @param rootUrl 
	 * @param pathVarMap 路徑參數的LinkedHashMap，K為路徑參數名，V為路徑參數值，沒有就傳null。
	 * */
	public ResponseEntity<?> forwardPutReq(HttpServletRequest req,
			String rootUrl,LinkedHashMap<String,String> pathVarMap) throws Exception;
	
	/**
	 * patch轉發。
	 * 
	 * @param req
	 * @param rootUrl 
	 * @param pathVarMap 路徑參數的LinkedHashMap，K為路徑參數名，V為路徑參數值，沒有就傳null。
	 * */
	public ResponseEntity<?> forwardPatchReq(HttpServletRequest req,
			String rootUrl,LinkedHashMap<String,String> pathVarMap) throws Exception;
	
	/**
	 * delete轉發。
	 * 
	 * @param req
	 * @param rootUrl 
	 * @param pathVarMap 路徑參數的LinkedHashMap，K為路徑參數名，V為路徑參數值，沒有就傳null。
	 * */
	public ResponseEntity<?> forwardDeleteReq(HttpServletRequest req,
			String rootUrl,LinkedHashMap<String,String> pathVarMap) throws Exception;
	
}
