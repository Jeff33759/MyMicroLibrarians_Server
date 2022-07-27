package jeff.authentication.common.constants;

/**
 * 使用者的授權類別。</p>
 * 
 * @author Jeff Huang
 * */
public enum UserRole {
	
	/**
	 * 可以操作使用者的CRUD(尚未規劃)、館藏的CRUD。
	 * */
	ADMIN,
	
	/**
	 * 可以操作館藏的CRUD。
	 * */
	ADVANCED, 
	
	/**
	 * 可以操作館藏的進階R。
	 * */
	NORMAL;
	
}
