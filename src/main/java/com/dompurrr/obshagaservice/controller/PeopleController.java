package com.dompurrr.obshagaservice.controller;

import com.dompurrr.obshagaservice.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("people")
public class PeopleController {
    //------------
    private int peopleNum = 3;

    private static List<String> people = new ArrayList<>(){{
        add("Member1");
        add("Member2");
        add("Member3");
    }};

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
    }

    public static List<String> getPeople() {
        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }
    //---REQUESTS---
    @GetMapping
    public List<Map<String, String>> list() {
        List<Map<String, String>> temp = new ArrayList<>();
        for (int i = 0; i < getPeopleNum(); i++){
            int finalI = i;
            temp.add(new HashMap<>() {{ put("id", Integer.toString(finalI +1)); put("name", people.get(finalI)); }});
        }
        return temp;
    }

    @GetMapping("{id}")
    public Map<String, String> getOne(@PathVariable String id) {
        Map<String, String>                 temp = new HashMap<>() {{
            put("id", id);
            put("name", people.get(Integer.parseInt(id)-1));
        }};
        return temp;
    }

    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> message) {
        setPeopleNum(getPeopleNum()+1);
        getPeople().add(message.get("name"));
        return message;
    }

    @PutMapping("{id}")
    public Map<String, String> update(@PathVariable String id, @RequestBody Map<String, String> message) {
        getPeople().set(Integer.parseInt(id)-1, message.get("name"));
        Map<String, String> temp = new HashMap<>(){{
            put("id", id);
            put("name", message.get("name"));
        }};
        return temp;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        List<String> temp = getPeople();
        temp.remove(Integer.parseInt(id)-1);
        setPeople(temp);
        setPeopleNum(getPeopleNum()-1);
    }
}
