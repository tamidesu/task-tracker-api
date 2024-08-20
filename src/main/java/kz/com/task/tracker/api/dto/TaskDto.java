package kz.com.task.tracker.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDto {

    @NonNull
    Long id;

    @NonNull
    String name;

    @NonNull
    String description;

    @NonNull
    @JsonProperty("created_at")
    Instant createdAt;
}
