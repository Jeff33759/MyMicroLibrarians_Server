package jeff.authentication.model.po;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.User;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 館藏系統使用者資訊。</p>
 * 為避免和Security內有的{@link User}搞混，取名為Account。</p>
 * 
 * @author Jeff Huang
 * */
@Document(collection = "account")
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	private String password;
	
	private String name;
	
	private String auth_role;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date start_date;
	
	private Boolean enable;

	/**
	 * @return 帳號，同時也是ID。
	 * */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuth_role() {
		return auth_role;
	}

	public void setAuth_role(String auth_role) {
		this.auth_role = auth_role;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	

}
