package jeff.apigateway.serviceimpl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jeff.apigateway.common.util.MyUtil;
import jeff.apigateway.config.MyComponentConfig;
import jeff.apigateway.service.BusinessForwardingService;

@Service
public class BusinessForwardingServiceImpl implements BusinessForwardingService{

	/**
	 * @see {@link MyComponentConfig}
	 * */
	@Resource(name="businessRestTemplateBean")
	private RestTemplate restTemplate;
	
	@Autowired
	private MyUtil util;

	/**
	 * @throws Exception 
	 * @implNote 某些get的標頭可能需要攜帶Token，所以用exchange做。
	 * */
	@Override
	public ResponseEntity<?> forwardGetReq(HttpServletRequest req, 
			String rootUrl, LinkedHashMap<String,String> pathVarMap) throws Exception {
		HttpEntity<?> reqObj = 
				util.genHttpEntity(util.genHttpHeader(req), null);
		String pathVarStr = util.handlePathVar(pathVarMap);
		String decodeQStr = util.decodeQueryStr(req.getQueryString());
		String url = 
				util.handleTheUrl(rootUrl, pathVarStr, decodeQStr);
		return restTemplate.exchange(url,HttpMethod.GET, reqObj, Map.class);
	}

	@Override
	public ResponseEntity<?> forwardPostReq(HttpServletRequest req, 
			String rootUrl, LinkedHashMap<String,String> pathVarMap) throws Exception {
		HttpEntity<?> reqObj = 
				util.genHttpEntity(util.genHttpHeader(req), util.getRequestBody(req));
		String pathVarStr = util.handlePathVar(pathVarMap);
		String decodeQStr = util.decodeQueryStr(req.getQueryString());
		String url = 
				util.handleTheUrl(rootUrl, pathVarStr, decodeQStr);
		return restTemplate.postForEntity(url, reqObj, Map.class);
	}

	@Override
	public ResponseEntity<?> forwardPutReq(HttpServletRequest req, 
			String rootUrl, LinkedHashMap<String,String> pathVarMap) throws Exception{
		HttpEntity<?> reqObj = 
				util.genHttpEntity(util.genHttpHeader(req), util.getRequestBody(req));
		String pathVarStr = util.handlePathVar(pathVarMap);
		String decodeQStr = util.decodeQueryStr(req.getQueryString());
		String url = 
				util.handleTheUrl(rootUrl, pathVarStr, decodeQStr);
		return restTemplate.exchange(url, HttpMethod.PUT, reqObj, Map.class);
	}

	@Override
	public ResponseEntity<?> forwardPatchReq(HttpServletRequest req, 
			String rootUrl, LinkedHashMap<String,String> pathVarMap) throws Exception{
		HttpEntity<?> reqObj = 
				util.genHttpEntity(util.genHttpHeader(req), util.getRequestBody(req));
		String pathVarStr = util.handlePathVar(pathVarMap);
		String decodeQStr = util.decodeQueryStr(req.getQueryString());
		String url = 
				util.handleTheUrl(rootUrl, pathVarStr, decodeQStr);
		return restTemplate.exchange(url, HttpMethod.PATCH, reqObj, Map.class);
	}

	@Override
	public ResponseEntity<?> forwardDeleteReq(HttpServletRequest req, 
			String rootUrl, LinkedHashMap<String,String> pathVarMap) throws Exception{
		String pathVarStr = util.handlePathVar(pathVarMap);
		String decodeQStr = util.decodeQueryStr(req.getQueryString());
		String url = 
				util.handleTheUrl(rootUrl, pathVarStr, decodeQStr);
		return restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
	}

	
}
