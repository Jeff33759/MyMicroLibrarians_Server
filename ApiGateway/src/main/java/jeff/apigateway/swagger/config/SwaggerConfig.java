package jeff.apigateway.swagger.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
    public OpenAPI openAPI() { //建置API文件的各種資訊
		
//	  	1.API文件的資訊說明物件
	    Info infoObj = new Info() 
	        .title("Jeff's micro-service Restful API Document.") //文件標題
	        .description("API documentation for external access.") //文件詳細說明
	        .version("Ver. 1.0.0") //文件或程式版本號
	        .license(new License() //針對那些公開API的證書訊息，例如哪個伺服器、什麼通訊協議等等...
	                .name("Apache License, Version 2.0") //超連結名稱
	                .url("https://www.apache.org/licenses/LICENSE-2.0")); //超連結
	    
//	  	2.除了Info裡的證書(license)外，想要添加額外文件作為補充說明給人連結
	    ExternalDocumentation externalDoc = new ExternalDocumentation()
	    		.description("SpringBoot-2.7.0")
	    		.url("https://spring.io/blog/2022/05/19/spring-boot-2-7-0-available-now");
	    
//		3-1.建置安全方案列表
		String securitySchemeName = "JWT Authentication"; //安全方案的命名
//		本質是個List，內存放多組安全方案的名稱。
//		和一般List不同的是，內含與swqgger其他元件對接的方法，所以一定要傳這個給swagger，才有辦法run
	    SecurityRequirement securityRequirements =
	            new SecurityRequirement().addList(securitySchemeName);
	    
//	    3-2.創建並定義一個可以使用的安全方案物件
	    SecurityScheme securityScheme = new SecurityScheme()
	    		.name(securitySchemeName) //方案名
		        .type(SecurityScheme.Type.HTTP) //方案方法
		        .scheme("bearer") //bearer type的JWT
		        .bearerFormat("JWT");
	    
//	3-3.為OAS的各種面向，保存一組可重複使用的物件，可想成swagger的設定工具箱或方案盒，裡面放很多工具或建置好的模式方案
	    Components components = new Components()
//	    		安全方案表，是map，存放多個建置好的安全方案。
	            .addSecuritySchemes(securitySchemeName, securityScheme); 
	    
//================================================================================
	    
//	  	4.OpenApi的根元素，swagger的設定就從這裡吃進去，前面都是前置作業
	    OpenAPI openAPI = new OpenAPI() 
    		.info(infoObj) //*必須
	        .externalDocs(externalDoc)
	        .addSecurityItem(securityRequirements) //把可用的安全方案傳進去(單純的名字列表)
//			當OpenAPI在執行Security方面的程式時，會去SecurityItem抓名字列表...
//	        然後再去components抓符合名字的安全方案物件，然後進行設定
	        .components(components); 
		
        return openAPI;
    }
	
}
