package com.example.apiprojectdiablodamo.API;

import java.util.List;

public class Skills {
    private List<Skill> active;
    private List<Skill> passive;

    public Skills(List<Skill> active, List<Skill> passive) {
        this.active = active;
        this.passive = passive;
    }

    public List<Skill> getActive() {
        return active;
    }

    public void setActive(List<Skill> active) {
        this.active = active;
    }

    public List<Skill> getPassive() {
        return passive;
    }

    public void setPassive(List<Skill> passive) {
        this.passive = passive;
    }

    // Constructor, getters y setters
}
