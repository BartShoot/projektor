package top.cinema.app.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    /**
     * This endpoint is designed to be called by HTMX.
     * It returns an HTML fragment, not a full page.
     */
    @GetMapping("/api/users")
    public String getUsers(Model model, @RequestHeader(value = "HX-Request", required = false) String hxRequest) {
        // Dummy data for demonstration
        List<String> users = List.of("Alice", "Bob", "Charlie");
        model.addAttribute("users", users);
        model.addAttribute("timestamp", Instant.now().toString());

        // If it's an HTMX request, return the fragment. Otherwise, you could redirect or show an error.
        return "admin/fragments :: user-list";
    }
}