package fr.softeam.starpointsapp.service.util;

import fr.softeam.starpointsapp.service.dto.QuarterDTO;
import fr.softeam.starpointsapp.service.exception.QuarterFormatException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuarterUtilTest {

    @Test
    public void should_convert_quarter_string_to_quarter_dto() throws Exception {
        //when
        QuarterDTO quarterDTO = QuarterUtil.convertToQuarterDTO("Q3-2016");

        QuarterDTO expectedQuarter = new QuarterDTO(2016, 7, 9);
        assertThat(quarterDTO).isEqualTo(expectedQuarter);
    }

    @Test(expected = QuarterFormatException.class)
    public void should_not_convert_quarter_string_when_quarter_id_is_wrong() throws Exception {
        //when
        QuarterUtil.convertToQuarterDTO("Q7-2016");
    }

    @Test(expected = QuarterFormatException.class)
    public void should_not_convert_quarter_string_when_quarter_year_is_wrong() throws Exception {
        //when
        QuarterUtil.convertToQuarterDTO("Q3-fsdfdsf");
    }

    @Test(expected = QuarterFormatException.class)
    public void should_not_convert_quarter_string_when_quarter_is_wrong() throws Exception {
        //when
        QuarterUtil.convertToQuarterDTO("fsdfdsf");
    }

    @Test(expected = QuarterFormatException.class)
    public void should_not_convert_quarter_string_when_quarter_is_null() throws Exception {
        //when
        QuarterUtil.convertToQuarterDTO(null);
    }
}
