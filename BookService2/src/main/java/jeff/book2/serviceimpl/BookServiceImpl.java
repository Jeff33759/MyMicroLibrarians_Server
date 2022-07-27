package jeff.book2.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mongodb.MongoException;

import jeff.book2.common.constants.BookQueryConst;
import jeff.book2.common.constants.CustomMongoErrorCode;
import jeff.book2.common.util.MyUtil;
import jeff.book2.dao.BookRepository;
import jeff.book2.exception.MyDataNotFoundException;
import jeff.book2.exception.UpdatingBookDBException;
import jeff.book2.model.dto.qparam.MyBookQueryParam;
import jeff.book2.model.dto.receive.MyBookPatchReq;
import jeff.book2.model.dto.receive.MyBookReq;
import jeff.book2.model.dto.send.BookQueryRes;
import jeff.book2.model.po.MyBook;
import jeff.book2.service.BookService;

@Service
public class BookServiceImpl implements BookService{
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private MyUtil myUtil;

	@Override
	public BookQueryRes getAllBooks(MyBookQueryParam param) {
		myUtil.setDefaultValueForParam(param);
		PageRequest paginationStrategy = 
				myUtil.genMyPaginationStrategy(BookQueryConst.DEFAULT_ORDER_BY,
						param.getSortRule(),param.getPageSize(),param.getNowPage());
		Page<MyBook> pageResult = 
				bookRepository.findAll(paginationStrategy);
		return myUtil.tranformPageResultToExpectedResFormat(pageResult);
	}
	
	@Override
	public MyBook getBookById(String id) {
		return bookRepository.findById(id)
				.orElseThrow(()-> new MyDataNotFoundException(
					new MongoException(CustomMongoErrorCode.QUERY_NOT_FOUND.errCode()
							,CustomMongoErrorCode.QUERY_NOT_FOUND.errHint())));
	}
	

	@Override
	public BookQueryRes getBooksByCondition(MyBookQueryParam param) {
		myUtil.setDefaultValueForParam(param);
		PageRequest paginationStrategy = 
				myUtil.genMyPaginationStrategy(BookQueryConst.DEFAULT_ORDER_BY,
					param.getSortRule(),param.getPageSize(),param.getNowPage());
		Page<MyBook> pageResult = 
				bookRepository.findByMainTitleContainingIgnoreCaseAndAcquiredYearBetween(
						param.getTitleKw(), param.getYearFrom(),
						param.getYearTo(), paginationStrategy);
		return myUtil.tranformPageResultToExpectedResFormat(pageResult);
	}
	

	@Override
	public MyBook addBook(MyBookReq newBook) {
		MyBook myBook = 
				myUtil.ConvertMyBookReqToMyBookAndSetDefault(newBook,null);
		return bookRepository.insert(myBook);
	}
	
	@Override
	public MyBook replaceBook(MyBookReq bookReq, String bookId) {
		if(bookRepository.existsById(bookId)) {
			MyBook myBook = 
					myUtil.ConvertMyBookReqToMyBookAndSetDefault(bookReq,bookId);
			return bookRepository.save(myBook);
		}
		throw new MyDataNotFoundException(
				new MongoException(CustomMongoErrorCode.UPDATE_NOT_FOUND.errCode(),
						CustomMongoErrorCode.UPDATE_NOT_FOUND.errHint()));
	}


	@Override
	public MyBook updateBook(MyBookPatchReq newBook, String bookId){
		MyBook oldBook = bookRepository.findById(bookId)
			.orElseThrow(()-> new MyDataNotFoundException(
					new MongoException(CustomMongoErrorCode.UPDATE_NOT_FOUND.errCode()
							,CustomMongoErrorCode.UPDATE_NOT_FOUND.errHint())));
		myUtil.LoadMyBookPatchReqDataIntoOldMyBook(newBook, oldBook);
		return bookRepository.save(oldBook);
	}


	@Override
	public void deleteBook(String bookId) {
		if(bookRepository.existsById(bookId)) {
			bookRepository.deleteById(bookId);
		}else {
			throw new MyDataNotFoundException(
					new MongoException(CustomMongoErrorCode.DELETE_NOT_FOUND.errCode(),
							CustomMongoErrorCode.DELETE_NOT_FOUND.errHint()));
		}
	}

	
	@Override
	public void initBookDB(List<MyBook> bookList) 
			throws UpdatingBookDBException{
		try {
			bookRepository.saveAll(bookList);
		}catch(Exception e) {
			throw new UpdatingBookDBException(
					"The error occurred when saving data from Gov site into DB.",e);
		}
	}


	@Override
	public void cleanDB() {
		bookRepository.deleteAll();
	}


}
