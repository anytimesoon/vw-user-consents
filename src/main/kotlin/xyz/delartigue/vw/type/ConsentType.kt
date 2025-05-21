package xyz.delartigue.vw.type

enum class ConsentType (val value: String) {
    EMAIL("email_notifications"),
    SMS("sms_notifications");

    companion object {
        infix fun from(value: String): ConsentType? = ConsentType.entries.firstOrNull { it.value == value }
    }
}