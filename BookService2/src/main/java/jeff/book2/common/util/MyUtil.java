package jeff.book2.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import jeff.book2.common.constants.BookQueryConst;
import jeff.book2.dao.BookRepository;
import jeff.book2.exception.UpdatingBookDBException;
import jeff.book2.model.dto.qparam.MyBookQueryParam;
import jeff.book2.model.dto.receive.GovCollectionsCultureData;
import jeff.book2.model.dto.receive.MyBookPatchReq;
import jeff.book2.model.dto.receive.MyBookReq;
import jeff.book2.model.dto.send.BookQueryRes;
import jeff.book2.model.po.MyBook;

@Component
public class MyUtil {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	/**
	 * 得到目前西元年的數字。
	 * */
	public Integer getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	
	/**
	 * 建立分頁機制。</p>
	 * 
	 * @param orderBy - 排序的依據欄位
	 * @param sortRule - 排序要升冪還降冪
	 * @param pageSize - 一頁幾筆資料
	 * @param nowPage - 目前在第幾頁
	 * */
	public PageRequest genMyPaginationStrategy(String orderBy,String sortRule,
			Integer pageSize,Integer nowPage) {
		Sort sorting = 
				genSortingStrategy(orderBy, sortRule);
//		客戶端頁數從1開始算，JPA分頁的第一頁則是0。
		return PageRequest.of(nowPage-1,pageSize,sorting);
	}
	
	
	/**
	 * 若orderBy與sortRule都有東西，就回傳有排序規則的Sort物件；</p>
	 * 若非，則回傳無排序規則的Sort物件。
	 * 
	 * @throws Exception 若建置物件途中發生錯誤，則拋出。
	 * */
    private Sort genSortingStrategy(String orderBy, String sortRule) {
//    	無排序規則的Sort物件
        Sort sort = Sort.unsorted();
        if (Objects.nonNull(orderBy) && Objects.nonNull(sortRule)) {
            Sort.Direction direction = Sort.Direction.fromString(sortRule);
//         	有排序規則的Sort物件
            sort = Sort.by(direction, orderBy);
        }
        return sort;
    }
    
    /**
     * 從政府資料平台撈取資料。
     * 
     * @throws Exception - 
     * 		   若從平台上撈取資料並包裝成java陣列的過程失敗，則拋出。
     * */
    public List<MyBook> getDataFromGovSite() throws UpdatingBookDBException {
    	try {
    		String url = 
    				"https://collections.culture.tw/getMetadataList.aspx?format=OpenData&TYPEID=B0101";
    		GovCollectionsCultureData[] dataList = 
    				restTemplate.getForObject(url, GovCollectionsCultureData[].class);
    		List<MyBook> expectedDataList =
    				handleTheDataToExpectedFormat(dataList);
    		return expectedDataList;
    	}catch(Exception e) {
    		throw new UpdatingBookDBException(
    				"The error occurred when getting data from Gov site.",e);
    	}
    }
    
    /**
     * 將從政府資料平台撈來的資料，處理成自己想要的格式。
     * */
    private List<MyBook> handleTheDataToExpectedFormat(GovCollectionsCultureData[] dataList) 
    		throws Exception{
    	List<MyBook> myBookList = new ArrayList<MyBook>();
    	for(GovCollectionsCultureData data : dataList) {
    		MyBook myBook = new MyBook();
    		Integer beHandledAcquiredYear = 
    				setAcquiredYearFieldAsDefaultValueByBookId(
    						data.getIdentifier(),data.getAcquiredDate());
    		myBook.setAcquiredYear(beHandledAcquiredYear);
    		myBook.setCreatedYear(data.getCreatedYear());
    		myBook.setCreator(data.getCreator());
    		myBook.setDescription(data.getDescription());
    		myBook.setId(data.getIdentifier());
    		myBook.setImageUrl(data.getImageUrl());
    		myBook.setMainTitle(data.getMainTitle());
    		myBook.setOriginalUrl(data.getOriginalUrl());
    		myBook.setOwner(data.getOwner());
    		myBook.setOwnerCollectionsWebsite(data.getOwnerCollectionsWebsite());
    		myBook.setOwnerWebsite(data.getOwnerWebsite());
    		myBook.setType(data.getType());
    		myBook.setAvailable(true);
    		myBookList.add(myBook);
    	}
    	return myBookList;
    }
    
    /**
     * 若acquiredYear為空字串，就取id第一個位數，並將acquiredYear轉換為數字。
     * */
    private Integer setAcquiredYearFieldAsDefaultValueByBookId(String id, String acquiredYear) {
    	try {
    		return Integer.valueOf(acquiredYear.equals("") ? 
    				id.split("\\.",2)[0] : acquiredYear);
//    	萬一遇到此欄位不是數字的情況，回傳0。
    	}catch(NumberFormatException e){
    		return 0;
    	}
    }
    
    /**
     * 將{@link MyBookReq}轉換成{@link MyBook}，並設置預設值。</p>
     * 
     * @param bookId - 若想指定{@link MyBook #id}，就傳非空字串；
     * 				   否則傳null，將以日期時間作為{@link MyBook #id}。
     * */
    public MyBook ConvertMyBookReqToMyBookAndSetDefault(MyBookReq myBookReq,String bookId) {
    	MyBook myBook = new MyBook();
    	myBook.setMainTitle(myBookReq.getMainTitle());
    	myBook.setType(myBookReq.getType());
    	myBook.setAvailable(myBookReq.isAvailable());

    	myBook.setId(
    			bookId == null ? genBookIdByCurrentTime() 
    					: bookId);
    	myBook.setAcquiredYear(
    			Optional.ofNullable(myBookReq.getAcquiredYear())
    				.orElse(getCurrentYear()));
    	myBook.setCreatedYear(
    			Optional.ofNullable(myBookReq.getCreatedYear())
					.orElse(""));
    	myBook.setCreator(
    			Optional.ofNullable(myBookReq.getCreator())
					.orElse(""));
    	myBook.setDescription(
    			Optional.ofNullable(myBookReq.getDescription())
					.orElse(""));
    	myBook.setImageUrl(
    			Optional.ofNullable(myBookReq.getImageUrl())
					.orElse(""));
    	myBook.setOriginalUrl(
    			Optional.ofNullable(myBookReq.getOriginalUrl())
					.orElse(""));
    	myBook.setOwner(
    			Optional.ofNullable(myBookReq.getOwner())
					.orElse(""));
    	myBook.setOwnerWebsite(
    			Optional.ofNullable(myBookReq.getOwnerWebsite())
					.orElse(""));
    	myBook.setOwnerCollectionsWebsite(
    			Optional.ofNullable(myBookReq.getOwnerCollectionsWebsite())
					.orElse(""));
    	return myBook;
    }
    
    private String genBookIdByCurrentTime() {
		Calendar instance = Calendar.getInstance();
		int y = instance.get(Calendar.YEAR);
//		Calendar.MONTH取出來的月份從0開始算，所以+1
		int m = instance.get(Calendar.MONTH)+1;
		int d = instance.get(Calendar.DAY_OF_MONTH);
		int h = instance.get(Calendar.HOUR_OF_DAY);
		int ms = instance.get(Calendar.MILLISECOND);
		return String.format("%d.%d.%d.%d.%d",y,m,d,h,ms);
    }
    
    
    /**
     * 將{@link MyBookReq}中的資訊，更新入{@link MyBook}。</p>
     * 
     * @param newBook - 欲更新的新資訊。
     * @param oldBook - Query出來的舊書。
     * */
    public void LoadMyBookPatchReqDataIntoOldMyBook(MyBookPatchReq newBook, MyBook oldBook) {
    	if(newBook.getAcquiredYear() != null) {
    		oldBook.setAcquiredYear(newBook.getAcquiredYear());
    	}
    	if(newBook.getCreatedYear() != null) {
    		oldBook.setCreatedYear(newBook.getCreatedYear());
    	}
    	if(newBook.getCreator() != null) {
    		oldBook.setCreator(newBook.getCreator());
    	}
    	if(newBook.getDescription() != null) {
    		oldBook.setDescription(newBook.getDescription());
    	}
    	if(newBook.getImageUrl() != null) {
    		oldBook.setImageUrl(newBook.getImageUrl());
    	}
    	if(newBook.getMainTitle() != null) {
    		oldBook.setMainTitle(newBook.getMainTitle());
    	}
    	if(newBook.getOriginalUrl() != null) {
    		oldBook.setOriginalUrl(newBook.getOriginalUrl());
    	}
    	if(newBook.getOwner() != null) {
    		oldBook.setOwner(newBook.getOwner());
    	}
    	if(newBook.getOwnerCollectionsWebsite() != null) {
    		oldBook.setOwnerCollectionsWebsite(newBook.getOwnerCollectionsWebsite());
    	}
    	if(newBook.getOwnerWebsite() != null) {
    		oldBook.setOwnerWebsite(newBook.getOwnerWebsite());
    	}
    	if(newBook.getType() != null) {
    		oldBook.setType(newBook.getType());
    	}
    	if(newBook.getAvailable() != null) {
    		oldBook.setAvailable(newBook.getAvailable());
    	}
    }
    
    /**
     * 為參數物件建立預設值。</p>
     * 入館年份若使用者沒輸入，就預設0~最新年份，但因為JQPL的Between
     * 不含頭和尾，所以這裡把頭和尾-1與+1，讓Between可以查到頭和尾的年份。
     * 
     * @see BookRepository #findByMainTitleContainingIgnoreCaseAndAcquiredYearBetween
     * */
    public void setDefaultValueForParam(MyBookQueryParam param) {
    	if(param.getTitleKw() == null) {
    		param.setTitleKw("");
    	}
    	if(param.getYearFrom() == null) {
    		param.setYearFrom(-1);
    	}else {
    		param.setYearFrom(param.getYearFrom()-1);
    	}
    	if(param.getYearTo() == null) {
    		param.setYearTo(getCurrentYear()+1);
    	}else {
    		param.setYearTo(param.getYearTo()+1);
    	}
    	if(param.getSortRule() == null) {
    		param.setSortRule(BookQueryConst.DEFAULT_SORTING_RULE);
    	}
    	if(param.getPageSize() == null) {
    		param.setPageSize(BookQueryConst.DEFAULT_PAGE_SIZE);
    	}
    }
    
    /**
     * 把mongoDBRepo分頁查詢API吐出的PAGE物件，精簡成想要的回應格式。
     * */
    public BookQueryRes tranformPageResultToExpectedResFormat(Page<MyBook> pageResult) {
    	BookQueryRes res = new BookQueryRes();
		res.setResult(pageResult.getContent());
//		mongoDBRepo的分頁查詢API計算頁數從0開始，這裡加1迎合客戶端
		res.setNowPage(pageResult.getNumber()+1);
		res.setPageSize(pageResult.getSize());
		res.setTotalPages(pageResult.getTotalPages());
		res.setTotalElements(pageResult.getTotalElements());
		return res;
    }
    
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
    
}
