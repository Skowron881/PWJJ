package com.projekt.api;

import com.projekt.model.*;
import com.projekt.service.*;
import com.projekt.validator.CourseValidator;
import com.projekt.validator.DueDateValidator;
import com.projekt.validator.RoleValidator;
import com.projekt.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class UserControler {

    @Autowired
    private UserService userService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private CourseValidator courseValidator;

    @Autowired
    private RoleValidator roleValidator;

    @Autowired
    private DueDateValidator dueDateValidator;

    @Autowired
    private StudentCourseServise studentCourseServise;

    @Autowired
    private UserDocumentService userDocumentService;

    @Autowired
    private DatabaseFileService fileStorageService;//

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String  uploadFile(@RequestParam("file") MultipartFile file) {
        DatabaseFile fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName.getFileName())
                .toUriString();


        return "redirect:/courses-1";
        //return new Response(fileName.getFileName(), fileDownloadUri,
                //file.getContentType(), file.getSize());
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile( HttpServletRequest request) {
        // Load file as Resource
        DatabaseFile databaseFile = fileStorageService.getFile("3150e033-3c4f-4beb-860c-a4caf547c9d5");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(databaseFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + databaseFile.getFileName() + "\"")
                .body(new ByteArrayResource(databaseFile.getData()));
    }





    @GetMapping("/ind")
    public String hello(){
        return "index";
    }

    @RequestMapping(value = "/registration-student-{facultyId}", method = RequestMethod.GET)
    public String registrationStudent(ModelMap model, @PathVariable int facultyId) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration-student-{facultyId}", method = RequestMethod.POST)
    public String registrationStudent(@ModelAttribute("userForm") User userForm, @PathVariable int facultyId,
                                      BindingResult bindingResult) {
        int id = getLoggedInId();


        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        } else {
            userForm.setFaculty_id(facultyId);
            userForm.setRole_id(3);
            userService.save(userForm);
        }

        return "redirect:/";
    }

    @RequestMapping(value = "/list-of-users", method = RequestMethod.GET)
    public String listOfUsers(ModelMap model) {
        List<User> users = userService.findAllUsers();

        for (int i = 0; i < users.size(); i++) {
            int id = 0;
            id = users.get(i).getId();

            if(roleService.isStudent(id)) {
                users.get(i).setRole("student");
            } else {
                users.get(i).setRole("teacher");
            }

            id = users.get(i).getFaculty_id();
            users.get(i).setFaculty(facultyService.getFacultyById(id).getName());
        }

        model.addAttribute("users", users);
        return "List";
    }



    @RequestMapping(value = "/registration-teacher-{facultyId}", method = RequestMethod.GET)
    public String registrationTeacher(ModelMap model, @PathVariable int facultyId) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration-teacher-{facultyId}", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, @PathVariable int facultyId,
                               BindingResult bindingResult) {
        int id = getLoggedInId();


        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        } else {
            userForm.setFaculty_id(facultyId);
            userForm.setRole_id(2);
            userService.save(userForm);
        }

        return "redirect:/";
    }

    @RequestMapping(value = { "/delete-user-{userId}" }, method = RequestMethod.GET)
    public String deleteUser(@PathVariable int userId) {
        int id = getLoggedInId();

        if (roleService.isStudent(id)) {
            return "redirect:/list-of-users";
        } else {
            userService.deleteById(userId);
        }

        return "redirect:/list-of-users";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
    public String welcome(ModelMap model) {
        List<Faculty> faculty = facultyService.findAll();
        model.addAttribute("faculty", faculty);

        int id = getLoggedInId();
        if (roleService.isStudent(id)) {
            return "welcomeForStudents";
        }
        return "welcome";
    }


/*
    @RequestMapping(value = { "/delete-document-{docId}-{courseId}" }, method = RequestMethod.GET)
    public String deleteDocument(@PathVariable int docId, @PathVariable int courseId) {
        userDocumentService.delete(docId);

        return "redirect:/documents-" + courseId;
    }
*/

    @RequestMapping(value = { "/delete-document-{docId}-{courseId}" }, method = RequestMethod.GET)
    public String deleteDocument(@PathVariable String docId, @PathVariable int courseId) {
        fileStorageService.delete(docId);

        return "redirect:/documents-" + courseId;
    }


    @RequestMapping(value = {"/courses-{facultyId}"}, method = RequestMethod.GET)
    public String courses(ModelMap model, @PathVariable int facultyId) {
        List<Course> courses = courseService.findByFacultyId(facultyId);
        model.addAttribute("courses", courses);

        int id = getLoggedInId();
        if(roleService.isStudent(id)) {
            return "coursesForStudents";
        }
        return "courses";
    }


    @RequestMapping(value = {"/add-course-{facultyId}"}, method = RequestMethod.GET)
    public String addCourses(ModelMap model, @PathVariable int facultyId) {
        model.addAttribute("courseForm", new Course());

        return "addCourse";
    }

    @RequestMapping(value = "/add-course-{facultyId}", method = RequestMethod.POST)
    public String addCourses(@ModelAttribute("courseForm") Course courseForm, @PathVariable int facultyId, BindingResult bindingResult) {
        courseValidator.validate(courseForm, bindingResult);
        int id = getLoggedInId();

        if (bindingResult.hasErrors()) {
            return "addCourse";
        } else {
            courseForm.setFaculty_id(facultyId);
            courseForm.setTeacher_id(id);
            courseService.save(courseForm);
        }

        return "redirect:/";
    }

    @RequestMapping(value = { "/delete-course-{courseId}-{facultyId}" }, method = RequestMethod.GET)
    public String deleteCourse(@PathVariable int courseId, @PathVariable int facultyId) {
        int id = getLoggedInId();
        if (!courseService.isCourseTeacher(id)) {
            return "redirect:/courses-" + facultyId;
        }

        studentCourseServise.deleteAllStudentsFromCourse(courseId);
        userDocumentService.deleteAllFromCourse(courseId);
        courseService.delete(courseId);

        return "redirect:/courses-" + facultyId;
    }


                 ////////////////////
                                                                        ////////
    @RequestMapping(value = { "/download-document-{docId}-{courseId}-{facultyId}" }, method = RequestMethod.GET)
    public String downloadDocument(@PathVariable int docId, @PathVariable int courseId,
                                   HttpServletResponse response) throws IOException {
        UserDocument document = userDocumentService.findById(docId);
        response.setContentType(document.getType());
        response.setContentLength(document.getContent().length);
        response.setHeader("Content-Disposition","attachment; filename=\"" + document.getName() +"\"");

        FileCopyUtils.copy(document.getContent(), response.getOutputStream());

        return "redirect:/documents-" + courseId;
    }/////

    @RequestMapping(value = {"/documents-{courseId}-{facultyId}"}, method = RequestMethod.GET)
    public String documents(ModelMap model, @PathVariable int courseId, @PathVariable int facultyId) throws IOException {
        int id = getLoggedInId();
        Course course = courseService.findById(courseId);

        if(( course.getTeacher_id() != id ) & (!studentCourseServise.isInCourse(id)) ) {
            return "redirect:/courses-" + course.getFaculty_id();
        }///

        if(roleService.isStudent(id)) {
            List<DatabaseFile> files = fileStorageService.findAllFailes();
         //   for(int i = 0; i < documents.size(); i++) {

         //       documents.get(i).setAuthor(userService.findById(documents.get(i).getUserId()).getUsername());
         //   }

            model.addAttribute("files", files);
        } else {
            List<DatabaseFile> files = fileStorageService.findAllFailes();

         //   for(int i = 0; i < files.size(); i++) {

           //     files.get(i).setAuthor(userService.findById(files.get(i).getUser_id()).getUsername());
          // }

            model.addAttribute("files", files);
        }

        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
        model.addAttribute("courseId", courseId);

        if(roleService.isStudent(id)) {
            return "documentsForStudents";
        }

        return "documents";
    }
                                                     //////////
    @RequestMapping(value = {"/documents-{courseId}-{facultyId}"}, method = RequestMethod.POST)
    public String documents(ModelMap model, @RequestParam("file") MultipartFile file,
                            @PathVariable("courseId") int courseId, @PathVariable int facultyId,
                            @ModelAttribute("fileBucket") FileBucket fileBucket,
                            BindingResult bindingResult) throws IOException {
        Course course = courseService.findById(courseId);

        dueDateValidator.validate(course, bindingResult);

        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);

        int id = getLoggedInId();

        if(roleService.isStudent(id)){
            if (bindingResult.hasErrors()) {
                return "redirect:/documents-" + courseId + "-" + facultyId;
            }
        }

        saveDocument(fileBucket, id, courseId);

        return "redirect:/documents-" + courseId + "-" + facultyId;
    }



//////////////////

    @RequestMapping(value = {"/student-list-{courseId}-{facultyId}"}, method = RequestMethod.GET)
    public String studentList(ModelMap model, @PathVariable("courseId") int courseId, @PathVariable("facultyId") int facultyId) {
        List<User> users = userService.findAllStudents();

        model.addAttribute("users", users);
        model.addAttribute("courseId", courseId);

        int id = getLoggedInId();
        if (!courseService.isCourseTeacher(id)) {
            return "redirect:/courses-" + facultyId;
        }

        return "StudentsList";
    }


    @RequestMapping(value = {"/student-list-on-course-{courseId}-{facultyId}"}, method = RequestMethod.GET)
    public String studentListOnCourse(ModelMap model, @PathVariable("courseId") int courseId, @PathVariable("facultyId") int facultyId) {
        List<User> users = userService.findAllOnCourse(courseId);

        model.addAttribute("users", users);
        model.addAttribute("courseId", courseId);

        int id = getLoggedInId();
        if (!courseService.isCourseTeacher(id)) {
            return "redirect:/courses-" + facultyId;
        }

        return "StudentListOnCourse";
    }


    @RequestMapping(value = {"/add-student-to-course-{courseId}-{studentId}-{facultyId}"}, method = RequestMethod.GET)
    public String addStudentToCourse(ModelMap model, @PathVariable("courseId") int courseId,
                                     @PathVariable("studentId") int studentId, @PathVariable("facultyId") int facultyId) {
        studentCourseServise.addStudentToCourse(studentId, courseId);
        return "redirect:/student-list-" + courseId  + "-" + facultyId;
    }

    @RequestMapping(value = {"/delete-student-from-course-{courseId}-{studentId}-{facultyId}"}, method = RequestMethod.GET)
    public String deleteStudentFromCourse(ModelMap model, @PathVariable("courseId") int courseId,
                                          @PathVariable("studentId") int studentId, @PathVariable("facultyId") int facultyId) {

        studentCourseServise.deleteStudentFromCourse(studentId, courseId);
        return "redirect:/student-list-" + courseId + "-" + facultyId;
    }

    @RequestMapping(value = {"/set-due-date-{courseId}-{dueDate}"}, method = RequestMethod.GET)
    public String setDueDate(@PathVariable("courseId") int courseId, @PathVariable("courseId")Date dueDate) {

        return "";
    }

    private void saveDocument(FileBucket fileBucket, int id, int courseId) throws IOException{

        UserDocument document = new UserDocument();

        MultipartFile multipartFile = fileBucket.getFile();

        document.setName(multipartFile.getOriginalFilename());
        document.setDescription(fileBucket.getDescription());
        document.setType(multipartFile.getContentType());
        document.setContent(multipartFile.getBytes());

        userDocumentService.save(document, id, courseId);
    }


    private int getLoggedInId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        int id = 0;

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            user = userService.findByUsername(username);
            id = user.getId();
        }
        return id;
    }
}
