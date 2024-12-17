package com.example.crud_bd.Repository;

import com.example.crud_bd.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User getUserByPassport(String passport);

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    User getUserById(int id);

    List<User> findAll();

    @Query("select u from User u where u.age > 18")
    List<User> findAllAdultUsers();

    @Query("select count(*) from User u where u.passport = ?1")
    Integer contains(String passport);

    User save(User user);

    @Modifying
    @Transactional
    @Query("delete from User u where u.id=?1")
    void deleteUserById(Long id);

    @Modifying
    @Transactional
    @Query("delete from User u where u.passport=?1")
    int deleteUserByPassport(String passport);
}
