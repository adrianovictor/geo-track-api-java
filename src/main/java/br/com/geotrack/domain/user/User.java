package br.com.geotrack.domain.user;

public record User(Long id, String username, String passwordHash, Role role, boolean enabled) {}
