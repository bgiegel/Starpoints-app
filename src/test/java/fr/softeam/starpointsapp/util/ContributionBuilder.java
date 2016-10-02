package fr.softeam.starpointsapp.util;

import fr.softeam.starpointsapp.domain.Activity;
import fr.softeam.starpointsapp.domain.Community;
import fr.softeam.starpointsapp.domain.Contribution;

public class ContributionBuilder {
    private Contribution contribution;

    public ContributionBuilder() {
        contribution = new Contribution();
    }

    public ContributionBuilder withName(String deliverableName) {
        contribution.setDeliverableName(deliverableName);
        return this;
    }

    public ContributionBuilder withActivity(Activity activity) {
        contribution.setActivity(activity);
        return this;
    }

    public ContributionBuilder withCommunity(Community community) {
        contribution.setCommunity(community);
        return this;
    }

    public Contribution build(){
        return contribution;
    }
}
