package org.example.common.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;

    // getters/setters
}
