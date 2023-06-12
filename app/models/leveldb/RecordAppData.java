package models.leveldb;

import com.google.gson.JsonObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@NoArgsConstructor
public class RecordAppData extends Searchable {
    @ApiModelProperty(value = "json object", example = "", dataType = "models.leveldb.RecordAppDataDemo", required= true)
    private JsonObject content;
//    private JsonNode content;

    public RecordAppData(JsonObject content, Long time, Long time_end) {
        this.content = content;
        setTime(time);
        setTime_end(time_end);
    }
}
