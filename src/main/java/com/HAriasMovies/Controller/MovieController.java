package com.HAriasMovies.Controller;

import com.HAriasMovies.ML.FavoriteRequest;
import com.HAriasMovies.ML.Movie;
import com.HAriasMovies.ML.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/Movie")
public class MovieController {

    private RestTemplate restTemplate = new RestTemplate();
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1MjEzYzMxM2VlOTI4ZjIzZjdlYzAwYjRjZmQ5MjlhYSIsIm5iZiI6MTc0NzMzNTE1Mi4wMTQwMDAyLCJzdWIiOiI2ODI2MzdmMGEwOGY3NzA1MDE2ZjFjMjIiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.tXAXRnrXj6yXTW46QlqUD06Qq8oPscsjerW-iOy_tkM";

    @GetMapping
    public String Popular(HttpSession session, Model model) {
        if (session.getAttribute("session_id") != null) {

            HttpHeaders httpHeader = new HttpHeaders();
            httpHeader.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(httpHeader);

            try {
                ResponseEntity<Result<Movie>> listMovie = restTemplate.exchange("https://api.themoviedb.org/3/movie/popular",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<Result<Movie>>() {
                });

                Result result = listMovie.getBody();

                model.addAttribute("listMovies", result.results);

                return "Index";
            } catch (HttpStatusCodeException ex) {
                model.addAttribute("status", ex.getStatusCode());
                model.addAttribute("message", ex.getMessage());
                return "ErrorPage";
            }
        } else {
            return "redirect:/User/login";
        }

    }

    @PostMapping("/favorite")
    @ResponseBody
    public ResponseEntity<String> Favorite(@RequestParam Long movieId,@RequestParam boolean favorite,HttpSession session) {

        String sessionId = (String) session.getAttribute("session_id");
        if (sessionId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            

            String accountId = "22012958";

            FavoriteRequest favoriteRequest = new FavoriteRequest(movieId, favorite);

            HttpHeaders favoriteHeaders = new HttpHeaders();
            favoriteHeaders.setBearerAuth(token);
            favoriteHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<FavoriteRequest> favoriteEntity = new HttpEntity<>(favoriteRequest, favoriteHeaders);

            String url = "https://api.themoviedb.org/3/account/"+accountId+"/favorite";

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    favoriteEntity,
                    String.class);

            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/details/{id}")
    public String MovieDetails(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("session_id") != null) {
            HttpHeaders httpHeader = new HttpHeaders();
            httpHeader.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(httpHeader);

            try {
                ResponseEntity<Movie> response = restTemplate.exchange(
                        "https://api.themoviedb.org/3/movie/" + id,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<Movie>() {});
                Movie movie = response.getBody();
                model.addAttribute("movie", movie);
                return "Movie";
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
