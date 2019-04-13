package ch.leadrian.samp.kamp.fcnpcwrapper.entity

import ch.leadrian.samp.kamp.core.runtime.entity.EntityResolver
import com.google.inject.ImplementedBy

@ImplementedBy(FCNPCEntityResolverImpl::class)
interface FCNPCEntityResolver : EntityResolver {

    fun Int.toNPC(): FullyControllableNPC

}