package jeff.apigateway.unit;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import jeff.apigateway.common.util.MyUtil;
import jeff.apigateway.exception.PathVarIsBlankException;


/**
 * MyUtil的一些測試案例。
 * */
@ExtendWith(MockitoExtension.class)
public class MyUtilTest {
	
	@InjectMocks
	private MyUtil util;
	
	/**
	 * 測試有沒有把LinkedHashMap變成期望的字串格式。
	 * */
	@Test
	public void testHandlePathVar() {
		LinkedHashMap<String,String> pathVarMap = 
				new LinkedHashMap<String,String>();
		pathVarMap.put("id", "a1");
		pathVarMap.put("name", "product");
		pathVarMap.put("spec", "5kg");
		String pathVar = util.handlePathVar(pathVarMap);
		Assertions.assertEquals("/a1/product/5kg", pathVar);
	}
	
	/**
	 * 測試當路徑參數有空字串時，會不會正常跳錯。
	 * */
	@Test
	public void testHandlePathVarWhenVarIsBlank() {
		LinkedHashMap<String,String> pathVarMap = 
				new LinkedHashMap<String,String>();
		pathVarMap.put("id", "a1");
		pathVarMap.put("name", "product");
		pathVarMap.put("spec", "");
		Assertions.assertThrows(PathVarIsBlankException.class, ()->{
			util.handlePathVar(pathVarMap);
		});
	}

}
