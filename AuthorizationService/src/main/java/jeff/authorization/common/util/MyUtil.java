package jeff.authorization.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 自做的工具包，註冊為單例Spring Bean。
 * 
 * @author Jeff Huang
 * */
@Component
public class MyUtil {
	
	@Autowired
	private ObjectMapper objectMapper;

	
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
	 * 將JAVA物件序列化。</p>
	 * */
	public byte[] serializeObj(Object obj) throws IOException {
		byte[] bytes = null;
//		try with resource寫法，會自動把串流close掉，不用手動close。
		try(ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);) {
			oo.writeObject(obj);
			bytes = bo.toByteArray();
		} catch (IOException e) {
			throw e;
		}
		return bytes;		
	}


}
