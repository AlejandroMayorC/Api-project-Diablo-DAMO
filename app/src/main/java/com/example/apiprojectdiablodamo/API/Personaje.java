package com.example.apiprojectdiablodamo.API;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.apiprojectdiablodamo.R;

import java.io.Serializable;
import java.util.List;

public class Personaje implements Serializable{
    private String slug;
    private String name;
    private String maleName;
    private String femaleName;
    private String icon;
    private List<SkillCategory> skillCategories;
    private Skills skills;

    public Personaje(String slug, String name, String maleName, String femaleName, String icon, List<SkillCategory> skillCategories, Skills skills) {
        this.slug = slug;
        this.name = name;
        this.maleName = maleName;
        this.femaleName = femaleName;
        this.icon = icon;
        this.skillCategories = skillCategories;
        this.skills = skills;
    }


    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaleName() {
        return maleName;
    }

    public void setMaleName(String maleName) {
        this.maleName = maleName;
    }

    public String getFemaleName() {
        return femaleName;
    }

    public void setFemaleName(String femaleName) {
        this.femaleName = femaleName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<SkillCategory> getSkillCategories() {
        return skillCategories;
    }

    public void setSkillCategories(List<SkillCategory> skillCategories) {
        this.skillCategories = skillCategories;
    }

    public Skills getSkills() {
        return skills;
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
    }
}
