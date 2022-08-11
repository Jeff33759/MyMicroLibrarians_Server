package jeff.book.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import jeff.book.common.constants.OpenApiConst;
import jeff.book.controller.BookController;

/**
 * 針對所有Read Api的測試案例。
 * 
 * @author Jeff Huang
 * */
public class ReadApiTest extends BookApiTestBase{
	
	private RequestBuilder genReqBuilderForRead(String url, HttpHeaders headers) {
		return MockMvcRequestBuilders
				.get(url)
				.headers(headers);
	}
	
	/**
	 * 測試{@link BookController #queryAllBooks}的成功案例。</p>
	 * 測試分頁與排序與pageSize是否正常。
	 * 會先以AcquiredYear排序，然後相同的再以ID排序。
	 * */
	@Test
	public void testSuccessfulCaseOfQueryAllBooks () throws Exception {
//		升冪排序、一頁三筆、第一頁
		String queryString = "?nowPage=1&pageSize=3";
		String url = OpenApiConst.Path.BOOK_R + queryString;
		RequestBuilder mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr = {
			status().isOk(),
			header().string("Server-Name", "BookService1"),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.result[0].id").value("test4"),
			jsonPath("$.result[1].id").value("test5"),
			jsonPath("$.result[2].id").value("test2"),
			jsonPath("$.nowPage").value(1),
			jsonPath("$.pageSize").value(3),
			jsonPath("$.totalElements").value(5),
			jsonPath("$.totalPages").value(2),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr).andDo(print());
//		升冪排序、一頁三筆、第二頁
		queryString = "?nowPage=2&pageSize=3";
		url = OpenApiConst.Path.BOOK_R + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr2 = {
			status().isOk(),
			header().string("Server-Name", "BookService1"),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.result[0].id").value("test3"),
			jsonPath("$.result[1].id").value("test1"),
			jsonPath("$.nowPage").value(2),
			jsonPath("$.pageSize").value(3),
			jsonPath("$.totalElements").value(5),
			jsonPath("$.totalPages").value(2),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr2).andDo(print());
//		降冪排序、一頁三筆、第一頁
		queryString = "?nowPage=1&pageSize=3&sortRule=desc";
		url = OpenApiConst.Path.BOOK_R + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr3 = {
			status().isOk(),
			header().string("Server-Name", "BookService1"),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.result[0].id").value("test1"),
			jsonPath("$.result[1].id").value("test3"),
			jsonPath("$.result[2].id").value("test2"),
			jsonPath("$.nowPage").value(1),
			jsonPath("$.pageSize").value(3),
			jsonPath("$.totalElements").value(5),
			jsonPath("$.totalPages").value(2),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr3).andDo(print());
//		升冪排序、一頁五筆、第一頁
		queryString = "?nowPage=1&pageSize=5";
		url = OpenApiConst.Path.BOOK_R + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr4 = {
			status().isOk(),
			header().string("Server-Name", "BookService1"),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.result[0].id").value("test4"),
			jsonPath("$.result[1].id").value("test5"),
			jsonPath("$.result[2].id").value("test2"),
			jsonPath("$.result[3].id").value("test3"),
			jsonPath("$.nowPage").value(1),
			jsonPath("$.pageSize").value(5),
			jsonPath("$.totalElements").value(5),
			jsonPath("$.totalPages").value(1),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr4).andDo(print());
	}
	
	/**
	 * 測試{@link BookController #queryAllBooks}的失敗案例。</p>
	 * */
	@Test
	public void testFailedCaseOfQueryAllBooks () throws Exception {
//		queryString沒放必要參數的nowPage
		String queryString = "";
		String url = OpenApiConst.Path.BOOK_R + queryString;
		RequestBuilder mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr).andDo(print());
//		pageSize不得大於20
		queryString = "?nowPage=1&pageSize=21";
		url = OpenApiConst.Path.BOOK_R + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr2 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr2).andDo(print());
//		pageSize不得小於3
		queryString = "?nowPage=1&pageSize=2";
		url = OpenApiConst.Path.BOOK_R + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr3 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr3).andDo(print());
//		sortRule傳入了"asc"或"desc"以外的字串
		queryString = "?nowPage=1&pageSize=3&sortRule=ASC";
		url = OpenApiConst.Path.BOOK_R + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr4 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr4).andDo(print());
//		nowPage不得小於1
		queryString = "?nowPage=0&pageSize=3";
		url = OpenApiConst.Path.BOOK_R + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr5 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr5).andDo(print());
	}

	
	/**
	 * 測試{@link BookController #queryBookById}的成功案例。</p>
	 * */
	@Test
	public void testSuccessfulCaseOfQueryBookById () throws Exception {
		String bookId = "test3";
		String url = "/book/byid/" + bookId;
		RequestBuilder mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr = {
			status().isOk(),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.id").value("test3")
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr).andDo(print());
	}
	
	/**
	 * 測試{@link BookController #queryBookById}的失敗案例。</p>
	 * */
	@Test
	public void testFailedCaseOfQueryBookById () throws Exception {
//		查詢不存在的ID
		String bookId = "test100";
		String url = "/book/byid/" + bookId;
		RequestBuilder mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr = {
			status().isNotFound(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr).andDo(print());
//		pathVar為空
		bookId = "";
		url = "/book/byid/" + bookId;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr2 = {
			status().isMethodNotAllowed(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr2).andDo(print());
//		pathVar為純空白
		bookId = " ";
		url = "/book/byid/" + bookId;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr3 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr3).andDo(print());
	}
	
	
	/**
	 * 測試{@link BookController #queryBooksByCondition}的成功案例。</p>
	 * 入館年份區間，應該要連頭尾都算進去，例如查詢2010~2020，那就會抓出
	 * 包含2010和2020的資料。</p>
	 * 會先以AcquiredYear排序，然後相同的再以ID排序。
	 * */
	@Test
	public void testSuccessfulCaseOfQueryBooksByCondition () throws Exception {
//		升冪排序、一頁三筆、第一頁，無其他條件
		String queryString = "?nowPage=1&pageSize=3";
		String url = OpenApiConst.Path.BOOK_R_COND + queryString;
		RequestBuilder mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr = {
			status().isOk(),
			header().string("Server-Name", "BookService1"),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.result[0].id").value("test4"),
			jsonPath("$.result[1].id").value("test5"),
			jsonPath("$.result[2].id").value("test2"),
			jsonPath("$.nowPage").value(1),
			jsonPath("$.pageSize").value(3),
			jsonPath("$.totalElements").value(5),
			jsonPath("$.totalPages").value(2),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr).andDo(print());
//		升冪排序、一頁三筆、第一頁、標題關鍵字
		queryString = "?nowPage=1&pageSize=3&titleKw=日本";
		url = OpenApiConst.Path.BOOK_R_COND + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr2 = {
			status().isOk(),
			header().string("Server-Name", "BookService1"),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.result[0].id").value("test4"),
			jsonPath("$.result[1].id").value("test5"),
			jsonPath("$.result[2].id").value("test3"),
			jsonPath("$.nowPage").value(1),
			jsonPath("$.pageSize").value(3),
			jsonPath("$.totalElements").value(3),
			jsonPath("$.totalPages").value(1),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr2).andDo(print());
//		升冪排序、一頁三筆、第一頁、入館年份區間頭&尾
		queryString = "?nowPage=1&pageSize=3&yearFrom=2010&yearTo=2020";
		url = OpenApiConst.Path.BOOK_R_COND + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr3 = {
			status().isOk(),
			header().string("Server-Name", "BookService1"),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.result[0].id").value("test5"),
			jsonPath("$.result[1].id").value("test2"),
			jsonPath("$.result[2].id").value("test3"),
			jsonPath("$.nowPage").value(1),
			jsonPath("$.pageSize").value(3),
			jsonPath("$.totalElements").value(3),
			jsonPath("$.totalPages").value(1),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr3).andDo(print());
//		升冪排序、一頁三筆、第一頁、標題關鍵字+入館年份區間頭&尾
		queryString = "?nowPage=1&pageSize=3&titleKw=日本&yearFrom=2010&yearTo=2020";
		url = OpenApiConst.Path.BOOK_R_COND + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr4 = {
			status().isOk(),
			header().string("Server-Name", "BookService1"),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.result[0].id").value("test5"),
			jsonPath("$.result[1].id").value("test3"),
			jsonPath("$.nowPage").value(1),
			jsonPath("$.pageSize").value(3),
			jsonPath("$.totalElements").value(2),
			jsonPath("$.totalPages").value(1),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr4).andDo(print());
//		降冪排序、一頁三筆、第一頁、標題關鍵字+入館年份區間頭&尾
		queryString = "?nowPage=1&pageSize=3&sortRule=desc&titleKw=日本&yearFrom=2010&yearTo=2020";
		url = OpenApiConst.Path.BOOK_R_COND + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr5 = {
			status().isOk(),
			header().string("Server-Name", "BookService1"),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.result[0].id").value("test3"),
			jsonPath("$.result[1].id").value("test5"),
			jsonPath("$.nowPage").value(1),
			jsonPath("$.pageSize").value(3),
			jsonPath("$.totalElements").value(2),
			jsonPath("$.totalPages").value(1),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr5).andDo(print());
//		升冪排序、一頁三筆、第一頁、入館年份區間頭
		queryString = "?nowPage=1&pageSize=3&yearFrom=2010";
		url = OpenApiConst.Path.BOOK_R_COND + queryString;
		
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr6 = {
			status().isOk(),
			header().string("Server-Name", "BookService1"),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.result[0].id").value("test5"),
			jsonPath("$.result[1].id").value("test2"),
			jsonPath("$.result[2].id").value("test3"),
			jsonPath("$.nowPage").value(1),
			jsonPath("$.pageSize").value(3),
			jsonPath("$.totalElements").value(4),
			jsonPath("$.totalPages").value(2),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr6).andDo(print());
//		升冪排序、一頁三筆、第一頁、入館年份區間尾
		queryString = "?nowPage=1&pageSize=3&yearTo=2010";
		url = OpenApiConst.Path.BOOK_R_COND + queryString;
		
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr7 = {
			status().isOk(),
			header().string("Server-Name", "BookService1"),
			content().contentType(MediaType.APPLICATION_JSON),
			jsonPath("$.result[0].id").value("test4"),
			jsonPath("$.result[1].id").value("test5"),
			jsonPath("$.nowPage").value(1),
			jsonPath("$.pageSize").value(3),
			jsonPath("$.totalElements").value(2),
			jsonPath("$.totalPages").value(1),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr7).andDo(print());
	}
	
	/**
	 * 測試{@link BookController #queryBooksByCondition}的失敗案例。</p>
	 * */
	@Test
	public void testFailedCaseOfQueryBooksByCondition () throws Exception {
//		queryString沒放必要參數的nowPage
		String queryString = "?pageSize=3";
		String url = OpenApiConst.Path.BOOK_R_COND + queryString;
		RequestBuilder mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr).andDo(print());
//		queryString的yearTo必須是數字
		queryString = "?nowPage=1&pageSize=3&yearTo=a";
		url = OpenApiConst.Path.BOOK_R_COND + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr2 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr2).andDo(print());
//		queryString的yearTo不得為負數
		queryString = "?nowPage=1&pageSize=3&yearTo=-1";
		url = OpenApiConst.Path.BOOK_R_COND + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr3 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr3).andDo(print());
//		queryString的yearFrom必須是數字
		queryString = "?nowPage=1&pageSize=3&yearFrom=a";
		url = OpenApiConst.Path.BOOK_R_COND + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr4 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr4).andDo(print());
//		queryString的yearFrom不得為負數
		queryString = "?nowPage=1&pageSize=3&yearFrom=-1";
		url = OpenApiConst.Path.BOOK_R_COND + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr5 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr5).andDo(print());
//		pageSize不得大於20
		queryString = "?nowPage=1&pageSize=21";
		url = OpenApiConst.Path.BOOK_R + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr6 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr6).andDo(print());
//		pageSize不得小於3
		queryString = "?nowPage=1&pageSize=2";
		url = OpenApiConst.Path.BOOK_R + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr7 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr7).andDo(print());
//		sortRule傳入了"asc"或"desc"以外的字串
		queryString = "?nowPage=1&pageSize=3&sortRule=ASC";
		url = OpenApiConst.Path.BOOK_R + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr8 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr8).andDo(print());
//		nowPage不得小於1
		queryString = "?nowPage=0&pageSize=3";
		url = OpenApiConst.Path.BOOK_R + queryString;
		mockReq = genReqBuilderForRead(url,mockHttpHeaders);
		ResultMatcher[] resRMArr9 = {
			status().isBadRequest(),
		};
		mockMvc.perform(mockReq).andExpectAll(resRMArr9).andDo(print());
	}
	
}
