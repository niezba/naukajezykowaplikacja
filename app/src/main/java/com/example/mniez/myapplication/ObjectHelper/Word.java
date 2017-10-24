package com.example.mniez.myapplication.ObjectHelper;

/**
 * Created by mniez on 15.10.2017.
 */

public class Word {

    Integer id;
    String nativeDefinition;
    Integer nativeLanguageId;
    String nativeSound;
    String nativeWord;
    String picture;
    String tags;
    String translatedDefinition;
    Integer translatedLanguageId;
    String translatedSound;
    String translatedWord;
    Integer creatorId;

    public Word() {
    }

    public Word(Integer id, String nativeDefinition, Integer nativeLanguageId, String nativeSound, String nativeWord, String picture, String tags, String translatedDefinition, Integer translatedLanguageId, String translatedSound, String translatedWord, Integer creatorId) {
        this.id = id;
        this.nativeDefinition = nativeDefinition;
        this.nativeLanguageId = nativeLanguageId;
        this.nativeSound = nativeSound;
        this.nativeWord = nativeWord;
        this.picture = picture;
        this.tags = tags;
        this.translatedDefinition = translatedDefinition;
        this.translatedLanguageId = translatedLanguageId;
        this.translatedSound = translatedSound;
        this.translatedWord = translatedWord;
        this.creatorId = creatorId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNativeDefinition() {
        return nativeDefinition;
    }

    public void setNativeDefinition(String nativeDefinition) {
        this.nativeDefinition = nativeDefinition;
    }

    public Integer getNativeLanguageId() {
        return nativeLanguageId;
    }

    public void setNativeLanguageId(Integer nativeLanguageId) {
        this.nativeLanguageId = nativeLanguageId;
    }

    public String getNativeSound() {
        return nativeSound;
    }

    public void setNativeSound(String nativeSound) {
        this.nativeSound = nativeSound;
    }

    public String getNativeWord() {
        return nativeWord;
    }

    public void setNativeWord(String nativeWord) {
        this.nativeWord = nativeWord;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTranslatedDefinition() {
        return translatedDefinition;
    }

    public void setTranslatedDefinition(String translatedDefinition) {
        this.translatedDefinition = translatedDefinition;
    }

    public Integer getTranslatedLanguageId() {
        return translatedLanguageId;
    }

    public void setTranslatedLanguageId(Integer translatedLanguageId) {
        this.translatedLanguageId = translatedLanguageId;
    }

    public String getTranslatedSound() {
        return translatedSound;
    }

    public void setTranslatedSound(String translatedSound) {
        this.translatedSound = translatedSound;
    }

    public String getTranslatedWord() {
        return translatedWord;
    }

    public void setTranslatedWord(String translatedWord) {
        this.translatedWord = translatedWord;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }
}
