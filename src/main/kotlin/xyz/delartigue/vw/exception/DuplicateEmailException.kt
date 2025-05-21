package xyz.delartigue.vw.exception

class DuplicateEmailException(email: String) : RuntimeException("User with email $email already exists")
