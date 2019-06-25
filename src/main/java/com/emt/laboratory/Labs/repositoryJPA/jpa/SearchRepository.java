package com.emt.laboratory.Labs.repositoryJPA.jpa;

import java.util.List;

public interface SearchRepository {

    <T> List<T> searchKeyword(Class<T> entityClass, String text, String... fields);

    <T> List<T> searchPhrase(Class<T> entityClass,
                             String text,
                             String... fields);

}
