package jeff.book.model.dto.qparam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

public class MyBookQueryParam {
	
	/**
	 * 書名關鍵字
	 * */
	private String titleKw;

	/**
	 * 入館年分區間起始(西元年)，須為正整數
	 * */
	@PositiveOrZero(message = "The query param 'yearFrom' must be number and must not be less than 0.")
    private Integer yearFrom;
    
	/**
	 * 入館年分區間終點(西元年)，須為正整數
	 * */
	@PositiveOrZero(message = "The query param 'yearTo' must be number and must not be less than 0.")
    private Integer yearTo;
	
    /**
     * 依照Identifier降冪或升冪
     * */
    @Pattern(regexp = "asc|desc",message = "The query param 'sortRule' must be 'asc' or 'desc'.")
    private String sortRule;

    /**
     * 一頁有幾筆資料
     * */
    @Min(value = 3,message="The query param 'pageSize' must not be less than 3.")
    @Max(value = 20,message="The query param 'pageSize' must not be greater than 20.")
    private Integer pageSize;

    /**
     * 目前在第幾頁，值一定要為數字且至少為1。
     * */
    @NotNull(message="The query param 'nowPage' must not be null.")
    @Min(value = 1,message="The query param 'nowPage' must not be less than 1.")
    private Integer nowPage;

	public String getTitleKw() {
		return titleKw;
	}

	public void setTitleKw(String titleKw) {
		this.titleKw = titleKw;
	}




	public Integer getYearFrom() {
		return yearFrom;
	}

	public void setYearFrom(Integer yearFrom) {
		this.yearFrom = yearFrom;
	}

	public Integer getYearTo() {
		return yearTo;
	}

	public void setYearTo(Integer yearTo) {
		this.yearTo = yearTo;
	}

	public String getSortRule() {
		return sortRule;
	}

	public void setSortRule(String sortRule) {
		this.sortRule = sortRule;
	}


	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getNowPage() {
		return nowPage;
	}

	public void setNowPage(Integer nowPage) {
		this.nowPage = nowPage;
	}

	
}
