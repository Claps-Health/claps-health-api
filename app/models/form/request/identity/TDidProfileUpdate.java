package models.form.request.identity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import play.data.validation.Constraints;

@Data
public class TDidProfileUpdate {
    @ApiModelProperty(value = "Year of birthday", example = "1990", required= true)
    private int year_of_birth;

    @ApiModelProperty(value = "Gender", example = "Male", required= false)
    private String gender;

    @ApiModelProperty(value = "Country of living", example = "USA", required= true)
    private String living_country;

    @ApiModelProperty(value = "City of living", example = "San francisco", required= true)
    private String living_city;
}
