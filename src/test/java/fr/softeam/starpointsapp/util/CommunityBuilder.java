package fr.softeam.starpointsapp.util;

import fr.softeam.starpointsapp.domain.Community;
import fr.softeam.starpointsapp.domain.User;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CommunityBuilder {
    private static final String DEFAULT_NAME = "Default name";

    private Community community;

    public CommunityBuilder(User leader){
        community = new Community();
        community.setName(DEFAULT_NAME);
        community.setLeader(leader);
    }

    public CommunityBuilder withName(String name) {
        community.setName(name);
        return this;
    }

    public Community build() {
        return community;
    }

    public CommunityBuilder withMembers(User... members) {
        community.setMembers(Arrays.stream(members).collect(Collectors.toSet()));
        return this;
    }
}
