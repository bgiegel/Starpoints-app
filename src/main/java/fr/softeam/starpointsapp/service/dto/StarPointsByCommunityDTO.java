package fr.softeam.starpointsapp.service.dto;

public class StarPointsByCommunityDTO {
    private String community;
    private Long starpoints;

    public StarPointsByCommunityDTO(String community, Long starpoints) {
        this.community = community;
        this.starpoints = starpoints;
    }

    public String getCommunity() {
        return community;
    }

    public Long getStarpoints() {
        return starpoints;
    }


    @Override
    public String toString() {
        return "StarPointsByCommunityDTO{" +
            "community='" + community + '\'' +
            ", starpoints='" + starpoints + '\'' +
            '}';
    }


}
