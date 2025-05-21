package xyz.delartigue.vw.exception

class InvalidConsentException(consent: String): RuntimeException("$consent is not a valid consent")