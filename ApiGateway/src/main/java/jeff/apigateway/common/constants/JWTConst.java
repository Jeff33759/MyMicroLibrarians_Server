package jeff.apigateway.common.constants;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * 製作JWTs的參數，做成常數方便統一管理。
 * 
 * @author Jeff Huang
 * */
public interface JWTConst {
	
	/**
	 * RefreshToken因為解析及創造都在同個伺服端，因此使用HS256對稱型加密。
	 * */
	public static final SignatureAlgorithm RT_ALGORITHM = SignatureAlgorithm.HS256;
	
	/**
	 * RefreshToken密鑰。
	 * */
	public static final SecretKey RT_SECRETKEY = getSecretKeyOfRefreshToken();

    /**
     * RefreshToken時效(分鐘)
     * */
    public static final int RT_EXP = 60;
    
    /**
     * 使用RS256非對稱型加密，私鑰製作、公鑰解析。</p>
     * */
    public static final SignatureAlgorithm AT_ALGORITHM = SignatureAlgorithm.RS256;

    /**
     * AccessToken密鑰對。
     * 使用final，確保於APP運行期間透過此常數獲得的KeyPair為單例物件。
     * */
    public static final KeyPair AT_KEYPAIR = getKeyPairOfAccessToken();
    
    /**
     * AccessToken時效(分鐘)
     * */
    public static final int AT_EXP = 30;
    
    /**
     * 指定密鑰字串，得到SecretKey實例，用於HS256。
     * */
    private static SecretKey getSecretKeyOfRefreshToken() {
    	String keyStr = "KeyforEncodingAndDecodingRefreshTokenSignature";
    	return Keys.hmacShaKeyFor(keyStr.getBytes());
    }
    
    /**
     * 由KeyPairGenerator隨機產生符合規範的RS256密鑰對。</p>
     * 注意:每次JAVA程式啟動都會刷新公鑰與私鑰，所以客戶端需要重新請求公鑰。
     * */
    private static KeyPair getKeyPairOfAccessToken(){
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA"); //algorithm
			return generator.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		return null;
    }
}
