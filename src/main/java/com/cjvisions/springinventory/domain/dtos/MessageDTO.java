package com.cjvisions.springinventory.domain.dtos;

import org.springframework.http.HttpStatus;

public record MessageDTO(String message, HttpStatus status, Object payload) {
}
