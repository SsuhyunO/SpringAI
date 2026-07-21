package com.example.ch04.controller;

import com.example.ch04.dto.HotelDTO;
import com.example.ch04.service.GenericBeanOutputConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class GenericBeanOutputConverterController {
    private final GenericBeanOutputConverterService service;

    @GetMapping("/ai/generic-bean-output-converter")
    public String genericBeanOutputConverter(){
        return "/generic-bean-output-converter";
    }

    @ResponseBody
    @PostMapping("/ai/generic-bean-output-converter")
    public List<HotelDTO> genericBeanOutputConverter(@RequestParam("cities")String cities){
        //List<HotelDTO> hotel = service.convertLowLevel(cities);
        List<HotelDTO> hotelList = service.convertHighLevel(cities);

        return hotelList;
    }
}
