package jeff.book.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import jeff.book.controller.BookController;


/**
 * 針對所有Update Api的測試案例。
 * 
 * @author Jeff Huang
 * */
public class UpdateApiTest extends BookApiTestBase{
	
	private RequestBuilder genReqBuilderForReplace(String url, 
			HttpHeaders headers, JSONObject body) {
		return MockMvcRequestBuilders
				.put(url)
				.headers(headers)
				.content(body.toString());
	}

	private RequestBuilder genReqBuilderForUpdate(String url, 
			HttpHeaders headers, JSONObject body) {
		return MockMvcRequestBuilders
				.patch(url)
				.headers(headers)
				.content(body.toString());
	}
	
	/**
	 * 測試{@link BookController #replaceBook}的成功案例。</p>
	 * 一些非必要沒有傳的參數，看是不是為預設值。
	 * */
	@Test
	public void testSuccessfulCaseOfReplaceBook () throws Exception {
		String bookId = "test3";
		String url = "/book/" + bookId;
        JSONObject reqBody = new JSONObject()
                .put("mainTitle", "測試")
                .put("type", "test");
		RequestBuilder mockReq = 
				genReqBuilderForReplace(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr = {
			status().isOk(),
			header().string("Server-Name", "BookService1"),
			header().exists("location"),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.id").value("test3"),
			jsonPath("$.mainTitle").value("測試"),
			jsonPath("$.type").value("test"),
//			以下檢查預設值
			jsonPath("$.acquiredYear").value(
					Calendar.getInstance().get(Calendar.YEAR)),
			jsonPath("$.createdYear").value(""),
			jsonPath("$.creator").value(""),
			jsonPath("$.description").value(""),
			jsonPath("$.imageUrl").value(""),
			jsonPath("$.originalUrl").value(""),
			jsonPath("$.owner").value(""),
			jsonPath("$.ownerCollectionsWebsite").value(""),
			jsonPath("$.ownerWebsite").value(""),
			jsonPath("$.available").value(true)
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr).andDo(print());
	}
	
	
	/**
	 * 測試{@link BookController #replaceBook}的失敗案例。
	 * */
	@Test
	public void testFailedCaseOfReplaceBook () throws Exception {
		String bookId = "test3";
		String url = "/book/" + bookId;
//		沒有放必要參數的mainTitle
		JSONObject reqBody = new JSONObject()
                .put("type", "test");
		RequestBuilder mockReq = 
				genReqBuilderForReplace(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr).andDo(print());
//		沒有放必要參數的type
		reqBody = new JSONObject()
				.put("mainTitle", "測試");
		mockReq = genReqBuilderForReplace(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr2 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr2).andDo(print());
//		createdYear若有給值，必須為純數字
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test")
				.put("createdYear","a100");
		mockReq = genReqBuilderForReplace(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr3 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr3).andDo(print());
//		createdYear若有給值，不能為負數
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test")
				.put("createdYear","-1");
		mockReq = genReqBuilderForReplace(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr4 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr4).andDo(print());
//		acquiredYear若有給值，不能為負數
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test")
				.put("acquiredYear",-1);
		mockReq = genReqBuilderForReplace(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr5 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr5).andDo(print());
//		修改不存在的ID
		String notExistId = "test100";
		String idNotExistUrl = "/book/" + notExistId;
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test");
		mockReq = genReqBuilderForReplace(idNotExistUrl,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr6 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr6).andDo(print());
//		pathVar為空
		String emptyBookId = "";
		String emptyUrl = "/book/" + emptyBookId;
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test");
		mockReq = genReqBuilderForReplace(emptyUrl,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr7 = {
			status().isMethodNotAllowed(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr7).andDo(print());
//		pathVar不能為純空白
		String spaceBookId = " ";
		String spaceUrl = "/book/" + spaceBookId;
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test");
		mockReq = genReqBuilderForReplace(spaceUrl,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr8 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr8).andDo(print());
//		mainTitle不能為純空白
		reqBody = new JSONObject()
				.put("mainTitle", " ")
				.put("type", "test");
		mockReq = genReqBuilderForReplace(spaceUrl,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr9 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr9).andDo(print());
//		type不能為純空白
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", " ");
		mockReq = genReqBuilderForReplace(spaceUrl,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr10 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr10).andDo(print());
	}

	
	
	/**
	 * 測試{@link BookController #updateBook}的成功案例。</p>
	 * 一些非必要沒有傳的參數，看是不是為預設值。
	 * 因為是部分更新，沒有欄位為必填，全部都沒填就全部都沿用舊值。
	 * */
	@Test
	public void testSuccessfulCaseOfUpdateBook () throws Exception {
		String bookId = "test3";
		String url = "/book/" + bookId;
        JSONObject reqBody = new JSONObject();
		RequestBuilder mockReq = 
				genReqBuilderForUpdate(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr = {
			status().isOk(),
			header().string("Server-Name", "BookService1"),
			header().exists("location"),
			content().contentType(MediaType.APPLICATION_JSON),
//			以下檢查是否保留舊值
			jsonPath("$.id").value("test3"),
			jsonPath("$.mainTitle").value("日本史1"),
			jsonPath("$.type").value("test"),
			jsonPath("$.acquiredYear").value(2020),
			jsonPath("$.createdYear").value("2010"),
			jsonPath("$.creator").value("Jeff"),
			jsonPath("$.description").value("Book for test."),
			jsonPath("$.imageUrl").value(""),
			jsonPath("$.originalUrl").value(""),
			jsonPath("$.owner").value("Jeff's room."),
			jsonPath("$.ownerCollectionsWebsite").value(""),
			jsonPath("$.ownerWebsite").value(""),
			jsonPath("$.available").value(false)
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr).andDo(print());
	}
	
	
	/**
	 * 測試{@link BookController #updateBook}的失敗案例。
	 * */
	@Test
	public void testFailedCaseOfUpdateBook () throws Exception {
		String bookId = "test3";
		String url = "/book/" + bookId;
//		createdYear若有給值，必須為純數字
		JSONObject reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test")
				.put("createdYear","a100");
		RequestBuilder mockReq = 
				genReqBuilderForUpdate(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr).andDo(print());
//		createdYear若有給值，不能為負數
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test")
				.put("createdYear","-1");
		mockReq = genReqBuilderForUpdate(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr2 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr2).andDo(print());
//		acquiredYear若有給值，不能為負數
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test")
				.put("acquiredYear",-1);
		mockReq = genReqBuilderForUpdate(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr3 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr3).andDo(print());
//		修改不存在的ID
		String notExistId = "test100";
		String idNotExistUrl = "/book/" + notExistId;
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test");
		mockReq = genReqBuilderForUpdate(idNotExistUrl,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr4 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr4).andDo(print());
//		pathVar為空
		String emptyBookId = "";
		String emptyUrl = "/book/" + emptyBookId;
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test");
		mockReq = genReqBuilderForUpdate(emptyUrl,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr5 = {
			status().isMethodNotAllowed(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr5).andDo(print());
//		pathVar不能為純空白
		String spaceBookId = " ";
		String spaceUrl = "/book/" + spaceBookId;
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test");
		mockReq = genReqBuilderForUpdate(spaceUrl,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr6 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr6).andDo(print());
//		mainTitle不能為純空白
		reqBody = new JSONObject()
				.put("mainTitle", " ")
				.put("type", "test");
		mockReq = genReqBuilderForUpdate(spaceUrl,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr7 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr7).andDo(print());
//		type不能為純空白
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", " ");
		mockReq = genReqBuilderForUpdate(spaceUrl,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr8 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr8).andDo(print());
	}
	
}
