package jeff.apigateway.unit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jeff.apigateway.common.constants.InnerApiConst;
import jeff.apigateway.common.constants.InnerApiConst.Receive.PriApi;

/**
 * 一些私人API的測試案例。
 * 
 * @see InnerApiConst
 * */
public class InnerApiConstTest {
	
	/**
	 * 測試得到私人Api Matcher物件是否正常，並取出路徑字串，
	 * 排序後比對是否一樣。
	 * */
	@Test
	public void testGetAllSystemPriApiMatcher() {
		AntPathRequestMatcher[] matchArr = 
				InnerApiConst.Receive.getAllPriApiMatcher();
		ArrayList<String> srtArrList = new ArrayList<String>();
		ArrayList<String> srtArrList2 = new ArrayList<String>();
		PriApi[] priArr = InnerApiConst.Receive.PriApi.values();
		
		Stream.of(matchArr).forEach(
				matcher->srtArrList.add(matcher.getPattern()));
		Stream.of(priArr).forEach(
				pri->srtArrList2.add(pri.path()));
		srtArrList.sort(Comparator.naturalOrder());
		srtArrList2.sort(Comparator.naturalOrder());
		
		String[] strArr = srtArrList.toArray(new String[srtArrList.size()]);
		String[] strArr2 = srtArrList2.toArray(new String[srtArrList2.size()]);
		
		Assertions.assertArrayEquals(strArr2,strArr);
	}
	
}
