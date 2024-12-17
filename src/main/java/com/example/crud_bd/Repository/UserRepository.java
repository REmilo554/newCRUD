package com.example.crud_bd.Repository;

import com.example.crud_bd.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer> {

@Query("SELECT u FROM User u WHERE u.id = ?1")
User getUserById(int id);

List<User> findAll();
@Query("select u from User u where u.age > 18")
List<User> findAllAdultUsers();
}
