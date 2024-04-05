package org.example.sms4.service;

import jakarta.transaction.Transactional;
import org.example.sms4.entity.Student;
import org.example.sms4.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
@Transactional
public class StudentService {
    @Autowired
    private StudentRepository repo;

    public List<Student> listAll() {
        return (List<Student>) repo.findAll();
    }

    public void save(Student student) {
        repo.save(student);
    }

    public Student get(long id) {
        return repo.findById(id).get();
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public List<Student> searchName(String name) {
        List<Student> searchResult = repo.findByNameContainingIgnoreCase(name);
        return searchResult;
    }
    public String validateNewInformation(Student student){
        String err = null;
        if(student.getName() == ""){
            err = "Student must have a name";
            System.out.println("1");
        }
        if(student.getEmail() == ""){
            err = "Student must have an email address";
            System.out.println("2");
        }
        String email = student.getEmail();
        boolean emailDuplicate = repo.existsByEmail(email);
        if (emailDuplicate == true){
            err = "This email adress has been used";
            System.out.println("3");
        }
        return err;
    }

    public String validateEditInformation(Student student){
        String err = null;
        if(student.getName().equals("")){
            err = "Student must have a name";
            System.out.println("1");
        }
        if(student.getEmail().equals("")){
            err = "Student must have an email address";
            System.out.println("2");
        }
        return err;
    }
}
