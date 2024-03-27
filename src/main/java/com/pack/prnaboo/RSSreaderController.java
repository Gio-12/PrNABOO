package com.pack.prnaboo;

import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.pack.prnaboo.Directory.addNews;
import static com.pack.prnaboo.Directory.newsExist;

public class RSSreaderController {
    public static void read(String feedUrl) throws IOException, FeedException { //ArrayList<News>
        URL feedSource = new URL(feedUrl);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedSource));
        Iterator itr = feed.getEntries().iterator();
        while (itr.hasNext()) {
            SyndEntry syndEntry = (SyndEntry) itr.next();
            News newsToAdd = mapToArticle(syndEntry);
            if (!newsExist(newsToAdd.getTitle(), newsToAdd.getTimestamp())){
                addNews(newsToAdd);
            }
        }
    }
    /**
     * Map to Article
     * @param syndEntry
     */
    private static News mapToArticle(SyndEntry syndEntry) {
        News newsArticle = new News();
        newsArticle.setTitle(syndEntry.getTitle());
        newsArticle.setTimestamp(syndEntry.getPublishedDate().toString());
        newsArticle.setDescription(syndEntry.getDescription().getValue());
        newsArticle.setAuthor(syndEntry.getAuthor());
        newsArticle.setSources(toStringList(syndEntry.getContributors())); // DA SISTEMARE QUESTO PROBABILMENTE è MEGLIO PRENDERE IL LINK E OTTENERE INFO
        newsArticle.setLink(syndEntry.getLink());
        newsArticle.setImagePath("");
        newsArticle.setCategories(toStringList(syndEntry.getCategories()));
        newsArticle.setComments(new ArrayList<String>());
        newsArticle.setVotes(new ArrayList<Integer>());

        return newsArticle;

    }

    public static String toStringList(List<SyndCategoryImpl> list){
        String listS = "";
        for (int i = 0; i < list.size(); i++){
            String name = list.get(i).getName();
            listS += name + "\n";
        }
        return listS;
    }
}
