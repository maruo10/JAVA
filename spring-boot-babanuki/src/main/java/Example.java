// import org.springframework.boot.*;
// import org.springframework.boot.autoconfigure.*;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.ui.Model;
// import org.springframework.stereotype.Controller;

// @Controller
// @EnableAutoConfiguration
// public class Example {

//     @RequestMapping("/")
//     String home(@RequestParam(value="name", required=false, defaultValue="masa") String name, Model model) {
//         model.addAttribute("name", name);
//         return "home";
//     }

//     @RequestMapping("/hello")
//     public String hello(@RequestParam(value="name", required=false, defaultValue="masa") String name, Model model) {
//         model.addAttribute("name", name);
// 	    return "hello";
//     }

//     public static void main(String[] args) {
//         SpringApplication.run(Example.class, args);
//     }

// }
