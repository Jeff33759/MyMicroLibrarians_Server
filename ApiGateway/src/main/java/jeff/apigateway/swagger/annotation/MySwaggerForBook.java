package jeff.apigateway.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jeff.apigateway.model.dto.send.ActionFailedRes;
import jeff.apigateway.swagger.annotation.MySwaggerForComm.MySwaggerAnnoForAllApi;
import jeff.apigateway.swagger.annotation.MySwaggerForComm.MySwaggerAnnoForProtApi;
import jeff.apigateway.swagger.tmpl.req.CreateAndReplaceBookReqTmpl;
import jeff.apigateway.swagger.tmpl.req.UpdateBookReqTmpl;
import jeff.apigateway.swagger.tmpl.res.BookResTmpl;
import jeff.apigateway.swagger.tmpl.res.BooksPaginationResTmpl;

/**
 * 把有關於Book的自做swagger annotation都做進這裡，統一管理。</p>
 * 自做swagger annotation的目的，在於讓控制器那邊可以不用塞一堆東西，
 * 也較便於維護。
 * */
public interface MySwaggerForBook {
	
	/**
	 * 針對BookController的Swagger Annotation。
	 * */
	@MySwaggerAnnoForAllApi
	@ApiResponses(value = {
			@ApiResponse(
			        responseCode = "503",
			        description = "When the system does not provide book-related services.",
			        content = @Content(
			        		schema = @Schema(
			        				implementation = ActionFailedRes.class)))})
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface MySwaggerForAllBookApi {
		//nothing
	}
	
	/**
	 * 針對QueryAllBooks路徑的Swagger。
	 * */
    @Operation(
            summary = "The api to list books and paginate it.",
            description = "Execute without authentication.",
            parameters = {
            		@Parameter(
            	    		name = "nowPage",
            	    		description = "Current page count, must be an integer and not less than 1.",
            	    		schema = @Schema(
            	    				type = "integer",
            	    				minimum = "1",
            	    				example = "1"),
            	    		in = ParameterIn.QUERY,
            	    		required = true
            	    		),
            		@Parameter(
            	    		name = "pageSize",
            	    		description = "Specifies how many DATA there are on a page, "
            	    				+ "must be an integer between 3 and 20.",
            	    		schema = @Schema(
            	    				type = "integer",
            	    				minimum = "3",
            	    				maximum = "20",
            	    				defaultValue = "10"
            	    				),
            	    		in = ParameterIn.QUERY,
            	    		required = false),
            		@Parameter(
            	    		name = "sortRule",
            	    		description = "Sorting rules for books.",
            	    		schema = @Schema(
            	    				type = "string",
            	    				defaultValue = "asc",
            	    				allowableValues = {"asc","desc"}),
            	    		in = ParameterIn.QUERY,
            	    		required = false)},
            responses =  {
	                @ApiResponse(
	                        responseCode = "200",
	                        description = "A JSON object containing a list of books and other pagination INFOs.",
                    		headers = @Header(
    								name = "server-name",
    								description = "Number of the book-server in book-server-cluster."),
	                        content = @Content(
	                        		schema = @Schema(
	                        				implementation = BooksPaginationResTmpl.class)))})
    @Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface MySwaggerForQueryAllBooks {
    	//nothing
	}
    
    
    /**
     * 針對queryBookById路徑的Swagger。
     * */
    @MySwaggerAnnoForProtApi
    @Operation(
    		summary = "Query a book by book ID.",
    		description = "Requires authentication to execute, and the account authority must at least 'NORMAL'.",
    		parameters = @Parameter(
    				name = "id",
    	    		description = "The ID of book, must contain at least one non-whitespace character..",
    	    		schema = @Schema(
    	    				type = "string",
    	    				example = "2001.008.0075"),
    	    		in = ParameterIn.PATH,
    	    		required = true
    				),
    		responses =  {
    				@ApiResponse(
    						responseCode = "200",
    						description = "A JSON object for a book.",
                    		headers = @Header(
    								name = "server-name",
    								description = "Number of the book-server in book-server-cluster."),
							content = @Content(
	                        		schema = @Schema(
	                        				implementation = BookResTmpl.class))),
    				@ApiResponse(
    						responseCode = "404",
    						description = "When the book ID does not exist in the DB.",
    						content = @Content(
    				        		schema = @Schema(
    				        				implementation = ActionFailedRes.class)))})
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface MySwaggerForQueryBookById {
    	//nothing
    }
    
    /**
     * 針對QueryBooksByCondition路徑的swagger。
     * */
    @MySwaggerAnnoForProtApi
    @Operation(
    		summary = "Query books by conditions.",
    		description = "Requires authentication to execute, and the account authority must at least 'NORMAL'.</p>"
    					+ "If no data is found according to the conditions, still return the status code 200, but the result list of books will be empty.",
	        parameters = {
	        		@Parameter(
	        	    		name = "nowPage",
	        	    		description = "Current page count, must be an integer and not less than 1.",
	        	    		schema = @Schema(
	        	    				type = "integer",
	        	    				minimum = "1",
	        	    				example = "1"),
	        	    		in = ParameterIn.QUERY,
	        	    		required = true
	        	    		),
	        		@Parameter(
	        	    		name = "pageSize",
	        	    		description = "Specifies how many DATA there are on a page, "
	        	    				+ "must be an integer between 3 and 20.",
	        	    		schema = @Schema(
	        	    				type = "integer",
	        	    				minimum = "3",
	        	    				maximum = "20",
	        	    				defaultValue = "10"
	        	    				),
	        	    		in = ParameterIn.QUERY,
	        	    		required = false),
	        		@Parameter(
	        	    		name = "sortRule",
	        	    		description = "Sorting rules for books.",
	        	    		schema = @Schema(
	        	    				type = "string",
	        	    				defaultValue = "asc",
	        	    				allowableValues = {"asc","desc"}),
	        	    		in = ParameterIn.QUERY,
	        	    		required = false),
	        		@Parameter(
	        	    		name = "titleKw",
	        	    		description = "Book title keyword to query.",
	        	    		schema = @Schema(
	        	    				type = "string"),
	        	    		in = ParameterIn.QUERY,
	        	    		required = false),
	        		@Parameter(
	        	    		name = "yearFrom",
	        	    		description = "The starting year of the 'acquiredYear' field range, must not be less than 0.",
	        	    		schema = @Schema(
	        	    				type = "integer",
	        	    				minimum = "0",
	        	    				example = "2005"),
	        	    		in = ParameterIn.QUERY,
	        	    		required = false),
	        		@Parameter(
	        	    		name = "yearTo",
	        	    		description = "The ending year of the 'acquiredYear' field range, must not be less than 0.",
	        	    		schema = @Schema(
	        	    				type = "integer",
	        	    				minimum = "0",
	        	    				example = "2010"),
	        	    		in = ParameterIn.QUERY,
	        	    		required = false)},
    		responses =  {
    				@ApiResponse(
    						responseCode = "200",
    						description = "A JSON object containing a list of books and other pagination INFOs.",
                    		headers = @Header(
    								name = "server-name",
    								description = "Number of the book-server in book-server-cluster."),
    						content = @Content(
	                        		schema = @Schema(
	                        				implementation = BooksPaginationResTmpl.class)))})
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface MySwaggerForQueryBooksByCondition {
    	//nothing
    }
    
    /**
     * 針對新增書本API的swagger。
     * */
    @MySwaggerAnnoForProtApi
	@Operation(
    		summary = "Create a new book.",
    		description = "Requires authentication to execute, and the account authority must at least 'ADVANCED'.",
    		requestBody = @RequestBody(
    				content = @Content(
    						schema = @Schema(
    								implementation = CreateAndReplaceBookReqTmpl.class
    								)
    						),
    				required = true),
    		responses =  {
    				@ApiResponse(
    						responseCode = "201",
    						description = "A JSON object for created book.",
    						headers = {
    								@Header(
    								name = "server-name",
    								description = "Number of the book-server in book-server-cluster."),
    								@Header(
    	    								name = "location",
    	    								description = "The URL of the book that has been created, can access it to find this book.")},
    						content = @Content(
    								schema = @Schema(
    										implementation = BookResTmpl.class)))})
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface MySwaggerForAddNewBook {
    	//nothing
    }
    
    
    /**
     * 針對取代書本API的swagger。
     * */
    @MySwaggerAnnoForProtApi
    @Operation(
    		summary = "Replace a book.",
    		description = "Requires authentication to execute, and the account authority must at least 'ADVANCED'.",
    		requestBody = @RequestBody(
    				content = @Content(
    						schema = @Schema(
    								implementation = CreateAndReplaceBookReqTmpl.class
    								)
    						),
    				required = true),
    		responses =  {
    				@ApiResponse(
    						responseCode = "200",
    						description = "A JSON object for replaced book.",
    						headers = {
    								@Header(
    										name = "server-name",
    										description = "Number of the book-server in book-server-cluster."),
    								@Header(
    	    								name = "location",
    	    								description = "The URL of the book that has been replaced, can access it to find this book.")},
    						content = @Content(
    								schema = @Schema(
    										implementation = BookResTmpl.class)))})
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface MySwaggerForReplaceBook {
    	//nothing
    }
    
    /**
     * 針對更新書本API的swagger。
     * */
    @MySwaggerAnnoForProtApi
    @Operation(
    		summary = "Update a book.",
    		description = "Requires authentication to execute, and the account authority must at least 'ADVANCED'.</p>"
    					+ "All fields in body are optional, because the old data will be used for the fields that are not entered.",
    		requestBody = @RequestBody(
    				description = "The request body is required, even if it is an empty JSON.",
    				content = @Content(
    						schema = @Schema(
    								implementation = UpdateBookReqTmpl.class
    								)
    						),
    				required = true),
    		parameters = @Parameter(
    				name = "id",
    	    		description = "The ID of book, must contain at least one non-whitespace character.",
    	    		schema = @Schema(
    	    				type = "string",
    	    				example = "2001.008.0075"),
    	    		in = ParameterIn.PATH,
    	    		required = true),
    		responses =  {
    				@ApiResponse(
    						responseCode = "200",
    						description = "A JSON object for updated book.",
    						headers = {
    								@Header(
    										name = "server-name",
    										description = "Number of the book-server in book-server-cluster."),
    								@Header(
    	    								name = "location",
    	    								description = "The URL of the book that has been updated, can access it to find this book.")},
    						content = @Content(
    								schema = @Schema(
    										implementation = BookResTmpl.class)))})
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface MySwaggerForUpdateBook {
    	//nothing
    }
    
    
    /**
     * 針對刪除書本API的swagger。
     * */
    @MySwaggerAnnoForProtApi
    @Operation(
    		summary = "Delete a book.",
    		description = "Requires authentication to execute, and the account authority must at least 'ADVANCED'.",
    		parameters = @Parameter(
    				name = "id",
    	    		description = "The ID of book, must contain at least one non-whitespace character.",
    	    		schema = @Schema(
    	    				type = "string"),
    	    		in = ParameterIn.PATH,
    	    		required = true),
    		responses =  {
    				@ApiResponse(
    						responseCode = "204",
    						description = "No content.",
    						headers = {
    								@Header(
    										name = "server-name",
    										description = "Number of the book-server in book-server-cluster.")},
    						content = @Content())})
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface MySwaggerForDeleteBook {
    	//nothing
    }
    

}
