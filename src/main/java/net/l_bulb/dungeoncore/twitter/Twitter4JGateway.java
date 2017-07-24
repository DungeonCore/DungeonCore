package net.l_bulb.dungeoncore.twitter;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.TwitterException;

public class Twitter4JGateway {

  public static void postTweet(String tweet) throws TwitterException {
    AsyncTwitterFactory factory = new AsyncTwitterFactory();
    AsyncTwitter twitter = factory.getInstance();
    twitter.updateStatus(tweet);
  }

}
