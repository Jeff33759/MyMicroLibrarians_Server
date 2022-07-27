package jeff.apigateway.swagger.tmpl.req;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 更新本書的主體模板，用於swagger。
 * 
 * @author Jeff Huang
 * */
public class UpdateBookReqTmpl {
	
	@Schema(description = "Book title, must contain at least one non-whitespace character.",
			example = "My test book ver2.",
			required = false)
	public String mainTitle;
	
	@Schema(description = "Book creator.",
			required = false)
	public String creator;
	
	@Schema(description = "Year of creation of the book.",
			minimum = "0",
			type = "integer",
			required = false)
	public String createdYear;
	
	@Schema(description = "Book type, must contain at least one non-whitespace character.",
			required = false)
	public String type;
	
	@Schema(description = "Which museum has this book.",
			required = false)
	private String owner;
	
	@Schema(description = "In what year did the museum acquire the book.",
			minimum = "0",
			type = "integer",
			required = false)
	public Integer acquiredYear;
	
	@Schema(description = "Original Url.",
			required = false)
	public String originalUrl;
	
	@Schema(description = "Owner collections website.",
			required = false)
	public String ownerCollectionsWebsite;
	
	@Schema(description = "Owner website.",
			required = false)
	public String ownerWebsite;
	
	@Schema(description = "Description of the book.",
			required = false)
	public String description;
	
	@Schema(description = "Image Url of the book.",
			required = false)
	public String imageUrl;
	
	@Schema(description = "Whether the book is currently available for loan.",
			required = false)
	public Boolean available;

}
