package com.lun.managedController;
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.dao.DataAccessException;

import com.lun.model.Customer;
import com.lun.service.CustomerService;


/**
 *
 * Customer Managed Bean
 *
 * @author JK
 * @since 18-2-23
 * @version 1.0.0
 *
 */
@ManagedBean(name="customerMB")//Default Scope is @RequestScope, As long as there are request from client, , the init() method which is @PostConstuct run again
@ViewScoped//As long as the page in one browser is refreshed, the init() method which is @PostConstuct run again
//@SessionScoped//Whenever the page in one browser is refreshed, the init() method which is @PostConstuct just run once  
public class CustomerManagedBean implements Serializable {
 
    private static final long serialVersionUID = 1L;
    private boolean hide;
 
    //Spring Customer Service is injected...
    @ManagedProperty(value="#{CustomerService}")
    private CustomerService customerService;
 
    private List<Customer> customerList;
    private Customer customer;
 
    @PostConstruct
    public void init() {
    	customerList = customerService.getCustomers();
    }

    public void info(String sth) {
        FacesContext.getCurrentInstance()
        	.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, sth, ""));
    }
    
    public void preprocessCustomer(Customer customer) {
    	try {
    		this.customer = customer == null ? new Customer() : customer;
//    		System.out.println(this.customer);
    		hide = false;
    	}catch (Exception e) {
    		e.printStackTrace();
			info("Something Wrong!");
		}

    }
    
    public void confirm() {
    	try {
        	if(customer.getId() == null) {
        		customerService.addCustomer(customer);
        		customerList.add(customer);
        	}else {
        		customerService.updateCustomer(customer);
        	}
        	hide = true;
        	info("OK!");
    	}catch (Exception e) {
    		e.printStackTrace();
			info("Something Wrong!");
		}
    }
    
    public void deleteCustomer(Customer customer) {
        try {
            customerService.deleteCustomer(customer);
            customerList.remove(customer);
        } catch (DataAccessException e) {
            e.printStackTrace();
            info("Something Wrong!");
        }
        info("Delete Successfully!");
    }
 
    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}
}