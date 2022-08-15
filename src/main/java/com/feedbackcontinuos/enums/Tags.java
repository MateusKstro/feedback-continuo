package com.feedbackcontinuos.enums;

public enum Tags {

    JAVA("Java"),
    JAVASCRIPT("JavaScript"),
    BACKEND("Backend"),
    FRONTEND("Frontend"),
    IOS("IOS"),
    AGILE("Agile"),
    DATABASE("Database"),
    SPRING("Spring"),
    ANDROID("Android"),
    WEB("Web"),
    CSS("Css");

    private final String name;

    Tags(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
