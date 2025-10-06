package com.hadeer.jetpackcomposepokemon.data.remote.response

import com.google.gson.annotations.SerializedName

data class AllPokemonResponse(

	@field:SerializedName("next")
	val next: Any? = null,

	@field:SerializedName("previous")
	val previous: Any? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("results")
	val results: List<PokemonItem?>? = null
)

data class PokemonItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
