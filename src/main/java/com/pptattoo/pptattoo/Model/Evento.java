package com.pptattoo.pptattoo.Model;

public class Evento {

    private String color;
    private String eventName;
    private int duration;


    public Evento(String eventName, int duration) {
        this.color = randomColor();

        this.eventName = eventName;
        this.duration = duration;
    }

    private String randomColor() {
        return new String();
    }

    public String getColor() { return color; }
    public String getEventName() { return eventName; }
    public int getDuration() { return duration; }


    public void setColor(String color) {
        this.color = color;

    }

    public void setEventName(String eventName) {
        this.eventName = eventName;

    }

    public void setDuration(int duration) {
        this.duration = duration;

    }
}
