package com.aimicor.rxmvp.presenter

import com.aimicor.rxmvp.model.*

val postList = listOf(
    Post(12, 34, "title1", "body1"),
    Post(56, 78, "title2", "body2")
)

val post = listOf(Post(91, 23, "title3", "body3"))

val userDataNoNulls = listOf(
    User(
        Address("city", Geo("lat", "long"), "street", "suite", "zip"),
        Company("bs", "catchPhrase", "name1"),
        "email1",
        123,
        "name2",
        "phone",
        "username",
        "website"
    )
)

val commentList = listOf(
    Comment("body4", "email2", 345, "name3", 678),
    Comment("body5", "email3", 910, "name4", 234),
    Comment("body6", "email4", 567, "name5", 891)
)