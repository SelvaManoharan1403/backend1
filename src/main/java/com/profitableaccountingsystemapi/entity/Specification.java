package com.profitableaccountingsystemapi.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;
@Data
@Document(collection="specification")
public class Specification {
    @Id
    private String objectId;
    @Indexed(unique = true)
    private String specId;
    private Map<String, String> specification;
}



