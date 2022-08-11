package jeff.book.integration;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jeff.book.dao.BookRepository;
import jeff.book.filter.SourceAuthenticationFilter;
import jeff.book.model.po.MyBook;
import jeff.book.AppInitRunner;

/**
 * 針對Book各API整合測試用的基底，把一些所有測試的公同方法參數寫在這裡。
 * </p>
 * 測試環境的{@link SourceAuthenticationFilter}和{@link AppInitRunner}
 * 不會註冊為Spring Bean，所以也不會執行；此外測試用的DB也與正式環境不同。
 * 
 * @author Jeff Huang
 * */
@AutoConfigureMockMvc
@SpringBootTest
public class BookApiTestBase {

	@Autowired
	protected BookRepository bookRepo;
	
    @Autowired
    protected MockMvc mockMvc;
    

    /**
     * 模擬客戶端發給BookServer的請求的標頭，
     * 於{@link #preprocessingForEachTestCase}進行初始化。
     * */
    protected HttpHeaders mockHttpHeaders;
	
    
	/**
	 * 每一次測試方法在執行前，先執行一次本方法。
	 * 準備每個測試案例都要的參數元件，因為測試案例之間不應該互相共用
	 * 元件，所以用@BeforeEach，每個測試案例都會new一個自己用的元件。
	 **/
	@BeforeEach
	protected void preprocessingForEachTestCase() {
		mockHttpHeaders = new HttpHeaders();
		mockHttpHeaders.add(HttpHeaders.CONTENT_TYPE, 
				MediaType.APPLICATION_JSON_VALUE);
		addBooksIntoDBForDemo();
    }

	/**
	 * 每一次測試方法在執行後，初始化資料(例如清空測試DB)，
	 * 確保下一個測試方法不會受到前一個測試方法的影響。
	 **/
	@AfterEach
	protected void postprocessingForEachTestCase() {
		bookRepo.deleteAll();
    }
	
	
	/**
	 * 加入一些書本資料，提供DEMO用。
	 * */
	protected void addBooksIntoDBForDemo() {
		MyBook myBook = new MyBook();
		myBook.setId("test1");
		myBook.setAcquiredYear(2022);
		myBook.setAvailable(true);
		myBook.setCreatedYear("2021");
		myBook.setCreator("Jeff");
		myBook.setDescription("Book for test.");
		myBook.setImageUrl("");
		myBook.setMainTitle("台灣史1");
		myBook.setOriginalUrl("");
		myBook.setOwner("Jeff's room.");
		myBook.setOwnerCollectionsWebsite("");
		myBook.setOwnerWebsite("");
		myBook.setType("test");
		
		MyBook myBook2 = new MyBook();
		myBook2.setId("test2");
		myBook2.setAcquiredYear(2018);
		myBook2.setAvailable(false);
		myBook2.setCreatedYear("2015");
		myBook2.setCreator("Jeff");
		myBook2.setDescription("Book for test.");
		myBook2.setImageUrl("");
		myBook2.setMainTitle("台灣史2");
		myBook2.setOriginalUrl("");
		myBook2.setOwner("Jeff's room.");
		myBook2.setOwnerCollectionsWebsite("");
		myBook2.setOwnerWebsite("");
		myBook2.setType("test");

		MyBook myBook3 = new MyBook();
		myBook3.setId("test3");
		myBook3.setAcquiredYear(2020);
		myBook3.setAvailable(false);
		myBook3.setCreatedYear("2010");
		myBook3.setCreator("Jeff");
		myBook3.setDescription("Book for test.");
		myBook3.setImageUrl("");
		myBook3.setMainTitle("日本史1");
		myBook3.setOriginalUrl("");
		myBook3.setOwner("Jeff's room.");
		myBook3.setOwnerCollectionsWebsite("");
		myBook3.setOwnerWebsite("");
		myBook3.setType("test");
		
		MyBook myBook4 = new MyBook();
		myBook4.setId("test4");
		myBook4.setAcquiredYear(2006);
		myBook4.setAvailable(false);
		myBook4.setCreatedYear("2006");
		myBook4.setCreator("Jeff");
		myBook4.setDescription("Book for test.");
		myBook4.setImageUrl("");
		myBook4.setMainTitle("日本史2");
		myBook4.setOriginalUrl("");
		myBook4.setOwner("Jeff's room.");
		myBook4.setOwnerCollectionsWebsite("");
		myBook4.setOwnerWebsite("");
		myBook4.setType("test");
		
		MyBook myBook5 = new MyBook();
		myBook5.setId("test5");
		myBook5.setAcquiredYear(2010);
		myBook5.setAvailable(false);
		myBook5.setCreatedYear("2010");
		myBook5.setCreator("Jeff");
		myBook5.setDescription("Book for test.");
		myBook5.setImageUrl("");
		myBook5.setMainTitle("日本史3");
		myBook5.setOriginalUrl("");
		myBook5.setOwner("Jeff's room.");
		myBook5.setOwnerCollectionsWebsite("");
		myBook5.setOwnerWebsite("");
		myBook5.setType("test");
		
		bookRepo.insert(myBook);
		bookRepo.insert(myBook2);
		bookRepo.insert(myBook3);
		bookRepo.insert(myBook4);
		bookRepo.insert(myBook5);
	}
	
}
