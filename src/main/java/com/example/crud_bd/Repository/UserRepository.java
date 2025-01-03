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

    @Query("select u from User u where u.passport = ?1")
    User getUserByPassport(String passport);

    //@Query("SELECT u FROM User u WHERE u.id = ?1")
    User findUserById(Long id);

    List<User> findAll();

    @Query("select u from User u where u.age > 18")
    List<User> findAllAdultUsers();

    User save(User user);

    @Modifying
    @Transactional
    //@Query("delete from User u where u.id=?1")
    Integer deleteUserById(Long id);


    /**
     *если убрать query,то генерируется два запроса,на селект по паспорту и делит по айди
     */
    @Modifying
    @Transactional
    @Query("delete from User u where u.passport=?1")
    Integer deleteUserByPassport(String passport);


    @Query("update User u set u.firstName=?2,u.secondName=?3,u.age=?4,u.passport=?5 where u.id=?1")
    @Modifying
    @Transactional
    Integer updateUser(Long id, String firstName, String secondName, Integer age, String passport);

    @Modifying
    @Transactional
    @Query("update User u set u.firstName=:firstName,u.secondName=:secondName,u.age=:age,u.passport=:passport where u.id=:id")
        // @Query("update User u set u.firstName=?2,u.secondName=?3,u.age=?4,u.passport=?5 where u.id=?1")
    Integer updateUserById(Long id, String firstName, String secondName, Integer age, String passport);
}
