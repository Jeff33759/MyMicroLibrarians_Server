package jeff.book.unit;

import java.util.Calendar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import jeff.book.common.constants.BookQueryConst;
import jeff.book.common.util.MyUtil;
import jeff.book.model.dto.qparam.MyBookQueryParam;
import jeff.book.model.dto.receive.MyBookReq;
import jeff.book.model.po.MyBook;

@ExtendWith(MockitoExtension.class)
public class MyUtilTest {

	@InjectMocks
	private MyUtil myUtil;
	
	@Test
    public void testSetAcquiredYearFieldAsDefaultValueFuntion() {
    	String id = "2001.05.113..25.";
    	String behandledacquiredYear = id.split("\\.",2)[0];
    	Assertions.assertEquals("2001", behandledacquiredYear);
    }
	
	
	/**
	 * 測試用當前時間做出來的id，是否容易重複。
	 * */
	@Test
    public void testGenBookIdByCurrentTime() {
		Calendar instance = Calendar.getInstance();
		int y = instance.get(Calendar.YEAR);
		int m = instance.get(Calendar.MONTH)+1;
		int d = instance.get(Calendar.DAY_OF_MONTH);
		int h = instance.get(Calendar.HOUR_OF_DAY);
		int ms = instance.get(Calendar.MILLISECOND);
		String id = String.format("%d.%d.%d.%d.%d",y,m,d,h,ms);
		Assertions.assertEquals(y+"."+m+"."+d+"."+h+"."+ms, id);
    }

	/**
	 * 高併發情況的ID會重複，
	 * 若要處理此情況，可考慮當遇到重複ID時，後面加個亂數。
	 * */
	@Test
	public void testGenBookIdByCurrentTimeWhenHighConcurrency() {
		Calendar instance = Calendar.getInstance();
		int y = instance.get(Calendar.YEAR);
		int m = instance.get(Calendar.MONTH)+1;
		int d = instance.get(Calendar.DAY_OF_MONTH);
		int h = instance.get(Calendar.HOUR_OF_DAY);
		int ms = instance.get(Calendar.MILLISECOND);
		int y2 = instance.get(Calendar.YEAR);
		int m2 = instance.get(Calendar.MONTH)+1;
		int d2 = instance.get(Calendar.DAY_OF_MONTH);
		int h2 = instance.get(Calendar.HOUR_OF_DAY);
		int ms2 = instance.get(Calendar.MILLISECOND);
		String id = String.format("%d.%d.%d.%d.%d",y,m,d,h,ms);
		String id2 = String.format("%d.%d.%d.%d.%d",y2,m2,d2,h2,ms2);
		Assertions.assertEquals(id, id2);
	}
	
	/**
	 * 測試{@link MyBookReq}轉換成{@link MyBook}時，是否正確設置預設值。
	 * */
	@Test
	public void testConvertMyBookReqToMyBookAndSetDefault() {
		MyBookReq myBookReq = new MyBookReq();
		myBookReq.setMainTitle("test book.");
		myBookReq.setType("The type used to test.");
		MyBook myBook = 
				myUtil.ConvertMyBookReqToMyBookAndSetDefault(myBookReq,null);
		
		Assertions.assertEquals(myBookReq.getMainTitle()
				, myBook.getMainTitle());
		Assertions.assertEquals(myUtil.getCurrentYear()
				, myBook.getAcquiredYear());
		Assertions.assertEquals(myBookReq.getType(), 
				myBook.getType());
		Assertions.assertEquals("", myBook.getCreatedYear());
		Assertions.assertEquals("", myBook.getCreator());
		Assertions.assertEquals("", myBook.getDescription());
		Assertions.assertEquals("", myBook.getImageUrl());
		Assertions.assertEquals("", myBook.getOriginalUrl());
		Assertions.assertEquals("", myBook.getOwner());
		Assertions.assertEquals("", myBook.getOwnerCollectionsWebsite());
		Assertions.assertEquals("", myBook.getOwnerWebsite());
		Assertions.assertEquals(true, myBook.isAvailable());
	}
	
	/**
	 * 測試設置預設值給QueryString是否正常。
	 * */
	@Test
	public void testSetDefaultValueForParam() {
		MyBookQueryParam param = new MyBookQueryParam();
		myUtil.setDefaultValueForParam(param);
		Assertions.assertEquals("", param.getTitleKw());
		Assertions.assertEquals(-1, param.getYearFrom());
		Assertions.assertEquals(myUtil.getCurrentYear()+1, param.getYearTo());
		Assertions.assertEquals(BookQueryConst.DEFAULT_SORTING_RULE, param.getSortRule());
		Assertions.assertEquals(BookQueryConst.DEFAULT_PAGE_SIZE, param.getPageSize());
	}
		
}
	
