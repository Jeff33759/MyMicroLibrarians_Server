package jeff.book2.model.dto.receive;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

import jeff.book2.model.po.MyBook;

/**
 * Book patch請求對接用物件，與{@link MyBook}解耦。
 * */
public class MyBookPatchReq {
	
	/**
	 * mainTitle若有輸入，必須至少要有一個非空白字元。
	 * */
	@Pattern(regexp = "^(?=\\s*\\S).*$",
			message = "The param 'mainTitle' must contain at least one non-whitespace character.")
	private String mainTitle;
	
	private String creator;
	
	/**
	 * 創作年份若有給值，需為數字且為含0在內的正整數。</p>
	 * 因為來自政府資料平台的資料，有部分資料此欄位為空字串，
	 * 所以迎合之，設計為String而非Intrger。
	 * */
	@PositiveOrZero(message = "The param 'createdYear' must be number "
			+ "and must not be less than 0.")
	private String createdYear;
	
	/**
	 * type若有輸入，必須至少要有一個非空白字元。
	 * */
	@Pattern(regexp = "^(?=\\s*\\S).*$",
			message = "The param 'type' must contain at least one non-whitespace character.")
	private String type;
	
	private String owner;
	
	/**
	 * 入館年份若有給值，需為數字且為含0在內的正整數。</p>
	 * */
	@PositiveOrZero(message = "The param 'acquiredYear' must be number and must not be less than 0.")
	private Integer acquiredYear;
	
	private String originalUrl;
	
	private String ownerCollectionsWebsite;
	
	private String ownerWebsite;
	
	private String description;
	
	private String imageUrl;
	
	/**
	 * 型態為物件的{@link Boolean}，可以對null做一些判斷。</p>
	 * 當使用者沒有傳這個值(為null)，想要保留舊值。
	 * */
	private Boolean available;
	
	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatedYear() {
		return createdYear;
	}

	public void setCreatedYear(String createdYear) {
		this.createdYear = createdYear;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Integer getAcquiredYear() {
		return acquiredYear;
	}

	public void setAcquiredYear(Integer acquiredYear) {
		this.acquiredYear = acquiredYear;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getOwnerCollectionsWebsite() {
		return ownerCollectionsWebsite;
	}

	public void setOwnerCollectionsWebsite(String ownerCollectionsWebsite) {
		this.ownerCollectionsWebsite = ownerCollectionsWebsite;
	}

	public String getOwnerWebsite() {
		return ownerWebsite;
	}

	public void setOwnerWebsite(String ownerWebsite) {
		this.ownerWebsite = ownerWebsite;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
}
