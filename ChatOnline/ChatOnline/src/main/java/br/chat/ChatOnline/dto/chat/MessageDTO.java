package br.chat.ChatOnline.dto.chat;

import lombok.Builder;

@Builder
public record MessageDTO(String to, String message, String from) { }