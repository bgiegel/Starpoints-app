package fr.softeam.starpointsapp.util;

import fr.softeam.starpointsapp.domain.Activity;
import fr.softeam.starpointsapp.domain.Level;
import fr.softeam.starpointsapp.domain.enumeration.ActivityType;

public class ActivityBuilder {
    public static final String DEFAULT_NAME = "Default name";
    private Activity activity;

    public ActivityBuilder() {
        activity = new Activity();
        activity.setName(DEFAULT_NAME);
        activity.setType(ActivityType.BLOG_POST);
    }

    public ActivityBuilder(String name, ActivityType type){
        activity = new Activity();
        activity.setName(name);
        activity.setType(type);
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

    public ActivityBuilder withDeliverableDefinition(String defaultDeliverableDefinition) {
        activity.setDeliverableDefinition(defaultDeliverableDefinition);
        return this;
    }
}
