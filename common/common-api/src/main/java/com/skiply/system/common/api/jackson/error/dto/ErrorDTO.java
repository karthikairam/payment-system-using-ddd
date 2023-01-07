package com.skiply.system.common.api.jackson.error.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ErrorDTO(String code, List<String> messages) {}
