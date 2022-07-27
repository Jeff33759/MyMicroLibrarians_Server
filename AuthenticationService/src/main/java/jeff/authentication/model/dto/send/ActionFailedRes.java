package jeff.authentication.model.dto.send;

/**
 * 當操作因為例外或授權問題而失敗時的回應。。
 * */
public class ActionFailedRes {

	private String msg;
	
	private String path;
	
	private String method;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
}
