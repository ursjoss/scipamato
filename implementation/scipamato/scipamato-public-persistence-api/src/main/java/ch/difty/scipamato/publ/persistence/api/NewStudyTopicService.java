package ch.difty.scipamato.publ.persistence.api;

import java.util.List;

import ch.difty.scipamato.publ.entity.NewStudyTopic;

public interface NewStudyTopicService {

    List<NewStudyTopic> findMostRecentNewStudyTopics();
}
