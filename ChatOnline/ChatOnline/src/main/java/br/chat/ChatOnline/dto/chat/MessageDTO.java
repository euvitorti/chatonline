package br.chat.ChatOnline.dto.chat;

import lombok.Builder;

@Builder
public record MessageDTO(String message, String from, String image) { }