package models.twitter;

import models.twitter.response.TweetInfo;
import models.twitter.response.TwitterUserInfo;
import models.twitter.response.TwitterApiConstants;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import utils.HttpUtils;

import java.util.*;

public class TwitterApiWrapper extends TwitterApiAuthorization {

    public TwitterApiWrapper(String api_key) {
        super(api_key);
    }

    public class TweetParser {
        private String link;
        private String tweet_id;

        TweetParser(String link) {
            this.link = link;
            parseLink(link);
        }

        public void parseLink(String link) {
            if(link==null || link.isEmpty()) return;
            String[] datas= link.split("/");
            if(datas.length==0) return;

            this.tweet_id= datas[datas.length-1];
        }

        public TweetInfo getTweetInfo() {
            String tweet_url= TwitterApiConstants.TWITTER_API_HOST + "/tweets/"+tweet_id+"?tweet.fields=author_id";
            try {
                List<NameValuePair> headers= Arrays.asList(new BasicNameValuePair("Authorization", "Bearer "+getApi_key()));
                TweetInfo ti= (TweetInfo)HttpUtils.getWithHeadersResponseJsonIgnoreSSL(tweet_url, headers, TweetInfo.class);
                return ti;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public String getLink() {
            return link;
        }

        public String getTweet_id() {
            return tweet_id;
        }
    }

    public TweetParser createTweetParser(String link) {
        return new TweetParser(link);
    }

    public TwitterUserInfo getUserInfo(String author_id) {
        String tweet_url= TwitterApiConstants.TWITTER_API_HOST + "/users/"+author_id+"?user.fields=name,username,profile_image_url,description";
        try {
            List<NameValuePair> headers= Arrays.asList(new BasicNameValuePair("Authorization", "Bearer "+getApi_key()));
            TwitterUserInfo ui= (TwitterUserInfo)HttpUtils.getWithHeadersResponseJsonIgnoreSSL(tweet_url, headers, TwitterUserInfo.class);
            return ui;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
