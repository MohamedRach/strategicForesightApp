package com.example.SearchService.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


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
    private String caption;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String img;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String likes;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String username;
}
