package fr.softeam.starpointsapp.service.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Représentation d'un trimestre d'une année.
 */
public class QuarterDTO {
    private Integer year;
    private Integer startMonth;
    private Integer endMonth;

    public QuarterDTO(Integer year, Integer startMonth, Integer endMonth) {
        this.year = year;
        this.startMonth = startMonth;
        this.endMonth = endMonth;
    }

    public Integer getYear() {
        return year;
    }

    public Integer getStartMonth() {
        return startMonth;
    }

    public Integer getEndMonth() {
        return endMonth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        QuarterDTO that = (QuarterDTO) o;

        return new EqualsBuilder()
            .append(year, that.year)
            .append(startMonth, that.startMonth)
            .append(endMonth, that.endMonth)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(year)
            .append(startMonth)
            .append(endMonth)
            .toHashCode();
    }

    @Override
    public String toString() {
        return "QuarterDTO{" +
            "year=" + year +
            ", startMonth=" + startMonth +
            ", endMonth=" + endMonth +
            '}';
    }
}
