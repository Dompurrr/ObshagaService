package com.dompurrr.obshagaservice.controller;

import com.dompurrr.obshagaservice.controller.PeopleController;
import com.dompurrr.obshagaservice.exceptions.NotFoundException;
import com.dompurrr.obshagaservice.exceptions.dutiesOverflowException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("duty")
public class DutyController {
    //Temporary local db (replace with PostgreSQL)
    private List<Map<String, String>> duties = new ArrayList<>() {{
        add(new HashMap<>() {{ put("id", "1"); put("name", PeopleController.getPeople().get(0)); }});
        add(new HashMap<>() {{ put("id", "2"); put("name", PeopleController.getPeople().get(1)); }});
        add(new HashMap<>() {{ put("id", "3"); put("name", PeopleController.getPeople().get(2)); }});
    }};
    //-----REQUESTS------
    @GetMapping
    public List<Map<String, String>> list() {
        return duties;
    }

    @GetMapping("{id}")
    public Map<String, String> getOne(@PathVariable String id) {
        return getDuty(id);
    }

    private Map<String, String> getDuty(@PathVariable String id) {
        return duties.stream()
                .filter(duty -> duty.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
