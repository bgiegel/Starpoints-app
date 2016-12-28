package fr.softeam.starpointsapp.util;

import fr.softeam.starpointsapp.domain.Activity;
import fr.softeam.starpointsapp.domain.Community;
import fr.softeam.starpointsapp.domain.Contribution;
import fr.softeam.starpointsapp.domain.User;
import fr.softeam.starpointsapp.domain.enumeration.ContributionStatusType;

import java.time.LocalDate;

public class ContributionBuilder {
    private static final String DEFAULT_NAME = "Default name";

    private Contribution contribution;

    public ContributionBuilder(Activity activity, Community community, User author) {
        contribution = new Contribution();
        contribution.setDeliverableDate(LocalDate.now());
        contribution.setDeliverableName(DEFAULT_NAME);
        contribution.setActivity(activity);
        contribution.setCommunity(community);
        contribution.setAuthor(author);
    }

    public ContributionBuilder withDeliverableName(String deliverableName) {
        contribution.setDeliverableName(deliverableName);
        return this;
    }

    public ContributionBuilder withDeliverableDate(LocalDate date) {
        contribution.setDeliverableDate(date);
        return this;
    }

    public ContributionBuilder withDeliverableUrl(String url) {
        contribution.setDeliverableUrl(url);
        return this;
    }

    public ContributionBuilder withComment(String comment) {
        contribution.setComment(comment);
        return this;
    }

    public ContributionBuilder withPreparatoryDate1(LocalDate date) {
        contribution.setPreparatoryDate1(date);
        return this;
    }

    public ContributionBuilder withPreparatoryDate2(LocalDate date) {
        contribution.setPreparatoryDate2(date);
        return this;
    }

    public ContributionBuilder withStatus(ContributionStatusType status) {
        contribution.setStatus(status);
        return this;
    }

    public Contribution build(){
        return contribution;
    }
}
