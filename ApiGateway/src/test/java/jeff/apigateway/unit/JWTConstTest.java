package jeff.apigateway.unit;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jeff.apigateway.common.constants.JWTConst;

public class JWTConstTest {
	
	/**
	 * 測試透過JWTConstants得到的RT_SECRETKEY是否為單例。
	 * */
	@Test
	public void testGetRTSecretKeyFromJWTConstantsIsSingletonOrNot() {
		SecretKey RTkey = JWTConst.RT_SECRETKEY;
		SecretKey RTkey2 = JWTConst.RT_SECRETKEY;
		Assertions.assertSame(RTkey, RTkey2);
	}

	/**
	 * 測試透過JWTConstants得到的AT_KEYPAIR是否為單例。
	 * */
	@Test
	public void testGetATKeyPairFromJWTConstantsIsSingletonOrNot() {
		KeyPair ATKeyPair =  JWTConst.AT_KEYPAIR;
		KeyPair ATKeyPair2 =  JWTConst.AT_KEYPAIR;
		Assertions.assertSame(ATKeyPair, ATKeyPair2);
		
		PublicKey pubKey1 = ATKeyPair.getPublic();
		PublicKey pubKey2 = ATKeyPair2.getPublic();
		Assertions.assertSame(pubKey1, pubKey2);
		
		PrivateKey privKey1 = ATKeyPair.getPrivate();
		PrivateKey privKey2 = ATKeyPair2.getPrivate();
		Assertions.assertSame(privKey1, privKey2);
	}

}
