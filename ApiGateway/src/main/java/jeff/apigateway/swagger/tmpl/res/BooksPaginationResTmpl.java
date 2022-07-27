package jeff.apigateway.swagger.tmpl.res;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * book的分頁responseBody模板，用以設定Swagger。
 * 
 * @author Jeff HUang
 * */
public class BooksPaginationResTmpl {

	@Schema(description = "Result list of books.")
	public List<BookResTmpl> result = 
			new ArrayList<BookResTmpl>();
	
	@Schema(description = "Current page number.")
	public Integer nowPage;

	@Schema(description = "Total pages.")
	public Integer totalPages;
	
	@Schema(description = "How many books in total.")
	public Integer totalElements;
	
	@Schema(description = "How many  book data the result list has on a page.")
	public Integer pageSize;
	
}
