package kz.com.task.tracker.api.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AckDto {

    Boolean answer;

    public static AckDto makeDefault(Boolean answer) {
        return AckDto.builder().answer(answer).build();
    }
}
