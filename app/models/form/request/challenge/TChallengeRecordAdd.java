package models.form.request.challenge;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import models.leveldb.RecordData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class TChallengeRecordAdd {
    @ApiModelProperty(value = "type name of challenge (application): mood_jouraling/ reduce_alcohol/ body_pain", example = "mood_jouraling", required= true)
    private String app_id;
    private RecordData data;
}
