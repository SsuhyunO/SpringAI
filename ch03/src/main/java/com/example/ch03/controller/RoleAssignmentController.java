package com.example.ch03.controller;

import com.example.ch03.service.RoleAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RoleAssignmentController {
    private final RoleAssignmentService service;

    @GetMapping("/ai/role-assignment")
    public String roleAssignment(){
        return "/role-assignment";
    }

    @PostMapping(
            value = "/ai/role-assignment",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<String> roleAssignment(
            @RequestParam("requirements") String requirements){
        Flux<String> travelSuggestions = service.roleAssignment(requirements);
        return travelSuggestions;
    }
}
