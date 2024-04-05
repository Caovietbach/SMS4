package org.example.sms4.controller;


import org.example.sms4.entity.Student;
import org.example.sms4.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StudentController {

    private final StudentService service;

    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    @RequestMapping("/")
    public String viewHomePage(Model model) {
        List<Student> listStudents = service.listAll();
        model.addAttribute("listStudents", listStudents);

        return "index";
    }

    @RequestMapping(value = "/searchByName", method = RequestMethod.POST)
    public String searchStudent(@RequestParam("name") String name, @ModelAttribute("student") Student student, Model model) {
        System.out.println(name);
        List<Student> result = service.searchName(name);
        System.out.println(result);
        model.addAttribute("student", result);

        return "searchedStudent";
    }


    @RequestMapping("/addStudent")
    public String showNewStudentPage(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);

        return "addStudent";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveStudent(@ModelAttribute("student") Student student, Model model) {
        String errorMessage = service.validateNewInformation(student);
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            return "addStudent";
        } else {
            service.save(student);
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/saveEdit", method = RequestMethod.POST)
    public String saveEditStudent(@ModelAttribute("student") Student student, RedirectAttributes model) {
        String errorMessage = service.validateEditInformation(student);
        if (errorMessage != null) {
            model.addFlashAttribute("errorMessage", errorMessage);
            long id = student.getId();
            return "redirect:/edit/" + id;
        } else {
            service.save(student);
            return "redirect:/";
        }
    }

    @RequestMapping("/edit/{id}")
    public ModelAndView showEditStudentPage(@PathVariable(name = "id") long id) {
        ModelAndView mav = new ModelAndView("editStudent"); // Corrected template name
        Student student = service.get(id);
        mav.addObject("student", student);

        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deleteStudent(@PathVariable(name = "id") long id) {
        service.delete(id);
        return "redirect:/";
    }

}
