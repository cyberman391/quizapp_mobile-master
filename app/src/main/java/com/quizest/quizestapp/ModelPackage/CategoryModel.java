package com.quizest.quizestapp.ModelPackage;

public class CategoryModel {

    String name;
    int image;

    public CategoryModel(String name, int image) {
        this.name = name;
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
}
