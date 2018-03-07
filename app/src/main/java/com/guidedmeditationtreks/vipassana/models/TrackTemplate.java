package com.guidedmeditationtreks.vipassana.models;


/**
 * Created by aerozero on 12/21/17.
 */

public class TrackTemplate {

    private String name;
    private String longName;
    private boolean isMultiPart;

    private String part1ResourceName;
    private String part2ResourceName;
    private int buttonId;
    private int spacerId;


    public TrackTemplate(String name, String longName, String part1ResourceName, String part2ResourceName) {

        this.name = name;
        this.longName = longName;
        this.part1ResourceName = part1ResourceName;

        try {
            if (part2ResourceName != null) {
                this.part2ResourceName = part2ResourceName;
                this.isMultiPart = true;
            } else {
                this.isMultiPart = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isMultiPart() {
        return isMultiPart;
    }

    public String getPart1ResourceName() {
        return part1ResourceName;
    }

    public String getPart2ResourceName() {
        return part2ResourceName;
    }

    public String getName() { return name; }

    public String getLongName() { return longName; }

    public int getButtonId() {
        return buttonId;
    }

    public void setButtonId(int buttonId) {
        this.buttonId = buttonId;
    }

    public int getSpacerId() {
        return spacerId;
    }

    public void setSpacerId(int spacerId) {
        this.spacerId = spacerId;
    }


}
