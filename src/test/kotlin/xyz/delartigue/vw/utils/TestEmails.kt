package xyz.delartigue.vw.utils

val validEmails: List<String> = listOf(
    "email@example.com",
    "firstname.lastname@example.com",
    "email@subdomain.example.com",
    "firstname+lastname@example.com",
    "email@123.123.123.123",
    "1234567890@example.com",
    "email@example-one.com"
)

val invalidEmails: List<String> = listOf(
    "plainaddress",
    "#@%^%#$@#$@#.com",
    "@example.com",
    "Joe Smith <email@example.com>",
    "email.example.com",
    "email@example@example.com",
    ".email@example.com",
    "email.@example.com",
    "email..email@example.com",
    "あいうえお@example.com",
    "email@example.com (Joe Smith)",
    "email@-example.com",
    "email@example..com",
    "Abc..123@example.com",
    "”(),:;<>[\\]@example.com",
    "just”not”right@example.com",
    "this\\ is\"really\"not\\allowed@example.com"
)