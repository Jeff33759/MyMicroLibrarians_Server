package jeff.book.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import jeff.book.common.util.MyUtil;
import jeff.book.model.po.MyBook;

@Repository
public interface BookRepository extends MongoRepository<MyBook, String>{
	
	/**
	 * 注意:Between為A年份最末~B年份最初，假設若設2004~2005，會查不出東西；
	 * 2004~2006，只會出現2005。</p>
	 * 為了涵蓋頭尾，會將使用者輸入的年份，頭-1，尾+1，例如使用者輸入2004~2005，
	 * 那就會將請求物件變成2003~2006，這樣執行此DAO，就會查出2004~2005了。
	 * 
	 * @see MyUtil #setDefaultValueForParam
	 * */
	public Page<MyBook> findByMainTitleContainingIgnoreCaseAndAcquiredYearBetween(
			String titleKw,Integer yearFrom,Integer yearTo,Pageable pageable);
	
}
