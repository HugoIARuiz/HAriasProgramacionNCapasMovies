package com.HAriasMovies.Controller;

import com.HAriasMovies.ML.Movie;
import com.HAriasMovies.ML.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/Movie")
public class MovieController {

    private RestTemplate restTemplate = new RestTemplate();
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1MjEzYzMxM2VlOTI4ZjIzZjdlYzAwYjRjZmQ5MjlhYSIsIm5iZiI6MTc0NzMzNTE1Mi4wMTQwMDAyLCJzdWIiOiI2ODI2MzdmMGEwOGY3NzA1MDE2ZjFjMjIiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.tXAXRnrXj6yXTW46QlqUD06Qq8oPscsjerW-iOy_tkM";

    @GetMapping
    public String Popular(@RequestParam(required = false) Integer page, HttpSession session, Model model) {
        if (session.getAttribute("session_id") != null) {

            HttpHeaders httpHeader = new HttpHeaders();
            httpHeader.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(httpHeader);

            try {

                ResponseEntity<Result<Movie>> getMovies = restTemplate.exchange("https://api.themoviedb.org/3/movie/popular",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<Result<Movie>>() {
                });

                Result result = getMovies.getBody();

                model.addAttribute("listMovies", result.results);

                return "Index";
            } catch (HttpStatusCodeException ex) {
                model.addAttribute("status", ex.getStatusCode());
                model.addAttribute("message", ex.getMessage());
                return "ErrorPage";
            }
        } else {
            return "redirect:/login";
        }

    }
}
