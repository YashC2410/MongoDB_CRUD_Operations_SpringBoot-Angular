package com.neosoft.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Document(collection = "Employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    private long id;

    @NotBlank
    @Size(max=100)
    @Indexed(unique=true)
    private String firstName;
    private String lastName;

    private int age;

    @NotBlank
    @Size(max=100)
    @Indexed(unique=true)
    private String emailId;

    @Transient
    public static final String SEQUENCE_NAME="user_sequence";

    @Override
    public String toString() {
        final StringBuffer sb= new StringBuffer("Employee:{");
               sb.append("id=").append(id);
                sb.append(", firstName='").append(firstName).append( '\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", emailId='").append(emailId).append('\'');
         sb.append('}');
         return sb.toString();
    }
}
