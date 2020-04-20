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

1.  add dependencies in project pom file
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

2. Create swagger config 
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
<br> browser path for swagger page
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