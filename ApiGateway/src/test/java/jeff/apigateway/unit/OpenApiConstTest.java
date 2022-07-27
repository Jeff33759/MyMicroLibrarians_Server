package jeff.apigateway.unit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jeff.apigateway.common.constants.OpenApiConst;
import jeff.apigateway.common.constants.OpenApiConst.ProtApi;
import jeff.apigateway.common.constants.OpenApiConst.PubApi;

public class OpenApiConstTest {

	/**
	 * 測試得到所有Api路徑字串是否正常。
	 * 私人、受保護及公開API，各抓一個來斷言測試。
	 * */
	@Test
	public void testGetAllApiPath() {
		String[] arr = OpenApiConst.getAllApiPath();
		boolean protMatch = Stream.of(arr).anyMatch(
				path->path.equals(OpenApiConst.ProtApi.BOOK_C.path()));
		boolean pubMatch = Stream.of(arr).anyMatch(
				path->path.equals(OpenApiConst.PubApi.AUTH_LOGIN.path()));
		
		Assertions.assertTrue(protMatch);
		Assertions.assertTrue(pubMatch);
	}
	

	
	/**
	 * 測試得到受保護Api Matcher物件是否正常，並取出路徑字串，排序後比對是否一樣。
	 * */
	@Test
	public void testGetAllProtApiMatcher() {
		AntPathRequestMatcher[] matchArr = 
				OpenApiConst.getAllProtApiMatcher();
		ArrayList<String> srtArrList = new ArrayList<String>();
		ArrayList<String> srtArrList2 = new ArrayList<String>();
		ProtApi[] protArr = OpenApiConst.ProtApi.values();

		Stream.of(matchArr).forEach(
				matcher->srtArrList.add(matcher.getPattern()));
		Stream.of(protArr).forEach(
				prot->srtArrList2.add(prot.path()));
		srtArrList.sort(Comparator.naturalOrder());
		srtArrList2.sort(Comparator.naturalOrder());
		
		String[] strArr = srtArrList.toArray(new String[srtArrList.size()]);
		String[] strArr2 = srtArrList2.toArray(new String[srtArrList2.size()]);
		
		Assertions.assertArrayEquals(strArr2,strArr);
	}
	
	/**
	 * 測試得到Lv1受保護Api Matcher物件是否正常，
	 * 隨便抓已知的Lv1以及非Lv1來斷言測試，
	 * 看是不是只抓出Lv1。</p>
	 * */
	@Test
	public void testGetLv1ProtApiMatcher() {
		AntPathRequestMatcher[] matchArr = 
				OpenApiConst.getLv1ProtApiMatcher();
		
		boolean lv1Flag = 
				Stream.of(matchArr).anyMatch(matcher->
					matcher.getPattern().equals(
							OpenApiConst.Path.BOOK_R_COND));
		boolean lv2Flag = 
				Stream.of(matchArr).anyMatch(matcher->
				matcher.getPattern().equals(
						OpenApiConst.Path.BOOK_C));
		
		Assertions.assertEquals(true,lv1Flag);
		Assertions.assertEquals(false,lv2Flag);
	}
	
	/**
	 * 測試得到Lv2受保護Api Matcher物件是否正常，
	 * 隨便抓已知的Lv2以及非Lv2來斷言測試，
	 * 看是不是只抓出Lv2。</p>
	 * */
	@Test
	public void testGetLv2ProtApiMatcher() {
		AntPathRequestMatcher[] matchArr = 
				OpenApiConst.getLv2ProtApiMatcher();
		
		boolean lv1Flag = 
				Stream.of(matchArr).anyMatch(matcher->
				matcher.getPattern().equals(
						OpenApiConst.Path.BOOK_R_COND));
		boolean lv2Flag = 
				Stream.of(matchArr).anyMatch(matcher->
				matcher.getPattern().equals(
						OpenApiConst.Path.BOOK_C));
		
		Assertions.assertEquals(false,lv1Flag);
		Assertions.assertEquals(true,lv2Flag);
	}
	
	
	/**
	 * 測試得到公開Api Matcher物件是否正常，並取出路徑字串，排序後比對是否一樣。
	 * */
	@Test
	public void testGetAllPubProtApiMatcher() {
		AntPathRequestMatcher[] matchArr = 
				OpenApiConst.getAllPubApiMatcher();
		ArrayList<String> srtArrList = new ArrayList<String>();
		ArrayList<String> srtArrList2 = new ArrayList<String>();
		PubApi[] pubArr = OpenApiConst.PubApi.values();
		
		Stream.of(matchArr).forEach(
				matcher->srtArrList.add(matcher.getPattern()));
		Stream.of(pubArr).forEach(
				pub->srtArrList2.add(pub.path()));
		srtArrList.sort(Comparator.naturalOrder());
		srtArrList2.sort(Comparator.naturalOrder());
		
		String[] strArr = srtArrList.toArray(new String[srtArrList.size()]);
		String[] strArr2 = srtArrList2.toArray(new String[srtArrList2.size()]);
		
		Assertions.assertArrayEquals(strArr2,strArr);
	}
	
	/**
	 * 利用反射機制，測試兩個內部介面，
	 * {@link OpenApiConst #Path} 、{@link OpenApiConst #HttpMethods}、
	 * 內的常數數量是否一樣，因為一個路徑都會對應一組HttpMethods。
	 * */
	@Test
	public void testApiPathAndHttpMethodsAndReqMothodsHaveTheSamaAmount() {
		Class<OpenApiConst.Path> paths = OpenApiConst.Path.class;
		Class<OpenApiConst.HttpMethods> methods = OpenApiConst.HttpMethods.class;
		
		int pathFieldLength = paths.getFields().length;
		int methodFieldLength = methods.getFields().length;
		
		Assertions.assertEquals(methodFieldLength,pathFieldLength);
	}
	
	
}
