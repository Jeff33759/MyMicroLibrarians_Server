package jeff.authorization.service;

import java.io.IOException;

/**
 * AuthZ業務邏輯。
 * */
public interface AuthZService {

	/**
	 * 解析AccessToken並且授權，最後序列化。
	 * 
	 * @throws IOException
	 * 		   當序列化Authentication時發生錯誤。
	 * */
	public byte[] parseATAndAuthorizateAndSerialize(String Token) throws IOException;
	
}
