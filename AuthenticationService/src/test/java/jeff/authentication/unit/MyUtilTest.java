package jeff.authentication.unit;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import jeff.authentication.common.util.MyUtil;

@ExtendWith(MockitoExtension.class)
public class MyUtilTest {
	
	@InjectMocks
	private MyUtil myUtil;
	
	/**
	 * 測試每次呼叫{@code Calendar.getInstance()}得到的是否都是不同的實例。
	 * */
	@Test
	public void testCalendarGetInstanceMethodIsMultitonOrNot() {
		Calendar instance = Calendar.getInstance();
		Calendar instance2 = Calendar.getInstance();
		
		Assertions.assertNotSame(instance, instance2);
	}
	
	
	@Test
	public void testGetCurrentTimeIsMultitonOrNot() {
		Date currentDate = myUtil.getCurrentTime();
		Date currentDate2 = myUtil.getCurrentTime();
		
		Assertions.assertNotSame(currentDate, currentDate2);
	}
	
	
}
