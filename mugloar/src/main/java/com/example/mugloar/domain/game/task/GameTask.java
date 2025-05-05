package com.example.mugloar.domain.game.task;

import com.example.mugloar.common.constants.TaskSuccessProbability;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record GameTask(
        @NonNull
        String taskId,
        int reward,
        int expiresIn,
        Integer encrypted,
        TaskSuccessProbability taskSuccessProbability
) { }
