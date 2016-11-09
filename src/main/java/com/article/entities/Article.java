package com.article.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;

/**
 * Created by musaceylan on 11/8/16.
 */
@Document(indexName = "name" ,type = "article",  replicas = 0)
public class Article {
    @Id
    private String id;


    private String title;
    private String publisher;
    private String source;



    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss.SSS'Z'")
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time, index = FieldIndex.not_analyzed)
    @CreatedDate
    private Date publishDate;

}
