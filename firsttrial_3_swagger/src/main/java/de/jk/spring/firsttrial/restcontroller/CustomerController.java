package de.jk.spring.firsttrial.restcontroller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.jk.spring.firsttrial.entity.CustomerEntity;
import de.jk.spring.firsttrial.exception.ErrorsResponse;
import de.jk.spring.firsttrial.exception.RecordNotFoundException;
import de.jk.spring.firsttrial.functionalservice.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
 
 
@RestController
@RequestMapping("/customers")
//Swagger
@Api(value="Customer Demo Management System")
public class CustomerController 
{
    @Autowired
    CustomerService service;
 
    @GetMapping()
    //Swagger
    @ApiOperation(value = "View a list of available customers", response = CustomerEntity.class, responseContainer = "List")
    @ApiResponses(value = {
    	    @ApiResponse(code = 200, message = "Successfully retrieved list of customers", response = CustomerEntity.class, responseContainer = "List"),
    	    @ApiResponse(code = 401, message = "You are not authorized to view the resource", response = ErrorsResponse.class),
    	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ErrorsResponse.class),
    	    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ErrorsResponse.class)
    	})
    public ResponseEntity<List<CustomerEntity>> getAllcustomersWithPagination(
    		@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    		) {
        List<CustomerEntity> list = service.getAllCustomers(pageNo, pageSize, sortBy);
 
        return new ResponseEntity<List<CustomerEntity>>(list, new HttpHeaders(), HttpStatus.OK);
    }
 
    @GetMapping("/{id}")
    //Swagger
    @ApiOperation(value = "Get a single customer by unique id", response = CustomerEntity.class)
    @ApiResponses(value = {
    	    @ApiResponse(code = 200, message = "Successfully retrieved customer", response = CustomerEntity.class),
    	    @ApiResponse(code = 401, message = "You are not authorized to view the resource", response = ErrorsResponse.class),
    	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ErrorsResponse.class),
    	    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ErrorsResponse.class)
    	})
    public ResponseEntity<CustomerEntity> getcustomerById(
    		@ApiParam(value = "unique id of existing customer", required = true)
    		@PathVariable("id") Long id) throws RecordNotFoundException {
        CustomerEntity entity = service.getCustomerById(id);
 
        return new ResponseEntity<CustomerEntity>(entity, new HttpHeaders(), HttpStatus.OK);
    }
    
    @GetMapping("/name/{name}")
    //Swagger
    @ApiOperation(value = "Get a single customer by customer name like provided chars", response = CustomerEntity.class, responseContainer = "List")
    @ApiResponses(value = {
    	    @ApiResponse(code = 200, message = "Successfully retrieved list of customers", response = CustomerEntity.class, responseContainer = "List"),
    	    @ApiResponse(code = 401, message = "You are not authorized to view the resource", response = ErrorsResponse.class),
    	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ErrorsResponse.class),
    	    @ApiResponse(code = 404, message = "The resource(s) you were trying to reach is not found", response = ErrorsResponse.class)
    	})
    public ResponseEntity<List<CustomerEntity>> getCustomersByLastNameLike(
    		@ApiParam(value = "chars used for like search on lastName", required = true) 
    		@PathVariable("name") Optional<String> name) { 
        List<CustomerEntity> list = service.getCustomersByNameLike(name);
        
        return new ResponseEntity<List<CustomerEntity>>(list, new HttpHeaders(), HttpStatus.OK);
    }
    
    @PostMapping
    //Swagger
    @ApiOperation(value = "Create or update a single customer by unique id - for create use id <= 0", response = CustomerEntity.class)
    @ApiResponses(value = {
    	    @ApiResponse(code = 200, message = "Successfully created or updated customer", response = CustomerEntity.class),
    	    @ApiResponse(code = 401, message = "You are not authorized to view the resource", response = ErrorsResponse.class),
    	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ErrorsResponse.class),
    	    @ApiResponse(code = 400, message = "Your input structure is incorrect or input values are missing/not correct", response = ErrorsResponse.class),
    	    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ErrorsResponse.class)
    	})
    
    public ResponseEntity<CustomerEntity> createOrUpdatecustomer(
    		@ApiParam(value = "Customer record to be stored in database table", required = true) 
    		@Valid @RequestBody CustomerEntity customer)
                                                    throws RecordNotFoundException {
        CustomerEntity updated = service.createOrUpdateCustomer(customer);
        return new ResponseEntity<CustomerEntity>(updated, new HttpHeaders(), HttpStatus.OK);
    }
 
    @DeleteMapping("/{id}")
    //Swagger
    @ApiOperation(value = "Delete single customer by unique id")
    @ApiResponses(value = {
    	    @ApiResponse(code = 401, message = "You are not authorized to view the resource", response = ErrorsResponse.class),
    	    @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden", response = ErrorsResponse.class),
    	    @ApiResponse(code = 204, message = "The record for given ID does (no longer) exist"),
    	    @ApiResponse(code = 404, message = "The resource you were trying to reach is not found", response = ErrorsResponse.class)
    	})
    public ResponseEntity deletecustomerById(
    		@ApiParam(value = "unique id of existing customer", required = true)
    		@PathVariable("id") Long id) 
                                                    throws RecordNotFoundException {
        service.deleteCustomerById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
 
}