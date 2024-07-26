package com.example.SearchService.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Data
@Document(indexName = "result")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {
    @Id
    private String Id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String source;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String keyword;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String caption;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String img;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String likes;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String username;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String href;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String sentiment;
    @Field(type = FieldType.Date, analyzer = "standard")
    private LocalDate date;


}
