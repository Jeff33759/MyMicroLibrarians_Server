package jeff.book2.aop;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mongodb.MongoException;

import jeff.book2.common.constants.CustomMongoErrorCode;
import jeff.book2.controller.BookController;
import jeff.book2.model.dto.send.ActionFailedRes;

/**
 * 切面程式，攔截{@link BookController}所發生的例外，將其處理成期望的回應格式。</p>
 * 這裡沒有攔截到的，就會交給dispatcherServlet處理例外回應。
 * 
 * @author Jeff Huang
 * */
@RestControllerAdvice(basePackageClasses = {BookController.class})
public class BookControllerExResAspect {
	
	/**
	 * 攔截有關於參數對接的型別驗證及@Valid驗證時發生的例外。
	 * */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ActionFailedRes handleDataDockingException(BindException ex, HttpServletRequest request) {
    	FieldError fieldError = ex.getFieldError();
    	String errCause = fieldError.getCode();
    	ActionFailedRes res = new ActionFailedRes();
    	res.setPath(request.getRequestURI());
    	res.setMethod(request.getMethod());
    	if(errCause.equals("typeMismatch")) {
//    		當參數型別匹配錯誤
    		res.setMsg(String.format("The type of query parameter '%s' is wrong. "
    				+ "Consider changing it to boolean or numeric.", fieldError.getField()));
    	}else {
//    		來自DTO裡面欄位驗證的message
    		res.setMsg(fieldError.getDefaultMessage());
    	}
        return res;
    }
    
    
    /**
     * 攔截有關於{@link @RequestBody}參數對接時，客戶端使用了錯誤的JSON格式時發生的例外。</p>
     * */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ActionFailedRes handleDataDockingException2(HttpMessageNotReadableException ex, 
    		HttpServletRequest request) {
    	ActionFailedRes res = new ActionFailedRes();
    	res.setPath(request.getRequestURI());
    	res.setMethod(request.getMethod());
    	res.setMsg("The error occurred when parsing JSON in book server, "
    			+ "please check if your JSON format are as expected.");
    	return res;
    }
    
    
	/**
	 * 攔截有關於{@link @RequestBody}參數對接，使用@Validated驗證時發生的例外。
	 * 客戶端的JSON格式對，但輸入了我們不想被輸入的值。</p>
	 * 捕捉所有沒驗證過的message(可能有複數)，組成錯誤訊息模板。
	 * */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ActionFailedRes handleDataDockingException3(ConstraintViolationException ex, 
    		HttpServletRequest request) {
    	String errMsgTemplate = "The path variable validation failed. ";
    	Set<ConstraintViolation<?>> set = ex.getConstraintViolations();
    	for(ConstraintViolation<?> v : set) {
    		errMsgTemplate += v.getMessage();
    	}
    	ActionFailedRes res = new ActionFailedRes();
    	res.setPath(request.getRequestURI());
    	res.setMethod(request.getMethod());
    	res.setMsg(errMsgTemplate);
        return res;
    }
    

	/**
	 * 攔截與MongoDB互動(CRUD)時所發生的例外。</p>
	 * JPA會把資料庫的例外包裝成{@link DataAccessException}，
	 * 所以這裡先{@code getRootCause()}的到最原始的Exception後，
	 * 再強轉成{@link MongoException}，然後就可以呼叫方法得到MongoDB的錯誤碼去進行例外處理，
	 * 一些MongoDB原生錯誤碼沒有對應的情況，就自定義狀態碼。</p>
	 * 
	 * @see MongoException的子項目: </p>
	 * 		<a>https://mongodb.github.io/mongo-java-driver/3.6/javadoc/com/mongodb/MongoException.html</a>
	 * @see MongoDB的錯誤碼列表:</p> 
	 * 		<a>https://docs.rs/mongodb/0.1.6/src/mongodb/.cargo/registry/src/github.com-1ecc6299db9ec823/mongodb-0.1.6/src/error.rs.html</a>
	 * */
    @ExceptionHandler(value = DataAccessException.class)
    public ActionFailedRes handleMongoOperationException(DataAccessException ex, HttpServletRequest request,
    		HttpServletResponse response) {
    	MongoException mongoE = (MongoException)ex.getRootCause();
    	int mongoErrcode = mongoE.getCode();
    	ActionFailedRes res = new ActionFailedRes();
    	res.setPath(request.getRequestURI());
    	res.setMethod(request.getMethod());
    	if(mongoErrcode==11000) {
//    		當新增時，Book ID已經重複
    		res.setMsg("Duplicate primary key already exists in database. "
    				+ "It may be caused by someone doing the same operation at the same time as you, "
    				+ "please try again during off-peak hours.");
    		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    	}else if(mongoErrcode == CustomMongoErrorCode.QUERY_NOT_FOUND.errCode()){
//    		當查詢單本書時，客戶端所傳的id並不存在於資料庫。
    		res.setMsg(mongoE.getMessage());
    		response.setStatus(HttpStatus.NOT_FOUND.value());
    	}else if(mongoErrcode == CustomMongoErrorCode.UPDATE_NOT_FOUND.errCode()){
//    		當更新或取代時，客戶端所傳的id並不存在於資料庫。
    		res.setMsg(mongoE.getMessage());
    		response.setStatus(HttpStatus.BAD_REQUEST.value());
    	}else if(mongoErrcode == CustomMongoErrorCode.DELETE_NOT_FOUND.errCode()) {
//    		當刪除時，客戶端所傳的id並不存在於資料庫。
    		res.setMsg(mongoE.getMessage());
    		response.setStatus(HttpStatus.BAD_REQUEST.value());
    	}else {
//    		其他跟資料庫有關的未預期的錯誤
    		res.setMsg("Some errors occurred while performing database operations.");
    		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    	}
        return res;
    }
    
    /**
     * 發生了其他無法預期的例外。
     * */
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ActionFailedRes handleException(
    		Exception e, HttpServletRequest req,
    			HttpServletResponse res) {
    	ActionFailedRes resObj = new ActionFailedRes();
    	resObj.setMethod(req.getMethod());
    	resObj.setPath(req.getRequestURI());
    	resObj.setMsg("An unexpected error occurs in the Book server, "
    			+ "please contact the webmaster.");
    	return resObj;
    }
	
}
