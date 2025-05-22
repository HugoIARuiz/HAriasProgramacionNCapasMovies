package com.HAriasMovies.Controller;

import com.HAriasMovies.DTO.Token;
import com.HAriasMovies.DTO.Session;
import com.HAriasMovies.ML.Client;
import com.HAriasMovies.ML.RequestLogin;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/User")
public class ClientController {

    private RestTemplate restTemplate = new RestTemplate();
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1MjEzYzMxM2VlOTI4ZjIzZjdlYzAwYjRjZmQ5MjlhYSIsIm5iZiI6MTc0NzMzNTE1Mi4wMTQwMDAyLCJzdWIiOiI2ODI2MzdmMGEwOGY3NzA1MDE2ZjFjMjIiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.tXAXRnrXj6yXTW46QlqUD06Qq8oPscsjerW-iOy_tkM";

    @GetMapping("/login")
    public String login(@ModelAttribute Client client, Model model) {
        model.addAttribute("client", client);
        return "Login";
    }

    @PostMapping("/login")
    public String Authentication(@ModelAttribute Client client, Model model, HttpSession session) {
        try {
            HttpHeaders httpHeader = new HttpHeaders();
            httpHeader.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(httpHeader);

            ResponseEntity<RequestLogin> response = restTemplate.exchange("https://api.themoviedb.org/3/authentication/token/new",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<RequestLogin>() {
            });

            if (response.getStatusCode().is2xxSuccessful()) {
                HttpHeaders httpHeaderSession = new HttpHeaders();
                httpHeaderSession.setBearerAuth(token);
                client.setRequest_token(response.getBody().request_token);
                HttpEntity<Client> entitySession = new HttpEntity<>(client, httpHeaderSession);
                ResponseEntity<RequestLogin> responseCreateSession = restTemplate.exchange("https://api.themoviedb.org/3/authentication/token/validate_with_login",
                        HttpMethod.POST, entitySession,
                        new ParameterizedTypeReference<RequestLogin>() {
                });
                if (responseCreateSession.getStatusCode().is2xxSuccessful()) {
                    String validToken = responseCreateSession.getBody().request_token;

                    HttpHeaders httpHeaderValidation = new HttpHeaders();
                    httpHeaderValidation.setBearerAuth(token);
                    httpHeaderValidation.setContentType(MediaType.APPLICATION_JSON);
                    Token tokenValid = new Token(validToken);
                    HttpEntity<Token> entityValid = new HttpEntity<>(tokenValid, httpHeaderValidation);
                    ResponseEntity<RequestLogin> responseValidate = restTemplate.exchange("https://api.themoviedb.org/3/authentication/session/new",
                            HttpMethod.POST, entityValid, new ParameterizedTypeReference<RequestLogin>() {
                    });
                    if (responseValidate.getStatusCode().is2xxSuccessful()) {
                        String sessionId = responseValidate.getBody().session_id;
                        session.setAttribute("session_id", sessionId);
                    }

                }

            }

        } catch (Exception e) {
        }
        return "redirect:/Movie";
    }
    
    @GetMapping
    public String LogOut(HttpSession session, Model model){
        String sessionId = session.getAttribute("session_id").toString();
        try {
            HttpHeaders httpHeader = new HttpHeaders();
            httpHeader.setBearerAuth(token);
            httpHeader.setContentType(MediaType.APPLICATION_JSON);
            
            Session sessionDTO = new Session(sessionId);
            HttpEntity<Session> entity = new HttpEntity<>(sessionDTO,httpHeader);
            
            ResponseEntity<RequestLogin> outSession = restTemplate.exchange("https://api.themoviedb.org/3/authentication/session", 
                    HttpMethod.DELETE, 
                    entity, 
                    new ParameterizedTypeReference<RequestLogin>(){});
            if(outSession.getStatusCode().is2xxSuccessful()){
                session.invalidate();
                return "redirect:/User/login";
            }
        } catch (Exception e) {
        }
        
        
        return "Index";
    }
    

}
