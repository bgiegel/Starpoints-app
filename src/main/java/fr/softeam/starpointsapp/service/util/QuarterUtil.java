package fr.softeam.starpointsapp.service.util;

import fr.softeam.starpointsapp.service.dto.QuarterDTO;
import fr.softeam.starpointsapp.service.exception.QuarterFormatException;

import java.util.HashMap;
import java.util.Map;

/**
 * Contient des méthodes permettant d'effectuer des traitements relatifs aux trimestres.
 */
public class QuarterUtil {

    private static final Map<String, MonthRange> MONTH_RANGE_MAP = new HashMap<>();
    public static final String QUARTER_YEAR_SEPARATOR = "-";

    static {
        MONTH_RANGE_MAP.put("Q1", new MonthRange(1, 3));
        MONTH_RANGE_MAP.put("Q2", new MonthRange(4, 6));
        MONTH_RANGE_MAP.put("Q3", new MonthRange(7, 9));
        MONTH_RANGE_MAP.put("Q4", new MonthRange(10, 12));
    }

    /**
     * Permet de générer un objet de type QuarterDTO à partir d'une représentation sous forme de chaine de caractère d'un trimestre
     * (ex : Q4-2016).
     */
    public static QuarterDTO convertToQuarterDTO(String quarter){
        if (quarter == null) {
            throw new QuarterFormatException();
        }

        String quarterId = quarter.split(QUARTER_YEAR_SEPARATOR)[0];

        MonthRange monthRange = MONTH_RANGE_MAP.get(quarterId);
        if (monthRange == null) {
            throw new QuarterFormatException();
        }

        Integer year;
        try {
            year = Integer.valueOf(quarter.split(QUARTER_YEAR_SEPARATOR)[1]);
        } catch (NumberFormatException e) {
            throw new QuarterFormatException();
        }

        return new QuarterDTO(year, monthRange.getStart(), monthRange.getEnd());
    }

    private static class MonthRange {
        private Integer start;
        private Integer end;

        public MonthRange(Integer start, Integer end) {
            this.start = start;
            this.end = end;
        }

        public Integer getStart() {
            return start;
        }

        public Integer getEnd() {
            return end;
        }
    }
}
