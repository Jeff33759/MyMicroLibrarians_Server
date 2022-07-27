package jeff.apigateway.swagger.tmpl.res;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 一本書的回應主體模板，用於swagger。
 * 
 * @author Jeff Huang
 * */
public class BookResTmpl {
	
	@Schema(description = "Book ID.")
	public String id;

	@Schema(description = "Book title.")
	public String mainTitle;
	
	@Schema(description = "Book creator.")
	public String creator;
	
	@Schema(description = "Year of creation of the book.")
	public String createdYear;
	
	@Schema(description = "Book type.")
	public String type;
	
	@Schema(description = "Which museum has this book.")
	public String owner;
	
	@Schema(description = "In what year did the museum acquire the book.")
	public Integer acquiredYear;
	
	@Schema(description = "Original Url.")
	public String originalUrl;
	
	@Schema(description = "Owner collections website.")
	public String ownerCollectionsWebsite;
	
	@Schema(description = "Owner website.")
	public String ownerWebsite;
	
	@Schema(description = "Description of the book.")
	public String description;
	
	@Schema(description = "Image Url of the book.")
	public String imageUrl;
	
	@Schema(description = "Whether the book is currently available for loan.")
	public boolean available;
	
}