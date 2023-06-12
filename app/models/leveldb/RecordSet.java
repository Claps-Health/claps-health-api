package models.leveldb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.Utils;

import java.util.ArrayList;

@Data
//@Builder
@NoArgsConstructor
//@AllArgsConstructor
//@Jacksonized
public class RecordSet {
    private RecordData data;

    @ApiModelProperty(value = "time_creation", example = "1686022651000", required= false)
    private Long time_creation;

    public RecordSet(RecordData data) {
        this.data = data;
        setTime_creation(Utils.getNowTimeUtcLong(false));
    }

    public static ArrayList<RecordSet> searchListByTime(ArrayList<RecordSet> rss, Long time, Long time_end/*, boolean asc*/) {
        ArrayList<RecordSet> list= new ArrayList<>();
        if(rss==null || rss.isEmpty()) return list;

        for(int i= rss.size()-1;i>=0;i--) {
            if(rss.get(i).getData()==null) continue;
            if(rss.get(i).getData().getApp()==null) continue;
            if(rss.get(i).getData().getApp().getTime()==null) continue;
            if(time!=null && time.compareTo(rss.get(i).getData().getApp().getTime()) > 0) continue;
            if(time_end!=null && time_end.compareTo(rss.get(i).getData().getApp().getTime()) < 0) continue;
            list.add(rss.get(i));
        }

        //if(!asc) Collections.reverse(list);
        return list;
    }
}
