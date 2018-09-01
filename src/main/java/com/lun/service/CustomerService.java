package com.lun.service;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lun.dao.CustomerDAO;
import com.lun.model.Customer;

/**
 *
 * Customer Service
 *
 */
@Service("CustomerService")
@Transactional(readOnly = true)
public class CustomerService {
 
    // CustomerDAO is injected...
    @Autowired
    CustomerDAO customerDAO;
 
    @Transactional(readOnly = false)
    public void addCustomer(Customer customer) {
        getCustomerDAO().addCustomer(customer);
    }
 
    @Transactional(readOnly = false)
    public void deleteCustomer(Customer customer) {
        getCustomerDAO().deleteCustomer(customer);
    }
 
    @Transactional(readOnly = false)
    public void updateCustomer(Customer customer) {
        getCustomerDAO().updateCustomer(customer);
    }
 

    public Customer getCustomerById(int id) {
        return getCustomerDAO().getCustomerById(id);
    }
 
    public List<Customer> getCustomers() {
        return getCustomerDAO().getCustomers();
    }
 
    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }
 
    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }
}