package ch.difty.scipamato.core.persistence

import io.mockk.mockk

inline fun <reified T : Any> mock(): T = mockk<T>()!!
