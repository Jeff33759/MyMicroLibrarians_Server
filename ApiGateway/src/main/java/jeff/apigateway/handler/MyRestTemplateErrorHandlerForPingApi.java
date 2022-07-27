package jeff.apigateway.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import jeff.apigateway.exception.RestTemplateErrorResException;

/**
 * 發送ping請求給各微服務時，收到錯誤回應的處理。</p>
 * 
 * @author Jeff Huang
 * */
public class MyRestTemplateErrorHandlerForPingApi implements ResponseErrorHandler{

	/**
     * 通過回傳狀態碼(非200)，判斷HttpResponse是否為例外回應。</p>
     * 若狀態碼有在{@link HttpStatus}中，就回傳T，跑{@link handleError}處理；
     * 若無，就呼叫{@link unknownStatusHasError}，做第二次判斷。
     */
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        int rawStatusCode = response.getRawStatusCode();
        HttpStatus statusCode = HttpStatus.resolve(rawStatusCode);
        return (statusCode != null ? statusCode.isError() : 
        	unknownStatusHasError(rawStatusCode));
    }

    /**
     * 若回傳狀態碼不在{@link HttpStatus}中，呼叫{@link HttpStatus.Series #resolve}，
     * 其內部藉由將狀態碼/100，得出是哪一個分類的狀態碼後，如果是屬於4XX或5XX的，
     * 就跑{@link handleError}處理。
     * */
    protected boolean unknownStatusHasError(int unknownStatusCode) {
        HttpStatus.Series series = HttpStatus.Series.resolve(unknownStatusCode);
        return (series == HttpStatus.Series.CLIENT_ERROR || 
        		series == HttpStatus.Series.SERVER_ERROR);
    }
 
    /**
     * 仿照{@link DefaultResponseErrorHandler}的寫法，
     * 將拋出的例外重新包裝，以利捕捉並進行後續處理。
     * */
	@Override
    public void handleError(ClientHttpResponse response) throws IOException {
		String msg = 
				new String(response.getBody().readAllBytes());
		HttpStatus statusCode = response.getStatusCode();
		switch(statusCode.series()) {
			case CLIENT_ERROR:
				throw new RestTemplateErrorResException(
						new HttpClientErrorException(statusCode,msg));
			case SERVER_ERROR:
				throw new RestTemplateErrorResException(
						new HttpServerErrorException(statusCode,msg));
			default:
				throw new RestTemplateErrorResException(msg);
		}
    }

}
