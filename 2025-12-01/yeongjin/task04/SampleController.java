package com.example.bootdailymission.springBasic.task04;

import com.example.bootdailymission.daily1001.PollService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SampleController {

    private final CalculatorService calculatorService;

    public SampleController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/sample")
    public String sample() {
        calculatorService.performCalculations();
        return "redirect:/";
    }
}
