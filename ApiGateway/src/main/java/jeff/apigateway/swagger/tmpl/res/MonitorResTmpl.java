package jeff.apigateway.swagger.tmpl.res;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jeff.apigateway.model.dto.send.MonitorRes;

/**
 * 顯示各微服務狀態的說明模板，因為不想把Schema寫在{@link MonitorRes}
 * 所以又創了一個模板。
 * 
 * @author Jeff Huang
 * */
public class MonitorResTmpl {
	
	public MicroServiceStatus authNService;
	
	public MicroServiceStatus bookService;

	
	public class MicroServiceStatus {
		
		@Schema(description = "Whether the micro-service provides external services.",
				example = "true")
		public boolean inService;

		@Schema(description = "Micro-services-cluster.")
		public List<ServerInstance> cluster;

	}
	
	
	public class ServerInstance {
		
		@Schema(description = "Server instance name.",
				example = "Server1")
		public String name;
		
		@Schema(description = "Server instance enabled status.",
				example = "true")
		public boolean enabled;
		
	}
	
}
