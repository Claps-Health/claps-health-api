package models.twitter.response;

import lombok.Data;

@Data
public class TwitterUserInfoData {
    private String username;
    private String name;
    private String profile_image_url;
    private String description;
}
