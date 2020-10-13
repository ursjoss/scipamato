package ch.difty.scipamato.common.web.component

import java.io.Serializable
import java.util.function.BiConsumer
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Supplier

// TODO replace with serializable kotlin lambdas
fun interface SerializableBiConsumer<T, U> : BiConsumer<T, U>, Serializable
fun interface SerializableBiFunction<T, U, R> : BiFunction<T, U, R>, Serializable
fun interface SerializableConsumer<T> : Consumer<T>, Serializable
fun interface SerializableFunction<T, R> : Function<T, R>, Serializable
fun interface SerializableSupplier<T> : Supplier<T>, Serializable
