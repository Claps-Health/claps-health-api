package models.twitter.response;

import lombok.Data;

import java.util.List;

@Data
public class TweetInfoData {
    private String text;
    private String id;
    private List<String> edit_history_tweet_ids;
    private String author_id;
}
