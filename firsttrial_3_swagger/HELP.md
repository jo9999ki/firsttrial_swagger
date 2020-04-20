# Application FirstTrial

This is an Spring Boot 2 Project for Java 11 and Maven 3.3+ 
The code contains best case approaches for fast project adaption.

Its part of a spring boot tutorial containing follwing chapters
1. Create Spring boot 2 project with H2 database, Data Model, Hibernate Repository
2. Add internal CRUD service with basic test capability
3. Create REST Controller, add error output and error handler, create JUnit test for REST controller
4. Add swagger (OpenAPI) documentation for REST controller
5. Add Spring Boot Actuator with Micrometer Metrics, local Prometheus and Graphana severs and customized health check and furhter enhancements

## Content of the enhancements in this repository and best case approaches for project adaption
* The current repository contains enhancements to the basic and REST controller repositories for creation swagger (OpenAPI) documentation based on swagger document version 2 
* The project uses all swagger version 2 capabilities for a complete documentation of 
<br> description and base path of the application
<br> description and base path of all REST resources
<br> in / out data model for positive and negative cases (in general, per method)
<br> input parameter details for data type, format, length and valid values 
<br> detailed description of service method, its path, parameters and data models
<br> complete list of combinations of http codes and output models per service method

## Expose REST service documentation with Swagger (OpenAPI standard)

### Add dependencies in project pom file
<pre><code>
	&lt;!-- Swagger Spring Fox Integration 2.8.0--&gt;
	&lt;dependency&gt;
	    &lt;groupId&gt;io.springfox&lt;/groupId&gt;
	    &lt;artifactId&gt;springfox-swagger2&lt;/artifactId&gt;
	    &lt;version&gt;2.9.2&lt;/version&gt;
	&lt;/dependency&gt;
	&lt;dependency&gt;
	    &lt;groupId&gt;io.springfox&lt;/groupId&gt;
	    &lt;artifactId&gt;springfox-swagger-ui&lt;/artifactId&gt;
	    &lt;version&gt;2.9.2&lt;/version&gt;
	&lt;/dependency&gt;
    &lt;dependency&gt;
        &lt;groupId&gt;io.springfox&lt;/groupId&gt;
        &lt;artifactId&gt;springfox-bean-validators&lt;/artifactId&gt;
        &lt;version&gt;2.9.2&lt;/version&gt;
    &lt;/dependency&gt;
</pre></code>

### Create swagger config class
* Create new package (...) configuration
* Create new class "FirstTrialConfig"
* Create class annotations
<pre><code>
	@Configuration
	--> Recommended by Spring Boot to have one configuration class only
	//Swagger
	@EnableSwagger2
	@Import(springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class)
</pre></code>	
* Add following methods 			
<br> Limits code packages for exposal in swagger file for our customer controller only
<br> Defines Path for swagger file, if swagger page shall show static file instead of automatically created content from code annotations
<pre><code>
	//Swagger
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors
						.basePackage("de.jk.spring.firsttrial"))
						//.any()) Shows all endpoints available
				.paths(
						PathSelectors
						.regex("/.*"))
						//.any())
				.build()
				.apiInfo(apiEndPointsInfo());
	}
</pre></code>
<br> This is the basic Configuratation shown in top of swagger page
<pre><code>			
	//Swagger
	private ApiInfo apiEndPointsInfo() {
		return new ApiInfoBuilder().title("Spring Boot REST API")
			.description("Customer Management REST API")
			.contact(new Contact("Jochen Kirchner", "www.sn-invent.de", "jochen.kirchner@sn-invent.de"))
			.license("Apache 2.0")
			.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
			.version("1.0.0")
			.build();
	}
</pre></code>
<br> endpoint (browser) path for swagger page and class pathes for swagger generation or retrieval of predefined swagger file
<pre><code>			
	//Swagger
	@Override //Required to show swagger UI
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
		  .addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
		  .addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
</pre></code>

### Enhance Configuration in application.properties file to ignore swagger severity warnings
<pre><code>
		#Ignore severity warnings
		logging.level.io.swagger.models.parameters.AbstractSerializableParameter=error	
</pre></code>		
### Start application and use swagger ui in browser
* Start swagger ui with http://localhost:8080/swagger-ui.html
* Send requests for single methods.
<br>Detailed description per method contains default values - to be enhanced by annotations in controller class
<br> Look at model definitions end of page for both customer entity, response entity and ErrorsResponse. Detailed information for attributes missing - to be enhanced by annotations in beans
	
### Add swagger annotations in REST controller class (if not partly existing for input validation purpose)
* create class annotation
<pre><code>		
	@Api(value="Customer Demo Management System")
</pre></code>				
* create method annotations (description, input parameters, http-code + response model combinations) - example getAllcustomersWithPagination
<pre><code>		
	@ApiOperation(value = "View a list of available customers", response = CustomerEntity.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully retrieved list of customers", response = CustomerEntity.class, responseContainer = "List"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource", response = ErrorsResponse.class),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ErrorsResponse.class),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ErrorsResponse.class)
			})
</pre></code>						
<br> Refresh swagger page and check method changes	
		
### Add swagger annotations in customer bean
* Create class annotation
<pre><code>
	@ApiModel(description = "Attributes single Customer")
</pre></code>		
* Create attribute annotations - example for first name:
<pre><code>
	 @NotNull //Input Validation
	@Size(min=0, max = 255) //Input Validation + Swagger Output
	@ApiModelProperty(required = true, dataType = "String", notes  = "first name of the customer", position = 2, example = "Harry") //Swagger
</pre></code>
<br> Example email:
<pre><code>
	//RFC 5321 for mail adress length
	@NotNull //Input Validation
	@Size(min=0, max = 320) //Input Validation + Swagger Output
	@Email //Input Validation
	@ApiModelProperty(required = true, dataType = "String", notes  = "last name of the customer, max. length=320 (RFC 3696), format: valid email format (must contain ... localpart(max. 64 chars)@hostname.topleveldomain(max 255 chars))",  example = "harry.potter@hogwarts.com", position = 4) //Swagger
</pre></code>		
<br> Refresh swagger page and check model changes	

### Add swagger annotations in response error beans
* class ResponseErrors - create class annotation
<pre><code>
	@ApiModel(description = "List of errors - by default each method might provides 1..n errors in on response")
</pre></code>
* create attribute annotations
<pre><code>
	@ApiModelProperty(required = true, dataType = "ErrorResponse", notes  = "List of errors")
</pre></code>
<br>
* class ResponseErrors - create class annotation
<pre><code>
	@ApiModel(description = "Standard error format")
</pre></code>			
* create attribute annotations - example timestamp
<pre><code>
	@ApiModelProperty(required = true, dataType = "String", notes  = "timestamp (UTC), when the error occured, JSONZ format", position = 1, example = "2020-04-02T08:09:18.687701800Z") //Swagger
</pre></code>
<br> Refresh swagger page and check error model changes
