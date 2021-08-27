package org.tsdes.cards.dto

import io.swagger.annotations.ApiModelProperty

class CardDto(
    @get:ApiModelProperty("ID for a card")
    var cardId: String? = null,

    @get:ApiModelProperty("name of the card")
    var name: String? = null,

    @get:ApiModelProperty("description of the card")
    var description: String? = null,

    @get:ApiModelProperty("the rarity of the card")
    var rarity: Rarity? = null,

    @get:ApiModelProperty("the id of the image for this card")
    var imageId: String? = null
)