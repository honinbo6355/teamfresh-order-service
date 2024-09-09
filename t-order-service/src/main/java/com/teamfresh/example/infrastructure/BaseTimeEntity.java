package com.teamfresh.example.infrastructure;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
