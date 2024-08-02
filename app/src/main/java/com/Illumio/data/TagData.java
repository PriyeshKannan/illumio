package com.Illumio.data;


public class TagData implements Data {
    public final static String UNTAGGED = "Untagged";
    public final String tag;

    public TagData(String tag) {
        this.tag = tag;
    }

    @Override
    public String identifier() {
        return tag;
    }
}
