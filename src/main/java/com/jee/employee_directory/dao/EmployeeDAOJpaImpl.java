package com.jee.employee_directory.dao;

import com.jee.employee_directory.entity.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // traduit les exceptions JPA en DataAccessException
public class EmployeeDAOJpaImpl implements EmployeeDAO {

    private final EntityManager entityManager;

    @Autowired
    public EmployeeDAOJpaImpl(EntityManager theEntityManager) {
        this.entityManager = theEntityManager;
    }

    @Override
    public List<Employee> findAll() {
        TypedQuery<Employee> query =
                entityManager.createQuery("from Employee", Employee.class);
        return query.getResultList();
    }

    @Override
    public Employee findById(int id) {
        return entityManager.find(Employee.class, id);
    }

    @Override
    public Employee save(Employee theEmployee) {
        // merge : INSERT si id == 0, sinon UPDATE
        return entityManager.merge(theEmployee);
    }

    @Override
    public void deleteById(int id) {
        Employee e = entityManager.find(Employee.class, id);
        entityManager.remove(e);
    }
}
