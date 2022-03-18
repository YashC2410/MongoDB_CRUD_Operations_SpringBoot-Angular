package com.neosoft.repository;

import com.neosoft.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee,Long>{


    @Query("{'firstName': ?0, 'emailId' : ?1}")
    public List<Employee> findByFirstNameAndEmailId(String firstName, String emailId);

    @Query("{'lastName': {'$regex': ?0, '$options': 'i'}, 'emailId' : {'$regex': ?1, '$options': 'i'}}")
    public List<Employee> findByLastNameAndEmailId(String lastName, String emailId);

    @Query("{'lastName': {'$regex': ?0, '$options': 'i'}, 'age' : ?1}")
    public List<Employee> findByLastNameAndAge(String lastName, int age);

    @Query("{'emailId': ?0, 'age' : ?1}")
    public List<Employee> findByEmailIdAndAge(String lastName, int age);

    @Query("{'firstName': ?0, 'age' : ?1}")
    public List<Employee> findByFirstNameAndAge(String firstName, int age);

    @Query("{'firstName': ?0, ,'lastName' : ?1 ,'emailId' : ?2}")
    public List<Employee> findByFirstNameAndLastNameAndEmailId(String firstName,String lastName, String emailId);

    @Query("{'lastName': {'$regex': ?0, '$options': 'i'}}")
    public List<Employee> findByLastName(String lastName);

    @Query("{'lastName': {'$regex': ?0, '$options': 'i'}, 'emailId' : {'$regex': ?1, '$options': 'i'}, 'age': ?2}")
    List<Employee> findByLastNameAndEmailIdAndAge(String lastName, String emailId,int age);

    @Query("{'lastName': {'$regex': ?0, '$options': 'i'}, 'emailId' : {'$regex': ?1, '$options': 'i'}, 'age': {'$gte' : ?2, '$lte': ?3}}")
    List<Employee> findByLastNameAndEmailIdAndAge(String lastName, String emailId, int age1, int age2);

    @Query("{'firstName': {'$regex': ?0, '$options': 'i'}}")
    List<Employee> findByFirstName(String firstName);

}
