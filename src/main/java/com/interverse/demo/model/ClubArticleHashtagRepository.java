package com.interverse.demo.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubArticleHashtagRepository  extends JpaRepository<ClubArticleHashtag,Integer>{

    Optional<ClubArticleHashtag> findByTag(String tag);

}
