package jeff.apigateway.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jeff.apigateway.common.param.SystemParam;
import jeff.apigateway.exception.PathVarIsBlankException;



/**
 * 自做的工具包，註冊為單例Spring Bean。
 * 
 * @author Jeff Huang
 * */
@Component
public class MyUtil {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private SystemParam.AuthN authNParam;
	
	@Autowired
	private SystemParam.AuthZ authZParam;
	
	@Autowired
	private SystemParam.Common commonParam;

	
	/**
	 * 設定Response的格式。</p>
	 * {@code response.getWriter()}不可以在這裡flush掉，否則變成commits狀態的response，
	 * 會造成dispatcherServlet跳ERROR。
	 * */
	public void setFormatOfResponse(HttpServletResponse response,
			int sc, Object resObj) throws IOException {
		String jsonStr = this.objectMapper.writeValueAsString(resObj);
		response.setStatus(sc);
		response.setContentType("application/json");
		response.getWriter().print(jsonStr);
	}
	
	/**
	 * 檢查強依賴的幾個微服務是否註冊成功，
	 * 若其中一個強依賴微服務沒在服務中，那就關閉gateway的對外開放。
	 * */
	public void checkIfTheStrongDependencyServiceIsInTheService() {
		if(authNParam.IN_SERVICE && authZParam.IN_SERVICE) {
			commonParam.GW_IN_SERVICE = true;
		}else {
			commonParam.GW_IN_SERVICE = false;
		}
	}
	
	/**
	 * 將byte陣列反序列化為java物件。
	 * */
	public Object deserializeBytes(byte[] bytes) throws Exception {
	    Object obj = null;
//	    try with resource寫法，會自動close掉，不用手動close
	    try(ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
		    ObjectInputStream oi = new ObjectInputStream(bi);) {
	        obj = oi.readObject();
	    } catch (Exception e) {
	        throw e;
	    }
	    return obj;
	}
	
	/**
	 * 驗證路徑參數是否為空或者為純空白字元。
	 * @throws PathVarIsBlankException
	 * 		   若路徑參數為空或者為純空白字元，則拋出。
	 * */
	public void validatePathVariableIfItIsBlank(String pathVarKey, String pathVarVal) 
		throws PathVarIsBlankException{
		if(pathVarVal.isBlank()) {
			throw new PathVarIsBlankException("'" + pathVarKey + "' must not be null and "
					+ "must contain at least onenon-whitespace character.");
		}
	}
	
	/**
	 * 將路徑參數陣列處理成想要的路徑。
	 * 
	 * @param pathVarMap 
	 * 		  路徑參數LinkedHashMap(有序Map，確保路徑參數按順序拼接)，若為null就會回傳空字串。
	 * */
	public String handlePathVar(LinkedHashMap<String,String> pathVarMap) {
		String path = "";
		if(pathVarMap != null) {
			for(Entry<String,String> entry : pathVarMap.entrySet()) {
				validatePathVariableIfItIsBlank(
						entry.getKey(), entry.getValue());
				path += "/" + entry.getValue();
			}
		}
		return path;
	}
	
	/**
	 * 新建一個HttpHeader。
	 * */
	public HttpHeaders genHttpHeader(HttpServletRequest req) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String token = req.getHeader(HttpHeaders.AUTHORIZATION);
		if(token != null) {
			headers.setBearerAuth(token);
		}
		return headers;
	}
	
	/**
	 * 取出HttpBody的二進制資料。
	 * */
	public byte[] getRequestBody(HttpServletRequest req) throws IOException {
		return req.getInputStream().readAllBytes();
	}
	
	/**
	 * 創建一個請求用的物件。
	 * */
	public HttpEntity<?> genHttpEntity(HttpHeaders headers, byte[] body) {
		return new HttpEntity<>(body,headers);
	}
	
	/**
	 * 將訪問的路徑加上路徑參數以及queryString。
	 * */
	public String handleTheUrl(String rootUrl, String pathVarStr, String queryStr) {
		return queryStr.equals("") ? 
				 rootUrl + pathVarStr :
					 rootUrl + pathVarStr + "?" + queryStr;
	}
	
	/**
	 * 因為QueryString可能會帶有中文字，所以用{@link URLDecoder}解碼，若為null就回傳空字串。
	 * @throws UnsupportedEncodingException 
	 * 		   若{@link URLDecoder #decode}若第二個參數指定不支持的編碼
	 */
	public String decodeQueryStr(String quereStr) 
			throws UnsupportedEncodingException {
		String decodedQStr = "";
		if(quereStr!=null) {
			decodedQStr = URLDecoder.decode(quereStr,"UTF-8");
		}
		return decodedQStr;
	}
	
	
}
