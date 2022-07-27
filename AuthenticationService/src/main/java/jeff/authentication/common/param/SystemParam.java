package jeff.authentication.common.param;

import java.security.PrivateKey;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtParser;

/**
 *  存放一些系統用的變數，從properties檔取值。
 *  
 *  @author Jeff Huang
 * */
@Component
@PropertySource("classpath:SystemParam.properties")
public class SystemParam {
	
	/**
	 * gateway的位址。
	 * */
	@Value("${ip.gateway}")
	public String GATEWAY_IP;

	/**
	 * gateway的port。
	 * */
	@Value("${port.gateway}")
	public String GATEWAY_PORT;
	
	
	/**
	 * 是否已經向gateway註冊成功，預設false。
	 * */
	public boolean HAS_REGISTERED = false;
	
	/**
	 * 向gateway發送心跳請求，結果跳超時例外。
	 * 預設容許額度為3次，每次請求連接超時都-1，
	 * 若為0，代表3次心跳請求都沒得到預期的回應，
	 * 本微服務將會重新回歸未註冊的狀態。
	 * */
	public Integer HB_TIMEOUT_QUATA = 3;
	
    
	/**
	 * RefreshToken解析器，向gateway註冊後賦值，預設null。
	 * */
	public JwtParser RT_PARSER = null;
	
	/**
	 * RefreshTokenSignature加密使用的演算法名稱
	 * */
	public String RT_SIGN_ALGO_NAME = null;
	
	/**
	 * RefreshToken有效期限(分鐘)，預設null
	 * */
	public Integer RT_EXP = null;
	
	/**
	 * RefreshToken加密簽名及解析簽名用的密鑰，預設null
	 * */
	public SecretKey RT_SECRET_KEY = null;

	/**
	 * AccessRefreshTokenSignature加密使用的演算法名稱
	 * */
	public String AT_SIGN_ALGO_NAME = null;

	
	/**
	 * AccessToken有效期限(分鐘)，預設null
	 * */
	public Integer AT_EXP = null;

	/**
	 * AccessTokenSignature加密用的私鑰，預設null
	 * */
	public PrivateKey AT_PRIV_KEY = null;

    
    /**
     * 將系統參數回歸剛啟動，未註冊時的狀態。
     * */
    public void initSystemParam() {
    	HAS_REGISTERED = false;
    	HB_TIMEOUT_QUATA = 3;
    	RT_PARSER = null;
    	RT_SIGN_ALGO_NAME = null;
    	RT_EXP = null;
    	RT_SECRET_KEY = null;
    	AT_SIGN_ALGO_NAME = null;
    	AT_EXP = null;
    	AT_PRIV_KEY = null;
    }

}
