package fr.softeam.starpointsapp.service.util;

import fr.softeam.starpointsapp.service.dto.StarPointsByCommunityDTO;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour transformer les résultats des requete starpoints en DTO
 */
public class StarpointsUtil {

    public static final int COMMUNITY_INDEX = 0;
    public static final int STARPOINTS_INDEX = 1;

    /**
     * On construit la liste de StarPointsByCommunityDTO à partir de la liste de tableau d'objets retournée par la méthode de
     * starpoints repository.
     */
    public static List<StarPointsByCommunityDTO> buildStarPointsByCommunityDTO(List<Object[]> results) {
        List<StarPointsByCommunityDTO> starpointsList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(results)){
            starpointsList.addAll(results.stream().
                map(StarpointsUtil::buildStarPointsByCommunityDTO).
                collect(Collectors.toList()));
        }
        return starpointsList;
    }

    /**
     * Construit un objet de type StarPointsByCommunityDTO a Partir d'un tableau d'objet
     */
    private static StarPointsByCommunityDTO buildStarPointsByCommunityDTO(Object[] result) {
        String community = result[COMMUNITY_INDEX].toString();
        Long points = 0L;
        if (result[STARPOINTS_INDEX] != null) {
            points = Long.valueOf(result[STARPOINTS_INDEX].toString());
        }
        return new StarPointsByCommunityDTO(community, points);
    }
}
