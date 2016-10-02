package fr.softeam.starpointsapp.util;

import fr.softeam.starpointsapp.domain.Activity;
import fr.softeam.starpointsapp.domain.Level;

public class ActivityBuilder {
    private Activity activity;

    public ActivityBuilder() {
        activity = new Activity();
    }

    public ActivityBuilder withName(String deliverableName) {
        activity.setName(deliverableName);
        return this;
    }

    public ActivityBuilder withLevel(Level level) {
        activity.setLevel(level);
        return this;
    }

    public Activity build(){
        return activity;
    }
}
