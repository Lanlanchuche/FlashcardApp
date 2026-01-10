package com.mycompany.flashcardapp.model;

import javafx.beans.property.*;

public class Flashcard {
    private final IntegerProperty id;
    private final IntegerProperty userId;
    private final StringProperty vocabulary;
    private final StringProperty definition;
    private final BooleanProperty isLearned;

    public Flashcard() {
        this(0, 0, "", "", false);
    }

    public Flashcard(int id, int userId, String vocabulary, String definition, boolean isLearned) {
        this.id = new SimpleIntegerProperty(id);
        this.userId = new SimpleIntegerProperty(userId);
        this.vocabulary = new SimpleStringProperty(vocabulary);
        this.definition = new SimpleStringProperty(definition);
        this.isLearned = new SimpleBooleanProperty(isLearned);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getUserId() {
        return userId.get();
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }

    public String getVocabulary() {
        return vocabulary.get();
    }

    public StringProperty vocabularyProperty() {
        return vocabulary;
    }

    public String getDefinition() {
        return definition.get();
    }

    public StringProperty definitionProperty() {
        return definition;
    }

    public boolean isLearned() {
        return isLearned.get();
    }

    public BooleanProperty isLearnedProperty() {
        return isLearned;
    }

    public void setLearned(boolean isLearned) {
        this.isLearned.set(isLearned);
    }

    @Override
    public String toString() {
        return "Flashcard{" +
                "id=" + getId() +
                ", userId=" + getUserId() +
                ", vocabulary='" + getVocabulary() + '\'' +
                ", definition='" + getDefinition() + '\'' +
                ", isLearned=" + isLearned() +
                '}';
    }
}
