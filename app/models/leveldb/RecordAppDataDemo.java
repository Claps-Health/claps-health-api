package models.leveldb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RecordAppDataDemo {
    @ApiModelProperty(value = "key of content", example = "happy", required= true)
    private String mood;
}
