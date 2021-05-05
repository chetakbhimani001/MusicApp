package com.example.test.models

import org.simpleframework.xml.*

@Root(name = "feed", strict = false)
data class FeedResponse @JvmOverloads constructor(

    @field:ElementList(entry = "entry", inline = true, required = true)
    var feed: List<FeedEntry>? = null

)

@Root(strict = false)
data class FeedEntry @JvmOverloads constructor(

    @field:Attribute(name = "id", required = true)
    @field:org.simpleframework.xml.Path("id")
    var id: Long? = null,

    @field:Element(name = "title", required = true)
    var title: String? = null,

    @field:Element(name = "name", required = true)
    var name: String? = null,

    @field:Element(name = "artist")
    var artist: Artist? = null,

    @field:ElementList(entry = "link", inline = true, required = true)
    var links: List<PlaybackLink>? = null,

    @field:ElementList(entry = "image", inline = true, required = true)
    var images: List<PlaybackImages>? = null,

    @field:Element(name = "rights", required = true)
    var rights: String? = null,

    @field:Element(name = "updated", required = false)
    var updateAt: String? = null,
)

@Root(strict = false)
data class Artist @JvmOverloads constructor(

    @field:Attribute(name = "href", required = true)
    var image: String? = null,

    @field:Text()
    var value: String? = null
)

@Root(strict = false)
data class PlaybackLink @JvmOverloads constructor(

    @field:Attribute(name = "href", required = true)
    var url: String? = null,

    @field:Attribute(name = "type", required = true)
    var type: String? = null,

    @field:Element(name = "duration", required = false)
    var duration: Int? = null
)

@Root(strict = false)
data class PlaybackImages @JvmOverloads constructor(

    @field:Attribute(name = "height", required = true)
    var height: Int? = null,

    @field:Text()
    var value: String? = null,
)
