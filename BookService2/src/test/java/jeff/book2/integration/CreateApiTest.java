package jeff.book2.integration;

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

import jeff.book2.common.constants.OpenApiConst;
import jeff.book2.controller.BookController;

/**
 * 針對所有create Api的測試案例。
 * 
 * @author Jeff Huang
 * */
public class CreateApiTest extends BookApiTestBase{
	
	private RequestBuilder genReqBuilderForCreate(String url, 
			HttpHeaders headers, JSONObject body) {
		return MockMvcRequestBuilders
				.post(url)
				.headers(headers)
				.content(body.toString());
	}

	/**
	 * 測試{@link BookController #addNewBook}的成功案例。</p>
	 * 一些非必要沒有傳的參數，看是不是為預設值。
	 * */
	@Test
	public void testSuccessfulCaseOfAddNewBook () throws Exception {
		String url = OpenApiConst.Path.BOOK_C;
        JSONObject reqBody = new JSONObject()
                .put("mainTitle", "測試")
                .put("type", "test");
		RequestBuilder mockReq = 
				genReqBuilderForCreate(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr = {
			status().isCreated(),
			header().string("Server-Name", "BookService2"),
			header().exists("location"),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.id").exists(),
			jsonPath("$.mainTitle").value("測試"),
			jsonPath("$.type").value("test"),
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
	 * 測試{@link BookController #addNewBook}的失敗案例。</p>
	 * */
	@Test
	public void testFailedCaseOfAddNewBook () throws Exception {
		String url = OpenApiConst.Path.BOOK_C;
//		沒有放必要參數的mainTitle
		JSONObject reqBody = new JSONObject()
                .put("type", "test");
		RequestBuilder mockReq = 
				genReqBuilderForCreate(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr).andDo(print());
//		沒有放必要參數的type
		reqBody = new JSONObject()
				.put("mainTitle", "測試");
		mockReq = genReqBuilderForCreate(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr2 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr2).andDo(print());
//		createdYear若有給值，必須為純數字
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test")
				.put("createdYear", "a100");
		mockReq = genReqBuilderForCreate(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr3 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr3).andDo(print());
//		createdYear若有給值，不能為負數
		
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test")
				.put("createdYear", "-1");
		mockReq = genReqBuilderForCreate(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr4 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr4).andDo(print());
//		acquiredYear若有給值，不能為負數
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", "test")
				.put("acquiredYear", -1);
		mockReq = genReqBuilderForCreate(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr5 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr5).andDo(print());
//		mainTitle不能為純空白
		reqBody = new JSONObject()
				.put("mainTitle", " ")
				.put("type", "test");
		mockReq = genReqBuilderForCreate(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr6 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr6).andDo(print());
//		type不能為純空白
		reqBody = new JSONObject()
				.put("mainTitle", "測試")
				.put("type", " ");
		mockReq = genReqBuilderForCreate(url,mockHttpHeaders,reqBody);
		ResultMatcher[] resRMArr7 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr7).andDo(print());
	}
	
}

