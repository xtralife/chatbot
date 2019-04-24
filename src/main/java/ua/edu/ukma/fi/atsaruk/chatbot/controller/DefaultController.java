package ua.edu.ukma.fi.atsaruk.chatbot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

  @GetMapping
  public String healthCheck() {
    return "I'm alive.";
  }
}
