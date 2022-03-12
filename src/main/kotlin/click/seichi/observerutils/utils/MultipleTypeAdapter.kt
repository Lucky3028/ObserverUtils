package click.seichi.observerutils.utils

import com.google.gson.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class MultipleTypeAdapter : JsonSerializer<MultipleType<*>>, JsonDeserializer<MultipleType<*>> {
    override fun serialize(src: MultipleType<*>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
        context.serialize(if (src.isMultiple) src.values else src.value)

    /**
     * @throws [JsonParseException] See [JsonDeserializationContext.deserialize]
     */
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): MultipleType<*> =
        if (json.isJsonArray) MultipleType(values = deserializeArray<Type>(json, typeOfT, context))
        else MultipleType(value = context.deserialize<Type>(json, getGenericType(typeOfT)))

    private fun <T> deserializeArray(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): List<T> =
        json.asJsonArray.map { context.deserialize<T>(it, getGenericType(typeOfT)) }

    private fun getGenericType(typeOfT: Type): Type? = (typeOfT as? ParameterizedType)?.actualTypeArguments?.get(0)
}
