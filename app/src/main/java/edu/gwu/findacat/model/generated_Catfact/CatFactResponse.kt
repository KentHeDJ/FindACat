package edu.gwu.findacat.model.generated_Catfact

import com.squareup.moshi.Json

data class CatFactResponse(

	@Json(name="fact")
	val fact: String? = null,

	@Json(name="length")
	val length: Int? = null
)