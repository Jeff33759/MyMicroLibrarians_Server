package jeff.book.model.dto.send;

/**
 * 當操作遭遇例外而失敗時的回應物件。
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
