package jeff.apigateway.swagger.tmpl.req;


import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 一本書的請求主體模板，用於swagger。
 * 
 * @author Jeff Huang
 * */
public class CreateAndReplaceBookReqTmpl {
	
	@Schema(description = "Book title, must contain at least one non-whitespace character.",
			example = "My test book.",
			required = true)
	public String mainTitle;
	
	@Schema(description = "Book creator.",
			example = "Jeff",
			required = false)
	public String creator;
	
	@Schema(description = "Year of creation of the book. "
			+ "If this field has value, it must be a number and must not be less than 0."
			+ "If has no value, it will default to empty which means createdYear of book is unknown.",
			pattern = "^\\d+$",
			type = "string",
			example = "2000",
			required = false)
	public String createdYear;
	
	@Schema(description = "Book type, must contain at least one non-whitespace character.",
			example = "test",
			required = true)
	public String type;
	
	@Schema(description = "Which museum has this book.",
			example = "Jeff's room.",
			required = false)
	private String owner;
	
	@Schema(description = "In what year did the museum acquire the book.",
			minimum = "0",
			type = "integer",
			example = "2022",
			required = false)
	public Integer acquiredYear;
	
	@Schema(description = "Original Url.",
			required = false)
	public String originalUrl;
	
	@Schema(description = "Owner collections website.",
			required = false)
	public String ownerCollectionsWebsite;
	
	@Schema(description = "Owner website.",
			example = "https://github.com/Jeff33759",
			required = false)
	public String ownerWebsite;
	
	@Schema(description = "Description of the book.",
			example = "The book used to test.",
			required = false)
	public String description;
	
	@Schema(description = "Image Url of the book.",
			required = false)
	public String imageUrl;
	
	@Schema(description = "Whether the book is currently available for loan.",
			defaultValue = "true",
			required = false)
	public boolean available;

	
}
