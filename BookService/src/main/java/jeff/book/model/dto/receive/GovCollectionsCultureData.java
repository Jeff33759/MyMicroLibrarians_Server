package jeff.book.model.dto.receive;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 向政府資料平台發出請求後，用以承接資料的DTO。
 * */
public class GovCollectionsCultureData {
	
	@JsonProperty
	private String Identifier;
	
	@JsonProperty
	private String MainTitle;
	
	@JsonProperty
	private String Creator;
	
	@JsonProperty
	private String CreatedYear;
	
	@JsonProperty
	private String Size;
	
	@JsonProperty
	private String Type;
	
	@JsonProperty
	private String Owner;
	
	@JsonProperty
	private String AcquiredDate;
	
	@JsonProperty
	private String OriginalUrl;
	
	@JsonProperty
	private String OwnerCollectionsWebsite;
	
	@JsonProperty
	private String OwnerWebsite;
	
	@JsonProperty
	private String ApplicationGuide;
	
	@JsonProperty
	private String Description;
	
	@JsonProperty
	private String ImageUrl;

	public String getIdentifier() {
		return Identifier;
	}

	public void setIdentifier(String identifier) {
		Identifier = identifier;
	}

	public String getMainTitle() {
		return MainTitle;
	}

	public void setMainTitle(String mainTitle) {
		MainTitle = mainTitle;
	}

	public String getCreator() {
		return Creator;
	}

	public void setCreator(String creator) {
		Creator = creator;
	}

	public String getCreatedYear() {
		return CreatedYear;
	}

	public void setCreatedYear(String createdYear) {
		CreatedYear = createdYear;
	}

	public String getSize() {
		return Size;
	}

	public void setSize(String size) {
		Size = size;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getOwner() {
		return Owner;
	}

	public void setOwner(String owner) {
		Owner = owner;
	}

	public String getAcquiredDate() {
		return AcquiredDate;
	}

	public void setAcquiredDate(String acquiredDate) {
		AcquiredDate = acquiredDate;
	}

	public String getOriginalUrl() {
		return OriginalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		OriginalUrl = originalUrl;
	}

	public String getOwnerCollectionsWebsite() {
		return OwnerCollectionsWebsite;
	}

	public void setOwnerCollectionsWebsite(String ownerCollectionsWebsite) {
		OwnerCollectionsWebsite = ownerCollectionsWebsite;
	}

	public String getOwnerWebsite() {
		return OwnerWebsite;
	}

	public void setOwnerWebsite(String ownerWebsite) {
		OwnerWebsite = ownerWebsite;
	}

	public String getApplicationGuide() {
		return ApplicationGuide;
	}

	public void setApplicationGuide(String applicationGuide) {
		ApplicationGuide = applicationGuide;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getImageUrl() {
		return ImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}

}
