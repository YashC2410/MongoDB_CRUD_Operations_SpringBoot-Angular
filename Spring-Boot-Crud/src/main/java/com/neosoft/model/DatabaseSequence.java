package com.neosoft.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection="database_sequence")
public class DatabaseSequence {

    @Id
    private String id;
    private long seq;
}
