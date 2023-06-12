package models.leveldb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Searchable {
    @ApiModelProperty(value = "time", example = "1686022651000", required= false)
    private Long time;
    @ApiModelProperty(value = "time_end", example = "null", required= false)
    private Long time_end;
}
