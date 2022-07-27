package jeff.book2.model.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "book")
public class MyBook {

	@Id
	private String id;
	
	@Field("main_title")
	private String mainTitle;
	
	private String creator;
	
	@Field("created_year")
	private String createdYear;
	
	private String type;
	
	private String owner;
	
	@Field("acquired_year")
	private Integer acquiredYear;
	
	@Field("original_url")
	private String originalUrl;
	
	@Field("owner_collections_website")
	private String ownerCollectionsWebsite;
	
	@Field("owner_website")
	private String ownerWebsite;
	
	private String description;
	
	@Field("image_url")
	private String imageUrl;
	
	private boolean available;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getCreatedYear() {
		return createdYear;
	}

	public void setCreatedYear(String createdYear) {
		this.createdYear = createdYear;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	
}
