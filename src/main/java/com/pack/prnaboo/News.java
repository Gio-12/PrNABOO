package com.pack.prnaboo;

import java.util.ArrayList;

public class News {

    private String title;
    private String timestamp;
    private String description;
    private String author;
    private String sources;
    private String link;
    private String imagePath;
    private String categories;
    private ArrayList<String> comments;
    private ArrayList<Integer> votes;
    private int id;
    private double vote;



    public News() {
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public void setVotes(ArrayList<Integer> votes) {
        this.votes = votes;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public News(String title, String timestamp, String description, String author, String sources, String link, String imagePath, String Categories) {
        this.title = title;
        this.timestamp = timestamp;
        this.description = description;
        this.author = author;
        this.sources = sources;
        this.link = link;
        this.imagePath = imagePath;
        this.categories = Categories;
        this.vote = getVote();
        this.comments = new ArrayList<String>();
        this.votes = new ArrayList<Integer>();
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String toStringNews(){
        return id + "\n" + title + "\n" + timestamp + "\n" + categories + "\n" + description + "\n" +  sources + "\n" + link;
    }

    //gestione voti e commenti

    public void addVote(String vote){
        int voteParsed = Integer.parseInt(vote);
        if (voteParsed > 10){
            voteParsed = 10;
        }
        if (voteParsed < 0){
            voteParsed = 0;
        }
        votes.add(voteParsed);
    }

    public double getVote(){
        double vote = 0;
        if (votes == null || votes.isEmpty()){
            vote = 0;
        } else {
            for (Integer i : votes) {
                vote += i;
            }
            vote = vote / votes.size();
        }
        return vote;
    }

    public void addComment(String comment){
        comments.add(comment);
        //callBack();
    }
    public ArrayList<String> getComments(){
        return comments;
    }

    public String toStringComments(){
        String commentToString = "";
        if (comments.isEmpty() || comments == null){
            commentToString = "Nessun Commento";
        }
        for (String comment : comments){
            commentToString += comment + "\n";
        }
        return commentToString;
    }
}
