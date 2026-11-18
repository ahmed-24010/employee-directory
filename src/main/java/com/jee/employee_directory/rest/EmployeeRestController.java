package com.jee.employee_directory.rest;

import com.jee.employee_directory.entity.Employee;
import com.jee.employee_directory.service.EmployeeService;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private final EmployeeService employeeService;
    private final JsonMapper jsonMapper;

    public EmployeeRestController(EmployeeService employeeService, JsonMapper jsonMapper) {
        this.employeeService = employeeService;
        this.jsonMapper = jsonMapper;
    }

    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {
        Employee e = employeeService.findById(employeeId);
        if (e == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }
        return e;
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee theEmployee) {
        // id = 0 -> merge fait un INSERT (evite un UPDATE deguise)
        theEmployee.setId(0);
        return employeeService.save(theEmployee);
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee theEmployee) {
        // Remplacement complet : l'id du JSON -> merge fait un UPDATE
        return employeeService.save(theEmployee);
    }

    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {
        Employee e = employeeService.findById(employeeId);
        if (e == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }
        employeeService.deleteById(employeeId);
        return "Deleted employee id - " + employeeId;
    }

    @PatchMapping("/employees/{employeeId}")
    public Employee patchEmployee(@PathVariable int employeeId,
                                  @RequestBody Map<String, Object> patchPayload) {
        Employee tempEmployee = employeeService.findById(employeeId);
        if (tempEmployee == null) {
            throw new RuntimeException("Employee id not found - " + employeeId);
        }

        // Garde-fou : l'id ne doit pas etre modifiable
        if (patchPayload.containsKey("id")) {
            throw new RuntimeException("Employee id cannot be modified");
        }

        // updateValue : applique seulement les champs presents dans la map
        Employee patched = jsonMapper.updateValue(tempEmployee, patchPayload);

        return employeeService.save(patched);
    }
}
