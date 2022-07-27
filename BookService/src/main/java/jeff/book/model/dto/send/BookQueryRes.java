package jeff.book.model.dto.send;

import java.util.List;

import jeff.book.model.po.MyBook;

public class BookQueryRes {
	
	private List<MyBook> result;

	private int nowPage;

	private int totalPages;

	private long totalElements;

	private int pageSize;

	public List<MyBook> getResult() {
		return result;
	}

	public void setResult(List<MyBook> result) {
		this.result = result;
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


}
