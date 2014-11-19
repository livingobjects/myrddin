package com.livingobjects.myrddin;

public final class SecurityScheme {

    public final String title;

    public final String name;

    public final String type;

    public final String locatedIn;

    public SecurityScheme(String title, String name, String type, String locatedIn) {
        this.title = title;
        this.name = name;
        this.type = type;
        this.locatedIn = locatedIn;
    }
}
